package eu.lestard.tmpmail.core.incoming;

import static org.fest.assertions.api.Assertions.assertThat;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

import eu.lestard.tmpmail.core.handling.MailFilterService;

/**
 * This is an integration test that verifies that the injection of instances of
 * {@link MessageHandlerFactory} is working correctly.
 * 
 * @author manuel.mauky
 * 
 */
public class MessageHandlerFactoryProducerTest {


	@Test
	public void testProducer() {

		WeldContainer weldContainer = new Weld().initialize();

		MessageHandlerFactory factory = weldContainer.instance().select(MessageHandlerFactory.class).get();


		MessageHandler messageHandler1 = factory.create(null);
		assertThat(messageHandler1).isInstanceOf(InputMessageHandler.class);

		InputMessageHandler inputMessageHandler1 = (InputMessageHandler) messageHandler1;

		/**
		 * To verify that the inputMessageHandler was created correctly by CDI I
		 * get the {@link MailFilterService} instance from it. When the
		 * filterService is not null then everything seems to be right.
		 */
		MailFilterService filterService = (MailFilterService) Whitebox.getInternalState(inputMessageHandler1,
				"filterService");

		assertThat(filterService).isNotNull();


		// the factory has to create new instances with every call
		MessageHandler messageHandler2 = factory.create(null);

		assertThat(messageHandler1).isNotSameAs(messageHandler2);
	}



}
