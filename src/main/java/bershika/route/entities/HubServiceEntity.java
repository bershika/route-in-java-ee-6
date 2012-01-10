package bershika.route.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="MEMBER_HUBSERVICE")
@IdClass(HubServiceId.class)
public class HubServiceEntity implements Serializable{
	@Id
	@NotNull
	@NotEmpty
	private String email;
	@Id
	@NotNull
	@NotEmpty
	private String city;
	@Id
	@NotNull
	@NotEmpty
	private String state;
	@Id
	private int rate;
	private String notes;
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
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getShortName(){
		return city + "," + state;
	}
	@Override
	public String toString() {
		return "HubServiceEntity [city=" + city + ", state=" + state
				+ ", rate=" + rate + ", notes=" + notes + ", email=" + email
				+ "]";
	}
	public HubServiceId getKey() {
		HubServiceId key = new HubServiceId();
		key.setCity(city);
		key.setState(state);
		key.setRate(rate);
		key.setEmail(email);
		return key;
	}

}
