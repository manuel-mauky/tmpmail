package eu.lestard.tmpmail.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({ @NamedQuery(name = User.FIND_BY_TEMP_EMAIL_ADDRESS_ID,
		query = "SELECT u FROM User u join u.tempEmailAddresses e WHERE e.id = :id") })
public class User extends AbstractEntity {

	public static final String FIND_BY_TEMP_EMAIL_ADDRESS_ID = "User.find_by_temp_email_address_id";


	@Email
	@Column(unique = true)
	private String emailAddress;

	private String passwordHash;

	private String passwordSalt;


	@OneToMany(orphanRemoval = true)
	private final List<TempEmailAddress> tempEmailAddresses;

	public User(final String emailAddress) {
		this();
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

	public void addTempEmailAddresses(final TempEmailAddress... tempEmailAddress) {
		tempEmailAddresses.addAll(Arrays.asList(tempEmailAddress));
	}

	public void removeTempEmailAddresses(final TempEmailAddress... tempEmailAddresses) {
		this.tempEmailAddresses.removeAll(Arrays.asList(tempEmailAddresses));
	}

}
