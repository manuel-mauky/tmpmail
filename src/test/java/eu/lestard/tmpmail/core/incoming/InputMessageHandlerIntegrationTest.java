package eu.lestard.tmpmail.core.incoming;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

import eu.lestard.tmpmail.JavaMailTestHelper;
import eu.lestard.tmpmail.core.handling.MailFilterService;

public class InputMessageHandlerIntegrationTest {
	private static final String FROM_ADDRESS = "obiwan@example.org";
	private static final String TO_ADDRESS = "luke@example.org";
	private static final String SUBJECT = "greetings";
	private static final String MESSAGE_STRING = "may the force be with you";


	private static final int PORT = 25000;

	private MailFilterService filterServiceMock;

	private JavaMailTestHelper javaMail;

	private SMTPServer smtpServer;

	@Before
	public void setup() {
		filterServiceMock = mock(MailFilterService.class);

		MessageHandlerFactory factory = new MessageHandlerFactory() {
			@Override
			public MessageHandler create(final MessageContext ctx) {
				return new InputMessageHandler(filterServiceMock);
			}
		};

		smtpServer = new SMTPServer(factory);
		smtpServer.setPort(PORT);

		javaMail = new JavaMailTestHelper(PORT);
	}

	@After
	public void tearDown() {
		smtpServer.stop();
	}

	@Test
	public void testIncomingEmail() throws MessagingException, IOException {

		smtpServer.start();

		javaMail.sendWithJavaMail(FROM_ADDRESS, TO_ADDRESS, SUBJECT,
				MESSAGE_STRING);

		// get the message instance that was given to the filterService mock
		ArgumentCaptor<MimeMessage> argument = ArgumentCaptor
				.forClass(MimeMessage.class);
		verify(filterServiceMock).filterEmail(argument.capture());


		MimeMessage sendMessage = argument.getValue();

		assertThat(sendMessage.getFrom()[0].toString()).isEqualTo(FROM_ADDRESS);
		assertThat(sendMessage.getRecipients(RecipientType.TO)[0].toString())
				.isEqualTo(TO_ADDRESS);
		assertThat(sendMessage.getSubject()).isEqualTo(SUBJECT);
		assertThat(sendMessage.getContent().toString().trim()).isEqualTo(
				MESSAGE_STRING);

	}
}
