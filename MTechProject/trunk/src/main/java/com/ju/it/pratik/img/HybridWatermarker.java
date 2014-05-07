package com.ju.it.pratik.img;

import java.util.Arrays;
import java.util.logging.Logger;

import com.ju.it.pratik.img.util.DCTTransformUtil;

public class HybridWatermarker {

	private static Logger LOG = Logger.getLogger(WaveletWatermarker.class.getName());
	
	public int windowSize = 8;
	public double strength;
	double threshold;
	int level;
	int monitorX = 4,monitorY=4;
	
	public HybridWatermarker(int windowSize, double strength, int level) {
		this.windowSize = windowSize;
		this.strength = strength;
		this.level = (int) Math.pow(2, level);
		this.threshold = (strength+(1/strength))*0.5;
		LOG.info("windowSize: "+windowSize+"\tStrength: "+strength+"\tthreshold: "+threshold+"\tlevel: "+level);
	}
	
	public void hybridWatermark(double[][] input, int[] watermarkLogo) {
		int xwindows = input[0].length/(windowSize*level);
		int ywindows = input.length/(windowSize*level);
		LOG.fine("ywindows: "+ywindows+"\txwindows"+xwindows); 
		int wlenCtr = 0;
		boolean exit = false;
		for(int i=0;i<input.length/(level*2) && !exit;i=i+4) {
			for(int j=input[0].length/(level*2);j<input[0].length/level;j=j+4) {
				hybridWatermark(input, i, j, watermarkLogo, (wlenCtr)%watermarkLogo.length);
				wlenCtr += 4;
			}
		}
		for(int i=input.length/(level*2);i<input.length/level && !exit;i=i+4) {
			for(int j=0;j<input[0].length/(2*level);j=j+4) {
				hybridWatermark(input, i, j, watermarkLogo, (wlenCtr)%watermarkLogo.length);
				wlenCtr += 4;
			}
		}
	}
	
	public void hybridWatermark(double[][] input, int startY, int startX, int[] watermarkLogo, int index) {
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(windowSize);
		double[][] tmp = new double[windowSize][windowSize];
		
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				tmp[i-startY][j-startX] = input[i][j];
			}
			System.out.println("Origunal");
			System.out.println(Arrays.toString(tmp[i-startY]));
			double[] dct = dctTransformUtil.applyDCT(tmp[i-startY]);
			System.out.println("After DCT");
			System.out.println(Arrays.toString(dct));
			
