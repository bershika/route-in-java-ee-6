package bershika.route.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import bershika.route.entities.MemberEntity;
import bershika.route.entities.SurchargeEntity;
import bershika.route.repository.Repository;

@SessionScoped
@Named
public class MemberController implements Serializable {

	@Inject
	private Logger log;

	@Inject
	private Repository repository;
	@Inject
	private EntityManager em;
	
	private MemberEntity member;
	
	private Float newSurchargeValue;

	@PostConstruct
	public void initMember() {
		member = em.find(MemberEntity.class, "bershika@gmail.com");
	}

	@Produces
	@Member
	@Named
	public MemberEntity getMember() {
		return member;
	}

	public Float getNewSurchargeValue() {
		return newSurchargeValue;
	}

	public void setNewSurchargeValue(Float newSurchargeValue) {
		this.newSurchargeValue = newSurchargeValue;
	}

	public void login() {
	}

	public void logout() {
	}
	
	public void updateSurcharge(){
		System.out.println("Updating " + newSurchargeValue);
		SurchargeEntity entity;
		if(validateSurcharge(newSurchargeValue)){
			entity = member.getSurcharge();
			entity.setValue(newSurchargeValue);
			repository.saveSurcharge(entity);
			initMember();
		}
	}
	
	private boolean validateSurcharge(float surcharge){
		return surcharge > 0 && surcharge < 100;
	}

}
