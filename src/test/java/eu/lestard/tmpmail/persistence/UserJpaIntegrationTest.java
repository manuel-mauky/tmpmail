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

	private JpaTestHelper jpaTestHelper;

	@Before
	public void setup() {
		jpaTestHelper = new JpaTestHelper();
	}

	@After
	public void tearDown() {
		jpaTestHelper.tearDown();
	}

	@Test
	public void testCRUD() {
		User user = new User("yoda@example.org");

		String id = user.getId();

		// CREATE
		jpaTestHelper.persist(user);

		// READ
		User foundUser = jpaTestHelper.find(id, User.class);

		assertThat(foundUser).isEqualsToByComparingFields(user);


		// UPDATE
		foundUser.setPasswordHash("secredPasswordHash");
		foundUser.setPasswordSalt("secredPasswordSalt");

		jpaTestHelper.merge(foundUser);

		User updatedUser = jpaTestHelper.find(id, User.class);

		assertThat(updatedUser).isEqualsToByComparingFields(foundUser);


		// DELETE
		jpaTestHelper.remove(updatedUser, User.class);

		User notFoundUser = jpaTestHelper.find(id, User.class);
		assertThat(notFoundUser).isNull();
	}


	@Test(expected = RollbackException.class)
	public void testEmailIsUnique() {
		User user = new User("yoda@example.org");
		jpaTestHelper.persist(user);

		User userWithSameEmail = new User("yoda@example.org");

		// user has the same email address so an exception has to
		// be thrown.
		jpaTestHelper.persist(userWithSameEmail);
	}

	@Test
	public void testHandlingOfTempEmailAddresses() {
		// yoda registeres an account
		User yoda = new User("yoda@example.org");
		jpaTestHelper.persist(yoda);
		String userId = yoda.getId();

		Domain exampleDotDe = new Domain("example.de");
		jpaTestHelper.persist(exampleDotDe);

		// yoda creates this to temp email addresses ...
		TempEmailAddress yodaAtExampleDotDe = new TempEmailAddress("yoda", exampleDotDe);
		jpaTestHelper.persist(yodaAtExampleDotDe);
		TempEmailAddress test123AtExampleDotDe = new TempEmailAddress("test123", exampleDotDe);
		jpaTestHelper.persist(test123AtExampleDotDe);

		// ... and adds them to his account
		yoda.addTempEmailAddresses(yodaAtExampleDotDe, test123AtExampleDotDe);

		jpaTestHelper.merge(yoda);

		// successfully persisted everything was ;-)
		User foundUser = jpaTestHelper.find(userId, User.class);
		assertThat(foundUser.getTempEmailAddresses()).hasSize(2).contains(yodaAtExampleDotDe,
				test123AtExampleDotDe);


		// lets remove this temp email address
		yoda.removeTempEmailAddresses(yodaAtExampleDotDe);
		jpaTestHelper.merge(yoda);

		// in the account now only one temp email address is saved
		foundUser = jpaTestHelper.find(userId, User.class);
		assertThat(foundUser.getTempEmailAddresses()).hasSize(1).doesNotContain(yodaAtExampleDotDe)
				.contains(test123AtExampleDotDe);

		// the deleted temp email address is not used anymore ...
		TempEmailAddress notFoundAddress = jpaTestHelper.find(yodaAtExampleDotDe.getId(), TempEmailAddress.class);

		// and so it is removed from the database
		assertThat(notFoundAddress).isNull();

	}
}
