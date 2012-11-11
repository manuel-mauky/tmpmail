package eu.lestard.tmpmail.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import eu.lestard.tmpmail.business.DomainService;
import eu.lestard.tmpmail.persistence.Domain;

@Model
public class DomainController {
	private final List<Domain> availableDomains;

	private DomainService domainService;

	@Inject
	public DomainController(DomainService domainService) {
		this();
		this.domainService = domainService;
	}

	protected DomainController() {
		availableDomains = new ArrayList<Domain>();
	}

	@PostConstruct
	protected void setup() {
		availableDomains.clear();
		availableDomains.addAll(domainService.getAllDomains());
	}

	public List<Domain> getAvailableDomains() {
		return availableDomains;
	}
}
