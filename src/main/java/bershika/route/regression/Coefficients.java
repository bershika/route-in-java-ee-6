package bershika.route.regression;

public class Coefficients {

	private float alpha;
	private float beta;
	
	public Coefficients(float a, float b){
		alpha = a;
		beta = b;
	}
	public float getAlpha() {
		return alpha;
	}
	public float getBeta() {
		return beta;
	}
}
