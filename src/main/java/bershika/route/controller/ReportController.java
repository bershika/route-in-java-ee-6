package bershika.route.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bershika.route.domain.Catchment;
import bershika.route.domain.CoefficientsInfo;
import bershika.route.domain.Report;
import bershika.route.domain.ReportEntry;
import bershika.route.domain.ReportEntryException;
import bershika.route.domain.ReportRow;
import bershika.route.domain.State;
import bershika.route.entities.HubEntity;
import bershika.route.entities.MemberEntity;
import bershika.route.entities.RouteEntity;
import bershika.route.entities.RouteId;
import bershika.route.googlehandler.GoogleHandler;
import bershika.route.googlehandler.RouteInfo;
import bershika.route.googleservice.GoogleServiceException;
import bershika.route.googleservice.GoogleServiceParamException;
import bershika.route.repository.InMemoryHubRegistry;
import bershika.route.repository.Repository;

@RequestScoped
@Named("report")
public class ReportController implements Serializable {

	@Inject
	private Logger log;

	@Inject
	@Member
	MemberEntity member;

	@Inject
	private InMemoryHubRegistry hubsRegistry;

	@Inject
	private Repository repo;

	@Inject
	private Catchment catchment;
	
	@Inject Report report;

	private List<ReportRow> entries = new ArrayList<ReportRow>();

	private String selectedHub = "";
	private String destination = "";
	private String destinationsTA = "";
	private List<String> selectedServices;
	

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

	public String getDestinationsTA() {
		return destinationsTA;
	}

	public void setDestinationsTA(String destinations) {
		this.destinationsTA = destinations;
	}

	public List<String> getSelectedServices() {
		return selectedServices;
	}

	public void setSelectedServices(List<String> selectedServices) {
		this.selectedServices = selectedServices;
	}

	public List<ReportRow> getReport() {
		return entries;
	}

