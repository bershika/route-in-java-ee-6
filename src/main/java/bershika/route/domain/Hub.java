package bershika.route.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.PointEntity;
import bershika.route.googleservice.GeoLocation;
import bershika.route.googleservice.GoogleServices;
import bershika.route.regression.Coefficients;
import bershika.route.regression.CoefficientsGenerator;
import bershika.route.regression.LinearRegression;
import bershika.route.regression.Point;

public class Hub extends HubEntity {

	private static final long serialVersionUID = 1L;
	private List<HubServiceEntity> services;

	private MapModel map;
	private CartesianChartModel chartModel;
	private Coefficients coef1;
	private Coefficients coef2;
	private float surcharge;

	public Hub() {
		services = new ArrayList<HubServiceEntity>(0);
		chartModel = new CartesianChartModel();
		System.out.println("New empty hub");
	}

	public CartesianChartModel getChartModel() {
		return chartModel;
	}

	public List<PointEntity> getSortedPoints() {
		List<PointEntity> points = new ArrayList<PointEntity>(getPoints());
		Collections.sort(points);
		return points;

	}

	public void copy(HubEntity entity, float surcharge) {
		setCity(entity.getCity());
		setState(entity.getState());
		setA1(entity.getA1());
		setA2(entity.getA2());
		setB1(entity.getB1());
		setB2(entity.getB2());
		setManualMode(entity.isManualMode());
		setPoints(entity.getPoints());
		setLocation(entity.getLocation());
		System.out.println("Surcharge " + surcharge);
		this.surcharge = surcharge;
		CoefficientsInfo coefs = CoefficientsGenerator.generateCoefficients(entity.getPoints(), surcharge);
		coef1 = coefs.getCoef1();
		coef2 = coefs.getCoef2();
		initMap();
		initChartModel();
	}

	public List<HubServiceEntity> getServices() {
		return services;
	}

	public void setServices(List<HubServiceEntity> services) {
		this.services = services;
	}

	public MapModel getMap() {
		return map;
	}
	public List<PointEntity> getPointsList() {
		if (getPoints() != null) {
			return new ArrayList<PointEntity>(getPoints());
		}
		return new ArrayList<PointEntity>(0);
	}

	private void initChartModel() {
		chartModel = new CartesianChartModel();
		ChartSeries points = new LineChartSeries();
		points.setLabel("Based on market data");
		ChartSeries regression = new LineChartSeries();

		for (PointEntity p : getSortedPoints()) {
			points.set(p.getRoute().getDistanceInMiles(), p.getRate()/100F
					* (1 + surcharge / 100F));
		}

		float a1 = 0, b1 = 0, a2 = 0, b2 = 0;
		if (isManualMode()) {
			a1 = getA1();
			b1 = getB1();
			a2 = getA2();
			b2 = getB2();
			regression.setLabel("Based on manual coefficients");
		} else {
			a1 = coef1.getAlpha();
			b1 = coef1.getBeta();
			a2 = coef2.getAlpha();
			b2 = coef2.getBeta();
			regression.setLabel("Based on generated coefficients");
		}
		float Bx = 0;
		if ((b1 - b2) != 0) {
			Bx = (a2 - a1) / (b1 - b2);
		}
		float By = b1 * Bx + a1;
		regression.set(0, a1);
		regression.set(Bx, By);
		regression.set(350, b2 * 350 + a2);
		if (getSortedPoints().size() != 0) {
			chartModel.addSeries(points);
		}
		chartModel.addSeries(regression);
	}

	public float getGa1() {
		return coef1.getAlpha();
	}

	public float getGb1() {
		return coef1.getBeta();
	}

	public float getGa2() {
		return coef2.getAlpha();
	}

	public float getGb2() {
		return coef2.getBeta();
	}

	public int getTotalPoints() {
		return getPoints().size();
	}

//	public void generateCoefficients() {
//		List<Point> points1 = new ArrayList<Point>();
//		List<Point> points2 = new ArrayList<Point>();
//		Point point;
//		for (PointEntity e : getPoints()) {
//			point = new Point(e.getRoute().getDistanceInMiles(),
//					e.getRate()/100F * (1 + surcharge / 100F));
//			if (e.getRoute().getDistanceInMiles() > 100)
//				points2.add(point);
//			else
//				points1.add(point);
//		}
//		coef1 = LinearRegression.getCoefficients(points1);
//		coef2 = LinearRegression.getCoefficients(points2);
//	}

	public void initMap() {
		map = new DefaultMapModel();
		Marker hub = new Marker(new LatLng(getLocation().getLan(), getLocation().getLng()), getCity());
		map.addOverlay(hub);  
		if(getPoints() == null) return;
		for (PointEntity p : getPoints()) {
			Marker marker = new Marker(new LatLng(p.getRoute().getDest().getLan(), p.getRoute().getDest().getLng()), p.getDestName());  
	        map.addOverlay(marker);
	        
			Polyline polyline = new Polyline();
			polyline.setStrokeWeight(2);
			polyline.setStrokeColor("#FF9900");
			polyline.setStrokeOpacity(0.7);
			int i = 0;
			for (GeoLocation loc : GoogleServices.decodePoly(p.getRoute()
					.getEncPoints())) {
				polyline.getPaths().add(new LatLng(loc.lat/1000000, loc.lng/1000000));
			}
			map.addOverlay(polyline);
		}
	}

}
