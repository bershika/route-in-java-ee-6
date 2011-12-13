package bershika.route.repository;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import bershika.route.controller.Member;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.LocationEntity;
import bershika.route.entities.MemberEntity;
import bershika.route.googleservice.GeocodeResponse;
import bershika.route.googleservice.GeocodeResponse.AddressComponent;
import bershika.route.googleservice.GoogleServiceHandler;

@Stateless
public class HubRepository implements Serializable {

	@Inject
	private EntityManager em;
	private GoogleServiceHandler google = new GoogleServiceHandler();

	@Inject
	@Member
	MemberEntity member;

	public List<HubServiceEntity> findServicesForHub(HubEntity hub) {
		System.out.println("Getting services for " + member.getEmail());
		Query query = em
				.createQuery("Select service from HubServiceEntity service "
						+ "where service.email = :email and city = :city and state = :state");
		query.setParameter("email", member.getEmail());

		query.setParameter("city", hub.getCity());
		query.setParameter("state", hub.getState());
		// CriteriaBuilder cb = em.getCriteriaBuilder();
		// CriteriaQuery<HubServiceEntity> criteria =
		// cb.createQuery(HubServiceEntity.class);
		// Root<HubServiceEntity> service =
		// criteria.from(HubServiceEntity.class);
		// criteria.where(cb.equal(service.get("email"), member.getEmail()));
		// criteria.where(cb.and(cb.equal(service.get("city"), hub.getCity())));
		// criteria.where(cb.and(cb.equal(service.get("state"),
		// hub.getState())));
		// return em.createQuery(criteria).getResultList();
		return query.getResultList();
	}

	public String createNewHub(String locName) {
		GeocodeResponse response = google.getGeocoding(locName);
		HubEntity hub = new HubEntity();
		LocationEntity location = new LocationEntity();
		String state = null, city = null;
		float lan, lng;
		for (AddressComponent addComp : response.results[0].address_components) {
			for (String type : addComp.types) {
				if (type.equalsIgnoreCase("administrative_area_level_1"))
					state = addComp.short_name;
			}
			for (String type : addComp.types) {
				if (type.equalsIgnoreCase("locality"))
					city = addComp.short_name;
			}
		}
		lan = response.results[0].geometry.location.lat;
		lng = response.results[0].geometry.location.lng;
		if (city != null && state != null) {
			location.setCity(city);
			location.setState(state);
			location.setLan(lan);
			location.setLng(lng);
			hub.setLocation(location);
			hub.setCity(city);
			hub.setState(state);
			try {
				em.persist(hub);
			} catch (Exception e) {

			}
		}
		return city + "," + state;

	}

}
