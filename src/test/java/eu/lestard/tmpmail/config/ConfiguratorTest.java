package eu.lestard.tmpmail.config;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ConfiguratorTest {

	private Configurator configurator;

	@Before
	public void setup() {
		configurator = new ConfiguratorImpl();
	}

	@Test
	public void testIntegerParams() {
		// At startup no values are saved
		assertThat(configurator.getValue(IntKey.INCOMMING_SMTP_PORT)).isNull();

		configurator.setValue(IntKey.INCOMMING_SMTP_PORT, 25);

		assertThat(configurator.getValue(IntKey.INCOMMING_SMTP_PORT)).isEqualTo(25);
	}

	@Test
	public void testStringParams() {
		assertThat(configurator.getValue(StringKey.OUTGOING_SMTP_HOST)).isNull();

		configurator.setValue(StringKey.OUTGOING_SMTP_HOST, "localhost");

		assertThat(configurator.getValue(StringKey.OUTGOING_SMTP_HOST));
	}

}
