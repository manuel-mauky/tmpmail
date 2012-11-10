package eu.lestard.tmpmail.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * This annotation is used as a qualifier to be able to inject configuration
 * values with cdi.
 * 
 * The key ({@link StringKey}) for the configuration parameter has to be
 * specified as parameter.
 * 
 * @author manuel.mauky
 * 
 */
@Qualifier
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, METHOD })
public @interface StringValue {
	@Nonbinding
	StringKey value();
}
