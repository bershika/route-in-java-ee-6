package bershika.route.domain;

import bershika.route.regression.Coefficients;
import bershika.route.regression.Point;

public class CoefficientsInfo {
	private Coefficients coef1;
	private Coefficients coef2;
	private boolean manual;
	
	public CoefficientsInfo(Coefficients coef1, Coefficients coef2) {
		this.coef1 = coef1;
		this.coef2 = coef2;
	}
	public CoefficientsInfo(float a1, float b1, float a2, float b2) {
		this.coef1 = new Coefficients(a1, b1);
		this.coef2 = new Coefficients(a2, b2);
		manual = true;
	}
	public Coefficients getCoef1() {
		return coef1;
	}
	public void setCoef1(Coefficients coef1) {
		this.coef1 = coef1;
	}
	public Coefficients getCoef2() {
		return coef2;
	}
	public void setCoef2(Coefficients coef2) {
		this.coef2 = coef2;
	}
	public boolean isManual() {
		return manual;
	}
	public void setManual(boolean manual) {
		this.manual = manual;
	}
	
	public Point getIntercept(){	
		float Bx = 0;
		if ((coef1.getBeta() - coef2.getBeta()) != 0) {
			Bx = (coef2.getAlpha() - coef1.getAlpha()) / (coef1.getBeta() - coef2.getBeta());
		}
		float By = coef1.getBeta() * Bx + coef1.getAlpha();
		return new Point(Bx, By);
	}
	
}
