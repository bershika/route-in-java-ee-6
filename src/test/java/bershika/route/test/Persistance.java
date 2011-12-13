package bershika.route.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.Before;
import org.junit.Test;

import bershika.route.controller.HubController;
import bershika.route.entities.HubEntity;

public class Persistance {
	@PersistenceUnit(unitName="RouteDB")
	EntityManager em;
	HubController hubController = new HubController();

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void createHub() {
		  HubEntity newHub = new HubEntity();
	      newHub.setCity("Seattle");
	      newHub.setState("WA");
	      try {
			//hubController.createHub();
		} catch (Exception e) {
			fail(e.toString());
		}
	      newHub = em.find(HubEntity.class, newHub);
	      assertNotNull("Saved " + newHub, newHub);
	}

}
