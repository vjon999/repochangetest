package com.ju.it.pratik.img.test.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

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
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;
import com.ju.it.pratik.img.util.WaveletTransformer;

public class WaveletWatermarkTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(WaveletWatermarkTester.class.getName());
	
	private String inputImage;
	private String outputImage;
	private WatermarkUtils watermarkUtils;
	private String watermarkStr;
	private WaveletWatermarker watermarker;
	private NoiseAnalysisUtil noiseAnalysisUtil;
	int level = 1;
	double strength = 1.07;
	double dwt2Doriginal[][];
	int height;
	int width;
	int watermarkHeight;
	int watermarkWidth;
	int y[], u[], v[];
	
	@Before
	public void setUp() {
		//inputImage = LENA;
		inputImage = MANDRILL;
		outputImage = inputImage.substring(0, inputImage.lastIndexOf("."))+"_wm";
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
			//watermarkUtils.writeBinaryWatermark(watermarkStr, watermarkWidth, watermarkHeight);
			LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LOG.info("inputFile: "+inputImage);
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(RESOURCE_IMAGES + inputImage));
			height = bufferedImage.getHeight();
			width = bufferedImage.getWidth();
			int[] rgb = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
			int[] r = new int[rgb.length];
			int g[] = new int[rgb.length];
			int b[] = new int[rgb.length];
			bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), rgb, 0, bufferedImage.getHeight());
			ImageUtils.getChannels(rgb, r, g, b);
			
			/** converting RGB to YUV starts */
			int yuv[] = new int[r.length];
			for(int i=0;i<rgb.length;i++) {
				yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
			}
			/** converting RGB to YUV ends */
			
			/** getting seperate Y, U, V channels */
			y = new int[rgb.length];
			u = new int[rgb.length];
			v = new int[rgb.length];
			ImageUtils.getChannels(yuv, y, u, v);
			/** getting seperate Y, U, V channels ends */
			
			int[][] u2D = ImageUtils.to2D(u, height, width);
			dwt2Doriginal = WaveletTransformer.discreteWaveletTransform(u2D, level);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testWaveletWatermark() throws IOException {
		inputImage = RESOURCE_IMAGES + inputImage;
		//read image
		
		double[][] wmdwt2D = new double[dwt2Doriginal.length][];
		for(int i=0;i<dwt2Doriginal.length;i++) {
			wmdwt2D[i] = new double[dwt2Doriginal[i].length];
			for(int j=0;j<dwt2Doriginal[i].length;j++) {
				wmdwt2D[i][j] = dwt2Doriginal[i][j];
			}
		}
		watermarker.waveletWatermark(wmdwt2D, watermarkStr);
		
		int[][] idwt2D = WaveletTransformer.inverseDiscreteWaveletTransform(wmdwt2D, level);
		int[] idwt = ImageUtils.to1D(idwt2D, height, width);
		
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[] resYUV = ImageUtils.mergeChannels(y, idwt, v);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[y.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		//String outputFileName = inputImage.substring(0, inputImage.lastIndexOf("."))+"_rgb_wmavg.bmp";
		new File(WATERMARKED_IMAGES + "wavelet/"+outputImage + ".jpg").delete();
		new File(WATERMARKED_IMAGES + "wavelet/"+outputImage + ".bmp").delete();
		BufferedImage outputBufferedImage = ImageUtils.toBufferedImage(convertedRgb, width, height);
		ImageIO.write(outputBufferedImage, "jpg", new File(WATERMARKED_IMAGES + "wavelet/"+outputImage + ".jpg"));
		ImageIO.write(outputBufferedImage, "BMP", new File(WATERMARKED_IMAGES + "wavelet/"+outputImage + ".bmp"));
		LOG.info("saving watermarked file to "+WATERMARKED_IMAGES + "wavelet/"+outputImage + ".bmp");		
	}
	
	@Test
	public void testRecoverWaveletWatermark() throws IOException {
		/** RECOVERY STEP */
		//BufferedImage bufferedImage2 = ImageIO.read(new File(RESOURCE_IMAGES + LENA));
		outputImage = outputImage + "_rotate_5.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		BufferedImage bufferedImage2 = ImageIO.read(new File(WATERMARKED_IMAGES+"wavelet/"+outputImage));
		int height = bufferedImage2.getHeight();
		int width = bufferedImage2.getWidth();
		int[] rgb = new int[width * height];
		int[] r = new int[rgb.length];
		int[] g = new int[rgb.length];
		int[] b = new int[rgb.length];
		bufferedImage2.getRGB(0, 0, width, height, rgb, 0, height);
		//rgb =  new ImageRotationUtil(rgb, height, width).rotate(-5);
		ImageUtils.getChannels(rgb, r, g, b);
		
		/** converting RGB to YUV starts */
		int[] yuv = new int[r.length];
		for(int i=0;i<rgb.length;i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** converting RGB to YUV ends */
		
		/** getting seperate Y, U, V channels */
		int[] y = new int[rgb.length];
		int[] u = new int[rgb.length];
		int[] v = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		/** getting seperate Y, U, V channels ends */
		
		int[][] u2D = ImageUtils.to2D(u, height, width);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(u2D, level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		/*watermarkUtils.writeBinaryWatermark(recoveredWatermark, watermarkWidth, watermarkHeight);*/
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}

	@Test
	public void testRecoverJPEG85Attack() throws IOException {
		/** RECOVERY STEP */
		outputImage = inputImage.substring(0, inputImage.lastIndexOf("."))+"_wm.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");	
	}
	
	@Test
	public void testRecoverJPEG50Attack() throws IOException {
		/** RECOVERY STEP */
		outputImage = inputImage.substring(0, inputImage.lastIndexOf("."))+"_wm_50.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG30Attack() throws IOException {
		/** RECOVERY STEP */
		outputImage = inputImage.substring(0, inputImage.lastIndexOf("."))+"_wm_30.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverJPEG20Attack() throws IOException {
		/** RECOVERY STEP */
		outputImage = inputImage.substring(0, inputImage.lastIndexOf("."))+"_wm_20.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverRotationAttack() throws IOException {
		outputImage = inputImage.replace(".bmp", "_wm_rotate_7.jpg");
		int threshold = 100;
		Image imgSrc = new Image(RESOURCE_IMAGES+inputImage, 64, 64, 448, 448);
		Image rotatedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage, 64, 64, 448, 448);
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
		
		rotatedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
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
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverCroppingAttack() throws IOException {
		/** RECOVERY STEP */
		//BufferedImage bufferedImage2 = ImageIO.read(new File(RESOURCE_IMAGES + LENA));
		outputImage = outputImage + "_crop_recovered.bmp";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverSharpenAttack() throws IOException {
		/** RECOVERY STEP */
		outputImage = outputImage + "_sharpen.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverBlurAttack() throws IOException {
		/** RECOVERY STEP */
		outputImage = outputImage + "_blur.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverNoiseAttack() throws IOException {
		/** RECOVERY STEP */
		outputImage = outputImage + "_noise.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}
	
	@Test
	public void testRecoverNoiseBlurAttack() throws IOException {
		/** RECOVERY STEP */
		outputImage = outputImage + "_noise_blur.jpg";
		LOG.fine("reading watermarked image: "+WATERMARKED_IMAGES+"wavelet/"+outputImage);
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"wavelet/"+outputImage);
		double dwt2D[][] = WaveletTransformer.discreteWaveletTransform(watermarkedImage.getU(), level);
		String recoveredWatermark = watermarker.retrieveWaveletWatermark(dwt2D, dwt2Doriginal, watermarkStr);
		LOG.info("recoveredWatermark len: "+recoveredWatermark.length());
		recoveredWatermark = recoveredWatermark.substring(0, watermarkStr.length());
		LOG.info("recoveredWatermark: "+recoveredWatermark);
		LOG.info("original watermark: "+watermarkStr);
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, watermarkWidth, watermarkHeight);
		String generatedWatermark = WATERMARKED_IMAGES+"wavelet/recovered/"+outputImage.replaceFirst(".jpg",  ".bmp");
		ImageUtils.saveImage(recoveredImage, watermarkWidth, watermarkHeight, new File(generatedWatermark), "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WATERMARK_LOGO, generatedWatermark);
		LOG.info(result+"");
	}

	/*@Test
	public void doNoiseAnalysisOfImage() throws IOException {
		String inputImagePath = RESOURCE_IMAGES+inputImage;
		String outputImagePath = WATERMARKED_IMAGES+"wavelet/"+outputImage+".bmp";
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(outputImagePath, inputImagePath);
		LOG.info(result+"");
	}*/
	
	/*@Test
	public void doNoiseAnalysisOfWatermark() throws IOException {
		String inputImagePath = "src/test/resources/images/ju_logo_bw.bmp";
		String outputImagePath = WATERMARKED_IMAGES+"wavelet/recovered/lena_512_wm.bmp";
		System.out.println(outputImagePath);
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(inputImagePath, outputImagePath);
		LOG.info(result+"");
	}*/
}
