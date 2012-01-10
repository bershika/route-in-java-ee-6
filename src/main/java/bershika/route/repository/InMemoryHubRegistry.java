package bershika.route.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bershika.route.controller.Member;
import bershika.route.domain.CoefficientsInfo;
import bershika.route.domain.State;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.LocationId;
import bershika.route.entities.MemberEntity;
import bershika.route.regression.CoefficientsGenerator;

@SessionScoped
public class InMemoryHubRegistry implements Serializable {
	@Inject
	private EntityManager em;
	@Inject
	@Member
	MemberEntity member;
	private Map<String, HubEntity> hubsMap;
	private Map<String, CoefficientsInfo> coefsMap;
	private Map<String, List<HubServiceEntity>> servicesMap;

	public Map<String, CoefficientsInfo> getCoefsMap() {
		return coefsMap;
	}

	public Map<String, HubEntity> getHubsMap() {
		return hubsMap;
	}

	public List<String> getHubsNames() {
		return new ArrayList<String>(hubsMap.keySet());
	}

	public Map<String, List<HubServiceEntity>> getServicesMap() {
		return servicesMap;
	}

	public void setServicesMap(Map<String, List<HubServiceEntity>> servicesMap) {
		this.servicesMap = servicesMap;
	}

	public void onHubListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final HubEntity hub) {
		retrieveAllHubsOrderedByName();
	}

	@PostConstruct
	public void init(){
		retrieveAllHubsOrderedByName();
		retrieveAllServicesForMember();
		System.out.println(servicesMap);
	}
	public void retrieveAllHubsOrderedByName() {
		hubsMap = new HashMap<String, HubEntity>();
		coefsMap = new HashMap<String, CoefficientsInfo>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HubEntity> criteria = cb.createQuery(HubEntity.class);
		Root<HubEntity> hub = criteria.from(HubEntity.class);
		for (HubEntity hubEntity : em.createQuery(criteria).getResultList()) {
			hubsMap.put(hubEntity.getShortName(), hubEntity);
			CoefficientsInfo coefs = null;
			if (hubEntity.isManualMode()) {
				coefs = new CoefficientsInfo(hubEntity.getA1(), hubEntity.getB1(), hubEntity.getA2(), hubEntity.getB2());
			} else {
				coefs = CoefficientsGenerator.generateCoefficients(hubEntity.getPoints(), member.getSurcharge()
						.getValue());
			}
			coefsMap.put(hubEntity.getShortName(), coefs);
		}
		System.out.println("Coeff " + coefsMap);
	}

	public HubEntity findHubByName(String name) {
		return hubsMap.get(name);
	}

	public HubEntity getFirst() {
		return hubsMap.values().iterator().next();
	}

	public HubEntity refreshHubFromDb(String city, String state) {
		LocationId id = new LocationId();
		id.setCity(city);
		id.setState(state);
		HubEntity entity = em.find(HubEntity.class, id);
		hubsMap.put(entity.getShortName(), entity);
		CoefficientsInfo coefs = null;
		if (entity.isManualMode()) {
			coefs = new CoefficientsInfo(entity.getA1(), entity.getB1(), entity.getA2(), entity.getB2());
		} else {
			coefs = CoefficientsGenerator.generateCoefficients(entity.getPoints(), member.getSurcharge().getValue());
		}
		coefsMap.put(entity.getShortName(), coefs);
		return entity;
	}

	public CoefficientsInfo getCoefficientsForHub(String hubName) {
		return coefsMap.get(hubName);
	}

	public void retrieveAllServicesForMember() {
		servicesMap = new HashMap<String, List<HubServiceEntity>>();
		System.out.println("SErvices " + member.getServices());
		for (HubServiceEntity service : member.getServices()) {
			System.out.println("----SErvice " + service);
			String shortName = service.getCity() + "," + service.getState();
			if (servicesMap.containsKey(shortName)) {
				servicesMap.get(shortName).add(service);
			} else {
				List<HubServiceEntity> list = new ArrayList<HubServiceEntity>();
				list.add(service);
				servicesMap.put(shortName, list);
			}
		}
	}
	
	public List<HubServiceEntity> getServicesForHub(String hubName){
		return servicesMap.get(hubName);
	}
	
	public List<HubEntity> getHubsForState(String state){
		List<HubEntity> hubs = new ArrayList<HubEntity>();
		for(HubEntity hub : hubsMap.values()){
			if(hub.getState().equalsIgnoreCase(state))
				hubs.add(hub);
		}
		return hubs;
	}

}
