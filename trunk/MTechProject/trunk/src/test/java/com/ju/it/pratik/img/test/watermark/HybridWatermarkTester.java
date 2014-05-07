package com.ju.it.pratik.img.test.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.HybridWatermarker;
import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.ImageMatcher;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.NoiseAnalysisUtil;
import com.ju.it.pratik.img.util.Sobel;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;
import com.ju.it.pratik.img.util.WaveletTransformer;

public class HybridWatermarkTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(HybridWatermarkTester.class.getName());
	
	private String inputImage;
	private String outputImage;
	private String watermarkedImageName;
	private WatermarkUtils watermarkUtils;
	private HybridWatermarker watermarker;
	private NoiseAnalysisUtil noiseAnalysisUtil;
	private NoiseAnalysisResult result;
	int level = 2;
	double strength = 10;
	double dwt2Doriginal[][];
	Image origLogo;
	Image src;
	
	@Before
	public void setUp() {
		inputImage = LENA;
		outputImage = inputImage.substring(0, inputImage.lastIndexOf("."))+"_wm";
		watermarkUtils = new WatermarkUtils();
		noiseAnalysisUtil = new NoiseAnalysisUtil();
		watermarker = new HybridWatermarker(4, strength, level);
		try {
			origLogo = new Image(WMConsts.WATERMARK_LOGO);
			src = new Image(RESOURCE_IMAGES + inputImage);
			dwt2Doriginal = WaveletTransformer.discreteWaveletTransform(src.getU(), level);
			//int[] recoveredImage = watermarkUtils.toBWImageArray(watermarkStr, origLogo.getWidth(), origLogo.getHeight());
			//ImageUtils.saveImage(recoveredImage, origLogo.getWidth(), origLogo.getHeight(), new File(WMConsts.WATERMARK_LOGO.replace(".bmp", "1.bmp")), "bmp");
			LOG.info("watermarkStr len: "+origLogo.getBinaryImage1D().length+"\twatermarkStr: "+Arrays.toString(origLogo.getBinaryImage1D()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testHybridWatermark() throws IOException {
		double[][] wmdwt2D = new double[dwt2Doriginal.length][];
		for(int i=0;i<dwt2Doriginal.length;i++) {
			wmdwt2D[i] = new double[dwt2Doriginal[i].length];
			for(int j=0;j<dwt2Doriginal[i].length;j++) {
				wmdwt2D[i][j] = dwt2Doriginal[i][j];
			}
		}
		watermarker.hybridWatermark(wmdwt2D, origLogo.getBinaryImage1D());
		int[][] idwt2D = WaveletTransformer.inverseDiscreteWaveletTransform(wmdwt2D, level);
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[][] resYUV = ImageUtils.mergeChannels(src.getY(), idwt2D, src.getV());
		int[] resYUV1D = ImageUtils.to1D(resYUV, resYUV.length, resYUV[0].length);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[resYUV1D.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV1D[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		new File(WATERMARKED_IMAGES + "hybrid/"+outputImage + ".jpg").delete();
		new File(WATERMARKED_IMAGES + "hybrid/"+outputImage + ".bmp").delete();
		BufferedImage outputBufferedImage = ImageUtils.toBufferedImage(convertedRgb, src.getWidth(), src.getHeight());
		ImageIO.write(outputBufferedImage, "jpg", new File(WATERMARKED_IMAGES + "hybrid/"+outputImage + ".jpg"));
		ImageIO.write(outputBufferedImage, "BMP", new File(WATERMARKED_IMAGES + "hybrid/"+outputImage + ".bmp"));
		LOG.info("saving watermarked file to "+WATERMARKED_IMAGES + "hybrid/"+outputImage + ".bmp");
		
		NoiseAnalysisResult noiseAnalysisResult = noiseAnalysisUtil.calculatePSNR(RESOURCE_IMAGES + inputImage, WATERMARKED_IMAGES + "hybrid/"+outputImage.replace(".bmp", "_wm.bmp") + ".bmp");
		LOG.info("noiseAnalysisResult: "+noiseAnalysisResult);
		//testRecoverWaveletWatermark();
	}
	
	@Test
	public void testRecoverHybridWatermark() throws IOException {
		watermarkedImageName = inputImage.substring(inputImage.indexOf("/")+1).replace(".bmp", "_wm.jpg");
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		System.out.println(Arrays.toString(origLogo.getBinaryImage1D()));
		System.out.println(Arrays.toString(recoveredLogo));
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String recWMFileName = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(recWMFileName), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, recWMFileName);
		LOG.info(result+"");
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
	public void testRecoverRotationAttacks() throws IOException {
		List<NoiseAnalysisResult> noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
		testRecoverRotationAttackM7();noiseAnalysisResults.add(result);
		testRecoverRotationAttackM5();noiseAnalysisResults.add(result);
		testRecoverRotationAttackM3();noiseAnalysisResults.add(result);
		testRecoverRotationAttackM1();noiseAnalysisResults.add(result);
		testRecoverRotationAttack1();noiseAnalysisResults.add(result);
		testRecoverRotationAttack3();noiseAnalysisResults.add(result);
		testRecoverRotationAttack5();noiseAnalysisResults.add(result);
		testRecoverRotationAttack7();noiseAnalysisResults.add(result);
		FileOutputStream fos = new FileOutputStream(new File("report.html"));
		fos.write(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults).getBytes());
		fos.close();
	}
	
	@Test
	public void testRecoverBlurAttacks() throws IOException {
		List<NoiseAnalysisResult> noiseAnalysisResults = new ArrayList<NoiseAnalysisResult>();
		testRecoverGaussianBlur1Attack();noiseAnalysisResults.add(result);
		testRecoverGaussianBlur2Attack();noiseAnalysisResults.add(result);
		testRecoverGaussianBlur3Attack();noiseAnalysisResults.add(result);
		testRecoverGaussianBlur4Attack();noiseAnalysisResults.add(result);
		testRecoverGaussianBlur5Attack();noiseAnalysisResults.add(result);
		FileOutputStream fos = new FileOutputStream(new File("report.html"));
		fos.write(noiseAnalysisUtil.generateHTMLReport(noiseAnalysisResults).getBytes());
		fos.close();
	}

	@Test
	public void testRecoverJPEG100Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_100.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG90Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_90.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG80Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_80.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG70Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_70.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG60Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_60.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG50Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_50.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG40Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_40_1.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG30Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_30.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG20Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_20.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG10Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_10.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttackM7() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_-7.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttackM5() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_-5.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttackM3() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_-3.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttackM1() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_-1.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttack1() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_1.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttack3() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_3.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttack5() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_5.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttack7() throws IOException {
		watermarkedImageName = inputImage.replace(".bmp", "_wm_rotate_7.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		rotatedImage.rotate(angle);
		rotatedImage.save(new File("src/test/resources/test3.jpg"), "jpg");
		
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(rotatedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverCroppingAttack() throws IOException, InterruptedException {
		/** RECOVERY STEP */
		ImageMatcher m = new ImageMatcher();
		String src = WMConsts.WATERMARKED_IMAGES+"hybrid/lena_512_wm.jpg";
		String target = WMConsts.WATERMARKED_IMAGES+"hybrid/lena_512_wm_crop.jpg";
		m.getStartingPixel(src, target);
		
		watermarkedImageName = inputImage.replace(".bmp", "_wm_crop_recovered.bmp");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverSharpen10Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_sharpen_10.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverGaussianBlur1Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_blur_1.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverGaussianBlur2Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_blur_2_1.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverGaussianBlur3Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_blur_3.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverGaussianBlur4Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_blur_4.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverGaussianBlur5Attack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_blur_5.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverNoiseAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_noise_50.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverNoiseBlurAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_noise_15_blur_2.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testEqualizationAttack() throws IOException {
		/** RECOVERY STEP */
		watermarkedImageName = inputImage.replace(".bmp", "_wm_equalize.jpg");
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"hybrid/"+watermarkedImageName);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		int[] recoveredLogo = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, origLogo.getRed());
		recoveredLogo = watermarkUtils.toBWImageArray(recoveredLogo, origLogo.getWidth(), origLogo.getHeight());
		String generatedWatermark = WATERMARKED_IMAGES+"hybrid/recovered/"+watermarkedImageName.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredLogo, origLogo.getWidth(), origLogo.getHeight(), new File(generatedWatermark), "bmp");
		
		result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
}
