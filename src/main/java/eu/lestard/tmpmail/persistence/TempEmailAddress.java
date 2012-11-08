package eu.lestard.tmpmail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "TEMP_EMAIL_ADDRESSES", uniqueConstraints = @UniqueConstraint(columnNames = {
		"localPart", "domainPart" }))
public class TempEmailAddress extends AbstractEntity {

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
