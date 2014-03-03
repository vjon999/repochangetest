package com.ju.it.pratik.img.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NoiseAnalysisUtil {
	
	public NoiseAnalysisResult calculatePSNR(String originalImageLoc, String watermarkedImageLoc) throws IOException {		
		BufferedImage bufferedImage = ImageIO.read(new File(originalImageLoc));
		int imageWidth = bufferedImage.getWidth();
		int imageHeight = bufferedImage.getHeight();
		int[] originalImage = new int[imageWidth*imageHeight];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, originalImage, 0, imageWidth);
		int[] rgb = new int[imageWidth * imageHeight];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, rgb, 0, imageWidth);
		int y[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		TransformUtils.rgb2yuv(rgb, y, u, v);
		
		bufferedImage = ImageIO.read(new File(watermarkedImageLoc));
		imageWidth = bufferedImage.getWidth();
		imageHeight = bufferedImage.getHeight();
		int[] resultImage = new int[imageWidth*imageHeight];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, resultImage, 0, imageWidth);
		int[] rgb1 = new int[imageWidth * imageHeight];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, rgb1, 0, imageWidth);
		int y1[] = new int[rgb.length];
		int u1[] = new int[rgb.length];
		int v1[] = new int[rgb.length];
		TransformUtils.rgb2yuv(rgb1, y1, u1, v1);
		return calculatePSNR(ImageUtils.to2D(y, imageHeight, imageWidth), ImageUtils.to2D(y1, imageHeight, imageWidth));
	}

	public NoiseAnalysisResult calculatePSNR(int[][] originalImage, int[][] watermarkedImage) {
		int nrows = originalImage[0].length, ncols = originalImage.length;
		double signal = 0, noise = 0, peak = 0;
		NoiseAnalysisResult result = new NoiseAnalysisResult();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				//System.out.println(originalImage[i][j]+"\t"+watermarkedImage[i][j]);
				signal += originalImage[i][j] * originalImage[i][j];
				noise += Math.pow((originalImage[i][j] - watermarkedImage[i][j]), 2);
				if (peak < watermarkedImage[i][j])
					peak = watermarkedImage[i][j];
			}
		}
		double mse = noise/(double)(nrows*ncols);
		
		result.setMse(mse);
		result.setPsnr(10*Math.log10(Math.pow(255, 2)/mse));
		result.setPsnrMax(10*Math.log10(Math.pow(peak, 2)/mse));
		result.setSnr(10*Math.log10(signal/noise));
		double ncc = new CorrelationCalculator().calcNormalizedCrossCorrelation(originalImage, watermarkedImage);
		result.setNcc(ncc);
		return result;
	}
	
	public NoiseAnalysisResult calculatePSNRForBinary(String processed, String original) {
		double signal = 0, noise = 0, peak = 0;
		NoiseAnalysisResult result = new NoiseAnalysisResult();

		int orig, res;
		int diff = 0;
		for (int j = 0; j < original.length(); j++) {
			orig = Character.getNumericValue(original.charAt(j));
			res = Character.getNumericValue(processed.charAt(j));
			//System.out.println(orig+"\t"+res);
			signal += Math.pow(orig, 2);
			noise += Math.pow((orig - res), 2);
			diff += (orig != res)?1:0;
			if (peak < res)
				peak = res;
		}
		System.out.println(diff);
		double mse = noise/(double)(original.length());
		
		result.setMse(mse);
		result.setPsnr(10*Math.log10(Math.pow(1, 2)/mse));
		result.setPsnrMax(10*Math.log10(Math.pow(peak, 2)/mse));
		result.setSnr(10*Math.log10(signal/noise));
		
		return result;
	}
}
