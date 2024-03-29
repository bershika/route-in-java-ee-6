package bershika.route.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="POINT")
@IdClass(PointId.class)
public class PointEntity implements Serializable, Comparable{
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
	@Id
	private int rate;

	private boolean fake;
	private Date createdDate;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
	@JoinColumns({
		@JoinColumn(name="hubName", referencedColumnName="hubName", insertable=false, updatable=false),
		@JoinColumn(name="hubState", referencedColumnName="hubState", insertable=false, updatable=false),
		@JoinColumn(name="destName", referencedColumnName="destName", insertable=false, updatable=false),
		@JoinColumn(name="destState", referencedColumnName="destState", insertable=false, updatable=false)
	})
	private RouteEntity route;

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

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
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

	public PointId getKey() {
		PointId key = new PointId();
		key.setDestName(destName);
		key.setDestState(destState);
		key.setHubName(hubName);
		key.setHubState(hubState);
		key.setRate(rate);
		return key;
	}

	public RouteEntity getRoute() {
		return route;
	}

	public void setRoute(RouteEntity route) {
		this.route = route;
	}

	@Override
	public int compareTo(Object o){
		return Math.round((getRoute().getDistanceInMiles())
				- ((PointEntity)o).getRoute().getDistanceInMiles());
	}

	@Override
	public String toString() {
		return "PointEntity [hubName=" + hubName + ", hubState=" + hubState
				+ ", destName=" + destName + ", destState=" + destState
				+ ", rate=" + rate + ", fake=" + fake + ", createdDate="
				+ createdDate  + "]";
	}

}
