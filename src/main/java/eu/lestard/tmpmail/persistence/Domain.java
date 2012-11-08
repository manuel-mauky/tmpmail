package eu.lestard.tmpmail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class encapsulates a domain that is managed by the software.
 * 
 * @author manuel.mauky
 * 
 */
@Entity
@Table(name = "DOMAINS")
public class Domain extends AbstractEntity {

	@NotBlank
	@Column(unique = true)
	private final String domainAsString;

	public Domain(final String domainAsString) {
		this.domainAsString = domainAsString;
	}

	/**
	 * no-argument constructor is needed for JPA
	 */
	protected Domain() {
		domainAsString = "";
	}

	public String getDomainAsString() {
		return domainAsString;
	}

}
