package bershika.route.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
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

import bershika.route.domain.Hub;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.MemberEntity;
import bershika.route.entities.PointEntity;
import bershika.route.entities.RouteId;
import bershika.route.repository.HubRepository;
import bershika.route.repository.InMemoryHubRegistry;
import bershika.route.repository.PointRegistration;

@SessionScoped
@Named
public class HubController implements Serializable {

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
	private HubRepository registry;
	
	@Inject PointRegistration pointR;

	private Hub currentHub;
	private HubServiceEntity selectedService;
	private PointEntity selectedPoint;
	private String pointsTA;

	public PointEntity getSelectedPoint() {
		return selectedPoint;
	}

	public void setSelectedPoint(PointEntity selectedPoint) {
		this.selectedPoint = selectedPoint;
	}

	public HubServiceEntity getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(HubServiceEntity selectedService) {
		this.selectedService = selectedService;
	}

	public String getPointsTA() {
		return pointsTA;
	}

	public void setPointsTA(String newPointsTA) {
		pointsTA = newPointsTA;
	}

	@Produces
	@Named
	public Hub getCurrentHub() {
		return currentHub;
	}

	@PostConstruct
	public void init() {
		initCurrentHub();
		initSelectedService();
		initSelectedPoint();
	}

	public void initCurrentHub() {
		System.out.println("init current hub");
		try {
			HubEntity entity = hubsRegistry.getFirst();
			currentHub.copy(entity, member.getSurcharge().getValue());
			currentHub.setServices(registry.findServicesForHub(entity));
		} catch (Exception e) {
			currentHub = new Hub();
		}
	}

	public void initCurrentHub(HubEntity entity) {
		currentHub.copy(entity, member.getSurcharge().getValue());
		currentHub.setServices(registry.findServicesForHub(entity));
	}

	public void initSelectedService() {
		selectedService = new HubServiceEntity();
	}

	public void initSelectedPoint() {
		selectedPoint = new PointEntity();
	}

	public void saveService() {
		System.out.println("save : Selected service " + selectedService);
		System.out.println("save : member " + member);
		// HubEntity entity =
		// hubsRegistry.findHubByName(currentHub.getShortName());
		// em.refresh(entity);
		// /entity.getServices().add(selectedService);
		try {
			if (selectedService != null) {
				utx.begin();
				selectedService = em.merge(selectedService);
				utx.commit();
				HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
				initCurrentHub(entity);
			}
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

	}

	public void addServiceRow() {
		if(currentHub.getServices().size() > 3){
			//Messanger.show("Too many services", "Delete a service, then try again.");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Too many services", "Delete a service, then try again."));

			return;
		}
		HubServiceEntity newService = new HubServiceEntity();
		newService.setCity(currentHub.getCity());
		newService.setState(currentHub.getState());
		newService.setEmail(member.getEmail());
		currentHub.getServices().add(newService);
	}

	public void deleteService() {
		System.out.println("Selected service " + selectedService);
		if (selectedService != null) {
			currentHub.getServices().remove(selectedService);
		}
		try {
			utx.begin();
			selectedService = em.find(HubServiceEntity.class,
					selectedService.getKey());
			if (selectedService != null) {

				em.remove(selectedService);
				
				HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
				initCurrentHub(entity);
			}
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

	}

	public void savePoint() {
		try {
			if (selectedPoint != null) {
				utx.begin();
				em.merge(selectedPoint);
				utx.commit();
				HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
				initCurrentHub(entity);
			}
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

	}

	public void deletePoint() {
		try {
			if (selectedPoint != null) {
				utx.begin();
				selectedPoint = em.find(PointEntity.class,
						selectedPoint.getKey());
				em.remove(selectedPoint);
				utx.commit();
				HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
				initCurrentHub(entity);
			}
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

	}

	public void saveHub() {
		try {
			HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
			if (entity != null) {
				entity.setA1(currentHub.getA1());
				entity.setA2(currentHub.getA2());
				entity.setB1(currentHub.getB1());
				entity.setB2(currentHub.getB2());
				entity.setManualMode(currentHub.isManualMode());

				utx.begin();
				entity = em.merge(entity);
				utx.commit();
				entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
				initCurrentHub(entity);
			}
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
	}

	public void uploadPoints() {
		List<String> lines = Parser.getLines(pointsTA);
//		registry.createPoints(currentHub.getCity(), currentHub.getState(),
//				lines);
		PointEntity p	= null;
		RouteId id		= null;
		Messanger.show("Starting uploading...", "Total points to create : "
				+ lines.size());
		for(String line : lines){
			id = new RouteId();
			id.setHubName(currentHub.getCity());
			id.setHubState(currentHub.getState());
			id.setDestName(Parser.getCity(line));
			id.setDestState(Parser.getState(line));
			try {
				p = pointR.createPoint(id, Parser.getPointRate(line));
				Messanger.show("Point created", p.getDestName() + ", " + p.getDestState());
			} catch (Exception e) {
				Messanger.show("Error", e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
		initCurrentHub(entity);
	}
	
	 public void pointSelect(ItemSelectEvent event) {
		 PointEntity point = currentHub.getPointsList().get(event.getItemIndex());
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",  
	                        point.toString());  
	  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	    } 
}
