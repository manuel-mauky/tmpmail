package eu.lestard.tmpmail.persistence;

import static org.fest.assertions.api.Assertions.assertThat;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class AbstractEntityTest {

	/**
	 * This test case verifies that the Id of the AbstractEntity class is
	 * created correctly. Correctly means:
	 * <ul>
	 * <li>The Id is not null
	 * <li>The Id is not an empty string
	 * <li>The Id is unique
	 * </ul>
	 */
	@Test
	public void testGenerationOfIds() {
		// first I create two instances of the class. As the name says,
		// AbstractEntity is an abstract class but for this test case it is ok
		// to use anonymous inner classes.
		AbstractEntity entityOne = new AbstractEntity() {
		};

		AbstractEntity entityTwo = new AbstractEntity() {
		};

		assertThat(entityOne.getId()).isNotNull();
		assertThat(entityTwo.getId()).isNotNull();

		assertThat(entityOne.getId()).isNotEmpty();
		assertThat(entityTwo.getId()).isNotEmpty();

		assertThat(entityOne.getId()).isNotEqualTo(entityTwo.getId());
	}

	/**
	 * To check the equals and hashCode method I use the tool "EqualsVerifier".
	 * It checks both methods for correct behavior.
	 */
	@Test
	public void testEqualsAndHashcode() {
		EqualsVerifier.forClass(AbstractEntity.class).verify();

	}

	/**
	 * This test is used to verify the toString method. I like to see the class
	 * name of the concrete subclass of AbstractEntity and the identifier in the
	 * output of the toString method.
	 */
	@Test
	public void testToString() {
		// this time I use a "non-anonymous" inner class so I can define a name
		class MyEntity extends AbstractEntity {
		}

		AbstractEntity entityOne = new MyEntity();

		assertThat(entityOne.toString()).contains("MyEntity");
		assertThat(entityOne.toString()).contains(entityOne.getId());
	}

}
