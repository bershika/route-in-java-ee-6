package bershika.route.regression;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import bershika.route.domain.CoefficientsInfo;
import bershika.route.entities.PointEntity;

public class CoefficientsGenerator {
	
	public static CoefficientsInfo generateCoefficients(List<PointEntity> points, float surcharge) {
		List<Point> points1 = new ArrayList<Point>();
		List<Point> points2 = new ArrayList<Point>();
		Point point;
		int i = 0;
		for (PointEntity e : points) {
			
			point = new Point(e.getRoute().getDistanceInMiles(),
					e.getRate() /100F * (1 + surcharge / 100F));
			if (e.getRoute().getDistanceInMiles() > 100)
				points2.add(point);
			else
				points1.add(point);
		}
		Coefficients coef1 = LinearRegression.getCoefficients(points1);
		Coefficients coef2 = LinearRegression.getCoefficients(points2);
		CoefficientsInfo info = new CoefficientsInfo(coef1, coef2);
		return info;
	}
}
