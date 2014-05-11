package com.ju.it.pratik.img.test.watermark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.WaveletWatermarker;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.WaveletTransformer;

public class WaveletWatermarkTester extends AbstractWatermarkTester {

	private static final Logger LOG = Logger.getLogger(WaveletWatermarkTester.class.getName());
	private WaveletWatermarker watermarker;
	int level = 2;
	double strength = 1.025;
	
	@Before
	public void setUp() {
		setUp(LENA);
		watermarker = new WaveletWatermarker(2, strength, level);
	}
	
	@Test
	public void testWaveletWatermark() throws IOException {
		watermarker.waveletWatermark(dwt2Doriginal, origLogo.getBinaryImage1D());
		int[][] idwt2D = WaveletTransformer.inverseDiscreteWaveletTransform(dwt2Doriginal, level);
		postWatermark(idwt2D);
	}
	
	@Test
	public void testRecoverWaveletWatermark() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_20.jpg");
		testRecoverWaveletWatermark(watermarkedImageName);
	}
	
	@Test
	public void testRecoverWaveletCroppedWatermark() throws IOException, InterruptedException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_-7.jpg");
		testRecoverWaveletWatermarkRotated(watermarkedImageName);
	}
	
	public void testRecoverWaveletWatermark(String fileName) throws IOException {
		watermarkedImageName = fileName;
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+folderName+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getBinaryImage1D().length);
		postWMRetrieval(recoveredLogo);
	}
	
	public void testRecoverWaveletWatermarkCrop(String fileName) throws IOException, InterruptedException {
		watermarkedImageName = fileName;
		Image watermarkedImage = recoverCrop(fileName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getBinaryImage1D().length);
		postWMRetrieval(recoveredLogo);
	}
	
	public void testRecoverWaveletWatermarkRotated(String fileName) throws IOException, InterruptedException {
		watermarkedImageName = fileName;
		Image watermarkedImage = recoverRotatedImage(fileName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getBinaryImage1D().length);
		postWMRetrieval(recoveredLogo);
	}
	
	@Test
	public void testRecoverySuite() throws IOException, InterruptedException {
		StringBuilder html = new StringBuilder();
		Map<String, List<NoiseAnalysisResult>> map = new HashMap<String, List<NoiseAnalysisResult>>();
		for(String s : TEST_IMAGES) {
			setUp(s);
			html.append("<h1>Results for "+inputImage.substring(0, inputImage.indexOf("_"))+"</h1><hr>");
			html.append("<p>JPEG Compression Results</p>");
			List<NoiseAnalysisResult> noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_100.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_90.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_80.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_70.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_60.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_50.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_40.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_30.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_20.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_10.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_jpeg", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Rotation Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();		
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_M7.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_M5.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_M3.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_M1.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_1.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_3.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_5.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermarkRotated(s.replace(".bmp","")+"_wm_rotate_7.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_rotate", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Gaussian Blur Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();		
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_blur_1.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_blur_2.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_blur_3.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_blur_4.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_blur_5.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_blur", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Equalization Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_equalize.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_equalization", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Hurl Noise Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_hurl_noise_5.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_hurl_noise_10.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_hurl_noise_15.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_hurl_noise_20.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_hurl_noise_25.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_noise", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Sharpen Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_sharpen_4.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_sharpen_8.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_sharpen_12.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_sharpen_16.jpg");noiseAnalysisResults.add(result);
			testRecoverWaveletWatermark(s.replace(".bmp","")+"_wm_sharpen_20.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_sharpen", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Cropping Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWaveletWatermarkCrop(s.replace(".bmp","")+"_wm_crop.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_crop", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
		}
		String s = noiseAnalysisUtil.generateComparativeReport(map);
		FileOutputStream fos = new FileOutputStream(new File("report.html"));
		fos.write(html.toString().getBytes());
		fos.close();
		
		fos = new FileOutputStream(new File("WaveletComparativeReport.html"));
		fos.write(s.getBytes());
		fos.close();
	}
}
