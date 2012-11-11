package eu.lestard.tmpmail.business;

import java.util.List;

import eu.lestard.tmpmail.persistence.Domain;

public interface DomainService {

	List<Domain> getAllDomains();

	void addDomain(Domain domain);
}
