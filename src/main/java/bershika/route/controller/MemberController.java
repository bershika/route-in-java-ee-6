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

@SessionScoped
public class MemberController implements Serializable {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;
	private MemberEntity member;

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

	public void login() {
	}

	public void logout() {
	}

}
