package eu.lestard.tmpmail.config;

/**
 * This is the central configuration component that stores all configuration
 * parameters.
 * 
 * @author manuel.mauky
 * 
 */
public interface Configurator {

	/**
	 * Return the Integer param for the given key or <code>null</code> if no
	 * param is available for the given key.
	 */
	Integer getValue(IntKey key);

	/**
	 * Persist the configuration parameter with the given key and value.
	 */
	void setValue(IntKey key, Integer value);

}
