package com.ju.it.pratik.img.util;

import java.io.IOException;

import com.ju.it.pratik.img.Image;

public class NoiseAnalysisUtil {
	
	public NoiseAnalysisResult calculatePSNR(String originalImageLoc, String watermarkedImageLoc) throws IOException {		
		Image origLogo = new Image(originalImageLoc);
		Image extractedLogo = new Image(watermarkedImageLoc);
		return calculatePSNR(origLogo.getY(), extractedLogo.getY());
	}

	public NoiseAnalysisResult calculatePSNR(int[][] originalImage, int[][] watermarkedImage) {
		int nrows = originalImage[0].length, ncols = originalImage.length;
		double signal = 0, noise = 0, peak = 0;
		int error = 0;
		NoiseAnalysisResult result = new NoiseAnalysisResult();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				//System.out.println(originalImage[i][j]+"\t"+watermarkedImage[i][j]);
				signal += originalImage[i][j] * originalImage[i][j];
				if(originalImage[i][j] != watermarkedImage[i][j]) {
					error++;
				}
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
		double ber = (double)error/(nrows*ncols);
		result.setBer(ber);
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
