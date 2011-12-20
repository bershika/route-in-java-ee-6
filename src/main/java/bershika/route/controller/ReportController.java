package bershika.route.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.event.ItemSelectEvent;

import bershika.route.domain.CoefficientsInfo;
import bershika.route.domain.Hub;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.LocationId;
import bershika.route.entities.MemberEntity;
import bershika.route.entities.PointEntity;
import bershika.route.entities.RouteEntity;
import bershika.route.entities.RouteId;
import bershika.route.googleservice.GoogleServiceException;
import bershika.route.googleservice.GoogleServiceHandler;
import bershika.route.googleservice.GoogleServiceParamException;
import bershika.route.repository.HubRepository;
import bershika.route.repository.InMemoryHubRegistry;
import bershika.route.repository.PointRegistration;

@RequestScoped
@Named("report")
public class ReportController implements Serializable {

	@Resource
	private UserTransaction utx;
	@Inject
	private Logger log;

	@Inject
	@Member
	MemberEntity member;

	@Inject
	private EntityManager em;

	@Inject
	InMemoryHubRegistry hubsRegistry;

	@Inject
	private PointRegistration registry;
	
	private String selectedHub;
	private String destination;
	
	public String getSelectedHub() {
		return selectedHub;
	}
	public void setSelectedHub(String selectedHub) {
		this.selectedHub = selectedHub;
	}

	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public List<String> getHubList() {
		return hubsRegistry.getHubsNames();
	}

	 public void getRate() {
		 RouteId id = new RouteId();
		 HubEntity hub = hubsRegistry.findHubByName(selectedHub);
		 CoefficientsInfo coef = hubsRegistry.getCoefficientsForHub(selectedHub);
		 id.setDestName(Parser.getCity(destination));
		 id.setDestState(Parser.getState(destination));
		 id.setHubName(hub.getCity());
		 id.setHubState(hub.getState());
		 RouteEntity route = registry.findRoute(id);
		 if(route == null){
		 try {
			route = registry.createRoute(id);
		} catch (GoogleServiceParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
		 float interceptX = coef.getIntercept().getX();
		 float rate = 0;
		 if(route.getDistanceInMiles() < interceptX){
			 rate = route.getDistanceInMiles() * coef.getCoef1().getBeta() + coef.getCoef1().getAlpha();
		 }
		 else{
			 rate = route.getDistanceInMiles() * coef.getCoef2().getBeta() + coef.getCoef2().getAlpha();
			 
		 }
		 
	        Messanger.show("Got rate ", route.getDestName() + " : " + route.getDistanceInMiles() + "mi, rate: " + rate);
	    }
}
