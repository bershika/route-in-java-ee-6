package bershika.route.entities;

import java.io.Serializable;

public class CatchmentId implements Serializable{
	private String hub;
	private String destination;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((hub == null) ? 0 : hub.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatchmentId other = (CatchmentId) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (hub == null) {
			if (other.hub != null)
				return false;
		} else if (!hub.equals(other.hub))
			return false;
		return true;
	}
	
}
