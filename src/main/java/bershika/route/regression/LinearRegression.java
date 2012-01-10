package bershika.route.regression;

import java.util.List;

public final class LinearRegression {

	private static Point getAvgPoint(final List<Point> points) {
		Point avgP = new Point(0, 0);;
		int n = points.size();
		if (n > 0) {
			float sumX = 0, sumY = 0;
			for (Point p : points) {
				sumX += p.x;
				sumY += p.y;
			}
			avgP = new Point(sumX / n, sumY / n);
		}
		return avgP;
	}

	public static Coefficients getCoefficients(final List<Point> points){
		
		int n = points.size() - 1;
		if(n < 1) return new Coefficients(0F,0F);
		Point avgP = getAvgPoint(points);
		float sumXY = 0, sumX2 = 0;
		
		for(Point p : points){
			sumXY += ((p.x - avgP.x)*(p.y - avgP.y));
			sumX2 += Math.pow((p.x - avgP.x), 2);
		}
		float b = (sumXY / n) / (sumX2 / n);
		float a = avgP.y - b * avgP.x;
		return new Coefficients(a, b);
	}	
}
