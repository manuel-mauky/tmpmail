package eu.lestard.tmpmail.view;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import eu.lestard.tmpmail.business.UserService;
import eu.lestard.tmpmail.persistence.User;

@Model
public class LoginController {

	@Email
	@NotEmpty
	private String emailAddress;

	@NotEmpty
	@Length(min = 5, max = 30)
	private String password;


	private UserService userService;

	private LoginContextBean loginContext;

	@Inject
	public LoginController(UserService userService, LoginContextBean loginContext) {
		this.userService = userService;
		this.loginContext = loginContext;
	}

	protected LoginController() {
	}

	public String login() {
		User user = userService.verifyPassword(emailAddress, password);

		if (user == null) {
			return "pretty:loginFailed";
		} else {
			loginContext.setCurrentUser(user);
			return "pretty:home";
		}
	}

	public String logout() {
		loginContext.setCurrentUser(null);
		return "pretty:home";
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
