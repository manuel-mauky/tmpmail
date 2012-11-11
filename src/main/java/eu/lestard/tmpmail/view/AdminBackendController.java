package eu.lestard.tmpmail.view;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import eu.lestard.tmpmail.business.DomainService;
import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.config.StringKey;
import eu.lestard.tmpmail.config.internal.Configurator;
import eu.lestard.tmpmail.persistence.Domain;

/**
 * The responsibility of the {@link AdminBackendController} is to get
 * configuration parameter from the user and persist it in the
 * {@link Configurator} and to save {@link Domain} instances that the user
 * creates.
 * 
 * @author manuel.mauky
 * 
 */
@Model
public class AdminBackendController {

	private Configurator configurator;
	private DomainService domainService;


	private int outgoingSmtpPort;
	private String outgoingSmtpHost;
	private String outgoingSmtpUsername;
	private String outgoingSmtpPassword;
	private List<Domain> domains;
	private String newDomain;


	@Inject
	public AdminBackendController(Configurator configurator, DomainService domainService) {
		this();
		this.configurator = configurator;
		this.domainService = domainService;

		updateDomainList();
	}

	private void updateDomainList() {
		domains.clear();
		domains.addAll(domainService.getAllDomains());
	}

	protected AdminBackendController() {
		domains = new ArrayList<Domain>();
	}

	public String addDomain() {
		Domain domain = new Domain(newDomain);

		domainService.addDomain(domain);

		return "pretty:adminbackend";
	}



	public void save() {
		if (outgoingSmtpPort != 0) {
			configurator.setValue(IntKey.OUTGOING_SMTP_PORT, outgoingSmtpPort);
		}

		if (outgoingSmtpHost != null && !outgoingSmtpHost.isEmpty()) {
			configurator.setValue(StringKey.OUTGOING_SMTP_HOST, outgoingSmtpHost);
		}
		if (outgoingSmtpUsername != null && !outgoingSmtpUsername.isEmpty()) {
			configurator.setValue(StringKey.OUTGOING_SMTP_USERNAME, outgoingSmtpUsername);
		}
		if (outgoingSmtpPassword != null && !outgoingSmtpPassword.isEmpty()) {
			configurator.setValue(StringKey.OUTGOING_SMTP_PASSWORD, outgoingSmtpPassword);
		}
	}

	public int getOutgoingSmtpPort() {
		return outgoingSmtpPort;
	}


	public void setOutgoingSmtpPort(int outgoingSmtpPort) {
		this.outgoingSmtpPort = outgoingSmtpPort;
	}


	public String getOutgoingSmtpHost() {
		return outgoingSmtpHost;
	}


	public void setOutgoingSmtpHost(String outgoingSmtpHost) {
		this.outgoingSmtpHost = outgoingSmtpHost;
	}


	public String getOutgoingSmtpUsername() {
		return outgoingSmtpUsername;
	}


	public void setOutgoingSmtpUsername(String outgoingSmtpUsername) {
		this.outgoingSmtpUsername = outgoingSmtpUsername;
	}


	public String getOutgoingSmtpPassword() {
		return outgoingSmtpPassword;
	}


	public void setOutgoingSmtpPassword(String outgoingSmtpPassword) {
		this.outgoingSmtpPassword = outgoingSmtpPassword;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	public String getNewDomain() {
		return newDomain;
	}

	public void setNewDomain(String newDomain) {
		this.newDomain = newDomain;
	}

}
