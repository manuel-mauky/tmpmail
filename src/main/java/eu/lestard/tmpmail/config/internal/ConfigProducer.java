package eu.lestard.tmpmail.config.internal;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.config.IntValue;
import eu.lestard.tmpmail.config.StringKey;
import eu.lestard.tmpmail.config.StringValue;


public class ConfigProducer {

	@Produces
	@IntValue(IntKey._DEFAULT)
	public Integer produceIntValue(InjectionPoint ip, Configurator configurator) {

		IntKey key = ip.getAnnotated().getAnnotation(IntValue.class).value();

		return configurator.getValue(key);
	}

	@Produces
	@StringValue(StringKey._DEFAULT)
	public String produceStringValue(InjectionPoint ip, Configurator configurator) {

		StringKey key = ip.getAnnotated().getAnnotation(StringValue.class).value();

		return configurator.getValue(key);
	}


}
