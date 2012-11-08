package eu.lestard.tmpmail.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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


	@OneToMany(orphanRemoval = true)
	private List<TempEmailAddress> tempEmailAddresses;

	public User(final String emailAddress) {
		this.emailAddress = emailAddress;
		passwordHash = "";
		passwordSalt = "";
	}

	/**
	 * no-argument constructor is needed for JPA
	 */
	protected User() {
		tempEmailAddresses = new ArrayList<TempEmailAddress>();
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

	public List<TempEmailAddress> getTempEmailAddresses() {
		return Collections.unmodifiableList(tempEmailAddresses);
	}

	public void addTempEmailAddresses(
			final TempEmailAddress... tempEmailAddress) {
		tempEmailAddresses.addAll(Arrays.asList(tempEmailAddress));
	}

	public void removeTempEmailAddresses(
			final TempEmailAddress... tempEmailAddresses) {
		this.tempEmailAddresses.removeAll(Arrays.asList(tempEmailAddresses));
	}

}
