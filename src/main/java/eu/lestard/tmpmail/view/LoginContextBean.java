package eu.lestard.tmpmail.view;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import eu.lestard.tmpmail.persistence.User;


@SessionScoped
@Named
public class LoginContextBean implements Serializable {
	private static final long serialVersionUID = 1290821019230341096L;

	private User currentUser;

	@Named
	@Produces
	public boolean isLoggedIn() {
		return currentUser != null;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
