package eu.lestard.tmpmail.core.handling;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

import eu.lestard.tmpmail.core.outgoing.ForwardingService;
import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.JpaTestHelper;
import eu.lestard.tmpmail.persistence.TempEmailAddress;

/**
 * This is an integration test that verifies the behavior of the
 * {@link MailFilterServiceImpl} and it't interaction with the JPA persistence
 * context.
 * 
 * @author manuel.mauky
 * 
 */
public class MailFilterServiceIntegrationTest {

	private JpaTestHelper<TempEmailAddress> tempEmailAddressPersistence;
	private JpaTestHelper<Domain> domainPersistence;


	private MailFilterServiceImpl filterService;

	private ForwardingService forwardingServiceMock;

	@Before
	public void setup() {
		tempEmailAddressPersistence = new JpaTestHelper<>();
		tempEmailAddressPersistence.init(TempEmailAddress.class);

		domainPersistence = new JpaTestHelper<>();
		domainPersistence.init(Domain.class);

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory(JpaTestHelper.PERSISTENCE_UNIT);

		forwardingServiceMock = mock(ForwardingService.class);

		filterService = new MailFilterServiceImpl(forwardingServiceMock, emf);

	}

	@After
	public void tearDown() {
		tempEmailAddressPersistence.tearDown();
		domainPersistence.tearDown();
	}

	@Test
	public void testMessageWithKnownAddressIsForwarded()
			throws MessagingException {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		TempEmailAddress test123 = new TempEmailAddress("test123",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(test123);


		MimeMessage message = createMessage();

		message.setRecipient(RecipientType.TO, new InternetAddress(
				"test123@example.org"));


		filterService.filterEmail(message);

		verify(forwardingServiceMock).forwardMessage(message, test123);
	}

	@Test
	public void testDomainIsNotKnown() throws MessagingException {
		final MimeMessage message = createMessage();

		message.setRecipient(RecipientType.TO, new InternetAddress(
				"test123@example.org"));

		filterService.filterEmail(message);

		// no message is forwarded
		verify(forwardingServiceMock, never()).forwardMessage(
				any(MimeMessage.class), any(TempEmailAddress.class));
	}



	@Test
	public void testDomainIsKnownButNoTempMailMappingSaved()
			throws MessagingException {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		MimeMessage message = createMessage();
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"test123@example.org"));

		filterService.filterEmail(message);


		// no message is forwarded
		verify(forwardingServiceMock, never()).forwardMessage(
				any(MimeMessage.class), any(TempEmailAddress.class));
	}


	@Test
	public void testLoadDomainFromDatabase() {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		Domain loadedDomain = filterService
				.loadDomainFromDatabase("example.org");

		assertThat(loadedDomain).isEqualTo(exampleDotOrg);
	}

	@Test
	public void testLoadDomainFromDatabaseFailNoDomainFound() {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		Domain loadedDomain = filterService
				.loadDomainFromDatabase("example.com"); // .com

		assertThat(loadedDomain).isNull();
	}


	/**
	 * For the tests there doesn't need to be a valid email session. Sadly
	 * {@link Session} is a final class and so it can't be mocked out.
	 * 
	 * @return
	 */
	private MimeMessage createMessage() {
		final Session sessionMock = null;
		final MimeMessage message = new MimeMessage(sessionMock);
		return message;
	}
}
