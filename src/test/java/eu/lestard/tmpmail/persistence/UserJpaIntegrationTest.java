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

}
