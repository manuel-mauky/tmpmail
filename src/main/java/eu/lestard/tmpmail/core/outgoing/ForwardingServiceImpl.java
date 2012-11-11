package eu.lestard.tmpmail.core.outgoing;

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.config.IntValue;
import eu.lestard.tmpmail.config.StringKey;
import eu.lestard.tmpmail.config.StringValue;
import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public class ForwardingServiceImpl implements ForwardingService {

	private static final Logger LOG = LoggerFactory.getLogger(ForwardingServiceImpl.class);

	private final EntityManagerFactory emf;

	private final String smtpHost;

	private final int smtpPort;

	private final String username;

	private final String password;

	@Inject
	public ForwardingServiceImpl(EntityManagerFactory emf,
			@StringValue(StringKey.OUTGOING_SMTP_HOST) String smtpHost,
			@IntValue(IntKey.OUTGOING_SMTP_PORT) Integer smtpPort,
			@StringValue(StringKey.OUTGOING_SMTP_USERNAME) String username,
			@StringValue(StringKey.OUTGOING_SMTP_PASSWORD) String password) {
		this.emf = emf;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.username = username;
		this.password = password;
	}

	@Override
	public void forwardMessage(MimeMessage message, final TempEmailAddress tempEmailAddress) {
		LOG.debug("Message will now be forwarded");

		User user = loadUserFromDatabase(tempEmailAddress);

		if (user == null) {
			return;
		}

		replaceRecipient(message, user);

		sendMessage(message);
	}

	protected void sendMessage(final MimeMessage message) {
		LOG.debug("Prepare for Sending the message");

		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", smtpPort);
		properties.put("mail.smtp.starttls.enable", "true");

		try {
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			Transport transport = session.getTransport(new URLName("smtp", smtpHost, smtpPort, null, username,
					password));

			MimeMessage messageToSend = new MimeMessage(message);

			transport.connect();
			transport.sendMessage(messageToSend, message.getAllRecipients());
			transport.close();

			LOG.debug("Message was send");

		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	protected void replaceRecipient(MimeMessage message, final User user) {
		try {
			message.setRecipient(RecipientType.TO, new InternetAddress(user.getEmailAddress()));

			LOG.debug("Recipient was replaced");
		} catch (final MessagingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Load the {@link User} instance that has saved the given
	 * {@link TempEmailAddress}.
	 * 
	 * If no user can be found then <code>null</code> is returned.
	 * 
	 * 
	 * @param tempEmailAddress
	 * @return
	 */
	protected User loadUserFromDatabase(final TempEmailAddress tempEmailAddress) {
		EntityManager entityManager = emf.createEntityManager();

		TypedQuery<User> query = entityManager.createNamedQuery(User.FIND_BY_TEMP_EMAIL_ADDRESS_ID, User.class);

		query.setParameter("id", tempEmailAddress.getId());

		List<User> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

}
