package bershika.route.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@IdClass(RouteId.class)
public class Route {
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
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="hubName", referencedColumnName="city"),
		@JoinColumn(name="hubState", referencedColumnName="state")})
	private Hub hub;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="destName", referencedColumnName="city"),
		@JoinColumn(name="destState", referencedColumnName="state")})
	private Location dest;

}
