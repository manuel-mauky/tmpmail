package eu.lestard.tmpmail.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

/**
 * This integration test verifies that the {@link Domain} entity can be used as
 * JPA entity without problems.
 * 
 * @author manuel.mauky
 * 
 */
public class DomainJpaIntegrationTest extends JpaTestHelper<Domain> {

	@Before
	public void setup() {
		init(Domain.class);
	}

	@Test
	public void testCRUD() {
		Domain domain = new Domain("example.com");

		String id = domain.getId();

		// CREATE
		persist(domain);

		// READ
		Domain foundDomain = find(id);

		assertThat(foundDomain).isEqualsToByComparingFields(domain);

		// There is no Update because the entity class is immutable

		// DELETE
		remove(foundDomain);

		Domain notFoundDomain = find(id);
		assertThat(notFoundDomain).isNull();
	}


	@Test(expected = RollbackException.class)
	public void testDomainAsStringIsUnique() {
		Domain domain = new Domain("example.com");
		persist(domain);

		Domain domainWithSameValue = new Domain("example.com");

		persist(domainWithSameValue);
	}

	@Test
	public void testNamedQueryFindByDomainName() {
		Domain domain = new Domain("example.com");
		persist(domain);


		EntityManager entityManager = getEntityManager();

		TypedQuery<Domain> query = entityManager.createNamedQuery(
				Domain.FIND_BY_DOMAIN_NAME, Domain.class);

		query.setParameter("domainName", "example.com");

		List<Domain> resultList = query.getResultList();

		assertThat(resultList).hasSize(1).contains(domain);
	}

}
