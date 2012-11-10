package eu.lestard.tmpmail.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This integration test verifies the behavior of the {@link TempEmailAddress}
 * entity class.
 * 
 * @author manuel.mauky
 * 
 */
public class TempEmailAddressJpaIntegrationTest {

	private JpaTestHelper<TempEmailAddress> tempEmailAddressPersistence;
	private JpaTestHelper<Domain> domainPersistence;

	@Before
	public void setup() {
		tempEmailAddressPersistence = new JpaTestHelper<>(
				TempEmailAddress.class);
		domainPersistence = new JpaTestHelper<>(Domain.class);
	}

	@After
	public void tearDown() {
		tempEmailAddressPersistence.tearDown();
		domainPersistence.tearDown();
	}

	@Test
	public void testCRUD() {
		Domain domain = new Domain("example.org");
		domainPersistence.persist(domain);

		TempEmailAddress address = new TempEmailAddress("test123", domain);

		String id = address.getId();

		// CREATE
		tempEmailAddressPersistence.persist(address);

		// READ
		TempEmailAddress foundAddress = tempEmailAddressPersistence.find(id);

		assertThat(foundAddress).isEqualsToByComparingFields(address);

		// There is no Update because the entity class is immutable.

		// DELETE
		tempEmailAddressPersistence.remove(foundAddress);

		TempEmailAddress notFoundAddress = tempEmailAddressPersistence.find(id);
		assertThat(notFoundAddress).isNull();
	}

	@Test(expected = RollbackException.class)
	public void testCombinationOfLocalPartAndDomainIsUnique() {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		TempEmailAddress address = new TempEmailAddress("test123",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(address);


		TempEmailAddress addressWithSameValues = new TempEmailAddress(
				"test123", exampleDotOrg);
		tempEmailAddressPersistence.persist(addressWithSameValues);
	}

	@Test
	public void testLocalPartMustNotBeUniqueForDifferentDomains() {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		Domain exampleDotCom = new Domain("example.com");
		domainPersistence.persist(exampleDotCom);

		TempEmailAddress address = new TempEmailAddress("test123",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(address);

		TempEmailAddress addressWithSameLocalPart = new TempEmailAddress(
				"test123", exampleDotCom);
		tempEmailAddressPersistence.persist(addressWithSameLocalPart);

		// no exception is thrown
	}


	@Test
	public void testDomainMustNotBeUniqueForDifferentLocalParts() {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		TempEmailAddress address = new TempEmailAddress("test123",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(address);

		TempEmailAddress addressWithSameDomain = new TempEmailAddress(
				"test456", exampleDotOrg);
		tempEmailAddressPersistence.persist(addressWithSameDomain);

		// no exception is thrown
	}

	@Test
	public void testNamedQueryFindByLocalAndDomainPart() {
		Domain exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		TempEmailAddress address1 = new TempEmailAddress("test123",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(address1);

		TempEmailAddress address2 = new TempEmailAddress("test456",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(address2);


		EntityManager entityManager = tempEmailAddressPersistence
				.getEntityManager();

		TypedQuery<TempEmailAddress> query = entityManager.createNamedQuery(
				TempEmailAddress.FIND_BY_LOCAL_AND_DOMAIN_PART,
				TempEmailAddress.class);

		query.setParameter("localpart", "test123");
		query.setParameter("domainpart", "example.org");

		List<TempEmailAddress> resultList = query.getResultList();

		assertThat(resultList).hasSize(1).contains(address1);
	}

}
