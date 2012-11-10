package eu.lestard.tmpmail.core.incoming;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

/**
 * This class is a CDI producer that creates an {@link MessageHandlerFactory}
 * instance. This factory is needed by the email Framework Subethasmpt. The
 * factory has to create instances of {@link InputMessageHandler} that handle
 * incoming emails.
 * 
 * @author manuel.mauky
 * 
 */
public class MessageHandlerFactoryProducer {

	@Inject
	@New
	private Instance<InputMessageHandler> handlerInstances;

	@Produces
	public MessageHandlerFactory produceFactory() {

		MessageHandlerFactory factory = new MessageHandlerFactory() {
			@Override
			public MessageHandler create(MessageContext arg0) {
				return handlerInstances.get();
			}
		};
		return factory;

	}
}
