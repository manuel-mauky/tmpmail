package eu.lestard.tmpmail.business;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import eu.lestard.tmpmail.persistence.Domain;

public class DomainServiceImpl implements DomainService {

	private final EntityManagerFactory emf;

	@Inject
	public DomainServiceImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public List<Domain> getAllDomains() {

		EntityManager entityManager = emf.createEntityManager();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Domain> criteriaQuery = criteriaBuilder.createQuery(Domain.class);

		Root<Domain> root = criteriaQuery.from(Domain.class);

		criteriaQuery.select(root);

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public void addDomain(Domain domain) {
		EntityManager entityManager = emf.createEntityManager();

		TypedQuery<Domain> query = entityManager.createNamedQuery(Domain.FIND_BY_DOMAIN_NAME, Domain.class);

		query.setParameter("domainName", domain.getDomainAsString());

		List<Domain> resultList = query.getResultList();

		if (resultList.isEmpty()) {
			entityManager.getTransaction().begin();
			entityManager.persist(domain);
			entityManager.getTransaction().commit();
		}
	}
}
