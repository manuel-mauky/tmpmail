package eu.lestard.tmpmail.core.incoming;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

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
@WebListener
public class MailInputListenerImpl extends SMTPServer implements
		ServletContextListener {

	public MailInputListenerImpl(final Integer port,
			final MessageHandlerFactory factory) {
		super(factory);
		setPort(port);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		super.stop();
	}

	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		super.start();
	}

}
