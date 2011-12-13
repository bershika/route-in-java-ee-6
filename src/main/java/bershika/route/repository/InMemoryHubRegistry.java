package bershika.route.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bershika.route.entities.HubEntity;
import bershika.route.entities.LocationId;

@SessionScoped
public class InMemoryHubRegistry implements Serializable{
	@Inject
	private EntityManager em;

	private Map<String,HubEntity> hubs;

	@Produces
	@Named
	public List<String> getHubs() {
		return new ArrayList<String>(hubs.keySet());
	}

	public void onHubListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final HubEntity hub) {
		retrieveAllHubsOrderedByName();
	}

	@PostConstruct
	public void retrieveAllHubsOrderedByName() {
		hubs = new HashMap<String, HubEntity>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HubEntity> criteria = cb.createQuery(HubEntity.class);
		Root<HubEntity> hub = criteria.from(HubEntity.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
		//criteria.select(hub).orderBy(cb.asc(hub.get("state")));
		for(HubEntity hubEntity: em.createQuery(criteria).getResultList()){
			hubs.put(hubEntity.getShortName(), hubEntity);
		}
	}
	
	public HubEntity findHubByName(String name){
		return hubs.get(name);
	}
	
	public HubEntity getFirst(){
		return hubs.values().iterator().next();
	}
	
	public HubEntity findDbHubByName(String city, String state){
		LocationId id = new LocationId();
		id.setCity(city);
		id.setState(state);
		HubEntity entity = em.find(HubEntity.class, id);
		hubs.put(entity.getShortName(), entity);
		return entity;
	}
}
