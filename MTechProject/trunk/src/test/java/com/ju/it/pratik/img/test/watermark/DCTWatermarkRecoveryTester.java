package com.ju.it.pratik.img.test.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.DCTWatermarker;
import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.DCTTransformUtil;
import com.ju.it.pratik.img.util.ImageMatcher;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.NoiseAnalysisUtil;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class DCTWatermarkRecoveryTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(DCTWatermarkTester.class.getName());
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private String inputImageName;
	private NoiseAnalysisUtil noiseAnalysisUtil = new NoiseAnalysisUtil();
	private int watermarkWidth;
	private int watermarkHeight;
	private String watermarkStr;
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
		inputImageName = LENA;
		BufferedImage bufWm;
		try {
			bufWm = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
			watermarkWidth = bufWm.getWidth();
			watermarkHeight = bufWm.getHeight();
			int[] watermark = new int[bufWm.getHeight()*bufWm.getWidth()];
			bufWm.getRGB(0, 0, bufWm.getWidth(), bufWm.getHeight(), watermark, 0, bufWm.getWidth());
			watermarkStr = watermarkUtils.readBinaryWatermark(watermark);
			LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
			//we would consider watermark length as power of 2 only
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testJPG85Attack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testJPG50Attack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_50.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testJPG30Attack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_30.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testJPG20Attack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_20.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testBlurAttack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_blur_3.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.75);
	}
	
	@Test
	public void testSharpenAttack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_sharpen_10.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testNoiseAttack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_noise_50.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testNoiseBlurAttack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_noise_blur_50.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testRotationAttack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_rotate_5.jpg");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);
		Image baseWatermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+inputImageName.replace(".bmp", "_wm.jpg"));
		int angle = new ImageMatcher().getBestRotationMatch(watermarkedImg, baseWatermarkedImg);
		LOG.info("angle: "+angle);
		watermarkedImg.rotate(angle);
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testCropAttack() throws IOException {
		String fName = inputImageName.replaceFirst(".bmp", "_wm_crop_recovered.bmp");
		Image watermarkedImg = new Image(WATERMARKED_IMAGES+"dct/"+fName);	
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImg.getU(), watermarkedImg.getWidth(), watermarkedImg.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImg.getHeight(), watermarkedImg.getWidth());
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImg.getHeight(), watermarkedImg.getWidth(), policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		File recoveredWatermarkFile = new File(WATERMARKED_IMAGES+"dct/recovered/"+fName.replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkedImg.getWidth(), watermarkedImg.getHeight());
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
		assert(result.getNcc() > 0.7);
	}
}
