package bershika.route.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;

import bershika.route.domain.Hub;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.MemberEntity;
import bershika.route.entities.PointEntity;
import bershika.route.entities.RouteEntity;
import bershika.route.entities.RouteId;
import bershika.route.googlehandler.GoogleHandler;
import bershika.route.repository.InMemoryHubRegistry;
import bershika.route.repository.Repository;

@SessionScoped
@Named
public class HubController implements Serializable {

	@Inject
	private Logger log;

	@Inject
	@Member
	MemberEntity member;

	@Inject
	InMemoryHubRegistry hubsRegistry;

	@Inject
	Repository repository;

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
		try {
			HubEntity entity = hubsRegistry.getFirst();
			currentHub.copy(entity, member.getSurcharge().getValue());
			currentHub.setServices(repository.findServicesForHub(entity));
		} catch (Exception e) {
			currentHub = new Hub();
		}
	}

	public void initCurrentHub(HubEntity entity) {
		currentHub.copy(entity, member.getSurcharge().getValue());
		currentHub.setServices(repository.findServicesForHub(entity));
	}

	public void initSelectedService() {
		selectedService = new HubServiceEntity();
	}

	public void initSelectedPoint() {
		selectedPoint = new PointEntity();
	}

	public void saveService() {
		if (selectedService != null) {
			System.out.println("selected service " + selectedService);
			selectedService.setCity(currentHub.getCity());
			selectedService.setState(currentHub.getState());
			selectedService.setEmail(member.getEmail());
			repository.saveService(selectedService);
			HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
			initCurrentHub(entity);
		}
	}

	public void addServiceRow() {
		System.out.println("Adding row");
		if (currentHub.getServices().size() > 3) {
			// Messanger.show("Too many services",
			// "Delete a service, then try again.");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Too many services",
							"Delete a service, then try again."));

			return;
		}
		HubServiceEntity newService = new HubServiceEntity();
		newService.setNotes("...");
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
		repository.deleteService(selectedService);

		HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
		initCurrentHub(entity);
	}

	public void savePoint() {
		if (selectedPoint == null)
			return;
		repository.savePoint(selectedPoint);
		HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
		initCurrentHub(entity);
	}

	public void deletePoint() {
		if (selectedPoint == null)
			return;
		repository.deletePoint(selectedPoint);
		HubEntity entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
		initCurrentHub(entity);

	}

	public void saveHub() {
			HubEntity entity = hubsRegistry.findHubByName(currentHub.getShortName());
			if (entity != null) {
				entity.setA1(currentHub.getA1());
				entity.setA2(currentHub.getA2());
				entity.setB1(currentHub.getB1());
				entity.setB2(currentHub.getB2());
				entity.setManualMode(currentHub.isManualMode());
				repository.saveHub(entity);
				entity = hubsRegistry.refreshHubFromDb(currentHub.getCity(), currentHub.getState());
				initCurrentHub(entity);
			}
		
	}

	public void uploadPoints() {
		List<String> lines = Parser.getLines(pointsTA);
		// registry.createPoints(currentHub.getCity(), currentHub.getState(),
		// lines);
		PointEntity p = null;
		RouteId id = null;
		Messanger.show("Starting uploading...", "Total points to create : " + lines.size());
		for (String line : lines) {
			id = new RouteId();
			id.setHubName(currentHub.getCity());
			id.setHubState(currentHub.getState());
			id.setDestName(Parser.getCity(line));
			id.setDestState(Parser.getState(line));
			try {
				RouteEntity route = GoogleHandler.createRouteEntity(id.getHubName(), id.getHubState(), id.getDestName(),
						id.getDestState());
				p = repository.savePoint(route, Math.round(Parser.getPointRate(line) * 100));
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
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected", point.toString());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
