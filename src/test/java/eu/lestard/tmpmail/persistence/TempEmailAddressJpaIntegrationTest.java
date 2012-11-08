package eu.lestard.tmpmail.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.persistence.RollbackException;

import org.junit.Before;
import org.junit.Test;

/**
 * This integration test verifies the behavior of the {@link TempEmailAddress}
 * entity class.
 * 
 * @author manuel.mauky
 * 
 */
public class TempEmailAddressJpaIntegrationTest extends
		JpaTestHelper<TempEmailAddress> {

	@Before
	public void setup() {
		init(TempEmailAddress.class);
	}

	@Test
	public void testCRUD() {
		Domain domain = new Domain("example.org");
		persist(domain);

		TempEmailAddress address = new TempEmailAddress("test123", domain);

		String id = address.getId();

		// CREATE
		persist(address);

		// READ
		TempEmailAddress foundAddress = find(id);

		assertThat(foundAddress).isEqualsToByComparingFields(address);

		// There is no Update because the entity class is immutable.

		// DELETE
		remove(foundAddress);

		TempEmailAddress notFoundAddress = find(id);
		assertThat(notFoundAddress).isNull();
	}

	@Test(expected = RollbackException.class)
	public void testCombinationOfLocalPartAndDomainIsUnique() {
		Domain exampleDotOrg = new Domain("example.org");
		persist(exampleDotOrg);

		TempEmailAddress address = new TempEmailAddress("test123",
				exampleDotOrg);
		persist(address);


		TempEmailAddress addressWithSameValues = new TempEmailAddress(
				"test123", exampleDotOrg);
		persist(addressWithSameValues);
	}

	@Test
	public void testLocalPartMustNotBeUniqueForDifferentDomains() {
		Domain exampleDotOrg = new Domain("example.org");
		persist(exampleDotOrg);

		Domain exampleDotCom = new Domain("example.com");
		persist(exampleDotCom);

		TempEmailAddress address = new TempEmailAddress("test123",
				exampleDotOrg);
		persist(address);

		TempEmailAddress addressWithSameLocalPart = new TempEmailAddress(
				"test123", exampleDotCom);
		persist(addressWithSameLocalPart);

		// no exception is thrown
	}


	@Test
	public void testDomainMustNotBeUniqueForDifferentLocalParts() {
		Domain exampleDotOrg = new Domain("example.org");
		persist(exampleDotOrg);

		TempEmailAddress address = new TempEmailAddress("test123",
				exampleDotOrg);
		persist(address);

		TempEmailAddress addressWithSameDomain = new TempEmailAddress(
				"test456", exampleDotOrg);
		persist(addressWithSameDomain);

		// no exception is thrown
	}


}
