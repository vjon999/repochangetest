package com.ju.it.pratik.img.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.WMConsts;

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

		for (int i = 0; i < ncols; i++) {
			for (int j = 0; j < nrows; j++) {
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
	
	public String generateHTMLReport(List<NoiseAnalysisResult> list) {
		StringBuilder html = new StringBuilder("<table><thead><tr><th>NCC</th><th>MSE</th><th>PSNR</th><th>PSNR Max</th><th>SNR</th><th>BER</th></thead><tbody>");
		for(NoiseAnalysisResult result : list) {
			html.append(result.toHTML());
		}
		html.append("</tbody></table>");
		return html.toString();
	}
	
	public String generateComparativeReport(Map<String, List<NoiseAnalysisResult>> map) {
		DecimalFormat df = new DecimalFormat("##.00");
		StringBuilder html = new StringBuilder("<style>table,td,th{border: 1px solid black;}table{border-collapse:collapse;}</style><h1>JPEG COMPRESSION RESULTS</h1>");//"<tr><th>JPEG Quality</th>"
		StringBuilder header = new StringBuilder();
		StringBuilder header1 = new StringBuilder("<table><thead><tr><th></th>");
		StringBuilder header2 = new StringBuilder("<tr><th>JPEG Quality</th>");
		for(String s : WMConsts.TEST_IMAGES) {
			s = s.replace("_512.bmp", "");
			header1.append("<th colspan='2'>"+s+"</th>");
			//html.append("<th>NCC("+s+")</th><th>PSNR("+s+")</th><th>BER("+s+")</th>");
			header2.append("<th>NCC</th><th>BER</th>");
		}
		header1.append("</tr>");
		header2.append("</tr>");
		header.append(header1).append(header2).append("</thead><tbody>");
		html.append(header);
		
		for(int c=100;c>0;c=c-10) {
			html.append("<tr><td>"+c+"</td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_jpeg").get((100-c)/10);
				//html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getPsnr())+"</td><td>"+df.format(result.getBer())+"</td>");
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		
		html.append("<h1>Sharpen Attack Results</h1>");
		html.append(header.toString().replace("JPEG Quality", "Sharpen Radius"));
		for(int c=1;c<=5;c++) {
			html.append("<tr><td>"+c*4+"</td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_sharpen").get(c-1);
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		
		
		
		html.append("<h1>Blur Attack Results</h1>");
		html.append(header.toString().replace("JPEG Quality", "Blur Radius"));
		for(int c=1;c<=5;c++) {
			html.append("<tr><td>"+c+"</td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_blur").get(c-1);
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		
		html.append("<h1>Rotation Attack Results</h1>");
		html.append(header.toString().replace("JPEG Quality", "Rotation Angle"));
		for(int c=0;c<8;c++) {
			html.append("<tr><td>"+(-7+(c*2))+"</td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_rotate").get(c);
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		
		html.append("<h1>Hurl Noise Attack Results</h1>");
		html.append(header.toString().replace("JPEG Quality", "Randomization"));
		for(int c=1;c<=5;c++) {
			html.append("<tr><td>"+c*5+"</td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_noise").get(c-1);
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		
		
		html.append("<h1>Equalization Attack Results</h1>");
		html.append(header.toString().replace("JPEG Quality", "Equalize"));
		//for(int c=1;c<5;c++) {
			html.append("<tr><td></td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_equalization").get(0);
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		//}
		html.append("</tbody></table>");
		
		
		html.append("<h1>Cropping Attack Results</h1>");
		html.append(header.toString().replace("JPEG Quality", "Cropped"));
		//for(int c=1;c<5;c++) {
			html.append("<tr><td></td>");
			for(String s : WMConsts.TEST_IMAGES) {
				NoiseAnalysisResult result = map.get(s+"_crop").get(0);
				html.append("<td>"+df.format(result.getNcc())+"</td><td>"+df.format(result.getBer())+"</td>");
			}
			html.append("</tr>");
		//}
		html.append("</tbody></table>");
		
		
		return html.toString();
	}
}
