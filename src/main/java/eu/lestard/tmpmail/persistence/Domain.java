package eu.lestard.tmpmail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({ @NamedQuery(name = Domain.FIND_BY_DOMAIN_NAME,
		query = "SELECT d FROM Domain d WHERE d.domainAsString LIKE :domainName") })
public class Domain extends AbstractEntity {

	public static final String FIND_BY_DOMAIN_NAME = "Domain.find_by_domain_name";

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
