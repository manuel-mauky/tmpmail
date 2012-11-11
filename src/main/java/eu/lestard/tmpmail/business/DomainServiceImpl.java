package eu.lestard.tmpmail.business;

import java.util.ArrayList;
import java.util.List;

import eu.lestard.tmpmail.persistence.Domain;

public class DomainServiceImpl implements DomainService {

	private static List<Domain> domains = new ArrayList<Domain>();

	/**
	 * bad code. Only for testing the view!
	 */
	static {
		domains.add(new Domain("example.org"));
		domains.add(new Domain("example.de"));
	}

	@Override
	public List<Domain> getAllDomains() {

		return domains;
	}

}
