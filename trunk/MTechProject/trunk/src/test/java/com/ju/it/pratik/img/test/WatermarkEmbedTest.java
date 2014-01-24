package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.NoiseAnalysisUtil;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class WatermarkEmbedTest implements WMConsts {
	
	private static final Logger LOG = Logger.getLogger(WatermarkEmbedTest.class.getName());
	
	static {
		System.setProperty("java.util.logging.config.file","src/main/resources/logging.properties");
	}
	

	/*public static final String WATER_MARK = "This is abc test";
	public static final String DEFAULT_FOLDER = "src/main/resources/images/";
	public static final String DEFAULT_WATERMARK_INPUT_FILE_NAME = "lena_512.bmp";//"Hydrangeas_512.jpg";
	public static final String DEFAULT_WATERMARKED_IMAGE = "Hydrangeas_512_rgb_wm.bmp";*/
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private BufferedImage bufferedImage;
	private int imageHeight;
	private int imageWidth;
	private int[] rgb;
	private int r[];
	private int g[];
	private int b[];
	
	public void init(String fileName) throws IOException {
		LOG.info("inputFile: "+RESOURCE_IMAGES+fileName);
		bufferedImage = ImageIO.read(new File(RESOURCE_IMAGES+fileName));
		imageHeight = bufferedImage.getHeight();
		imageWidth = bufferedImage.getWidth();
		rgb = new int[imageWidth * imageHeight];
		r = new int[rgb.length];
		g = new int[rgb.length];
		b = new int[rgb.length];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, rgb, 0, imageWidth);
		ImageUtils.getChannels(rgb, r, g, b);
	}
	
	@Test
	public void testDWTWatermark() throws IOException {
		int[] watermark = new int[WATER_MARK.getBytes().length];
		int ctr = 0;
		for(byte b : WATER_MARK.getBytes()) {
			watermark[ctr++] = b;
		}
		//we would consider watermark length as power of 2 only
		
		//read image
		init(DEFAULT_WATERMARK_INPUT_FILE_NAME);
		
		/** converting RGB to YUV starts */
		int yuv[] = new int[r.length];
		for(int i=0;i<rgb.length;i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** converting RGB to YUV ends */
		
		/** getting seperate Y, U, V channels */
		int y[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		/** getting seperate Y, U, V channels ends */
		
		/** converting U channel's DWT */
		double[] dwt = TransformUtils.discreteWaveletTransform(u);
		
		/** adding watermark to U channel's DWT co-efficients */
		double[] wdwt = ImageUtils.lineWatermark(dwt, watermark, 1);
		
		/** inverse DWT of U channel */
		int[] idwt = TransformUtils.inverseDiscreteWaveletTransform(wdwt);
		
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[] resYUV = ImageUtils.mergeChannels(y, idwt, v);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[r.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		ImageUtils.saveRGBImage(imageWidth, imageHeight, convertedRgb, RESOURCE_IMAGES + "output/" + DEFAULT_WATERMARK_INPUT_FILE_NAME.substring(0, DEFAULT_WATERMARK_INPUT_FILE_NAME.lastIndexOf("."))+"_rgb_wm.bmp");
		/*for(int i=0;i<r.length;i++) {
			System.out.println(idwt[i]);
		}*/
	}
	
	@Test
	public void testDWTBlockWatermark() throws IOException {
		String inputImage = LENA;
		int[] watermark = new int[WATER_MARK.getBytes().length];
		int ctr = 0;
		for(byte b : WATER_MARK.getBytes()) {
			watermark[ctr++] = b;
		}
		//we would consider watermark length as power of 2 only
		
		//read image
		init(inputImage);
		
		/** converting RGB to YUV starts */
		int yuv[] = new int[r.length];
		for(int i=0;i<rgb.length;i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** converting RGB to YUV ends */
		
		/** getting seperate Y, U, V channels */
		int y[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		/** getting seperate Y, U, V channels ends */
		
		/** converting U channel's DWT */
		double[] dwt = TransformUtils.discreteWaveletTransform(u);
		/** adding watermark to U channel's DWT co-efficients */
		double[] wdwt = watermarkUtils.blockWatermark(dwt,imageHeight, imageWidth, watermark);
		/** inverse DWT of U channel */
		int[] idwt = TransformUtils.inverseDiscreteWaveletTransform(wdwt);
		
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[] resYUV = ImageUtils.mergeChannels(y, idwt, v);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[r.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		String outputFileName = inputImage.substring(0, inputImage.lastIndexOf("."))+"_rgb_wm.bmp";
		LOG.info("saving watermarked file to "+outputFileName);
		ImageUtils.saveRGBImage(imageWidth, imageHeight, convertedRgb, RESOURCE_IMAGES + "output/" + outputFileName);
	}
	
	@Test
	public void testAvgBlockWatermark() throws IOException {
		String inputImage = SHIP;
		String outputImage = "output/" + inputImage.substring(0, inputImage.lastIndexOf("."))+"_rgb_wmavg.bmp";
		
		
		BufferedImage wmImage = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
		int[] watermark = new int[wmImage.getHeight()*wmImage.getWidth()];
		wmImage.getRGB(0, 0, wmImage.getWidth(), wmImage.getHeight(), watermark, 0, wmImage.getWidth());
		String watermarkStr = watermarkUtils.readWatermark(watermark);
		LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
		//we would consider watermark length as power of 2 only
		
		//set the policy
		Location[] policy = new Location[] {new Location(5,1), new Location(4,2), new Location(1,4), new Location(1,2)};
		
		//read image
		init(inputImage);
		
		/** converting RGB to YUV starts */
		int yuv[] = new int[r.length];
		for(int i=0;i<rgb.length;i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** converting RGB to YUV ends */
		
		/** getting seperate Y, U, V channels */
		int y[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		/** getting seperate Y, U, V channels ends */
		
		/** converting U channel's DWT */
		double[] dwt = TransformUtils.discreteWaveletTransform(u);
		/** adding watermark to U channel's DWT co-efficients */
		double[] wdwt = watermarkUtils.watermarkBlock1(dwt,imageHeight, imageWidth, watermarkStr, policy);
		/** inverse DWT of U channel */
		int[] idwt = TransformUtils.inverseDiscreteWaveletTransform(wdwt);
		
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[] resYUV = ImageUtils.mergeChannels(y, idwt, v);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[r.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		//String outputFileName = inputImage.substring(0, inputImage.lastIndexOf("."))+"_rgb_wmavg.bmp";
		LOG.info("saving watermarked file to "+outputImage);
		ImageUtils.saveRGBImage(imageWidth, imageHeight, convertedRgb, RESOURCE_IMAGES + outputImage);
		
		
		
		// RECOVERY
		//read image
		init(outputImage);
		
		/** converting RGB to YUV starts */
		yuv = new int[r.length];
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
		
		/** converting U channel's DWT */
		dwt = TransformUtils.discreteWaveletTransform(u);
		String recoveredWatermark = watermarkUtils.recWatermarkBlock1(dwt,imageHeight, imageWidth, policy, watermarkStr);
		LOG.info("watermarkStr len: "+recoveredWatermark.length()+"\t watermarkStr: "+recoveredWatermark);
		watermarkUtils.writeWatermark(recoveredWatermark);
		
		NoiseAnalysisUtil noiseAnalysisUtil = new NoiseAnalysisUtil();
		NoiseAnalysisResult noiseAnalysisResult = noiseAnalysisUtil.calculatePSNR(RESOURCE_IMAGES + inputImage, RESOURCE_IMAGES + outputImage);
		LOG.info("noiseAnalysisResult: "+noiseAnalysisResult);
	}
}
