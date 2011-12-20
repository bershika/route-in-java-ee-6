package bershika.route.repository;

import java.util.Calendar;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import bershika.route.controller.Parser;
import bershika.route.entities.LocationEntity;
import bershika.route.entities.PointEntity;
import bershika.route.entities.RouteEntity;
import bershika.route.entities.RouteId;
import bershika.route.entities.SynonymEntity;
import bershika.route.googleservice.DirectionsResponse;
import bershika.route.googleservice.DirectionsResponse.Leg;
import bershika.route.googleservice.GoogleServiceHandler;
import bershika.route.googleservice.OverQueryLimitException;
import bershika.route.view.Messanger;

@Stateless
public class PointRegistration {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	public PointEntity createPoint(RouteId id, float rate) throws Exception {
		RouteEntity route = null;
		DirectionsResponse response = null;
		PointEntity point = null;
		SynonymEntity syn = null;
		if ((route = findRoute(id)) == null) {
			response = GoogleServiceHandler.getDirections(id.getHubName() + ","
					+ id.getHubState() + ",USA",
					id.getDestName() + "," + id.getDestState() + ",USA");
			if (!response.status.equalsIgnoreCase("OK")) {
				throw new OverQueryLimitException(response.status);
			}
			LocationEntity dest = new LocationEntity();
			// syn = null;
			route = new RouteEntity();
			Leg leg = response.routes[0].legs[0];
			String city = Parser.getCity(leg.end_address);
			String state = Parser.getState(leg.end_address);
			if (city.compareTo(id.getDestName()) != 0) {
				syn = new SynonymEntity();
				syn.setSynonym(id.getDestName());
				syn.setCity(city);
				try{
					
					em.merge(syn);
					//utx.commit();
				}
				catch(Exception e){
					}
				}
			dest.setCity(city);
			dest.setState(state);
			dest.setLan(leg.end_location.lat);
			dest.setLng(leg.end_location.lng);
			route.setDest(dest);
			route.setDestName(city);
			route.setDestState(state);
			route.setHubName(id.getHubName());
			route.setHubState(id.getHubState());
			route.setDistance(leg.distance.value);
		}
		
		return createPointForRoute(route, rate);
	}

	private RouteEntity findRoute(RouteId id) {
		RouteEntity route = null;
		if ((route = em.find(RouteEntity.class, id)) == null) {
			SynonymEntity synonym = null;
			try {
				if ((synonym = em.find(SynonymEntity.class, id.getDestName())) != null) {
					id.setDestName(synonym.getSynonym());
					return em.find(RouteEntity.class, id);
				}
			} catch (EntityNotFoundException e) {

			}

		}
		return route;
	}

	private PointEntity createPointForRoute(RouteEntity route, float rate) {
		PointEntity point = new PointEntity();
		point.setCreatedDate(Calendar.getInstance().getTime());
		point.setDestName(route.getDestName());
		point.setDestState(route.getDestState());
		point.setHubName(route.getHubName());
		point.setHubState(route.getHubState());
		point.setRate(rate);
		point.setRoute(route);
		System.out.print("Point to save " + point);
		try {
			// utx.begin();
			point = em.merge(point);
			// utx.commit();
		} catch (Exception e) {
			System.out.println("Error saving point " + e);
			e.printStackTrace();
		}
		return point;
	}

}