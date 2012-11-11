package eu.lestard.tmpmail.business;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import eu.lestard.tmpmail.persistence.User;

public class UserServiceImpl implements UserService {

	private final EntityManagerFactory emf;

	private final HashGenerator hashGenerator;

	@Inject
	public UserServiceImpl(EntityManagerFactory emf, HashGenerator hashGenerator) {
		this.emf = emf;
		this.hashGenerator = hashGenerator;
	}

	@Override
	public boolean registerNewUser(String emailAddress, String password) {

		if (findUserByEmailAddress(emailAddress) != null) {
			return false;
		}

		User user = new User(emailAddress);

		String salt = hashGenerator.generateSalt();
		String hash = hashGenerator.generateHash(password, salt);

		user.setPasswordSalt(salt);
		user.setPasswordHash(hash);

		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist(user);

		entityManager.getTransaction().commit();

		return true;
	}


	@Override
	public User verifyPassword(String emailAddress, String password) {
		User user = findUserByEmailAddress(emailAddress);

		if (user == null) {
			return null;
		}

		String hash = user.getPasswordHash();
		String salt = user.getPasswordSalt();

		String generatedHash = hashGenerator.generateHash(password, salt);

		if (generatedHash.equals(hash)) {
			return user;
		} else {
			return null;
		}
	}


	private User findUserByEmailAddress(String emailAddress) {
		final EntityManager entityManager = emf.createEntityManager();

		TypedQuery<User> query = entityManager.createNamedQuery(User.FIND_BY_EMAIL_ADDRESS, User.class);

		query.setParameter("emailAddress", emailAddress);

		final List<User> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}
}
