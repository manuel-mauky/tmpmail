package eu.lestard.tmpmail.business;

import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public interface TempEmailAddressService {

	boolean addNewAddress(User user, TempEmailAddress address);

}
