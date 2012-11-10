package eu.lestard.tmpmail.core.handling;

import java.util.List;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.lestard.tmpmail.core.outgoing.ForwardingService;
import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.TempEmailAddress;

public class MailFilterServiceImpl implements MailFilterService {

	private static final Logger LOG = LoggerFactory.getLogger(MailFilterServiceImpl.class);

	private final ForwardingService forwardingService;

	private final EntityManagerFactory emf;

	@Inject
	public MailFilterServiceImpl(final ForwardingService forwardingService, final EntityManagerFactory emf) {
		this.forwardingService = forwardingService;
		this.emf = emf;
	}

	@Override
	public void filterEmail(final MimeMessage emailMessage) {
		if (emailMessage == null) {
			throw new IllegalArgumentException("The Param MimeMessage must not be null.");
		}

		String recipientAddress = getRecipientAddress(emailMessage);

		String domainPart = getDomainPart(recipientAddress);

		Domain domain = loadDomainFromDatabase(domainPart);

		if (domain == null) {
			return;
		}

		String localPart = getLocalPart(recipientAddress);

		TempEmailAddress tempEmailAddress = loadTempEmailAddressFromDatabase(localPart, domain);

		if (tempEmailAddress != null) {
			forwardingService.forwardMessage(emailMessage, tempEmailAddress);
		}
	}

	/**
	 * Load the {@link TempEmailAddress} instance with the given local part and
	 * domain from the database. When there is no temp email address persisted
	 * with the given values than <code>null</code> is returned.
	 * 
	 * @param localPart
	 * @param domain
	 * @return
	 */
	protected TempEmailAddress loadTempEmailAddressFromDatabase(final String localPart, final Domain domain) {

		EntityManager entityManager = emf.createEntityManager();

		TypedQuery<TempEmailAddress> query = entityManager.createNamedQuery(
				TempEmailAddress.FIND_BY_LOCAL_AND_DOMAIN_PART, TempEmailAddress.class);
		query.setParameter("localpart", localPart);
		query.setParameter("domainpart", domain.getDomainAsString());

		List<TempEmailAddress> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

	/**
	 * Load the {@link Domain} instance for the given domain name from the
	 * database.
	 * 
	 * If there is no Domain instance with the given domain name persisted then
	 * <code>null</code> is returned.
	 * 
	 * @param domainPart
	 * @return
	 */
	protected Domain loadDomainFromDatabase(final String domainPart) {
		EntityManager entityManager = emf.createEntityManager();

		TypedQuery<Domain> query = entityManager.createNamedQuery(Domain.FIND_BY_DOMAIN_NAME, Domain.class);

		query.setParameter("domainName", domainPart);

		List<Domain> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	/**
	 * Returns the domain part of the given email address. This method is
	 * awaiting an valid email address as string.
	 */
	protected String getDomainPart(final String recipientAddress) {
		return recipientAddress.split("@")[1];
	}

	/**
	 * Returns the local part of the given email address. This method is
	 * awaiting an valid email address as string.
	 */
	protected String getLocalPart(final String recipientAddress) {
		return recipientAddress.split("@")[0];
	}


	/**
	 * Extract the first recipient address from the given {@link MimeMessage}
	 * and return it as String.
	 * 
	 * When there is no recipient saved in the given message or an exception is
	 * thrown, then <code>null</code> is returned.
	 * 
	 * When there are more than one recipients only the first is returned.
	 * 
	 * 
	 * @param emailMessage
	 *            the message instance with the recipient.
	 * @return the first recipient address as String or <code>null</code> if
	 *         there is an error.
	 */
	protected String getRecipientAddress(final MimeMessage emailMessage) {
		try {
			Address[] recipients = emailMessage.getRecipients(RecipientType.TO);

			if (recipients == null) {
				return null;
			}

			return recipients[0].toString();
		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

}
