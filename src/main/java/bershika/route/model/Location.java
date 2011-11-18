package bershika.route.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@IdClass(LocationId.class)
public class Location {
	@Id
	@NotNull
	@NotEmpty
	private String city;
	@Id
	@NotNull
	@NotEmpty
	private String state;
	private float lan;
	private float lng;
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
	public float getLan() {
		return lan;
	}
	public void setLan(float lan) {
		this.lan = lan;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	
	

}
