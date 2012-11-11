package eu.lestard.tmpmail.view;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import eu.lestard.tmpmail.business.UserService;

@Model
public class RegistrationController {

	@Email
	@NotEmpty
	private String emailAddress;

	@NotNull
	@Length(min = 5, max = 30)
	private String password;

	private UserService userService;

	@Inject
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}

	protected RegistrationController() {
	}


	public String startRegistration() {
		boolean result = userService.registerNewUser(emailAddress, password);

		if (result) {
			return "pretty:registrationSuccessful";
		} else {
			return "pretty:registrationFailed";
		}
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
