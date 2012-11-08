package eu.lestard.tmpmail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.lestard.tmpmail.config.Configurator;
import eu.lestard.tmpmail.config.IntKey;
import eu.lestard.tmpmail.core.MailInputListener;
import eu.lestard.tmpmail.persistence.Domain;
import eu.lestard.tmpmail.persistence.JpaTestHelper;
import eu.lestard.tmpmail.persistence.TempEmailAddress;
import eu.lestard.tmpmail.persistence.User;

/**
 * This test should give an overview of the application process.
 * 
 * @author manuel.mauky
 * 
 */
public class ScenarioTest {

	private JpaTestHelper<User> userPersistence;
	private JpaTestHelper<Domain> domainPersistence;
	private JpaTestHelper<TempEmailAddress> tempEmailAddressPersistence;


	private Domain exampleDotOrg;

	private Domain localhost;

	private Configurator configurator;
	private MailInputListener mailInputListener;
	private User user;

	@Before
	public void setup() {
		userPersistence = new JpaTestHelper<User>();
		userPersistence.init(User.class);

		domainPersistence = new JpaTestHelper<Domain>();
		domainPersistence.init(Domain.class);

		tempEmailAddressPersistence = new JpaTestHelper<TempEmailAddress>();
		tempEmailAddressPersistence.init(TempEmailAddress.class);
	}

	@After
	public void tearDown() {
		userPersistence.tearDown();
		domainPersistence.tearDown();
		tempEmailAddressPersistence.tearDown();
	}

	@Test
	public void testScenario() {
		defineDomainsThatAreHandled();

		configureSmtpServer();

		startMailInputListener();

		createAnUserAccount();

		createSomeTempMailMappings();

		sendAnEmailToAMappedEmailAddress();

		verifyThatTheEmailWasForwarded();

		sendAnEmailToANotMappedEmailAddress();

		verifyThatNoEmailWasForwarded();
	}

	private void defineDomainsThatAreHandled() {
		exampleDotOrg = new Domain("example.org");
		domainPersistence.persist(exampleDotOrg);

		localhost = new Domain("localhost");
		domainPersistence.persist(localhost);
	}

	private void configureSmtpServer() {
		configurator.setValue(IntKey.SMTP_PORT, 25000);
	}

	private void startMailInputListener() {
		mailInputListener.start();
	}

	private void createAnUserAccount() {
		user = new User("testUser");
		userPersistence.persist(user);
	}

	private void createSomeTempMailMappings() {
		TempEmailAddress test123 = new TempEmailAddress("test123",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(test123);
		TempEmailAddress test456 = new TempEmailAddress("test456",
				exampleDotOrg);
		tempEmailAddressPersistence.persist(test456);

		user.addTempEmailAddresses(test123, test456);

		userPersistence.merge(user);
	}

	private void sendAnEmailToAMappedEmailAddress() {

	}

	private void verifyThatTheEmailWasForwarded() {
		// TODO Auto-generated method stub

	}

	private void sendAnEmailToANotMappedEmailAddress() {
		// TODO Auto-generated method stub

	}

	private void verifyThatNoEmailWasForwarded() {
		// TODO Auto-generated method stub

	}

}
