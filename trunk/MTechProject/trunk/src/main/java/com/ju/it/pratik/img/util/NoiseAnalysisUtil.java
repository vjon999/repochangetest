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
		
		bufferedImage = ImageIO.read(new File(watermarkedImageLoc));
		imageWidth = bufferedImage.getWidth();
		imageHeight = bufferedImage.getHeight();
		int[] resultImage = new int[imageWidth*imageHeight];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, resultImage, 0, imageWidth);
		return calculatePSNR(ImageUtils.to2D(originalImage, imageHeight, imageWidth), ImageUtils.to2D(resultImage, imageHeight, imageWidth));
	}

	public NoiseAnalysisResult calculatePSNR(int[][] originalImage, int[][] watermarkedImage) {
		int nrows = originalImage[0].length, ncols = originalImage.length;
		long signal = 0, noise = 0, peak = 0;
		NoiseAnalysisResult result = new NoiseAnalysisResult();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				signal += originalImage[i][j] * originalImage[i][j];
				noise += (originalImage[i][j] - watermarkedImage[i][j]) * (originalImage[i][j] - watermarkedImage[i][j]);
				if (peak < watermarkedImage[i][j])
					peak = watermarkedImage[i][j];
			}
		}
		double mse = (double)noise/(double)(nrows*ncols);
		
		result.setMse(mse);
		result.setPsnr(10*Math.log10(255*255/mse));
		result.setPsnrMax(10*Math.log10((peak*peak)/mse));
		result.setSnr(10*Math.log10(signal/noise));
		
		return result;
	}
}
