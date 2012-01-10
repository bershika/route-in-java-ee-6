package bershika.route.test;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import bershika.route.controller.HubController;
import bershika.route.domain.Hub;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.PointEntity;
import bershika.route.util.Resources;



@RunWith(Arquillian.class)
public class HubControllerTest {
	
	@Deployment
	   public static Archive<?> createTestArchive() {
	      return ShrinkWrap.create(WebArchive.class, "test.war")
	            .addClasses(Hub.class, HubEntity.class,HubServiceEntity.class, PointEntity.class, HubController.class, Resources.class)
	            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
	            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	   }
	 @Inject
	   HubController hubController = new HubController();

	   @Inject
	   Logger log;

	   @Test
	   public void testCreateHub() throws Exception {
//	      HubEntity newHub = hubController.getNewHub();
//	      newHub.setCity("Seattle");
//	      newHub.setState("WA");
//	      hubController.createHub();
//	      log.info(newHub.getShortName() + " was persisted" );
	   }
	   @Test
	   public void testSaveHub() throws Exception {
	      Hub hub = hubController.getCurrentHub();
	      hub.setCity("Seattle");
	      hub.setState("WA");
	      hub.setA1(1F);
	      hub.setB1(2F);
	      hub.setA2(3F);
	      hub.setB2(4F);
	      hub.setManualMode(true);
	      hubController.saveHub();
	      log.info(hub.getShortName() + " was saved" );
	   }
	   @Test
	   public void testSaveService() throws Exception {
//		   Hub hub = hubController.getCurrentHub();
//		      hub.setCity("Seattle");
//		      hub.setState("WA");
		  HubServiceEntity service = hubController.getSelectedService();
		  service.setCity("Seattle");
		  service.setState("WA");
		  service.setNotes("blah blah");
		  service.setRate(1234);
		  service.setEmail("ber_shika@yahoo.ca");
	      hubController.saveService();
	      log.info(service + " was persisted" );
	   }
	   @Test
	   public void testSavePoint() throws Exception {
	      PointEntity point = hubController.getSelectedPoint();
	      point.setHubName("Seattle");
	      point.setHubState("WA");
	      //point.setDestName("Tacoma");
	      point.setDestState("WA");
	      point.setRate(123);
	      hubController.savePoint();
	      log.info(point + " was persisted" );
	   }
//	   @Test
//	   public void testDeleteService() throws Exception {
//	      HubEntity newHub = hubController.getNewHub();
//	      newHub.setCity("Seattle");
//	      newHub.setState("WA");
//	      hubController.createHub();
//	      log.info(newHub.getShortName() + " was persisted" );
//	   }
//	   @Test
//	   public void testDeletePoint() throws Exception {
//	      HubEntity newHub = hubController.getNewHub();
//	      newHub.setCity("Seattle");
//	      newHub.setState("WA");
//	      hubController.createHub();
//	      log.info(newHub.getShortName() + " was persisted" );
//	   }
	   
}
