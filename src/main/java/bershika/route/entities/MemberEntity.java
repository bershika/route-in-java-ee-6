package bershika.route.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="MEMBER")
public class MemberEntity implements Serializable {

	@Id
	@NotNull
	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String name;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="email", referencedColumnName="email")
	private SurchargeEntity surcharge;

	@OneToMany
	@JoinColumn(name="email", referencedColumnName="email")
	private List<HubServiceEntity> services;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SurchargeEntity getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(SurchargeEntity surcharge) {
		this.surcharge = surcharge;
	}

	public List<HubServiceEntity> getServices() {
		return services;
	}

	public void setServices(List<HubServiceEntity> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return "MemberEntity [email=" + email + ", name=" + name
				+ ", surcharge=" + surcharge + "]";
	}

}