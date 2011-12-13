package bershika.route.controller;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import bershika.route.entities.HubEntity;
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
	
	private String selectedHub;
	private String newHubName;
	@Produces
	@Named
	public String getSelectedHub() {
		return selectedHub;
	}

	public void setSelectedHub(String selectedHub) {
		System.out.println("Setting hub " + selectedHub);
		this.selectedHub = selectedHub;
	}
	public List<String> getHubList(){
		return hubsRegistry.getHubs();
	}
	public String getNewHubName() {
		return newHubName;
	}

	public void setNewHubName(String newHubName) {
		this.newHubName = newHubName;
	}
	public String onHubSelect(){
		System.out.println("Hub " + selectedHub + " selected");
		HubEntity entity = hubsRegistry.findHubByName(selectedHub);
		if(entity != null){
			hub.initCurrentHub(entity);
		}
		return "hub.xhtml";
	}
	public void createHub(){
		String hub = registry.createNewHub(newHubName);
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,"Sample warn message", hub)); 
	}
}
