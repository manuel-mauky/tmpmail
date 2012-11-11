package eu.lestard.tmpmail.view;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import eu.lestard.tmpmail.business.TempEmailAddressService;
import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.TempEmailAddress;

/**
 * This class is a controller that handles the user input for creating new
 * {@link TempEmailAddress} instances.
 * 
 * @author manuel.mauky
 * 
 */
@Model
public class TempEmailController {

	private String localPart;

	private String domainId;


	private LoginContextBean loginContext;

	private TempEmailAddressService tempEmailAddressService;

	private DomainController domainController;


	@Inject
	public TempEmailController(LoginContextBean loginContext, TempEmailAddressService tempEmailAddressService,
			DomainController domainController) {
		this.loginContext = loginContext;
		this.tempEmailAddressService = tempEmailAddressService;
		this.domainController = domainController;
	}

	protected TempEmailController() {
	}

	public String addTempEmail() {
		Domain domain = null;
		for (Domain d : domainController.getAvailableDomains()) {
			if (d.getId().equals(domainId)) {
				domain = d;
			}
		}

		if (domain == null) {
			return null;
		}

		TempEmailAddress address = new TempEmailAddress(localPart, domain);
		boolean successful = tempEmailAddressService.addNewAddress(loginContext.getCurrentUser(), address);

		if (!successful) {
			FacesMessage fm = new FacesMessage("Fehler", "Es ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, fm);
		}

		return "pretty:home";
	}

	public String getLocalPart() {
		return localPart;
	}

	public void setLocalPart(String localPart) {
		this.localPart = localPart;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

}