	public void getRate() {
		RouteId id = new RouteId();
		HubEntity hub = hubsRegistry.findHubByName(selectedHub);
		CoefficientsInfo coef = hubsRegistry.getCoefficientsForHub(selectedHub);
		id.setDestName(Parser.getCity(destination));
		id.setDestState(Parser.getState(destination));
		id.setHubName(hub.getCity());
		id.setHubState(hub.getState());
		RouteEntity route = repo.findRoute(id);

		if (route == null) {
			try {
				route = GoogleHandler.createRouteEntity(id.getHubName(), id.getHubState(), id.getDestName(),
						id.getDestState());
				route = repo.saveRoute(route, id);
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
		if (route.getDistanceInMiles() < interceptX) {
			rate = route.getDistanceInMiles() * coef.getCoef1().getBeta() + coef.getCoef1().getAlpha();
		} else {
			rate = route.getDistanceInMiles() * coef.getCoef2().getBeta() + coef.getCoef2().getAlpha();

		}

		Messanger.show("Got rate ", route.getDestName() + " : " + route.getDistanceInMiles() + "mi, rate: " + rate);
	}
	
	public void generateReport() {
		if(destinationsTA.isEmpty()) return;
		List<String> lines = Parser.getLines(destinationsTA);
		Map<String, Set<String>> existingRoutesByDestMap = new HashMap<String, Set<String>>();
		Map<String, Set<String>> to = new HashMap<String, Set<String>>();
		Map<String, Set<String>> from = new HashMap<String, Set<String>>();
		Report report = new Report();
		List<String> errors = new ArrayList<String>();
		String destCity = "", destState = "";
		State destStateEnm, hubStateEnm;
		for (String line : lines) {
			System.out.println(line);
			destCity = Parser.getCity(line);
			destState = Parser.getState(line);
			try {
				destStateEnm = State.valueOf(destState);
			} catch (IllegalArgumentException e) {
				continue;
			}
			for (String hubName : hubsRegistry.getHubsNames()) {
				String hubCity = Parser.getCity(hubName);
				String hubState = Parser.getState(hubName);
				try {
					hubStateEnm = State.valueOf(hubState);
				} catch (IllegalArgumentException e) {
					continue;
				}
				if (destStateEnm != hubStateEnm & !catchment.getDestinations().get(destStateEnm).contains(hubStateEnm))
					continue;
				RouteId id = new RouteId();
				id.setHubName(hubCity);
				id.setHubState(hubState);
				id.setDestName(destCity);
				id.setDestState(destState);
				RouteEntity route = repo.findRoute(id);
				System.out.println("Found route: " + route);
				// add to request
				if (route == null) {
					System.out.println("Adding to request");
					if (to.containsKey(destState))
						to.get(destState).add(destCity);
					else {
						Set<String> dCities = new HashSet<String>();
						dCities.add(destCity);
						to.put(destState, dCities);
					}
					if (from.containsKey(hubState))
						from.get(hubState).add(hubCity);
					else {
						Set<String> hCities = new HashSet<String>();
						hCities.add(hubCity);
						from.put(hubState, hCities);
					}
					System.out.println("REquest : " + to + " / " + from);
				}
				// add to report
				else {
					// for(HubServiceEntity service :
					// hubsRegistry.getServicesForHub(hub.getShortName())){
					try {
						report.add(route, hubsRegistry.getServicesForHub(hubName),
								hubsRegistry.getCoefficientsForHub(hubName));
						if (existingRoutesByDestMap.containsKey(destCity + "," + destState))
							existingRoutesByDestMap.get(destCity + "," + destState).add(hubName);
						else {
							Set<String> hubsList = new HashSet<String>();
							hubsList.add(hubName);
							existingRoutesByDestMap.put(destCity + "," + destState, hubsList);
						}

					} catch (ReportEntryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		List<RouteInfo> routeInfoList = getRouteInfoForDestinations(from, to, errors);
		List<RouteEntity> savedRoutes = repo.saveRoutes(routeInfoList);
		for(RouteEntity route : savedRoutes){
			try {
				report.add(route, hubsRegistry.getServicesForHub(route.getHubName()+ "," + route.getHubState()),
						hubsRegistry.getCoefficientsForHub(route.getHubName()+ "," + route.getHubState()));
			} catch (ReportEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(routeInfoList);
		System.out.println(report);
		ReportRow row;

		for(Entry<String, ArrayList<ReportEntry>> next : report.entrySet()){
			row = new ReportRow();
			row.setDestInfo(next.getKey());
			ArrayList<ReportEntry> list = next.getValue();
			Collections.sort(list);
			row.setEntries(list);
			entries.add(row);
		}
		// }
		Messanger.show("Got rate ", report.size() + "/");	
	}

	private List<RouteInfo> getRouteInfoForDestinations(Map<String, Set<String>> from, Map<String, Set<String>> to,
			List<String> errors) {
		List<RouteInfo> routes = new ArrayList<RouteInfo>();
		// loop through all destination states and find all hubs that belongs to
		// the catchment
		for (String destState : to.keySet()) {
			List<String> reqTo = new ArrayList<String>(), reqFrom = new ArrayList<String>();
			for (String destCity : to.get(destState)) {
				reqTo.add(destCity + "," + destState);
			}
			if (from.get(destState) != null) {
				for (String hubName : from.get(destState)) {
					reqFrom.add(hubName + "," + destState);
				}
			}
			for (State hubState : catchment.getDestinations().get(State.valueOf(destState))) {
				if (from.get(hubState.name()) == null)
					continue;
				for (String hubName : from.get(hubState.name())) {
					reqFrom.add(hubName + "," + hubState);
				}
			}
			try {
				if (!reqFrom.isEmpty() & !reqTo.isEmpty())
					routes.addAll(GoogleHandler.getRouteInfoList(reqFrom, reqTo, errors));
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
		return routes;
	}
}
