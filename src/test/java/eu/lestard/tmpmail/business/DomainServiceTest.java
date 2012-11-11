package eu.lestard.tmpmail.business;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.JpaTestHelper;

public class DomainServiceTest {

	private JpaTestHelper jpaTestHelper;

	private DomainServiceImpl domainService;

	@Before
	public void setup() {
		jpaTestHelper = new JpaTestHelper();

		domainService = new DomainServiceImpl(jpaTestHelper.getEmf());
	}

	@Test
	public void testGetAllDomains() {
		Domain exampleDotOrg = new Domain("example.org");
		jpaTestHelper.persist(exampleDotOrg);

		Domain exampleDotDe = new Domain("example.de");
		jpaTestHelper.persist(exampleDotDe);

		List<Domain> allDomains = domainService.getAllDomains();

		assertThat(allDomains).hasSize(2).contains(exampleDotOrg, exampleDotDe);
	}


}
