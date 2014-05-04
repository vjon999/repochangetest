package com.ju.it.pratik.img;

import java.util.logging.Logger;

import com.ju.it.pratik.img.test.PrintUtil;


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
		LOG.info("windowSize: "+windowSize+"\tStrength: "+strength+"\tthreshold: "+threshold+"\tlevel: "+level);
	}
	int monitorX = 4,monitorY=4;
	public void waveletWatermark(double[][] input, String watermarkStr) {
		int xwindows = input[0].length/(windowSize*level);
		int ywindows = input.length/(windowSize*level);
		LOG.fine("ywindows: "+ywindows+"\txwindows"+xwindows); 
		int wlenCtr = 0;
		boolean exit = false;
		for(int i=0;i<ywindows/4 && !exit;i++) {
			for(int j=xwindows/4;j<xwindows*3/4;j++) {
				/*if(i<=monitorY && j<=xwindows/4+monitorX) {
					System.out.println(i*windowSize+","+ j*windowSize);
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
				waveletWatermark(input, i*windowSize, j*windowSize, Character.getNumericValue(watermarkStr.charAt(wlenCtr++%watermarkStr.length())));
				/*if(i<=monitorY && j<=xwindows/4+monitorX) {
					System.out.println(i*windowSize+","+ j*windowSize);
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
			}
		}
		for(int i=ywindows/4;i<ywindows*3/4 && !exit;i++) {
			for(int j=0;j<xwindows/4;j++) {
				/*if(i<=ywindows/4+monitorY && j<=monitorX) {
					System.out.println(i*windowSize+","+ j*windowSize);
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
				waveletWatermark(input, i*windowSize, j*windowSize, Character.getNumericValue(watermarkStr.charAt(wlenCtr++%watermarkStr.length())));
				/*if(i<=ywindows/4+monitorY && j<=monitorX) {
					System.out.println(i*windowSize+","+ j*windowSize);
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
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
	
	public String retrieveWaveletWatermark(double[][] input, double[][] original, String watermarkStr) {
		int xwindows = input[0].length/(windowSize*level);
		int ywindows = input.length/(windowSize*level);
		LOG.fine("ywindows: "+ywindows+"\txwindows"+xwindows); 
		int wlenCtr = 0;
		boolean exit = false;
		StringBuilder recoveredWatermark = new StringBuilder();
		for(int i=0;i<ywindows/4 && !exit;i++) {
			for(int j=xwindows/4;j<xwindows*3/4;j++) {
				/*if(i<=monitorY && j<=xwindows/4+monitorX) {
					System.out.println(i*windowSize+","+ j*windowSize);
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
				retrieveWaveletWatermark(input, original, i*windowSize, j*windowSize, Character.getNumericValue(watermarkStr.charAt(wlenCtr++%watermarkStr.length())), watermarkStr.length(), recoveredWatermark);
				/*if(i<=monitorY && j<=xwindows/4+monitorX) {
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
			}
			if(exit)
				break;
		}
		for(int i=ywindows/4;i<ywindows*3/4 && !exit;i++) {
			for(int j=0;j<xwindows/4;j++) {
				/*if(i<=ywindows/4+monitorY && j<=monitorX) {
					System.out.println(i*windowSize+","+ j*windowSize);
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
				retrieveWaveletWatermark(input, original, i*windowSize, j*windowSize, Character.getNumericValue(watermarkStr.charAt(wlenCtr++%watermarkStr.length())), watermarkStr.length(), recoveredWatermark);
				/*if(i<=ywindows/4+monitorY && j<=monitorX) {
					System.out.println(PrintUtil.print2DArray(input, i*windowSize, j*windowSize, windowSize));
				}*/
			}
			if(exit)
				break;
		}
		return recoveredWatermark.toString();
	}
	
	public void retrieveWaveletWatermark(double[][] input, double[][] original, int startY, int startX, int bit, int wmLen, StringBuilder recoveredWatermark ) {
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
		recoveredWatermark.append(resBit);
	}
}
