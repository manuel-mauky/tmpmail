package eu.lestard.tmpmail.config;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Before;
import org.junit.Test;

import eu.lestard.tmpmail.config.internal.Configurator;


/**
 * This test is used to verify that configuration values are properly injected.
 * 
 * @author manuel.mauky
 * 
 */
public class ConfigProducerTest {

	private WeldContainer weldContainer;

	/**
	 * This is an example class with public fields to check whether the
	 * injection has worked or not.
	 * 
	 * @author manuel.mauky
	 * 
	 */
	private static class ExampleClass {
		@Inject
		@IntValue(IntKey.OUTGOING_SMTP_PORT)
		public Integer port;

		@Inject
		@StringValue(StringKey.OUTGOING_SMTP_HOST)
		public String host;
	}

	@Before
	public void setup() {
		weldContainer = new Weld().initialize();
	}

	@Test
	public void testInjection() {
		Configurator configurator = weldContainer.instance().select(Configurator.class).get();

		configurator.setValue(IntKey.OUTGOING_SMTP_PORT, 2500);
		configurator.setValue(StringKey.OUTGOING_SMTP_HOST, "host.example.org");


		ExampleClass instance = weldContainer.instance().select(ExampleClass.class).get();

		assertThat(instance.port).isEqualTo(2500);
		assertThat(instance.host).isEqualTo("host.example.org");
	}

}
