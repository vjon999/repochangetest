package com.ju.it.pratik.img.util;

import java.awt.image.BufferedImage;

/**
 * @author pratik
 *
 */
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
	
	/**
	 * This method returns the Normalized Cross Correlation (NCC) 
	 * which is used for template matching and can be used for measuring 
	 * the similarities of 2 images 
	 * @param src image array
	 * @param dest image array
	 * @return normalized cross correlation value
	 */
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
	
	public double calcNormalizedCrossCorrelation(int[][] arr1, int[][] arr2) {
		double numerator = 0, d1=0, d2=0, res=0;
		for(int i=0;i<arr1.length;i++) {
			for(int j=0;j<arr1.length;j++) {
				numerator += arr1[i][j]*arr2[i][j];
				d1 += arr1[i][j]*arr1[i][j];
				d2 += arr2[i][j]*arr2[i][j];
			}
		}
		res = numerator/Math.sqrt(d1*d2);
		return res;
	}
	
	public double calcNormalizedCrossCorrelation(BufferedImage src, BufferedImage target, int startY, int startX) {
		double numerator = 0, d1=0, d2=0, res=0;
		int srcRed, targetRed;
		for(int y=0;y<target.getHeight();y++) {
			for(int x=0;x<target.getWidth();x++) {
				//System.out.println((y+startX)+","+(x+startY));
				srcRed = (src.getRGB(x+startY, y+startY)&0xFF0000)>>16;
				targetRed = (target.getRGB(x, y)&0xFF0000)>>16;
				//System.out.println("srcRed: "+srcRed+" targetRed: "+targetRed );
				numerator += srcRed*targetRed;
				d1 += Math.pow(srcRed, 2);
				d2 += Math.pow(targetRed, 2);
			}
		}
		res = numerator/Math.sqrt(d1*d2);
		return res;
	}
	
	public static double calcNormalizedCrossCorrelation(int[] src, int h1, int w1, int[] target, int h2, int w2) {
		double numerator = 0, d1=0, d2=0, res=0;
		int pixelSrc, pixelTarget;
		for(int y=0;y<h2;y++) {
			for(int x=0;x<w2;x++) {
				pixelSrc = src[y*w1+x]&0xFF;
				pixelTarget = target[y*w2+x]&0xFF;
				if(pixelSrc <= 1 || pixelTarget <= 1) {
					continue;
				}
				numerator += pixelSrc*pixelTarget;
				d1 += Math.pow(pixelSrc, 2);
				d2 += Math.pow(pixelTarget, 2);
			}
		}
		res = numerator/Math.sqrt(d1*d2);
		return res;
	}
}
