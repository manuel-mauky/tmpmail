package eu.lestard.tmpmail.core.handling;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;

import eu.lestard.tmpmail.core.outgoing.ForwardingService;
import eu.lestard.tmpmail.persistence.TempEmailAddress;

/**
 * This is a unit test that verifies all methods of
 * {@link MailFilterServiceImpl} that doesn't need a JPA context.
 * 
 * The handling of the persistence is tested in the
 * {@link MailFilterServiceIntegrationTest}.
 * 
 * @author manuel.mauky
 * 
 */
public class MailFilterServiceImplTest {

	private MailFilterServiceImpl filterService;

	private EntityManagerFactory emfMock;

	private ForwardingService forwardingServiceMock;

	@Before
	public void setup() {

		emfMock = mock(EntityManagerFactory.class);

		forwardingServiceMock = mock(ForwardingService.class);

		filterService = new MailFilterServiceImpl(forwardingServiceMock,
				emfMock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailMessageIsNull() {
		filterService.filterEmail(null);
	}

	@Test
	public void testFailMessageHasNoRecipient() {
		MimeMessage message = createMessage();

		filterService.getRecipientAddress(message);

		// no message is forwarded
		verify(forwardingServiceMock, never()).forwardMessage(
				any(MimeMessage.class), any(TempEmailAddress.class));
	}

	@Test
	public void testGetRecipientAddress() throws MessagingException {
		MimeMessage message = createMessage();

		message.addRecipient(RecipientType.TO, new InternetAddress(
				"test@example.org"));

		String result = filterService.getRecipientAddress(message);

		assertThat(result).isEqualTo("test@example.org");
	}

	@Test
	public void testGetRecipientAddressFailRecipientIsNull() {
		MimeMessage message = createMessage();

		String result = filterService.getRecipientAddress(message);

		assertThat(result).isNull();
	}

	/**
	 * When an exception is thrown by the
	 * {@link MimeMessage#getRecipients(RecipientType)} method then
	 * <code>null</code> should be returned.
	 * 
	 * @throws MessagingException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetRecipientAddressExceptionHandling()
			throws MessagingException {

		MimeMessage message = mock(MimeMessage.class);
		when(message.getRecipients(RecipientType.TO)).thenThrow(
				MessagingException.class);

		String result = filterService.getRecipientAddress(message);
		assertThat(result).isNull();
	}

	/**
	 * When there are more than one recipients then only the first is returned.
	 * 
	 * @throws MessagingException
	 */
	@Test
	public void testGetRecipientAddressFirstIsReturned()
			throws MessagingException {
		MimeMessage message = createMessage();

		message.addRecipient(RecipientType.TO, new InternetAddress(
				"test@example.org"));
		message.addRecipient(RecipientType.TO, new InternetAddress(
				"test123@example.org"));

		String result = filterService.getRecipientAddress(message);

		assertThat(result).isEqualTo("test@example.org");
	}



	@Test
	public void testGetDomainPart() {
		String result = filterService.getDomainPart("test@example.org");
		assertThat(result).isEqualTo("example.org");

		result = filterService.getDomainPart("test123@example");
		assertThat(result).isEqualTo("example");
	}

	@Test
	public void testGetUserPart() {
		String result = filterService.getUserPart("test@example.org");
		assertThat(result).isEqualTo("test");

		result = filterService.getUserPart("test123.test.456@example.org");
		assertThat(result).isEqualTo("test123.test.456");
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
