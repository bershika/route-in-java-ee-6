package bershika.route.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

import bershika.route.controller.Member;
import bershika.route.entities.HubEntity;
import bershika.route.entities.HubServiceEntity;
import bershika.route.entities.MemberEntity;
import bershika.route.entities.PointEntity;
import bershika.route.regression.Coefficients;
import bershika.route.regression.LinearRegression;
import bershika.route.regression.Point;

public class Hub extends HubEntity{

	private static final long serialVersionUID = 1L;
	private static final float METERS_IN_MILE = 1609.344F;

	private List<HubServiceEntity> services;
	
	private MapModel map;
	private CartesianChartModel chartModel;
	private Coefficients coef1;
	private Coefficients coef2;
	private float surcharge;
	
	public Hub(){
		services = new ArrayList<HubServiceEntity>(0);
		chartModel = new CartesianChartModel();
		System.out.println("New empty hub");
	}
	
	public CartesianChartModel getChartModel() {
		return chartModel;
	}

	public List<PointEntity> getSortedPoints(){
		List<PointEntity> points = new ArrayList<PointEntity>(getPoints());
		Collections.sort(points);
		return points;
		
	}
	public void copy(HubEntity entity, float surcharge){
		setCity(entity.getCity());
		setState(entity.getState());
		setA1(entity.getA1());
		setA2(entity.getA2());
		setB1(entity.getB1());
		setB2(entity.getB2());
		setManualMode(entity.isManualMode());
		setPoints(entity.getPoints());
		this.surcharge = surcharge;
		generateCoefficients();
		map = new DefaultMapModel();
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
	public void setMap(MapModel map) {
		this.map = map;
	}
	
	public List<PointEntity>getPointsList(){
		if(getPoints() != null){
		return new ArrayList<PointEntity>(getPoints());
		}
		return new ArrayList<PointEntity>(0);
	}
	
	private void initChartModel(){
		chartModel = new CartesianChartModel();
		ChartSeries points = new LineChartSeries();
		points.setLabel("Based on market data");
		ChartSeries regression = new ChartSeries();
		for(PointEntity p: getSortedPoints()){
			points.set(p.getRoute().getDistance()/METERS_IN_MILE, p.getRate() * (1 + surcharge / 100));
		}
		
		float a1 = 0, b1 = 0, a2 = 0, b2 = 0;
		if(isManualMode()){
			a1 = getA1();
			b1 = getB1();
			a2 = getA2();
			b2 = getB2();
			regression.setLabel("Based on manual coefficients");
		}
		else{
			a1 = coef1.getAlpha();
			b1 = coef1.getBeta();
			a2 = coef2.getAlpha();
			b2 = coef2.getBeta();
			regression.setLabel("Based on generated coefficients");
		}
		float Bx = (a2 - a1) / (b1 - b2);
		float By = b1 * Bx + a1;
		regression.set(0, a1);
		regression.set(Bx, By);
		regression.set(350, b2 * 350 + a2);
		chartModel.addSeries(points);
		chartModel.addSeries(regression);
	}
	
	public float getGa1(){
		return coef1.getAlpha();
	}
	public float getGb1(){
		return coef1.getBeta();
	}
	public float getGa2(){
		return coef2.getAlpha();
	}
	public float getGb2(){
		return coef2.getBeta();
	}
	
	public int getTotalPoints(){
		return getPoints().size();
	}
	
	public void generateCoefficients(){
		List<Point> points1 = new ArrayList<Point>();
		List<Point> points2 = new ArrayList<Point>();
		Point point;
		for(PointEntity e : getPoints()){
			point = new Point(e.getRoute().getDistance() / METERS_IN_MILE, e.getRate() * (1 + surcharge / 100));
			if(e.getRoute().getDistance() / METERS_IN_MILE > 100)
				points2.add(point);
			else 
				points1.add(point);
		}
		coef1 = LinearRegression.getCoefficients(points1);
		coef2 = LinearRegression.getCoefficients(points2);
	}
	

}
