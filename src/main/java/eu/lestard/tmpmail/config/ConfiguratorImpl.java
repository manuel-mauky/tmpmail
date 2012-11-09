package eu.lestard.tmpmail.config;

import java.util.EnumMap;

/**
 * This class encapsulates the mappings for the configuration parameters.
 * 
 * @author manuel.mauky
 * 
 */
public class ConfiguratorImpl implements Configurator {

	private final EnumMap<IntKey, Integer> integerValues = new EnumMap<IntKey, Integer>(IntKey.class);
	private final EnumMap<StringKey, String> stringValues = new EnumMap<StringKey, String>(StringKey.class);


	@Override
	public Integer getValue(final IntKey key) {
		return integerValues.get(key);
	}

	@Override
	public String getValue(final StringKey key) {
		return stringValues.get(key);
	}

	@Override
	public void setValue(final IntKey key, final Integer value) {
		integerValues.put(key, value);
	}

	@Override
	public void setValue(final StringKey key, final String value) {
		stringValues.put(key, value);
	}

}
