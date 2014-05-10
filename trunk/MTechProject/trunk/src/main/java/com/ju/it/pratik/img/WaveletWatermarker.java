package com.ju.it.pratik.img;

import java.util.logging.Logger;


public class WaveletWatermarker {

	private static Logger LOG = Logger.getLogger(WaveletWatermarker.class.getName());
	
	public int windowSize = 8;
	public double strength;
	double threshold;
	int level;
	
	public WaveletWatermarker(int windowSize, double strength, int level) {
		this.windowSize = windowSize;
		this.strength = strength;
		this.level = (int) Math.pow(2, level);
		this.threshold = (strength+(1/strength))*0.5;
		LOG.info("windowSize: "+windowSize+"\tStrength: "+strength+"\tthreshold: "+threshold+"\tlevel: "+this.level);
	}
	
	public void waveletWatermark(double[][] input, int[] watermarkLogo) {
		int ctr = 0;
		boolean exit = false;
		for(int i=0;i<input.length/(level*2) && !exit;i=i+2) {
			for(int j=input[0].length/(level*2);j<input[0].length/level;j=j+2) {
				waveletWatermark(input, i, j, watermarkLogo[ctr++%watermarkLogo.length]);
			}
		}
		for(int i=input.length/(level*2);i<input.length/level && !exit;i=i+2) {
			for(int j=0;j<input[0].length/(level*2);j=j+2) {
				waveletWatermark(input, i, j, watermarkLogo[ctr++%watermarkLogo.length]);
			}
		}
	}
	
	public void waveletWatermark(double[][] input, int startY, int startX, int bit) {
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				if(bit == 1) {
					input[i][j] = input[i][j]*strength;
				}
				else {
					input[i][j] = input[i][j]/strength;
				}
			}
		}		
	}
	
	public int[] retrieveWaveletWatermark(double[][] input, double[][] original, int origLogoLen) {
		int ctr = 0;
		int[] recoveredLogo = new int[origLogoLen];
		boolean exit = false;
		for(int i=0;i<input.length/(level*2) && !exit;i=i+2) {
			for(int j=input[0].length/(level*2);j<input[0].length/level;j=j+2) {
				recoveredLogo[ctr++] = retrieveWaveletWatermark(input, original, i, j);
			}
		}
		for(int i=input.length/(level*2);i<input.length/level && !exit;i=i+2) {
			for(int j=0;j<input[0].length/(level*2);j=j+2) {
				recoveredLogo[ctr++] = retrieveWaveletWatermark(input, original, i, j);
			}
		}
		return recoveredLogo;
	}
	
	public int retrieveWaveletWatermark(double[][] input, double[][] original, int startY, int startX) {
		int posCounter = 0, zeroCounter = 0;
		double ratio;
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				if(original[i][j] == 0)continue;
				ratio = input[i][j]/original[i][j];				
				if(ratio > threshold) {
					posCounter++;
				}
				else {
					zeroCounter++;
				}
			}
		}
		int resBit;
		if(posCounter > zeroCounter) {
			resBit = 1;
		}
		else {
			resBit = 0;
		}
		return resBit;
	}
}
