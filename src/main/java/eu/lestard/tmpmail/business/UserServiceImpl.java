package eu.lestard.tmpmail.business;

import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public class UserServiceImpl implements UserService {

	@Override
	public boolean registerNewUser(String emailAddress, String password) {
		return password.equals("geheim");
	}

	@Override
	public User verifyPassword(String emailAddress, String password) {
		if (password.equals("geheim")) {
			return null;
		} else {
			User user = new User(emailAddress);
			Domain domain = new Domain("example.org");

			TempEmailAddress address1 = new TempEmailAddress("test123", domain);
			TempEmailAddress address2 = new TempEmailAddress("anotherTest", domain);
			user.addTempEmailAddresses(address1, address2);



			return user;
		}
	}

}
