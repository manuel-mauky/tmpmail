package eu.lestard.tmpmail.business;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public class TempEmailAddressServiceImpl implements TempEmailAddressService {

	private final EntityManagerFactory emf;

	@Inject
	public TempEmailAddressServiceImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}


	@Override
	public boolean addNewAddress(User user, TempEmailAddress address) {

		EntityManager entityManager = emf.createEntityManager();

		TypedQuery<TempEmailAddress> query = entityManager.createNamedQuery(
				TempEmailAddress.FIND_BY_LOCAL_AND_DOMAIN_PART, TempEmailAddress.class);

		query.setParameter("localpart", address.getLocalPart());
		query.setParameter("domainpart", address.getDomainPart().getDomainAsString());

		List<TempEmailAddress> resultList = query.getResultList();

		// The address must not be already persisted
		if (!resultList.isEmpty()) {
			return false;
		} else {
			entityManager.getTransaction().begin();
			entityManager.persist(address);

			user.addTempEmailAddresses(address);
			entityManager.merge(user);

			entityManager.getTransaction().commit();

			return true;
		}
	}


}
