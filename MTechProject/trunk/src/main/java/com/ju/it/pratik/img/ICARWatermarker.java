package com.ju.it.pratik.img;

import java.text.DecimalFormat;

import com.ju.it.pratik.img.util.DCTTransformUtil;

public class ICARWatermarker {
	
	public DecimalFormat format = new DecimalFormat("##.##");

	public double[] watermarkBlock(double[] image, int width, int height, String watermarkBits, Location[] policy) {
		double[][] image2D = new double[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image2D[i][j] = image[i*width+j];
				if(i<8&&j<8)
				System.out.print(format.format(image2D[i][j])+"\t");
			}
			if(i<8)
				System.out.println();
		}
		
		int m = height/WMConsts.BLOCK_SIZE;
		int n = width/WMConsts.BLOCK_SIZE;;
		for(int y=0;y<m;y++) {
			for(int x=0;x<n;x++) {
				int num = Character.getNumericValue(watermarkBits.charAt(y*m+x)); 
				watermarkBlock(image2D, x*WMConsts.BLOCK_SIZE, y*WMConsts.BLOCK_SIZE, num, policy);			
			}
		}
		System.out.println("\nAfter Watermarked\n");
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image[i*width+j] = image2D[i][j];
				if(i<8&&j<8)
					System.out.print(format.format(image2D[i][j])+"\t");
			}
			if(i<8)
				System.out.println();
		}
		return image;	
	}
	
	public void watermarkBlock(double[][] block, int startX, int startY, int wBit, Location[] policy) {
		double avg = 0;
		double midBandAvg;
		for(int i=startX;i<startX+WMConsts.BLOCK_SIZE;i++) {
			for(int j=startY;j<startY+WMConsts.BLOCK_SIZE;j++) {
				boolean escape = false;
				for(int k=0;k<policy.length;k++) {
					if(policy[k].getY() == i && policy[k].getX() == j) {
						escape = true;
						break;
					}
				}
				if(!escape) {
					avg += block[i][j];
				}
			}
		}
		avg = avg/((WMConsts.BLOCK_SIZE*WMConsts.BLOCK_SIZE)-4);
		midBandAvg = DCTTransformUtil.computeMiddleBandAvg(block, startX, startY);
		if(wBit == 0) {
			for(int i=0;i<policy.length;i++) {
				block[startY+policy[i].getY()][startX+policy[i].getX()] = Math.round(midBandAvg - WMConsts.WM_THRESHOLD);
			}
		}
		else {
			for(int i=0;i<policy.length;i++) {
				block[startY+policy[i].getY()][startX+policy[i].getX()] = Math.round(midBandAvg + WMConsts.WM_THRESHOLD); 
			}
		}
	}
	
	
	
	// RECOVERY
	
	
	public String recoverWatermark(double[] image, int width, int height, Location[] policy, String watermarkStr) {
		StringBuilder wm = new StringBuilder();
		double[][] image2D = new double[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image2D[i][j] = image[i*width+j];	
				if(i<8&&j<8)
					System.out.print(format.format(image2D[i][j])+"\t");
			}
			if(i<8)
				System.out.println();
		}
		
		int m = height/WMConsts.BLOCK_SIZE;
		int n = width/WMConsts.BLOCK_SIZE;
		int ctr = 0;
		System.out.println("\nAfter\n");
		for(int y=0;y<m;y++) {
			for(int x=0;x<n;x++) {
				recoverWatermarkBlock(image2D, x*WMConsts.BLOCK_SIZE, y*WMConsts.BLOCK_SIZE, policy, wm, Character.getNumericValue(watermarkStr.charAt(ctr++)));
			}
		}
		return wm.toString();	
	}
	
	public void recoverWatermarkBlock(double[][] block, int startX, int startY, Location[] policy, StringBuilder wm, int expectedBit) {
		double avg = 0;
		for(int i=startX;i<startX+WMConsts.BLOCK_SIZE;i++) {
			for(int j=startY;j<startY+WMConsts.BLOCK_SIZE;j++) {
				boolean escape = false;
				for(int k=0;k<policy.length;k++) {
					if(policy[k].getY() == i && policy[k].getX() == j) {
						escape = true;
						break;
					}
				}
				if(!escape) {
					avg += block[i][j];
				}
				if(i<8&&j<8 && startY<8 && startX<8)
					System.out.print(format.format(block[i][j])+"\t");
			}
			if(i<8&startX<8&&startY<8)
				System.out.println();
		}
		avg = avg/((WMConsts.BLOCK_SIZE*WMConsts.BLOCK_SIZE)-4);
		double midBandAvg = DCTTransformUtil.computeMiddleBandAvg(block, startX, startY);
		int posCounter = 0, zeroCounter = 0;
		for(int i=0;i<policy.length;i++) {
			if(expectedBit == 0) {
				if((midBandAvg - block[startY+policy[i].getY()][startX+policy[i].getX()]) > 0) {
					zeroCounter++;
				}
			}
			else {
				if((block[startY+policy[i].getY()][startX+policy[i].getX()]) - midBandAvg > 0) {
					posCounter++;
				}
			}	
		}
		/*if(posCounter > zeroCounter) {
			wm.append(1);
		}
		else {			
			wm.append(0);
		}*/
		if(expectedBit == 0 && zeroCounter > 0) {
			wm.append(0);
		}
		else if(expectedBit == 1 && posCounter > 0) {
			wm.append(1);
		}
		else {
			wm.append(0);
		}
	}
}
