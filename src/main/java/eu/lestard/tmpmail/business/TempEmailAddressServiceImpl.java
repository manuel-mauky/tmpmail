package eu.lestard.tmpmail.business;

import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

public class TempEmailAddressServiceImpl implements TempEmailAddressService {

	@Override
	public boolean addNewAddress(User user, TempEmailAddress address) {
		if (address.getLocalPart().equals("geheim")) {
			return false;
		} else {
			user.addTempEmailAddresses(address);
			return true;
		}
	}


}
