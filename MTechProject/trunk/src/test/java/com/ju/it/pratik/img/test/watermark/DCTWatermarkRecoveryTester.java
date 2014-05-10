package com.ju.it.pratik.img.test.watermark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.DCTWatermarker;
import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.DCTTransformUtil;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.NoiseAnalysisUtil;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class DCTWatermarkRecoveryTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(DCTWatermarkTester.class.getName());
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private String inputImage;
	private NoiseAnalysisUtil noiseAnalysisUtil = new NoiseAnalysisUtil();
	private NoiseAnalysisResult result;
	private String folderName;
	private Image origLogo;
	//set the policy
	private Location[] policy = new Location[] {
		new Location(3,0), new Location(2,1), 
		new Location(1,2), new Location(0,3),
		new Location(5,0), new Location(4,1),
		new Location(3,2), new Location(2,3),
		//new Location(1,4), new Location(0,5)
	};
	
	@Before
	public void setUp() {
		setUp(MANDRILL);
	}
	
	public void setUp(String fName) {
		inputImage = fName;
		folderName = inputImage.substring(0, inputImage.indexOf("_"))+"/";
		try {
			origLogo = new Image(WMConsts.WATERMARK_LOGO);
			LOG.info("watermarkStr len: "+origLogo.getBinaryImage1D().length+"\twatermarkStr: "+Arrays.toString(origLogo.getBinaryImage1D()));
			//we would consider watermark length as power of 2 only
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRecoverWatermark() throws IOException {
		testRecoverWatermark(inputImage.replaceFirst(".bmp", "_wm_90.jpg"));
	}
	
	public void testRecoverWatermark(String fileName) throws IOException {
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"dct/"+folderName+fileName);
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImage.getU(), watermarkedImage.getWidth(), watermarkedImage.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImage.getHeight(), watermarkedImage.getWidth());
		/** adding watermark to U channel's DWT co-efficients */
		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImage.getHeight(), watermarkedImage.getWidth(), policy, origLogo.getBinaryImage1D());
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		//watermarkUtils.writeBinaryWatermark(recoveredWatermark, watermarkWidth, watermarkHeight);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/"+folderName+"recovered/" + fileName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, origLogo.getHeight(), origLogo.getWidth());
		ImageUtils.saveImage(recoveredImage, origLogo.getWidth(), origLogo.getHeight(), recoveredWatermarkFile, "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
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
			testRecoverWatermark(s.replace(".bmp","")+"_wm_100.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_90.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_80.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_70.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_60.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_50.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_40.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_30.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_20.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_10.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_jpeg", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Rotation Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();		
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_M7.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_M5.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_M3.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_M1.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_1.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_3.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_5.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_rotate_7.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_rotate", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Gaussian Blur Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();		
			testRecoverWatermark(s.replace(".bmp","")+"_wm_blur_1.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_blur_2.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_blur_3.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_blur_4.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_blur_5.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_blur", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Equalization Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWatermark(s.replace(".bmp","")+"_wm_equalize.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_equalization", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Hurl Noise Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWatermark(s.replace(".bmp","")+"_wm_hurl_noise_5.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_hurl_noise_10.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_hurl_noise_15.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_hurl_noise_20.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_hurl_noise_25.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_noise", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Sharpen Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWatermark(s.replace(".bmp","")+"_wm_sharpen_4.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_sharpen_8.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_sharpen_12.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_sharpen_16.jpg");noiseAnalysisResults.add(result);
			testRecoverWatermark(s.replace(".bmp","")+"_wm_sharpen_20.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_sharpen", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
			
			html.append("<p>Cropping Attack Results</p>");
			noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
			testRecoverWatermark(s.replace(".bmp","")+"_wm_crop.jpg");noiseAnalysisResults.add(result);
			map.put(s+"_crop", noiseAnalysisResults);
			html.append(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults));
		}
		String s = noiseAnalysisUtil.generateComparativeReport(map);
		FileOutputStream fos = new FileOutputStream(new File("report.html"));
		fos.write(html.toString().getBytes());
		fos.close();
		
		fos = new FileOutputStream(new File("DCTComparativeReport.html"));
		fos.write(s.getBytes());
		fos.close();
	}
}
