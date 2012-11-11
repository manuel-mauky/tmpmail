package eu.lestard.tmpmail.business;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;

import eu.lestard.tmpmail.persistence.JpaTestHelper;
import eu.lestard.tmpmail.persistence.User;

public class UserServiceTest {

	private static final String WRONG_HASH = "WrongHash";

	private static final String WRONG_PASSWORD = "wrongPassword";

	private static final String EMAIL_ADDRESS = "test@example.org";

	private static final String HASH = "TheUnpredictableHash";

	private static final String MY_SECRET_PASSWORD = "mySecretPassword";

	private static final String SALT = "myRandomSalt";

	private JpaTestHelper jpaTestHelper;

	private UserServiceImpl userService;

	private HashGenerator hashGeneratorMock;

	@Before
	public void setup() {
		jpaTestHelper = new JpaTestHelper();

		hashGeneratorMock = mock(HashGenerator.class);

		userService = new UserServiceImpl(jpaTestHelper.getEmf(), hashGeneratorMock);

		when(hashGeneratorMock.generateSalt()).thenReturn(SALT);
		when(hashGeneratorMock.generateHash(MY_SECRET_PASSWORD, SALT)).thenReturn(HASH);
		when(hashGeneratorMock.generateHash(WRONG_PASSWORD, SALT)).thenReturn(WRONG_HASH);

		jpaTestHelper.cleanDatabase();
	}

	@Test
	public void tearDown() {
		jpaTestHelper.tearDown();
	}

	@Test
	public void testRegisterNewUser() {

		boolean result = userService.registerNewUser(EMAIL_ADDRESS, MY_SECRET_PASSWORD);
		assertThat(result).isTrue();

		User user = findUserByEmailAddress(EMAIL_ADDRESS);

		assertThat(user).isNotNull();
		assertThat(user.getEmailAddress()).isEqualTo(EMAIL_ADDRESS);
		assertThat(user.getPasswordSalt()).isEqualTo(SALT);
		assertThat(user.getPasswordHash()).isEqualTo(HASH);

	}

	@Test
	public void testRegisterNewUserFailEmailAlreadyInUse() {
		User user1 = new User(EMAIL_ADDRESS);
		jpaTestHelper.persist(user1);


		boolean result = userService.registerNewUser(EMAIL_ADDRESS, MY_SECRET_PASSWORD);
		assertThat(result).isFalse();
	}


	@Test
	public void testVerifyPassword() {
		User user = new User(EMAIL_ADDRESS);
		user.setPasswordHash(HASH);
		user.setPasswordSalt(SALT);

		jpaTestHelper.persist(user);


		User verifiedUser = userService.verifyPassword(EMAIL_ADDRESS, MY_SECRET_PASSWORD);

		assertThat(verifiedUser).isEqualTo(user);
	}

	@Test
	public void testVerifyPasswordWrongPassword() {
		User user = new User(EMAIL_ADDRESS);
		user.setPasswordHash(HASH);
		user.setPasswordSalt(SALT);
		jpaTestHelper.persist(user);


		User verifiedUser = userService.verifyPassword(EMAIL_ADDRESS, WRONG_PASSWORD);

		assertThat(verifiedUser).isNull();
	}

	@Test
	public void testVerifyPassworWrongEmailAddress() {
		User user = new User(EMAIL_ADDRESS);
		user.setPasswordHash(HASH);
		user.setPasswordSalt(SALT);
		jpaTestHelper.persist(user);


		User verifiedUser = userService.verifyPassword("wrong@example.org", MY_SECRET_PASSWORD);

		assertThat(verifiedUser).isNull();

	}



	/**
	 * Little helper method to find a user by his email address.
	 */
	private User findUserByEmailAddress(String emailAddress) {

		EntityManager entityManager = jpaTestHelper.getEntityManager();
		TypedQuery<User> query = entityManager.createNamedQuery(User.FIND_BY_EMAIL_ADDRESS, User.class);

		query.setParameter("emailAddress", emailAddress);

		return query.getResultList().get(0);
	}
}
