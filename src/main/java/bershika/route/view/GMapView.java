package bershika.route.view;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import bershika.route.repository.InMemoryHubRegistry;

@Model
public class GMapView {
	@Inject
	InMemoryHubRegistry hubsRegistry;
//	private MapModel mapModel; 
//	public MapBean() {  
//        simpleModel = new DefaultMapModel();  
//          
//        //Shared coordinates  
//        LatLng coord1 = new LatLng(36.879466, 30.667648);  
//        LatLng coord2 = new LatLng(36.883707, 30.689216);  
//        LatLng coord3 = new LatLng(36.879703, 30.706707);  
//        LatLng coord4 = new LatLng(36.885233, 30.702323);  
//          
//        //Basic marker  
//        simpleModel.addOverlay(new Marker(coord1, "Konyaalti"));  
//        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));  
//        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));  
//        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));  
////    }  
}
