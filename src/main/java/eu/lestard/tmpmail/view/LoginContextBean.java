package eu.lestard.tmpmail.view;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import eu.lestard.tmpmail.persistence.User;


/**
 * This class holds the currently logged in user.
 * 
 * @author manuel.mauky
 * 
 */
@SessionScoped
@Named
public class LoginContextBean implements Serializable {
	private static final long serialVersionUID = 1290821019230341096L;

	private User currentUser;

	/**
	 * This method can be used to ask whether the user is logged in or not.
	 * 
	 * With the @Named and @Produces annotation this value is available in the
	 * EL-Context of JSF.
	 * 
	 * To access it you can simply write something like:
	 * 
	 * <code>
	 *  rendered="#{loggedIn}"
	 * </code>
	 * 
	 * 
	 * @return
	 */
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
