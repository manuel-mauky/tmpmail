package eu.lestard.tmpmail.business;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

/**
 * This is a hash generator that generates hash values with salt and iterative
 * hashing (key stretching).
 * 
 * @author manuel.mauky
 * 
 */
public class HashGenerator {

	private static final int iterationCount = 100;

	public String generateSalt() {
		return RandomStringUtils.random(20);
	}

	public String generateHash(String password, String salt) {

		String hash = password + salt;

		for (int i = 0; i < iterationCount; i++) {
			hash = DigestUtils.sha512Hex(hash);
		}

		return hash;
	}
}
