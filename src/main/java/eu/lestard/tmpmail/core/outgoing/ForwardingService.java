package eu.lestard.tmpmail.core.outgoing;

import javax.mail.internet.MimeMessage;

import eu.lestard.tmpmail.persistence.TempEmailAddress;

/**
 * This component is used to forward emails to there real receiver.
 * 
 * @author manuel.mauky
 * 
 */
public interface ForwardingService {

	/**
	 * Forward the given message to the email address of the user that has registered the given
	 * temporary email address mapping.
	 * 
	 * @param originalMessage
	 * @param tempEmailAddress
	 */
	void forwardMessage(MimeMessage originalMessage, TempEmailAddress tempEmailAddress);

}
