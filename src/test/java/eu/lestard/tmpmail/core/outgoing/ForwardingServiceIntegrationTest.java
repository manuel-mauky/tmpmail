package eu.lestard.tmpmail.core.outgoing;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.JpaTestHelper;
import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public class ForwardingServiceIntegrationTest {

	/**
	 * The temp email address that the message was send to.
	 */
	private static final String TEMP_ADDRESS = "test@example.org";

	private static final String SENDER_ADDRESS = "obi@example.de";
	private static final int OUTGOING_SMTP_PORT = 25000;
	private static final String OUTGOING_SMTP_HOST = "localhost";

	/**
	 * The address of the user where the message should be forwarded to.
	 */
	private static final String USERS_REAL_ADDRESS = "luke@localhost";


	private ForwardingServiceImpl forwardingService;

	private JpaTestHelper jpaTestHelper;

	private Wiser wiser;

	@Before
	public void setup() {
		jpaTestHelper = new JpaTestHelper();

		EntityManagerFactory emf = jpaTestHelper.getEmf();

		forwardingService = new ForwardingServiceImpl(emf, OUTGOING_SMTP_HOST, OUTGOING_SMTP_PORT, "", "");

		wiser = new Wiser();
		wiser.setPort(OUTGOING_SMTP_PORT);
		wiser.setHostname(OUTGOING_SMTP_HOST);
		wiser.start();
	}

	@After
	public void tearDown() {
		jpaTestHelper.tearDown();
		wiser.stop();
	}

	@Test
	public void testForwardMessage() throws MessagingException, IOException {
		MimeMessage message = createMessage();

		message.addRecipient(RecipientType.TO, new InternetAddress(TEMP_ADDRESS));
		message.setFrom(new InternetAddress(SENDER_ADDRESS));
		message.setSubject("an advice");
		message.setContent("trust the force, luke", "text/plain");

		Domain exampleDotOrg = new Domain("example.org");
		jpaTestHelper.persist(exampleDotOrg);

		TempEmailAddress tempEmailAddress = new TempEmailAddress("test", exampleDotOrg);
		jpaTestHelper.persist(tempEmailAddress);

		User luke = new User(USERS_REAL_ADDRESS);
		luke.addTempEmailAddresses(tempEmailAddress);
		jpaTestHelper.persist(luke);

		forwardingService.forwardMessage(message, tempEmailAddress);


		List<WiserMessage> messages = wiser.getMessages();

		assertThat(messages).hasSize(1);

		WiserMessage wiserMessage = messages.get(0);

		assertThat(wiserMessage.getEnvelopeReceiver()).isEqualTo(USERS_REAL_ADDRESS);
		assertThat(wiserMessage.getEnvelopeSender()).isEqualTo(SENDER_ADDRESS);
		assertThat(wiserMessage.getMimeMessage().getSubject()).isEqualTo("an advice");
		assertThat(wiserMessage.getMimeMessage().getContent().toString().trim())
				.isEqualTo("trust the force, luke");

	}

	@Test
	public void testLoadUserFromDatabase() {

		User user = new User(USERS_REAL_ADDRESS);
		jpaTestHelper.persist(user);

		Domain exampleDotOrg = new Domain("example.org");
		jpaTestHelper.persist(exampleDotOrg);

		TempEmailAddress address1 = new TempEmailAddress(TEMP_ADDRESS, exampleDotOrg);
		jpaTestHelper.persist(address1);

		user.addTempEmailAddresses(address1);
		jpaTestHelper.merge(user);


		User loadedUser = forwardingService.loadUserFromDatabase(address1);

		assertThat(loadedUser).isEqualTo(user);
	}

	@Test
	public void testLoadUserFromDatabaseFailNoUserFound() {

		Domain exampleDotOrg = new Domain("example.org");
		jpaTestHelper.persist(exampleDotOrg);

		TempEmailAddress address1 = new TempEmailAddress(TEMP_ADDRESS, exampleDotOrg);
		jpaTestHelper.persist(address1);

		User loadedUser = forwardingService.loadUserFromDatabase(address1);

		assertThat(loadedUser).isNull();

	}

	@Test
	public void testSend() throws MessagingException, IOException {

		MimeMessage mimeMessage = createMessage();
		mimeMessage.setContent("my important message", "text/plain");
		mimeMessage.setFrom(new InternetAddress(SENDER_ADDRESS));
		mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(USERS_REAL_ADDRESS));
		mimeMessage.setSubject("a subject");

		forwardingService.sendMessage(mimeMessage);

		List<WiserMessage> messages = wiser.getMessages();

		assertThat(messages).hasSize(1);

		WiserMessage wiserMessage = messages.get(0);

		assertThat(wiserMessage.getEnvelopeReceiver()).isEqualTo(USERS_REAL_ADDRESS);
		assertThat(wiserMessage.getEnvelopeSender()).isEqualTo(SENDER_ADDRESS);
		assertThat(wiserMessage.getMimeMessage().getSubject()).isEqualTo("a subject");
		assertThat(wiserMessage.getMimeMessage().getContent().toString().trim()).isEqualTo("my important message");

	}


	@Test
	public void testReplaceRecipient() throws MessagingException {
		MimeMessage message = createMessage();

		message.setRecipient(RecipientType.TO, new InternetAddress(TEMP_ADDRESS));

		User user = new User(USERS_REAL_ADDRESS);

		forwardingService.replaceRecipient(message, user);

		Address[] recipients = message.getRecipients(RecipientType.TO);

		assertThat(recipients).hasSize(1);

		String newRecipientAddress = recipients[0].toString();
		assertThat(newRecipientAddress).isEqualTo(USERS_REAL_ADDRESS);
	}


	private MimeMessage createMessage() {
		Session session = null;
		return new MimeMessage(session);
	}
}
