package com.ju.it.pratik.img.test.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.ietf.jgss.Oid;
import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.WaveletWatermarker;
import com.ju.it.pratik.img.util.ImageMatcher;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.NoiseAnalysisUtil;
import com.ju.it.pratik.img.util.Sobel;
import com.ju.it.pratik.img.util.WatermarkUtils;
import com.ju.it.pratik.img.util.WaveletTransformer;

public class WatermarkRecoveryTest implements WMConsts {

	private static final Logger LOG = Logger.getLogger(WatermarkRecoveryTest.class.getName());
	
	private String inputImage;
	private String watermarkedImageName;
	private WatermarkUtils watermarkUtils;
	private String watermarkStr;
	private WaveletWatermarker watermarker;
	private NoiseAnalysisUtil noiseAnalysisUtil;
	private NoiseAnalysisResult result;
	int level = 2;
	double strength = 1.025;
	double dwt2Doriginal[][];
	int watermarkHeight;
	int watermarkWidth;
	private Image srcImg;	
	
	@Before
	public void setUp() {
		inputImage = MANDRILL;
		watermarkUtils = new WatermarkUtils();
		noiseAnalysisUtil = new NoiseAnalysisUtil();
		watermarker = new WaveletWatermarker(2, strength, level);
		BufferedImage wmImage;
		try {
			wmImage = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
			watermarkHeight = wmImage.getHeight();
			watermarkWidth = wmImage.getWidth();
			int[] watermark = new int[wmImage.getHeight()*wmImage.getWidth()];
			wmImage.getRGB(0, 0, wmImage.getWidth(), wmImage.getHeight(), watermark, 0, wmImage.getWidth());
			WatermarkUtils watermarkUtils = new WatermarkUtils();
			watermarkStr = watermarkUtils.readBinaryWatermark(watermark);
			LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			LOG.info("inputFile: "+inputImage);
			srcImg = new Image(RESOURCE_IMAGES + inputImage);
			dwt2Doriginal = WaveletTransformer.discreteWaveletTransform(srcImg.getU(), level);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRecoverJPEGAttacks() throws IOException {
		List<NoiseAnalysisResult> noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
		testRecoverJPEG100Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG90Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG80Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG70Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG60Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG50Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG40Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG30Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG20Attack();noiseAnalysisResults.add(result);
		testRecoverJPEG10Attack();noiseAnalysisResults.add(result);
		FileOutputStream fos = new FileOutputStream(new File("report.html"));
		fos.write(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults).getBytes());
		fos.close();
	}
	
	@Test
	public void testRecoverJPEG100Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_100.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");	
	}
	
	@Test
	public void testRecoverJPEG90Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_90.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");	
	}
	
	@Test
	public void testRecoverJPEG80Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_80.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");	
	}

	@Test
	public void testRecoverJPEG70Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_70.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");	
	}
	
	@Test
	public void testRecoverJPEG60Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_60.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");	
	}
	
	@Test
	public void testRecoverJPEG50Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_50.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testRecoverJPEG40Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_40.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testRecoverJPEG30Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_30.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.8);
	}
	
	@Test
	public void testRecoverJPEG20Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_20.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.7);
	}
	
	@Test
	public void testRecoverJPEG10Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_10.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttack() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_7.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName, 64, 64, 448, 448);
		Sobel edgeDetector = new Sobel();		
		edgeDetector.setThreshold(threshold);
		
		int[] gradImgSrc = new int[imgSrc.getWidth()*imgSrc.getHeight()];
		edgeDetector.init(imgSrc.getBlue(), imgSrc.getWidth(), imgSrc.getHeight());
		gradImgSrc = edgeDetector.process();
		ImageUtils.saveImage(gradImgSrc, imgSrc.getWidth(), imgSrc.getWidth(), new File("src/test/resources/test1.bmp"), "BMP");
		edgeDetector.init(rotatedImage.getBlue(), rotatedImage.getWidth(), rotatedImage.getHeight());
		int[] gradImgTarget = edgeDetector.process();
		ImageUtils.saveImage(gradImgTarget, rotatedImage.getWidth(), rotatedImage.getWidth(), new File("src/test/resources/test2.bmp"), "BMP");		
		int angle = new ImageMatcher().getBestRotationMatch(gradImgSrc, imgSrc.getHeight(), imgSrc.getWidth(), gradImgTarget, rotatedImage.getHeight(), rotatedImage.getWidth());
		System.out.println("angle: "+angle);
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		watermarkUtils.writeBinaryWatermark(recoveredWatermark, watermarkWidth, watermarkHeight);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.9);
	}
	
	@Test
	public void testRecoverCroppingAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_crop_recovered.bmp");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.9);
	}
	
	@Test
	public void testRecoverSharpenAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_sharpen.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.9);
	}
	
	@Test
	public void testRecoverBlurAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_blur.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.9);
	}
	
	@Test
	public void testRecoverNoiseAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_noise.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.9);
	}
	
	@Test
	public void testRecoverNoiseBlurAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_noise_blur.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
		assert(result.getNcc() > 0.9);
	}
}
