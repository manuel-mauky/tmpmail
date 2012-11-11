package eu.lestard.tmpmail.business;

import eu.lestard.tmpmail.persistence.User;

public class UserServiceImpl implements UserService {

	@Override
	public boolean registerNewUser(String emailAddress, String password) {

		if (password.equals("geheim")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public User verifyPassword(String emailAddress, String password) {
		if (password.equals("geheim")) {
			return null;
		} else {
			return new User(emailAddress);
		}
	}

}
