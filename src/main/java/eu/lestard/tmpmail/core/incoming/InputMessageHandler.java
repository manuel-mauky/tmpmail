package eu.lestard.tmpmail.core.incoming;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandler;

import eu.lestard.tmpmail.core.handling.MailFilterService;

/**
 * This class handles the incoming emails.
 * 
 * It saves the recipient and sender addresses and creates a new
 * {@link MimeMessage} instance with the DATA from the incoming email and the
 * recipient and sender address.
 * 
 * Cases with more than one recipient are not handled at the moment.
 * 
 * @author manuel.mauky
 * 
 */
public class InputMessageHandler implements MessageHandler {

	private static final Logger LOG = LoggerFactory.getLogger(InputMessageHandler.class);

	private final MailFilterService filterService;

	private MimeMessage message;

	private String sender;

	private String recipient;

	@Inject
	public InputMessageHandler(final MailFilterService filterService) {
		this.filterService = filterService;
	}

	@Override
	public void data(final InputStream data) throws IOException {
		Session session = Session.getDefaultInstance(new Properties());
		try {
			message = new MimeMessage(session, data);
		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void done() {
		try {
			message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
			message.setFrom(new InternetAddress(sender));
			LOG.debug("new Mail was catched.");
			filterService.filterEmail(message);
		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void from(final String from) {
		LOG.debug("New Mail from: " + from);
		sender = from;
	}

	/**
	 * At the moment, only the first recipient will be saved.
	 */
	@Override
	public void recipient(final String recipient) {
		LOG.debug("New Mail to: " + recipient);
		if (this.recipient == null) {
			this.recipient = recipient;
		}
	}

}
