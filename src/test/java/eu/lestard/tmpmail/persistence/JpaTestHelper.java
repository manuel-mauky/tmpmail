package eu.lestard.tmpmail.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;

public class JpaTestHelper<T extends AbstractEntity> {

	public static final String PERSISTENCE_UNIT = "testdb";

	private EntityManagerFactory emf;

	private Class<T> clazz;


	public void init(final Class<T> clazz) {
		this.clazz = clazz;
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	@After
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
	public T find(final String id) {
		final EntityManager entityManager = emf.createEntityManager();

		final T entity = entityManager.find(clazz, id);

		entityManager.close();

		return entity;
	}


	/**
	 * This method can be used to find persisted instances of another type then
	 * the generic type of this class.
	 * 
	 * @param clazz
	 *            the type of the instance that should be found
	 * @param id
	 *            the id of the instance that should be found
	 * @return the found entity or null if no entity with this id can be found.
	 */
	public <T2> T2 find(final Class<? extends T2> clazz, final String id) {
		final EntityManager entityManager = emf.createEntityManager();

		final T2 entity = entityManager.find(clazz, id);
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
