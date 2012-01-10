package bershika.route.googlehandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bershika.route.controller.Parser;
import bershika.route.entities.HubEntity;
import bershika.route.entities.LocationEntity;
import bershika.route.entities.RouteEntity;
import bershika.route.googleservice.DirectionsResponse;
import bershika.route.googleservice.DirectionsResponse.Leg;
import bershika.route.googleservice.DistanceMatrixResponse;
import bershika.route.googleservice.DistanceMatrixResponse.Element;
import bershika.route.googleservice.DistanceMatrixResponse.Row;
import bershika.route.googleservice.GeocodeResponse;
import bershika.route.googleservice.GeocodeResponse.AddressComponent;
import bershika.route.googleservice.GoogleServiceException;
import bershika.route.googleservice.GoogleServiceParamException;
import bershika.route.googleservice.GoogleServices;


public class GoogleHandler {

	public static HubEntity createHubEntity(String locName)
			throws GoogleServiceException, IOException,
			GoogleServiceParamException {
		GeocodeResponse response = GoogleServices.getGeocoding(locName);
		if (!response.status.equalsIgnoreCase("OK")) {
			throw new GoogleServiceException(response.status);
		}
		HubEntity hub = new HubEntity();
		LocationEntity location = new LocationEntity();
		String state = null, city = null;
		float lan, lng;
		for (AddressComponent addComp : response.results[0].address_components) {
			for (String type : addComp.types) {
				if (type.equalsIgnoreCase("administrative_area_level_1"))
					state = addComp.short_name;
			}
			for (String type : addComp.types) {
				if (type.equalsIgnoreCase("locality"))
					city = addComp.short_name;
			}
		}
		lan = response.results[0].geometry.location.lat;
		lng = response.results[0].geometry.location.lng;
		if (city != null && state != null && lan != 0 && lng != 0) {
			location.setCity(city);
			location.setState(state);
			location.setLan(lan);
			location.setLng(lng);
			hub.setLocation(location);
			hub.setCity(city);
			hub.setState(state);
			System.out.println("Hub " + hub);
			return hub;
		}
		return null;
	}

	public static RouteEntity createRouteEntity(String hubName, String hubState,
			String destName, String destState)
			throws GoogleServiceParamException, IOException,
			GoogleServiceException {
		DirectionsResponse response = GoogleServices.getDirections(
				hubName + "," + hubState + ",USA", destName + "," + destState
						+ ",USA");
		if (!response.status.equalsIgnoreCase("OK")) {
			throw new GoogleServiceException(response.status);
		}
		RouteEntity route = new RouteEntity();
		LocationEntity dest = new LocationEntity();
		Leg leg = response.routes[0].legs[0];
		String city = Parser.getCity(leg.end_address);
		String state = Parser.getState(leg.end_address);
		dest.setCity(city);
		dest.setState(state);
		dest.setLan(leg.end_location.lat);
		dest.setLng(leg.end_location.lng);
		route.setDest(dest);
		route.setDestName(city);
		route.setDestState(state);
		route.setHubName(hubName);
		route.setHubState(hubState);
		route.setDistance(leg.distance.value);
		route.setEncPoints(response.routes[0].overview_polyline.points);
		return route;
	}
	
	public static List<RouteInfo> getRouteInfoList(List<String> from, List<String> to, List<String> errors) 
			throws GoogleServiceParamException, IOException, GoogleServiceException{
		if(from == null || to == null || from.isEmpty() || to.isEmpty())
			throw new GoogleServiceParamException();
		DistanceMatrixResponse response = GoogleServices.getDistanceMatrix(from, to);
		if (!response.status.equalsIgnoreCase("OK")) {
			throw new GoogleServiceException(response.status);
		}
		List<RouteInfo> routes = new ArrayList<RouteInfo>();
		int row_origin = 0;
		System.out.println(response);
		for( Row  row : response.rows){
			int column_dest = 0;
			for(Element elm : row.elements){
				if(!elm.status.equalsIgnoreCase("OK")){
					errors.add(from.get(row_origin) + " - " + to.get(column_dest) + ": " + elm.status);
					continue;
				}
				RouteInfo route = new RouteInfo();
				route.setUserFrom(from.get(row_origin));
				route.setUserTo(to.get(column_dest));
				route.setGoogleFrom(response.origin_addresses.get(row_origin));
				route.setGoogleTo(response.destination_addresses.get(column_dest));
				route.setDistanceInMeters(elm.distance.value);
				routes.add(route);
				column_dest++;
			}
			row_origin++;
		}
		return routes;
	}
}
