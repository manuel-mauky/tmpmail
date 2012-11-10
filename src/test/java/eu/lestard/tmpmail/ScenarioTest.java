package eu.lestard.tmpmail;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.config.StringKey;
import eu.lestard.tmpmail.config.internal.Configurator;
import eu.lestard.tmpmail.core.incoming.MailInputListener;
import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.JpaTestHelper;
import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

/**
 * This test should give an overview of the application process.
 * 
 * In this scenario the application is handling the domain "example.org" so that
 * all emails with this domain part are processed by the application.
 * 
 * The user "luke" with the email address "luke@example.de" (note the tld "de")
 * creates an account for the application and creates 2 temp email addresses
 * ("test123@example.org" and "test456@example.org").
 * 
 * Now a person with the email address "darth.vader@darkside.example.de" is
 * sending an email to one of lukes temp email addresses.
 * 
 * This email is forwarded to lukes real email address "luke@example.de".
 * 
 * 
 * Technical informations:
 * 
 * The application uses the PORT 25000 and listens to emails with the domain
 * part "example.org".
 * 
 * To forward emails the application needs an external SMTP-Server. This
 * external SMTP-Server is mocked out in this test with the {@link Wiser} class
 * from the testing framework "Subethasmtp". It is configured to use the
 * SMTP-Host "localhost" and the port 20000.
 * 
 * @author manuel.mauky
 * 
 */
public class ScenarioTest {

	private static final int INCOMMING_SMTP_PORT = 25000;
	private static final int OUTGOING_SMTP_PORT = 20000;
	private static final String OUTGOING_SMTP_HOST = "localhost";
	private JpaTestHelper jpaTestHelper;

	private Domain exampleDotOrg;

	private Configurator configurator;
	private MailInputListener mailInputListener;
	private User user;

	private Wiser wiser;

	private WeldContainer weld;

	@Before
	public void setup() {
		jpaTestHelper = new JpaTestHelper();

		wiser = new Wiser();
		wiser.setPort(OUTGOING_SMTP_PORT);
		wiser.setHostname(OUTGOING_SMTP_HOST);
		wiser.start();


		weld = new Weld().initialize();

		configurator = weld.instance().select(Configurator.class).get();


		configurator.setValue(IntKey.INCOMMING_SMTP_PORT, INCOMMING_SMTP_PORT);
		configurator.setValue(IntKey.OUTGOING_SMTP_PORT, OUTGOING_SMTP_PORT);
		configurator.setValue(StringKey.OUTGOING_SMTP_HOST, OUTGOING_SMTP_HOST);


		mailInputListener = weld.instance().select(MailInputListener.class).get();

	}

	@After
	public void tearDown() {
		jpaTestHelper.tearDown();

		wiser.stop();

		mailInputListener.stop();
	}

	@Test
	public void testScenario() {
		defineDomainsThatAreHandled();

		startMailInputListener();

		createAnUserAccount();

		createSomeTempMailMappings();

		sendAnEmailToAMappedEmailAddress();

		verifyThatTheEmailWasForwarded();
	}

	private void defineDomainsThatAreHandled() {
		exampleDotOrg = new Domain("example.org");
		jpaTestHelper.persist(exampleDotOrg);
	}

	private void startMailInputListener() {
		mailInputListener.start();
	}

	private void createAnUserAccount() {
		user = new User("luke@example.de");
		jpaTestHelper.persist(user);
	}

	private void createSomeTempMailMappings() {
		final TempEmailAddress test123 = new TempEmailAddress("test123", exampleDotOrg);
		jpaTestHelper.persist(test123);
		final TempEmailAddress test456 = new TempEmailAddress("test456", exampleDotOrg);
		jpaTestHelper.persist(test456);

		user.addTempEmailAddresses(test123, test456);

		jpaTestHelper.merge(user);
	}

	private void sendAnEmailToAMappedEmailAddress() {
		final String to = "test123@example.org";
		final String from = "darth.vader@darkside.example.de";
		final String subject = "family affairs";
		final String messageString = "I'm your Father";

		JavaMailTestHelper helper = new JavaMailTestHelper(INCOMMING_SMTP_PORT);

		helper.sendWithJavaMail(from, to, subject, messageString);

	}


	private void verifyThatTheEmailWasForwarded() {
		final List<WiserMessage> messages = wiser.getMessages();

		assertThat(messages).hasSize(1);

		final WiserMessage firstMessage = messages.get(0);

		assertThat(firstMessage.getEnvelopeSender()).isEqualTo("darth.vader@darkside.example.de");
		assertThat(firstMessage.getEnvelopeReceiver()).isEqualTo("luke@example.de");
		try {
			assertThat(firstMessage.getMimeMessage().getSubject()).isEqualTo("family affairs");
			assertThat(firstMessage.getMimeMessage().getContent().toString().trim()).isEqualTo("I'm your Father");
		} catch (final MessagingException | IOException e) {
			e.printStackTrace();
		}

	}
}
