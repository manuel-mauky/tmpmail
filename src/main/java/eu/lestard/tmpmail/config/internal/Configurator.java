package eu.lestard.tmpmail.config.internal;

import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.config.StringKey;


/**
 * This is the central configuration component that stores all configuration parameters.
 * 
 * @author manuel.mauky
 * 
 */
public interface Configurator {

	/**
	 * Return the Integer param for the given key or <code>null</code> if no param is available for
	 * the given key.
	 */
	Integer getValue(IntKey key);

	/**
	 * Return the String param for the given key or <code>null</code> if no param is available for
	 * the given key.
	 */
	String getValue(StringKey key);

	/**
	 * Persist the Integer configuration parameter with the given key and value.
	 */
	void setValue(IntKey key, Integer value);

	/**
	 * Persist the String configuration parameter with the given key and value.
	 */
	void setValue(StringKey key, String value);

}
