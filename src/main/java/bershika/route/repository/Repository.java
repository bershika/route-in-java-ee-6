package bershika.route.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import bershika.route.controller.Member;
import bershika.route.controller.Parser;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.LocationId;
import bershika.route.entities.MemberEntity;
import bershika.route.entities.PointEntity;
import bershika.route.entities.RouteEntity;
import bershika.route.entities.RouteId;
import bershika.route.entities.SurchargeEntity;
import bershika.route.entities.SynonymEntity;
import bershika.route.googlehandler.RouteInfo;

@Stateless
public class Repository implements Serializable {
	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	@Member
	MemberEntity member;

	public PointEntity savePoint(RouteEntity route, int rate) throws Exception {
		PointEntity point = createPointForRoute(route, rate);
		try {
			point = em.merge(point);
		} catch (Exception e) {
			System.out.println("Error saving point " + e);
			e.printStackTrace();
		}
		return point;
	}

	public void saveSyn(SynonymEntity syn) {
		try {
			em.merge(syn);
		} catch (Exception e) {
			System.out.println("Error saving route " + e);
			e.printStackTrace();
		}
	}

	public RouteEntity findRoute(RouteId id) {
		RouteEntity route = null;
		try {
			route = em.find(RouteEntity.class, id);
		} catch (EntityNotFoundException e) {
			System.out.println("Route not found " + id);
		}
		
		if (route == null) {
			SynonymEntity synonym = null;
			try {
				if ((synonym = em.find(SynonymEntity.class, id.getDestName())) != null) {
					id.setDestName(synonym.getSynonym());
					route = em.find(RouteEntity.class, id);
				}
			} catch (EntityNotFoundException e) {}
		}
		return route;
	}

	private PointEntity createPointForRoute(RouteEntity route, int rate) {
		PointEntity point = new PointEntity();
		point.setCreatedDate(Calendar.getInstance().getTime());
		point.setDestName(route.getDestName());
		point.setDestState(route.getDestState());
		point.setHubName(route.getHubName());
		point.setHubState(route.getHubState());
		point.setRate(rate);
		point.setRoute(route);
		return point;

	}

	public RouteEntity saveRoute(RouteEntity route, RouteId userInput) {
		if (route.getDestName().compareTo(userInput.getDestName()) != 0) {
			SynonymEntity syn = new SynonymEntity();
			syn.setSynonym(userInput.getDestName());
			syn.setCity(route.getDestName());
			saveSyn(syn);
		}
		try {

			if (findRoute(route.getKey()) == null) {
				System.out.println(route.getKey() + " is null");
				em.persist(route);
				route = em.find(RouteEntity.class, route.getKey());
			}

		} catch (Exception e) {
			System.out.println("Error saving route " + e);
			e.printStackTrace();
		}
		return route;
	}

	public List<HubServiceEntity> findServicesForHub(HubEntity hub) {
		Query query = em.createQuery("Select service from HubServiceEntity service "
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

	public HubEntity createNewHub(HubEntity hub) throws EntityExistsException {
		LocationId id = hub.getKey();
		if (em.find(HubEntity.class, id) != null) {
			throw new EntityExistsException();
		}
		if (hub != null)
			try {
				em.persist(hub);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		return em.find(HubEntity.class, id);
	}

	public void saveHub(HubEntity entity) {
		entity = em.merge(entity);
	}

	public void saveService(HubServiceEntity entity) {
		entity = em.merge(entity);
		System.out.println("Saving service " + entity);
	}

	public void deleteService(HubServiceEntity entity) {
		entity = em.find(HubServiceEntity.class, entity.getKey());
		if (entity != null) {
			em.remove(entity);
		}
	}

	public void savePoint(PointEntity entity) {
		System.out.println("Point to save " + entity + "/ " + entity.getKey());
		PointEntity fromDb = em.find(PointEntity.class, entity.getKey());
		System.out.println("Point to save " + fromDb);
		if (fromDb != null) {
			fromDb.setFake(entity.isFake());
			entity = em.merge(fromDb);
		}
	}

	public void deletePoint(PointEntity entity) {
		entity = em.find(PointEntity.class, entity.getKey());
		em.remove(entity);
	}

	public void saveSurcharge(SurchargeEntity entity) {
		entity = em.merge(entity);
	}

	public List<RouteEntity> saveRoutes(List<RouteInfo> routeInfoList) {
		List<RouteEntity> routeList = new ArrayList<RouteEntity>();
		for (RouteInfo routeInfo : routeInfoList) {
			System.out.println(routeInfo);
			RouteEntity route = new RouteEntity();
			RouteId userInput = new RouteId();
			route.setDestName(Parser.getCity(routeInfo.getGoogleTo()));
			route.setDestState(Parser.getState(routeInfo.getGoogleTo()));
			route.setHubName(Parser.getCity(routeInfo.getGoogleFrom()));
			route.setHubState(Parser.getState(routeInfo.getGoogleFrom()));
			route.setDistance(routeInfo.getDistanceInMeters());
			userInput.setDestName(Parser.getCity(routeInfo.getUserTo()));
			userInput.setDestState(Parser.getState(routeInfo.getUserTo()));
			userInput.setHubName(Parser.getCity(routeInfo.getUserFrom()));
			userInput.setHubState(Parser.getState(routeInfo.getUserFrom()));
			if ((route = saveRoute(route, userInput)) != null)
				routeList.add(route);
		}
		return routeList;
	}
}
