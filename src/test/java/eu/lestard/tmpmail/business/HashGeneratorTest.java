package eu.lestard.tmpmail.business;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class HashGeneratorTest {

	private static final String SALT_1 = "someRandomSalt";
	private static final String SALT_2 = "anotherRandomSalt";

	private static final String PASSWORD_1 = "mySecretPassword";
	private static final String PASSWORD_2 = "myOtherSecretPassword";


	private HashGenerator hashGenerator;

	@Before
	public void setup() {
		hashGenerator = new HashGenerator();
	}

	@Test
	public void testGenerateSalt() {
		String salt1 = hashGenerator.generateSalt();
		String salt2 = hashGenerator.generateSalt();

		assertThat(salt1).isNotEqualTo(salt2);

		assertThat(salt1.length()).isGreaterThan(15);
		assertThat(salt2.length()).isGreaterThan(15);
	}

	@Test
	public void testLenghtOfHashIsNotToSmall() {
		String hash1 = hashGenerator.generateHash(PASSWORD_1, SALT_1);

		assertThat(hash1.length()).isGreaterThan(20);
	}

	@Test
	public void testSameValuesGenerateSameHashs() {
		String hash1 = hashGenerator.generateHash(PASSWORD_1, SALT_1);
		String hash2 = hashGenerator.generateHash(PASSWORD_1, SALT_1);

		assertThat(hash1).isEqualTo(hash2);
	}

	@Test
	public void testDifferentPasswordAndSameSaltGeneratesDifferendHash() {
		String hash1 = hashGenerator.generateHash(PASSWORD_1, SALT_1);
		String hash2 = hashGenerator.generateHash(PASSWORD_2, SALT_1);

		assertThat(hash1).isNotEqualTo(hash2);
	}


	@Test
	public void testSamePasswordAnddifferendSaltGeneratesDifferendHash() {
		String hash1 = hashGenerator.generateHash(PASSWORD_1, SALT_1);
		String hash2 = hashGenerator.generateHash(PASSWORD_1, SALT_2);

		assertThat(hash1).isNotEqualTo(hash2);
	}
}
