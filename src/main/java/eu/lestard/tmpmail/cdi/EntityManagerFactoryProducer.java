package eu.lestard.tmpmail.cdi;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This class is a CDI producer that enables the injection of
 * {@link EntityManagerFactory} instances with CDI.
 * 
 * @author manuel.mauky
 * 
 */
public class EntityManagerFactoryProducer {

	private static final String PERSISTENCE_UNIT = "testdb";

	@Produces
	public EntityManagerFactory produceEMF() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

}
