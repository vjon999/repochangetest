package com.ju.it.pratik.img.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;

public class ImageMatcher implements WMConsts {

	private Location location = new Location(0,0,0);
	
	public void getStartingPixel(String src, String target) throws IOException, InterruptedException {
		BufferedImage srcBufImg = ImageIO.read(new File(src));
		BufferedImage targetBufImg = ImageIO.read(new File(target));
		
		int heightDiff = srcBufImg.getHeight() - targetBufImg.getHeight();
		int widthDiff = srcBufImg.getWidth() - targetBufImg.getWidth();
		System.out.println("heightDiff: "+heightDiff);
		System.out.println("widthDiff: "+widthDiff);
		CorrelationCalculator calculator = new CorrelationCalculator();
		double max = 0, ncc;
		List<Location> locations = new ArrayList<Location>();
		Long startTime = new Date().getTime();
		Location loc = new Location();
		ExecutorService executor = Executors.newFixedThreadPool(12);
		for(int y=0;y<heightDiff;y++) {
			for(int x=0;x<widthDiff;x++) {
				Location l = new Location(y, x);
				locations.add(l);
				Runnable worker = new ImageMatcherThread(srcBufImg, targetBufImg, l, this);
				executor.execute(worker);
			}
		}
		executor.shutdown();
		executor.awaitTermination(1,TimeUnit.HOURS);
		System.out.println("Finished all threads");
		Long endTime = new Date().getTime();
		System.out.println("time taken: "+(endTime-startTime)+" sec");
		for(Location location2 : locations) {
			if(location2.getValue() > max) {
				loc = location2;
			}
		}
		System.out.println(this.location);
		
		loc = new Location(46, 45);
		
		//save n extract
		int arr[][] = new int[srcBufImg.getHeight()][srcBufImg.getWidth()];
		for(int y=0;y<srcBufImg.getHeight();y++) {
			for(int x=0;x<srcBufImg.getWidth();x++) {
				if(x >= loc.getX() && y >= loc.getY()
						&& x < loc.getX()+targetBufImg.getWidth() && y < loc.getY()+targetBufImg.getHeight()) {
					arr[y][x] = srcBufImg.getRGB(x, y);
				}
				else
					arr[y][x] = 0;
			}
		}
		new File(WATERMARKED_IMAGES+"wavelet/extracted.bmp").delete();
		int[] pixels = ImageUtils.to1D(arr, srcBufImg.getHeight(), srcBufImg.getWidth());
		ImageUtils.saveImage(pixels, srcBufImg.getWidth(), srcBufImg.getHeight(), new File(WATERMARKED_IMAGES+"wavelet/extracted.bmp"), "bmp");
	}
	
	public int getBestRotationMatch(int[] arr1, int h1, int w1, int[] arr2, int h2, int w2) {
		int bestAngle = 0;
		double bestNCC = 0;
		for(int i=-10;i<=10;i++) {
			ImageRotationUtil rotationUtil = new ImageRotationUtil(arr2, h2, w2);
			int[] result = rotationUtil.rotate(i);
			double ncc = CorrelationCalculator.calcNormalizedCrossCorrelation(arr1, h1, w1, result, h2, w2);
			if(ncc > 0.95) {
				//System.out.println(ncc+" - "+i);
				if(ncc > bestNCC) {
					bestNCC = ncc;
					bestAngle = i;
				}
				
			}
		}
		//System.out.println(bestAngle+"\tNCC: "+bestNCC);
		return bestAngle;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ImageMatcher m = new ImageMatcher();
		String src = WMConsts.WATERMARKED_IMAGES+"wavelet/lena_512_wm.jpg";
		String target = WMConsts.WATERMARKED_IMAGES+"wavelet/lena_512_wm_crop.jpg";
		System.out.println(src);
		System.out.println(target);
		m.getStartingPixel(src, target);
	}

	public void setLocation(Location location) {
		synchronized (this.location) {
			if(location.getValue() > this.location.getValue()) {
				this.location = location;
				System.out.println("location updated: "+location);
			}
		}
	}
}
