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
import bershika.route.entities.HubEntity;
import bershika.route.entities.LocationId;
import bershika.route.entities.MemberEntity;
import bershika.route.regression.Coefficients;
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
	
	public Map<String, CoefficientsInfo> getCoefsMap() {
		return coefsMap;
	}

	public Map<String, HubEntity> getHubsMap() {
		return hubsMap;
	}

	public void setHubsMap(Map<String, HubEntity> hubsMap) {
		this.hubsMap = hubsMap;
	}

	public List<String> getHubsNames() {
		return new ArrayList<String>(hubsMap.keySet());
	}

	public void onHubListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final HubEntity hub) {
		retrieveAllHubsOrderedByName();
	}

	@PostConstruct
	public void retrieveAllHubsOrderedByName() {
		hubsMap = new HashMap<String, HubEntity>();
		coefsMap = new HashMap<String, CoefficientsInfo>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HubEntity> criteria = cb.createQuery(HubEntity.class);
		Root<HubEntity> hub = criteria.from(HubEntity.class);
		for (HubEntity hubEntity : em.createQuery(criteria).getResultList()) {
			hubsMap.put(hubEntity.getShortName(), hubEntity);
			CoefficientsInfo coefs = null;
			if(hubEntity.isManualMode()){
				coefs = new CoefficientsInfo(hubEntity.getA1(), hubEntity.getB1(), hubEntity.getA2(), hubEntity.getB2());
			}
			else{
				coefs = CoefficientsGenerator.generateCoefficients(hubEntity.getPoints(), member.getSurcharge().getValue());
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
		return entity;
	} 
	
	public CoefficientsInfo getCoefficientsForHub(String hubName){
		return coefsMap.get(hubName);
	}

}
