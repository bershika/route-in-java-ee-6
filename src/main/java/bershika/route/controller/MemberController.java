package bershika.route.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import bershika.route.entities.SurchargeEntity;


import bershika.route.entities.MemberEntity;

@SessionScoped
@Named
public class MemberController implements Serializable {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	@Resource
	private UserTransaction utx;
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
		SurchargeEntity entity;
		if(validateSurcharge(newSurchargeValue)){
			entity = member.getSurcharge();
			entity.setValue(newSurchargeValue);
			try {
				utx.begin();
				em.merge(entity);
				utx.commit();
			} catch (NotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HeuristicMixedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			initMember();
		}
	}
	
	private boolean validateSurcharge(float surcharge){
		return surcharge > 0 && surcharge < 100;
	}

}
