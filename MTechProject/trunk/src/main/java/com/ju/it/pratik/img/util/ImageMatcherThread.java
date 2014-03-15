package com.ju.it.pratik.img.util;

import java.awt.image.BufferedImage;

import com.ju.it.pratik.img.Location;

public class ImageMatcherThread implements Runnable {
	
	private BufferedImage src;
	private BufferedImage target;
	private Location location;
	private ImageMatcher imageMatcher;
	
	public ImageMatcherThread(BufferedImage src, BufferedImage target, Location location, ImageMatcher imageMatcher) {
		this.src = src;
		this.target = target;
		this.location = location;
		this.imageMatcher = imageMatcher;
	}
	
	@Override
	public void run() {
		location.setValue(calcNormalizedCrossCorrelation(src, target, location.getY(), location.getX()));
		imageMatcher.setLocation(location);
		//System.out.println(Thread.currentThread().getName()+" location: "+location);
	}
	
	public double calcNormalizedCrossCorrelation(int[] src, int h1, int w1, int[] target, int h2, int w2) {
		double numerator = 0, d1=0, d2=0, res=0;
		int srcRed, targetRed, rgbSrc, rgbTarget;
		for(int y=0;y<h2;y++) {
			for(int x=0;x<w2;x++) {
				rgbSrc = src[y*w1+x];
				rgbTarget = target[y*w2+x];
				srcRed = Math.round(((rgbSrc&0xFF0000>>16) + (rgbSrc&0xFF00>>8) + (rgbSrc&0xFF))/3);
				targetRed = Math.round(((rgbTarget&0xFF0000>>16) + (rgbTarget&0xFF00>>8) + (rgbTarget&0xFF))/3);
				numerator += srcRed*targetRed;
				d1 += Math.pow(srcRed, 2);
				d2 += Math.pow(targetRed, 2);
			}
		}
		res = numerator/Math.sqrt(d1*d2);
		numerator = 0;d1=0;d2=0;
		return res;
	}
	
	int windowWidth = 64;
	public double calcNormalizedCrossCorrelation(BufferedImage src, BufferedImage target, int startY, int startX) {
		double numerator = 0, d1=0, d2=0, res=0;
		int srcRed, targetRed, rgbSrc, rgbTarget;
		for(int y=0;y<windowWidth;y++) {
			for(int x=0;x<windowWidth;x++) {
				//System.out.println((y+startX)+","+(x+startX));
				rgbSrc = src.getRGB(x+startX, y+startY);
				rgbTarget = target.getRGB(x, y);
				srcRed = Math.round(((rgbSrc&0xFF0000>>16) + (rgbSrc&0xFF00>>8) + (rgbSrc&0xFF))/3);
				targetRed = Math.round(((rgbTarget&0xFF0000>>16) + (rgbTarget&0xFF00>>8) + (rgbTarget&0xFF))/3);
				//System.out.println("srcRed: "+srcRed+" targetRed: "+targetRed );
				numerator += srcRed*targetRed;
				d1 += Math.pow(srcRed, 2);
				d2 += Math.pow(targetRed, 2);
			}
		}
		res = numerator/Math.sqrt(d1*d2);
		numerator = 0;d1=0;d2=0;
		
		
		
		for(int y=0;y<windowWidth;y++) {
			for(int x=target.getWidth()-windowWidth;x<target.getWidth();x++) {
				//System.out.println((y+startY)+","+(x+startX));
				rgbSrc = src.getRGB(x+startX, y+startY);
				rgbTarget = target.getRGB(x, y);
				srcRed = Math.round(((rgbSrc&0xFF0000>>16) + (rgbSrc&0xFF00>>8) + (rgbSrc&0xFF))/3);
				targetRed = Math.round(((rgbTarget&0xFF0000>>16) + (rgbTarget&0xFF00>>8) + (rgbTarget&0xFF))/3);
				//System.out.println("srcRed: "+srcRed+" targetRed: "+targetRed );
				numerator += srcRed*targetRed;
				d1 += Math.pow(srcRed, 2);
				d2 += Math.pow(targetRed, 2);
			}
		}
		res += numerator/Math.sqrt(d1*d2);
		numerator = 0;d1=0;d2=0;
		
		
		for(int y=target.getHeight()-windowWidth;y<target.getHeight();y++) {
			for(int x=0;x<windowWidth;x++) {
				//System.out.println((y+startX)+","+(x+startX));
				rgbSrc = src.getRGB(x+startX, y+startY);
				rgbTarget = target.getRGB(x, y);
				srcRed = Math.round(((rgbSrc&0xFF0000>>16) + (rgbSrc&0xFF00>>8) + (rgbSrc&0xFF))/3);
				targetRed = Math.round(((rgbTarget&0xFF0000>>16) + (rgbTarget&0xFF00>>8) + (rgbTarget&0xFF))/3);
				//System.out.println("srcRed: "+srcRed+" targetRed: "+targetRed );
				numerator += srcRed*targetRed;
				d1 += Math.pow(srcRed, 2);
				d2 += Math.pow(targetRed, 2);
			}
		}
		res += numerator/Math.sqrt(d1*d2);
		numerator = 0;d1=0;d2=0;
		
		
		for(int y=target.getHeight()-windowWidth;y<target.getHeight();y++) {
			for(int x=target.getWidth()-windowWidth;x<target.getWidth();x++) {
				//System.out.println((y+startX)+","+(x+startY));
				rgbSrc = src.getRGB(x+startY, y+startY);
				rgbTarget = target.getRGB(x, y);
				srcRed = Math.round(((rgbSrc&0xFF0000>>16) + (rgbSrc&0xFF00>>8) + (rgbSrc&0xFF))/3);
				targetRed = Math.round(((rgbTarget&0xFF0000>>16) + (rgbTarget&0xFF00>>8) + (rgbTarget&0xFF))/3);
				//System.out.println("srcRed: "+srcRed+" targetRed: "+targetRed );
				numerator += srcRed*targetRed;
				d1 += Math.pow(srcRed, 2);
				d2 += Math.pow(targetRed, 2);
			}
		}
		res += numerator/Math.sqrt(d1*d2);
		res = res/4;
		
		
		
		
		return res;
	}
	
	public double product(BufferedImage src, BufferedImage target, int startY, int startX) {
		double product = 0;
		int srcRed, targetRed;
		for(int y=0;y<target.getHeight();y++) {
			for(int x=0;x<target.getWidth();x++) {
				srcRed = (src.getRGB(x+startY, y+startY)&0xFF0000)>>16;
				targetRed = (target.getRGB(x, y)&0xFF0000)>>16;
				//System.out.println("srcRed: "+srcRed+" targetRed: "+targetRed );
				product += (srcRed*targetRed)/Math.pow(255, 2);
			}
		}
		return product;
	}
}
