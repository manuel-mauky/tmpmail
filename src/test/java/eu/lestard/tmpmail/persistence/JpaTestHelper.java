package eu.lestard.tmpmail.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;

public class JpaTestHelper<T extends AbstractEntity> {

	private static final String PERSISTENCE_UNIT = "testdb";

	private EntityManagerFactory emf;

	private Class<T> clazz;


	public void init(final Class<T> clazz) {
		this.clazz = clazz;
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	@After
	public void tearDown() {
		emf.close();
	}


	/**
	 * This method encapsulates the persist method of the entityManager incl
	 * transaction management.
	 * 
	 * @param entity
	 *            the entity instance that has to be persisted.
	 */
	protected void persist(final AbstractEntity entity) {
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
	protected T find(final String id) {
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
	protected void merge(final T entity) {
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
	protected void remove(final T entity) {
		final EntityManager entityManager = emf.createEntityManager();

		final AbstractEntity foundEntity = entityManager.find(clazz,
				entity.getId());

		entityManager.getTransaction().begin();
		entityManager.remove(foundEntity);
		entityManager.getTransaction().commit();

		entityManager.close();
	}

}
