package eu.lestard.tmpmail.core.handling;

import javax.mail.internet.MimeMessage;

/**
 * This component is used to handle incomming email messages.
 * 
 * @author manuel.mauky
 * 
 */
public interface MailFilterService {

	/**
	 * This method is used to handle an incomming email message and give it to the forwarding
	 * mechanism when there is a mapping persisted that matches the given message.
	 * 
	 * At first it is checked whether the domain part of the receiver is handled by this
	 * application.
	 * 
	 * After that it is checked whether there is a mapping persisted for this domain part.
	 * 
	 * When the receivers domain is not known or there is no mapping persisted, the email is
	 * discarded without response.
	 * 
	 * @param emailMessage
	 */
	void filterEmail(MimeMessage emailMessage);

}
