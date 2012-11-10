package eu.lestard.tmpmail.core.handling;

import javax.mail.internet.MimeMessage;

import eu.lestard.tmpmail.core.outgoing.ForwardingService;

public class MailFilterServiceImpl implements MailFilterService {

	private final ForwardingService forwardingService;

	public MailFilterServiceImpl(final ForwardingService forwardingService) {
		this.forwardingService = forwardingService;
	}

	@Override
	public void filterEmail(final MimeMessage emailMessage) {
		// TODO Auto-generated method stub

	}

}
