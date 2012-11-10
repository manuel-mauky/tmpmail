package eu.lestard.tmpmail.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;

public class JpaTestHelper<T extends AbstractEntity> {

	public static final String PERSISTENCE_UNIT = "testdb";

	private final EntityManagerFactory emf;

	private final Class<T> clazz;

	public JpaTestHelper(final Class<T> clazz) {
		this.clazz = clazz;
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	public void tearDown() {
		EntityManager entityManager = emf.createEntityManager();

		// This is a HSQLDB specific feature to clear all data from the im
		// memory database.
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE SCHEMA public AND COMMIT")
				.executeUpdate();
		entityManager.getTransaction().commit();
		entityManager.close();

		emf.close();
	}


	protected EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	/**
	 * This method encapsulates the persist method of the entityManager incl
	 * transaction management.
	 * 
	 * @param entity
	 *            the entity instance that has to be persisted.
	 */
	public void persist(final T entity) {
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
	public T find(final String id) {
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
	public void merge(final T entity) {
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
	public void remove(final T entity) {
		final EntityManager entityManager = emf.createEntityManager();

		final AbstractEntity foundEntity = entityManager.find(clazz,
				entity.getId());

		entityManager.getTransaction().begin();
		entityManager.remove(foundEntity);
		entityManager.getTransaction().commit();

		entityManager.close();
	}

}
