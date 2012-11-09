package eu.lestard.tmpmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;

public class JavaMailTestHelper {

	private final int port;

	public JavaMailTestHelper(final int port) {
		this.port = port;
	}

	public void sendWithJavaMail(final String from, final String to,
			final String subject, final String messageString) {
		final Properties props = new Properties();
		props.put("mail.smtp.host", "locahost");

		final Session session = Session.getDefaultInstance(props);
		try {
			final Transport transport = session.getTransport(new URLName(
					"smtp", "localhost", port, null, "", ""));

			final MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			message.setSubject(subject);
			message.setContent(messageString, "text/plain");

			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (final NoSuchProviderException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		} catch (final MessagingException e) {
			e.printStackTrace();
		}
	}

}
