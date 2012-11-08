package eu.lestard.tmpmail.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.persistence.RollbackException;

import org.junit.Before;
import org.junit.Test;


/**
 * This integration test verifies that the {@link User} entity can be used as
 * JPA entity without problems.
 * 
 * @author manuel.mauky
 * 
 */
public class UserJpaIntegrationTest extends JpaTestHelper<User> {

	@Before
	public void setup() {
		init(User.class);
	}

	@Test
	public void testCRUD() {
		User user = new User("yoda@example.org");

		String id = user.getId();

		// CREATE
		persist(user);

		// READ
		User foundUser = find(id);

		assertThat(foundUser).isEqualsToByComparingFields(user);


		// UPDATE
		foundUser.setPasswordHash("secredPasswordHash");
		foundUser.setPasswordSalt("secredPasswordSalt");

		merge(foundUser);

		User updatedUser = find(id);

		assertThat(updatedUser).isEqualsToByComparingFields(foundUser);


		// DELETE
		remove(updatedUser);

		User notFoundUser = find(id);
		assertThat(notFoundUser).isNull();
	}


	@Test(expected = RollbackException.class)
	public void testEmailIsUnique() {
		User user = new User("yoda@example.org");
		persist(user);

		User userWithSameEmail = new User("yoda@example.org");

		// user has the same email address so an exception has to
		// be thrown.
		persist(userWithSameEmail);
	}

	@Test
	public void testHandlingOfTempEmailAddresses() {
		// yoda registeres an account
		User yoda = new User("yoda@example.org");
		persist(yoda);
		String userId = yoda.getId();

		Domain exampleDotDe = new Domain("example.de");
		persist(exampleDotDe);

		// yoda creates this to temp email addresses ...
		TempEmailAddress yodaAtExampleDotDe = new TempEmailAddress("yoda",
				exampleDotDe);
		persist(yodaAtExampleDotDe);
		TempEmailAddress test123AtExampleDotDe = new TempEmailAddress(
				"test123", exampleDotDe);
		persist(test123AtExampleDotDe);

		// ... and adds them to his account
		yoda.addTempEmailAddresses(yodaAtExampleDotDe, test123AtExampleDotDe);

		merge(yoda);

		// successfully persisted everything was ;-)
		User foundUser = find(userId);
		assertThat(foundUser.getTempEmailAddresses()).hasSize(2).contains(
				yodaAtExampleDotDe, test123AtExampleDotDe);


		// lets remove this temp email address
		yoda.removeTempEmailAddresses(yodaAtExampleDotDe);
		merge(yoda);

		// in the account now only one temp email address is saved
		foundUser = find(userId);
		assertThat(foundUser.getTempEmailAddresses()).hasSize(1)
				.doesNotContain(yodaAtExampleDotDe)
				.contains(test123AtExampleDotDe);

		// the deleted temp email address is not used anymore ...
		TempEmailAddress notFoundAddress = find(TempEmailAddress.class,
				yodaAtExampleDotDe.getId());

		// and so it is removed from the database
		assertThat(notFoundAddress).isNull();

	}
}
