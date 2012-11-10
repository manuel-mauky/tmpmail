package eu.lestard.tmpmail.core.incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.jboss.solder.servlet.event.Destroyed;
import org.jboss.solder.servlet.event.Initialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.config.IntValue;

/**
 * The responsibility of this component is to integrate the SMTP server in the
 * application context. This means that the server needs to be configured with
 * the desired port and to start the server at application startup and stop it
 * at application shutdown.
 * 
 * 
 * @author manuel.mauky
 * 
 */
@ApplicationScoped
public class MailInputListenerImpl implements MailInputListener {

	private SMTPServer smtpServer;

	private static final Logger LOG = LoggerFactory.getLogger(MailInputListenerImpl.class);

	private int port;

	@Inject
	public MailInputListenerImpl(@IntValue(IntKey.INCOMMING_SMTP_PORT) final Integer port,
			final MessageHandlerFactory factory) {
		smtpServer = new SMTPServer(factory);
		this.port = port;
	}

	/**
	 * no-arg constructor is needed for CDI
	 */
	protected MailInputListenerImpl() {
	}

	public void start(@Observes @Initialized ServletContext ctx) {
		this.start();
	}

	public void stop(@Observes @Destroyed ServletContext ctx) {
		this.stop();
	}

	@Override
	public void stop() {
		smtpServer.stop();
		LOG.info("SMTP Server is stopped");
	}

	@Override
	public void start() {
		LOG.info("Starting SMTP Server on Port:" + port);
		smtpServer.setPort(port);
		smtpServer.start();
		LOG.info("SMTP Server is started");
	}

}
