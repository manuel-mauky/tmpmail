package eu.lestard.tmpmail.business;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.JpaTestHelper;
import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public class TempEmailAddressServiceTest {

	private JpaTestHelper jpaTestHelper;

	private TempEmailAddressServiceImpl service;

	@Before
	public void setup() {
		jpaTestHelper = new JpaTestHelper();

		service = new TempEmailAddressServiceImpl(jpaTestHelper.getEmf());
		jpaTestHelper.cleanDatabase();
	}

	@After
	public void tearDown() {
		jpaTestHelper.tearDown();
	}

	@Test
	public void testAddNewAddress() {
		User user = new User("user@example.org");
		String userId = user.getId();
		jpaTestHelper.persist(user);

		Domain exampleDotDe = new Domain("example.de");
		jpaTestHelper.persist(exampleDotDe);

		TempEmailAddress address = new TempEmailAddress("test", exampleDotDe);
		String addressId = address.getId();

		boolean result = service.addNewAddress(user, address);
		assertThat(result).isTrue();


		TempEmailAddress persistedAddress = jpaTestHelper.find(addressId, TempEmailAddress.class);
		assertThat(persistedAddress).isNotNull().isEqualTo(address);

		User foundUser = jpaTestHelper.find(userId, User.class);
		assertThat(foundUser.getTempEmailAddresses()).contains(persistedAddress);
	}

	/**
	 * When the address is already persisted, then it can't be added to the
	 * user.
	 */
	@Test
	public void testAddNewAddressFailAlreadyPersisted() {

		User user = new User("user@example.org");
		String userId = user.getId();
		jpaTestHelper.persist(user);

		Domain exampleDotDe = new Domain("example.de");
		jpaTestHelper.persist(exampleDotDe);

		TempEmailAddress address = new TempEmailAddress("test", exampleDotDe);
		String addressId = address.getId();

		// This time the address is persisted already
		jpaTestHelper.persist(address);

		boolean result = service.addNewAddress(user, address);
		assertThat(result).isFalse();


		User foundUser = jpaTestHelper.find(userId, User.class);
		assertThat(foundUser.getTempEmailAddresses()).doesNotContain(address);
	}

}
