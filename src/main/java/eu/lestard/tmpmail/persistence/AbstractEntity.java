package eu.lestard.tmpmail.persistence;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * This class is used as a base class for entities.
 * 
 * The management of the Identifier that is needed for JPA-Entities is done in
 * this class so that entity classes that subclass this class don't need to
 * define an identifier on it's own.
 * 
 * @author manuel.mauky
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	private static final long serialVersionUID = -906169673642621559L;

	@Id
	private final String id;

	public AbstractEntity() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}


	@Override
	public final boolean equals(final Object o) {
		if (getId() == null) {
			return false;
		}

		if (o == null) {
			return false;
		}

		if (o == this) {
			return true;
		}

		if (!(o instanceof AbstractEntity)) {
			return false;
		}

		AbstractEntity other = (AbstractEntity) o;

		if (!getId().equals(other.getId())) {
			return false;
		}

		return true;
	}

	@Override
	public final int hashCode() {
		if (getId() == null) {
			return 0;
		} else {
			return getId().hashCode();
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id + "]";
	}
}
