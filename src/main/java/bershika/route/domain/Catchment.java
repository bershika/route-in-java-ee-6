package bershika.route.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import bershika.route.entities.CatchmentEntity;

@Singleton
@Startup
public class Catchment {
	@Inject
	private EntityManager em;
	
	private Map<State, Set<State>> destinations;
	private Map<State, Set<State>> hubs;
	
	public Catchment(){
		destinations = new HashMap<State, Set<State>>();
		initMap(destinations);
		hubs = new HashMap<State, Set<State>>();
		initMap(hubs);
	}
	
	private void initMap(Map<State, Set<State>> map){
		for(State s : State.values()){
			map.put(s, new HashSet<State>());
		}
	}
	@PostConstruct
	public void setup(){
		final String queryString = "SELECT * "
                + "FROM CATCHMENT c ";
		Query query=em.createNativeQuery(queryString, CatchmentEntity.class);
		List<CatchmentEntity> list = query.getResultList();
		System.out.println(hubs);
		System.out.println(destinations);
		for(CatchmentEntity area:list){
			System.out.println(area);
			destinations.get(State.valueOf(area.getDestination())).add(State.valueOf(area.getHub()));
			hubs.get(State.valueOf(area.getHub())).add(State.valueOf(area.getDestination()));
		}
	}

	public Map<State, Set<State>> getDestinations() {
		return destinations;
	}

	public void setDestinations(Map<State, Set<State>> destinations) {
		this.destinations = destinations;
	}

	public Map<State, Set<State>> getHubs() {
		return hubs;
	}

	public void setHubs(Map<State, Set<State>> hubs) {
		this.hubs = hubs;
	}
	
}
