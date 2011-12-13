package bershika.route.view;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import bershika.route.domain.Hub;
import bershika.route.repository.InMemoryHubRegistry;

@Model
public class HubsMenuView {
	@Inject
	InMemoryHubRegistry hubsRegistry;
//	@Inject
//	Hub currentHub;
	List<String>hubs;
	
	public List<String> getHubs() {
		return hubs;
	}
	
	public void onHubSelect(){
		
	}
	
}
