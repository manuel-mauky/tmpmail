package eu.lestard.tmpmail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class represents a temporary email address.
 * 
 * @author manuel.mauky
 * 
 */
@Entity
@Table(name = "TEMP_EMAIL_ADDRESSES", uniqueConstraints = @UniqueConstraint(columnNames = { "localPart",
		"domainPart" }))
@NamedQueries({ @NamedQuery(
		name = TempEmailAddress.FIND_BY_LOCAL_AND_DOMAIN_PART,
		query = "SELECT t FROM TempEmailAddress t WHERE t.localPart LIKE :localpart AND t.domainPart.domainAsString LIKE :domainpart") })
public class TempEmailAddress extends AbstractEntity {

	public static final String FIND_BY_LOCAL_AND_DOMAIN_PART = "TempEmailAddress.find_by_local_and_domain_part";

	@NotBlank
	@Column
	private String localPart;

	@NotNull
	@JoinColumn(name = "domainPart")
	private Domain domainPart;


	public TempEmailAddress(final String localPart, final Domain domainPart) {
		this.localPart = localPart;
		this.domainPart = domainPart;
	}

	/**
	 * no-arg constructor is needed for JPA
	 */
	protected TempEmailAddress() {
	}

	public String getLocalPart() {
		return localPart;
	}

	public Domain getDomainPart() {
		return domainPart;
	}

}
