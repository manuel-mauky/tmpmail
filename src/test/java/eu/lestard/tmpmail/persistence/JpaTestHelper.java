package eu.lestard.tmpmail.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;

public class JpaTestHelper {

	public static final String PERSISTENCE_UNIT = "testdb";

	private final EntityManagerFactory emf;

	public JpaTestHelper() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	public JpaTestHelper(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void tearDown() {
		cleanDatabase();

		emf.close();
	}

	public void cleanDatabase() {
		EntityManager entityManager = emf.createEntityManager();

		// This is a HSQLDB specific feature to clear all data from the im
		// memory database.
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE SCHEMA public AND COMMIT").executeUpdate();
		entityManager.getTransaction().commit();
		entityManager.close();
	}


	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	/**
	 * This method encapsulates the persist method of the entityManager incl.
	 * transaction management.
	 * 
	 * @param entity
	 *            the entity instance that has to be persisted.
	 */
	public void persist(final AbstractEntity entity) {
		final EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist(entity);

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	/**
	 * This method encapsulates the find method of the entityManager. The
	 * parameters are the same as in the EntityManager.find method.
	 * 
	 * @param id
	 *            The id of the entity
	 * @return The found entity or null if there is no entity with this Id.
	 */
	public <T> T find(final String id, final Class<T> clazz) {
		final EntityManager entityManager = emf.createEntityManager();

		final T entity = entityManager.find(clazz, id);

		entityManager.close();

		return entity;
	}



	/**
	 * This method encapsulates the merge method of the entityManager.
	 * 
	 * @param entity
	 *            The entity that should be merged
	 */
	public void merge(final AbstractEntity entity) {
		final EntityManager entityManager = emf.createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.merge(entity);
		entityManager.getTransaction().commit();

		entityManager.close();
	}

	/**
	 * This method encapsulates the remove method of the entityManager.
	 * 
	 * @param entity
	 *            The entity that should be removed.
	 */
	public <T> void remove(final AbstractEntity entity, final Class<T> clazz) {
		final EntityManager entityManager = emf.createEntityManager();

		final T foundEntity = entityManager.find(clazz, entity.getId());

		entityManager.getTransaction().begin();
		entityManager.remove(foundEntity);
		entityManager.getTransaction().commit();

		entityManager.close();
	}

}
