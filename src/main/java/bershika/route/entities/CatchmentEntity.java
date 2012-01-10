package bershika.route.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CATCHMENT")
@IdClass(CatchmentId.class)
public class CatchmentEntity implements Serializable{
	@Id
	@Size(min = 2, max = 2)
	@Pattern(regexp = "[A-Z]{2}", message = "state must contain two upper letters")
	private String hub;
	@Id
	@Size(min = 2, max = 2)
	@Pattern(regexp = "[A-Z]{2}", message = "state must contain two upper letters")
	private String destination;

	public String getHub() {
		return hub;
	}

	public void setHub(String hub) {
		this.hub = hub;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "CatchmentEntity [hub=" + hub + ", destination=" + destination + "]";
	}

}