			//watermarking
			if(watermarkLogo[index++] == 1) {
				double diff = dct[2] - dct[3];
				if(diff < 0) {
					if(Math.abs(diff) > strength) {
						System.out.println();
					}
					dct[2] = dct[2] - ((diff-strength)/2);
					dct[3] = dct[3] + ((diff-strength)/2);
				}
				else {
					if(diff < strength) {
						dct[2] = dct[2] + ((diff+strength)/2);
						dct[3] = dct[3] - ((diff+strength)/2);
					}
				}
			}
			else {// bit is 0
				double diff = dct[3] - dct[2];				
				if(diff < 0) {
					dct[2] = dct[2] + ((diff-strength)/2);
					dct[3] = dct[3] - ((diff-strength)/2);
				}
				else {
					if(diff < strength) {
						dct[2] = dct[2] - ((diff+strength)/2);
						dct[3] = dct[3] + ((diff+strength)/2);
					}
					
				}
			}
			System.out.println("After Watermarking");
			System.out.println(Arrays.toString(dct));
			double[] idct = dctTransformUtil.applyIDCT(dct);
			System.out.println("After Inverse DCT");
			System.out.println(Arrays.toString(idct));
			for(int j=startX;j<startX+windowSize;j++) {
				input[i][j] = idct[j-startX];
			}
		}
	}
	
	public void hybridWatermark2(double[][] input, int startY, int startX, int[] watermarkLogo, int index) {
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(windowSize);
		double[][] tmp = new double[windowSize][windowSize];
		
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				tmp[i-startY][j-startX] = input[i][j];
			}
			double[] dct = dctTransformUtil.applyDCT(tmp[i-startY]);
			int sign1 = (int) Math.signum(dct[2]);
			int sign2 = (int) Math.signum(dct[3]);
			dct[2] = Math.abs(dct[2]);
			dct[3] = Math.abs(dct[3]);
			double absDiff = dct[2] - dct[3];
			double change = 0;
			//watermarking
			if(watermarkLogo[index++] == 1) {
				//bit is 1
				if(absDiff > 0) {
					if(absDiff > strength) {
						//do nothing
					}
					else {
						change = (strength-absDiff)/2d;
						if(dct[3] > (change/2d)) {
							dct[3] = dct[3] - change;
							dct[2] = dct[2] + change;
						}
						else {
							dct[2] = dct[2] + 2*change;
						}						
					}
					
				}
				else {
					change = (strength-absDiff)/2d;
					
					//dct[3] = (dct[3] - change < 0)?0:dct[3]-change;
					if(dct[3] > (change/2d)) {
						dct[3] = dct[3] - change;
						dct[2] = dct[2] + change;
					}
					else {
						dct[2] = dct[2] + 2*change;
					}
				}
			}
			else {// bit is 0
				if(absDiff > 0) {
					change = (strength+absDiff)/2d;
					dct[3] = dct[3] + change;
					dct[2] = (dct[2] - change < 0)?0:dct[2]-change;
				}
				else {
					if(Math.abs(absDiff) > strength) {
						//do nothing
					}
					else {
						change = (strength+absDiff)/2d;
						
						if(dct[2] > (change/2d)) {
							dct[2] = dct[2] - change;
							dct[3] = dct[3] + change;
						}
						else {
							dct[3] = dct[3] + 2*change;
						}
					}
				}
			}
			dct[2] = dct[2]*sign1;
			dct[3] = dct[3]*sign2;
			double[] idct = dctTransformUtil.applyIDCT(dct);
			for(int j=startX;j<startX+windowSize;j++) {
				input[i][j] = idct[j-startX];
			}
		}
	}
	
	public void hybridWatermark3(double[][] input, int startY, int startX, int[] watermarkLogo, int index) {
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(windowSize);
		double[][] tmp = new double[windowSize][windowSize];
		
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				tmp[i-startY][j-startX] = input[i][j];
			}
			double[] dct = dctTransformUtil.applyDCT(tmp[i-startY]);
			int sign1 = (int) Math.signum(dct[2]);
			int sign2 = (int) Math.signum(dct[3]);
			dct[2] = Math.abs(dct[2]);
			dct[3] = Math.abs(dct[3]);
			double absDiff = dct[2] - dct[3];
			double change = 0;
			//watermarking
			if(watermarkLogo[index++] == 1) {
				//bit is 1
				if(absDiff > 0) {
					if(absDiff > strength) {
						//do nothing
					}
					else {
						change = (strength)/2d;
						if(dct[3] - change < 0) {
							dct[2] = dct[2] + 2*change;
							dct[3] = 0;
						}
						else {
							dct[2] = dct[2] + change;
							dct[2] = dct[3] - change;
						}						
					}
				}
				else {
					change = (strength-absDiff)/2d;					
					if(dct[3] - change < 0) {
						dct[2] = dct[2] + 2*change;
						dct[3] = 0;
					}
					else {
						dct[2] = dct[2] + change;
						dct[3] = dct[3] - change;
					}
				}
			}
			else {// bit is 0
				if(absDiff > 0) {
					change = (strength+absDiff)/2d;
					if(Math.abs(dct[2]-change) > dct[2]) {
						dct[3] = dct[3] + 2*change;
						dct[2] = 0;
					}
					else {
						dct[3] = dct[3] + change;
						dct[2] = dct[2] - change;
					}
				}
				else {
					if(Math.abs(absDiff) > strength) {
						//do nothing
					}
					else {
						change = (strength+absDiff)/2d;						
						if(Math.abs(dct[2]-change) > dct[2]) {
							dct[3] = dct[3] + 2*change;
							dct[2] = 0;
						}
						else {
							dct[2] = dct[2] - change;
							dct[3] = dct[3] + change;
						}
					}
				}
			}
			dct[2] = dct[2]*sign1;
			dct[3] = dct[3]*sign2;
			double[] idct = dctTransformUtil.applyIDCT(dct);
			for(int j=startX;j<startX+windowSize;j++) {
				input[i][j] = idct[j-startX];
			}
		}
	}
	
	public int[] retrieveWaveletWatermark(double[][] input, double[][] original, int[] watermarkLogo) {
		int[] recoveredLogo = new int[watermarkLogo.length];
		int ctr = 0;
		
		for(int i=0;i<input.length/(level*2);i=i+4) {
			for(int j=input[0].length/(level*2);j<input[0].length/level;j=j+4) {
				retrieveWaveletWatermark(input, i, j, recoveredLogo, ctr, watermarkLogo[ctr]);
				ctr += 4;
			}
		}
		for(int i=input.length/(level*2);i<input.length/level;i=i+4) {
			for(int j=0;j<input[0].length/(level*2);j=j+4) {
				retrieveWaveletWatermark(input, i, j, recoveredLogo, ctr, watermarkLogo[ctr]);
				ctr += 4;
			}
		}
		return recoveredLogo;
	}
	
	public void retrieveWaveletWatermark(double[][] input, int startY, int startX, int[] recoveredLogo, int index, int expectedBit) {
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(windowSize);
		double[][] tmp = new double[windowSize][windowSize];
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				tmp[i-startY][j-startX] = input[i][j];
			}
			/*System.out.println("Original");
			System.out.println(Arrays.toString(tmp[i-startY]));*/
			//System.out.println("After DCT");
			double[] dct = dctTransformUtil.applyDCT(tmp[i-startY]);
			//System.out.println(Arrays.toString(dct));
			if(dct[2] > dct[3]) {
				recoveredLogo[index++] = 1;
			}
			else {
				recoveredLogo[index++] = 0;
			}
		}
	}
	
	public void retrieveWaveletWatermark2(double[][] input, int startY, int startX, int[] recoveredLogo, int index, int expectedBit) {
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(windowSize);
		double[][] tmp = new double[windowSize][windowSize];
		for(int i=startY;i<startY+windowSize;i++) {
			for(int j=startX;j<startX+windowSize;j++) {
				tmp[i-startY][j-startX] = input[i][j];
			}
			double[] dct = dctTransformUtil.applyDCT(tmp[i-startY]);
			if(Math.abs(dct[2]) > Math.abs(dct[3])) {
				recoveredLogo[index++] = 1;
			}
			else {
				recoveredLogo[index++] = 0;
			}
		}
	}
}
