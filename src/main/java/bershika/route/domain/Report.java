package bershika.route.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.SessionScoped;

import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.RouteEntity;

@SessionScoped
public class Report extends HashMap<String, ArrayList<ReportEntry>> implements Serializable{
	
	public Report(){
		System.out.println("report gen");
	}

	public void add(RouteEntity route, List<HubServiceEntity> services, CoefficientsInfo coef)
			throws ReportEntryException {
		if(services == null) return;
		String hubName = route.getHubName() + "," + route.getHubState();
		for (HubServiceEntity service : services) {
			if (!(hubName).equalsIgnoreCase(service.getShortName()))
				throw new ReportEntryException();
			ReportEntry entry = new ReportEntry();
			entry.setDistance(route.getDistanceInMiles());
			entry.setHubName(hubName);
			entry.setServiceNotes(service.getNotes());
			entry.setServiceRate(service.getRate());
			float drayage = 0;
			if (entry.getDistance() < 100)
				drayage = coef.getCoef1().getAlpha() * entry.getDistance();
			else
				drayage = coef.getCoef2().getAlpha() * entry.getDistance();
			entry.setDrayage(drayage);
			putEntry(route.getDestName() + route.getDestState(), entry);
		}
	}

	public void putEntry(String destination, ReportEntry entry) {
		if (containsKey(destination))
			get(destination).add(entry);
		else {
			ArrayList<ReportEntry> list = new ArrayList<ReportEntry>();
			list.add(entry);
			put(destination, list);
		}

	}
	
	@Override
	public String toString() {
		String builder = "";
		for(java.util.Map.Entry<String, ArrayList<ReportEntry>> e : entrySet()){
			builder += (e.getKey() + " - " + e.getValue() + "\n");
		}
		return builder;
	}
	
}
