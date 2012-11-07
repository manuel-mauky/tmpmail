package eu.lestard.tmpmail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;

/**
 * This class represents a user of the system.
 * 
 * @author manuel.mauky
 * 
 */
@Entity
@Table(name = "USERS")
public class User extends AbstractEntity {

	@Email
	@Column(unique = true)
	private String emailAddress;

	private String passwordHash;

	private String passwordSalt;

	public User(final String emailAddress) {
		this.emailAddress = emailAddress;
		passwordHash = "";
		passwordSalt = "";
	}

	/**
	 * no-argument constructor is needed for JPA
	 */
	protected User() {
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(final String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(final String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

}
