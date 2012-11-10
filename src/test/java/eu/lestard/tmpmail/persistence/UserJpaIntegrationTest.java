package eu.lestard.tmpmail.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.persistence.RollbackException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * This integration test verifies that the {@link User} entity can be used as
 * JPA entity without problems.
 * 
 * @author manuel.mauky
 * 
 */
public class UserJpaIntegrationTest {

	private JpaTestHelper<User> userPersistence;
	private JpaTestHelper<Domain> domainPersistence;
	private JpaTestHelper<TempEmailAddress> tempEmailAddressPersistence;

	@Before
	public void setup() {
		userPersistence = new JpaTestHelper<>(User.class);
		domainPersistence = new JpaTestHelper<>(Domain.class);
		tempEmailAddressPersistence = new JpaTestHelper<>(
				TempEmailAddress.class);
	}

	@After
	public void tearDown() {
		userPersistence.tearDown();
		domainPersistence.tearDown();
		tempEmailAddressPersistence.tearDown();
	}

	@Test
	public void testCRUD() {
		User user = new User("yoda@example.org");

		String id = user.getId();

		// CREATE
		userPersistence.persist(user);

		// READ
		User foundUser = userPersistence.find(id);

		assertThat(foundUser).isEqualsToByComparingFields(user);


		// UPDATE
		foundUser.setPasswordHash("secredPasswordHash");
		foundUser.setPasswordSalt("secredPasswordSalt");

		userPersistence.merge(foundUser);

		User updatedUser = userPersistence.find(id);

		assertThat(updatedUser).isEqualsToByComparingFields(foundUser);


		// DELETE
		userPersistence.remove(updatedUser);

		User notFoundUser = userPersistence.find(id);
		assertThat(notFoundUser).isNull();
	}


	@Test(expected = RollbackException.class)
	public void testEmailIsUnique() {
		User user = new User("yoda@example.org");
		userPersistence.persist(user);

		User userWithSameEmail = new User("yoda@example.org");

		// user has the same email address so an exception has to
		// be thrown.
		userPersistence.persist(userWithSameEmail);
	}

	@Test
	public void testHandlingOfTempEmailAddresses() {
		// yoda registeres an account
		User yoda = new User("yoda@example.org");
		userPersistence.persist(yoda);
		String userId = yoda.getId();

		Domain exampleDotDe = new Domain("example.de");
		domainPersistence.persist(exampleDotDe);

		// yoda creates this to temp email addresses ...
		TempEmailAddress yodaAtExampleDotDe = new TempEmailAddress("yoda",
				exampleDotDe);
		tempEmailAddressPersistence.persist(yodaAtExampleDotDe);
		TempEmailAddress test123AtExampleDotDe = new TempEmailAddress(
				"test123", exampleDotDe);
		tempEmailAddressPersistence.persist(test123AtExampleDotDe);

		// ... and adds them to his account
		yoda.addTempEmailAddresses(yodaAtExampleDotDe, test123AtExampleDotDe);

		userPersistence.merge(yoda);

		// successfully persisted everything was ;-)
		User foundUser = userPersistence.find(userId);
		assertThat(foundUser.getTempEmailAddresses()).hasSize(2).contains(
				yodaAtExampleDotDe, test123AtExampleDotDe);


		// lets remove this temp email address
		yoda.removeTempEmailAddresses(yodaAtExampleDotDe);
		userPersistence.merge(yoda);

		// in the account now only one temp email address is saved
		foundUser = userPersistence.find(userId);
		assertThat(foundUser.getTempEmailAddresses()).hasSize(1)
				.doesNotContain(yodaAtExampleDotDe)
				.contains(test123AtExampleDotDe);

		// the deleted temp email address is not used anymore ...
		TempEmailAddress notFoundAddress = tempEmailAddressPersistence
				.find(yodaAtExampleDotDe.getId());

		// and so it is removed from the database
		assertThat(notFoundAddress).isNull();

	}
}
