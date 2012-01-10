package bershika.route.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class RouteId implements Serializable{
	@Size(min = 1, max = 50)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String hubName;
	@Size(min = 2, max = 2)
	@Pattern(regexp = "[A-Z]{2}", message = "state must contain two upper letters")
	private String hubState;
	@Size(min = 1, max = 50)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String destName;
	@Size(min = 2, max = 2)
	@Pattern(regexp = "[A-Z]{2}", message = "state must contain two upper letters")
	private String destState;
	
	
	public void setHubName(String hubName) {
		this.hubName = hubName;
	}
	public void setHubState(String hubState) {
		this.hubState = hubState;
	}
	public void setDestName(String destName) {
		this.destName = destName;
	}
	public void setDestState(String destState) {
		this.destState = destState;
	}
	
	public String getHubName() {
		return hubName;
	}
	public String getHubState() {
		return hubState;
	}
	public String getDestName() {
		return destName;
	}
	public String getDestState() {
		return destState;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destName == null) ? 0 : destName.hashCode());
		result = prime * result
				+ ((destState == null) ? 0 : destState.hashCode());
		result = prime * result + ((hubName == null) ? 0 : hubName.hashCode());
		result = prime * result
				+ ((hubState == null) ? 0 : hubState.hashCode());
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
		RouteId other = (RouteId) obj;
		if (destName == null) {
			if (other.destName != null)
				return false;
		} else if (!destName.equals(other.destName))
			return false;
		if (destState == null) {
			if (other.destState != null)
				return false;
		} else if (!destState.equals(other.destState))
			return false;
		if (hubName == null) {
			if (other.hubName != null)
				return false;
		} else if (!hubName.equals(other.hubName))
			return false;
		if (hubState == null) {
			if (other.hubState != null)
				return false;
		} else if (!hubState.equals(other.hubState))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RouteId [from " + hubName + "," + hubState + " to " + destName + ","
				+ destState + "]";
	}
	
	
}
