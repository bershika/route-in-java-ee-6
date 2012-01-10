package bershika.route.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
@Entity
@Table(name="SYNONYM")
public class SynonymEntity implements Serializable {
	@Id
	@NotNull
	@NotEmpty
	private String synonym;
	@NotNull
	@NotEmpty
	private String city;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSynonym() {
		return synonym;
	}
	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}
	
}
