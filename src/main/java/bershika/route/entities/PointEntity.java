package bershika.route.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@IdClass(RouteId.class)
public class Point {
	@Id
	@NotNull
	@NotEmpty
	private String hubName;
	@Id
	@NotNull
	@NotEmpty
	private String hubState;
	@Id
	@NotNull
	@NotEmpty
	private String destName;
	@Id
	@NotNull
	@NotEmpty
	private String destState;
	private float rate;
	private boolean fake;
	private Date createdDate;
	
	@OneToOne
	private Route route;

	public String getHubName() {
		return hubName;
	}

	public void setHubName(String hubName) {
		this.hubName = hubName;
	}

	public String getHubState() {
		return hubState;
	}

	public void setHubState(String hubState) {
		this.hubState = hubState;
	}

	public String getDestName() {
		return destName;
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}

	public String getDestState() {
		return destState;
	}

	public void setDestState(String destState) {
		this.destState = destState;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public boolean isFake() {
		return fake;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}
