package bershika.route.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="HUB")
@IdClass(LocationId.class)
public class HubEntity implements Serializable{
	@Id
	@NotNull
	@NotEmpty
	private String city;
	@Id
	@NotNull
	@NotEmpty
	private String state;
	private boolean manualMode;
	private float a1;
	private float a2;
	private float b1;
	private float b2;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="hubName", referencedColumnName="city"),
		@JoinColumn(name="hubState", referencedColumnName="state")})
	private Set<PointEntity>points;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	@PrimaryKeyJoinColumn
	LocationEntity location;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isManualMode() {
		return manualMode;
	}
	public void setManualMode(boolean manualMode) {
		this.manualMode = manualMode;
	}
	public float getA1() {
		return a1;
	}
	public void setA1(float a1) {
		this.a1 = a1;
	}
	public float getA2() {
		return a2;
	}
	public void setA2(float a2) {
		this.a2 = a2;
	}
	public float getB1() {
		return b1;
	}
	public void setB1(float b1) {
		this.b1 = b1;
	}
	public float getB2() {
		return b2;
	}
	public void setB2(float b2) {
		this.b2 = b2;
	}
	
	
	public Set<PointEntity> getPoints() {
		return points;
	}
	public void setPoints(Set<PointEntity> points) {
		this.points = points;
	}
	
	public LocationEntity getLocation() {
		return location;
	}
	public void setLocation(LocationEntity location) {
		this.location = location;
	}
	public String getShortName(){
		return city + "," + state;
	}

}
