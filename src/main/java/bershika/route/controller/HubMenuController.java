package bershika.route.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import bershika.route.domain.CoefficientsInfo;
import bershika.route.entities.HubEntity;
import bershika.route.googleservice.GoogleServiceException;
import bershika.route.googleservice.GoogleServiceParamException;
import bershika.route.repository.EntityExistsException;
import bershika.route.repository.HubRepository;
import bershika.route.repository.InMemoryHubRegistry;

@RequestScoped
@Named("menu")
public class HubMenuController {
	@Inject
	private InMemoryHubRegistry hubsRegistry;
	@Inject
	private HubRepository registry;
	@Inject
	private HubController hub;
	@Inject
	private Event<HubEntity> hubEventSrc;

	private String selectedHub;
	private String newHubName;
	private MapModel menuMap;

	public String getSelectedHub() {
		return selectedHub;
	}

	public void setSelectedHub(String selectedHub) {
		this.selectedHub = selectedHub;
	}

	public List<String> getHubList() {
		return hubsRegistry.getHubsNames();
	}
	
	public ArrayList<Entry<String, CoefficientsInfo>> getHubsMap(){
		return new ArrayList<Entry<String, CoefficientsInfo>>(hubsRegistry.getCoefsMap().entrySet());
	}

	public String getNewHubName() {
		return newHubName;
	}

	public void setNewHubName(String newHubName) {
		this.newHubName = newHubName;
	}

	public MapModel getMap() {
		menuMap = new DefaultMapModel();
		for(Entry<String, HubEntity> entry : hubsRegistry.getHubsMap().entrySet()){
			Marker hub = new Marker(
					new LatLng(entry.getValue().getLocation().getLan(), entry.getValue().getLocation().getLng()), 
					entry.getValue().getShortName());
			menuMap.addOverlay(hub);  
		}
		return menuMap;
	}

	public String onHubSelect() {
		HubEntity entity = hubsRegistry.findHubByName(selectedHub);
		if (entity != null) {
			hub.initCurrentHub(entity);
		}
		return "hub.xhtml";
	}

	public void createHub() {
		try {
			HubEntity hub = registry.createNewHub(newHubName);
			//hubEventSrc.fire(hub);
		} catch (GoogleServiceException e) {
			Messanger.show("Google maps response.", e.getMessage());
		} catch (IOException e) {
			Messanger.show("Internet is not up?", "Cannot connect to Google. Check your internet connection and try again.");
		} catch (GoogleServiceParamException e) {
			Messanger.show("Internal error.", "Something went wrong. We will look into the issue.");
		} catch (EntityExistsException e) {
			Messanger.show("Hub already exists!", newHubName + " already exists.");
		} 
	}
}
