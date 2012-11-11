package eu.lestard.tmpmail.business;

import eu.lestard.tmpmail.persistence.User;

public interface UserService {

	boolean registerNewUser(String emailAddress, String password);

	User verifyPassword(String emailAddress, String password);
}
