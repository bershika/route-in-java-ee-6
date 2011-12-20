package bershika.route.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="ROUTE")
@IdClass(RouteId.class)
public class RouteEntity {
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
	private int distance;
	private String encPoints;
	
	@Transient
	public static final float METERS_IN_MILE = 1609.344F;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="hubName", referencedColumnName="city", insertable=false, updatable=false),
		@JoinColumn(name="hubState", referencedColumnName="state", insertable=false, updatable=false)})
	private HubEntity hub;
	
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumns({
		@JoinColumn(name="destName", referencedColumnName="city", insertable=false, updatable=false),
		@JoinColumn(name="destState", referencedColumnName="state", insertable=false, updatable=false)})
	private LocationEntity dest;

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

	public float getDistanceInMiles() {
		return distance / METERS_IN_MILE;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getEncPoints() {
		return encPoints;
	}

	public void setEncPoints(String encPoints) {
		this.encPoints = encPoints;
	}

	public HubEntity getHub() {
		return hub;
	}

	public void setHub(HubEntity hub) {
		this.hub = hub;
	}

	public LocationEntity getDest() {
		return dest;
	}

	public void setDest(LocationEntity dest) {
		this.dest = dest;
	}

	@Override
	public String toString() {
		return "RouteEntity [hubName=" + hubName + ", hubState=" + hubState
				+ ", destName=" + destName + ", destState=" + destState
				+ ", distance=" + distance + ", encPoints=" + encPoints + "]";
	}
	
}
