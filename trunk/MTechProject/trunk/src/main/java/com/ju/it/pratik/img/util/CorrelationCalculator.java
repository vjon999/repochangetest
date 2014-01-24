package com.ju.it.pratik.img.util;

public class CorrelationCalculator {

	public float getCorrelationCoefficient(int[] x, int[] y) {
		int n = x.length;
		if(y.length < n) {
			n = y.length;
		}
		double sum_xy = 0, sum_x = 0,sum_y = 0, sum_xsq = 0, sum_ysq = 0, numerator = 0, denominator = 0;
		for(int i=0;i<n;i++) {
			sum_xy += x[i]*y[i];
			sum_x += x[i];
			sum_y += y[i];
			sum_xsq += x[i]*x[i];
			sum_ysq += y[i]*y[i];
		}
		numerator = (n*sum_xy) - (sum_x*sum_y);
		denominator = Math.sqrt(n*sum_xsq - sum_x*sum_x) * Math.sqrt(n*sum_ysq - sum_y*sum_y);
		return (float)(numerator/denominator);
	}
	
	public float getCorrelationCoefficient(float[] x, float[] y) {
		int n = x.length;
		if(y.length < n) {
			n = y.length;
		}
		double sum_xy = 0, sum_x = 0,sum_y = 0, sum_xsq = 0, sum_ysq = 0, numerator = 0, denominator = 0;
		for(int i=0;i<n;i++) {
			sum_xy += x[i]*y[i];
			sum_x += x[i];
			sum_y += y[i];
			sum_xsq += x[i]*x[i];
			sum_ysq += y[i]*y[i];
		}
		numerator = (n*sum_xy) - (sum_x*sum_y);
		denominator = Math.sqrt((n*sum_xsq - sum_x*sum_x) * (n*sum_ysq - sum_y*sum_y));
		return (float)(numerator/denominator);
	}
	
	public double calcNormalizedCrossCorrelation(int[] arr1, int[] arr2) {
		double numerator = 0, d1=0, d2=0, res=0;
		for(int i=0;i<arr1.length;i++) {
			numerator += arr1[i]*arr2[i];
			d1 += arr1[i]*arr1[i];
			d2 += arr2[i]*arr2[i];
		}
		res = numerator/Math.sqrt(d1*d2);
		return res;
	}
}
