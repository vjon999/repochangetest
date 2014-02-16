package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.DCTWatermarker;
import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.DCTTransformUtil;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class DCTWatermarkTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(DCTWatermarkTester.class.getName());
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private String inputImageName;
	private BufferedImage bufferedImage;
	private int imageHeight;
	private int imageWidth;
	private int watermarkWidth;
	private int watermarkHeight;
	private int[] rgb;
	private int r[];
	private int g[];
	private int b[];
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
	}
	
	public void init(String fileName) throws IOException {
		LOG.info("inputFile: "+fileName);
		bufferedImage = ImageIO.read(new File(fileName));
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
	public void testEmbedDCTWatermark() throws IOException {
		String inputImage = RESOURCE_IMAGES + inputImageName;
		String outputImage = WATERMARKED_IMAGES +"dct/"+ inputImageName.substring(0, inputImageName.lastIndexOf("."))+"_wm";		
		
		BufferedImage wmImage = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
		int[] watermark = new int[wmImage.getHeight()*wmImage.getWidth()];
		wmImage.getRGB(0, 0, wmImage.getWidth(), wmImage.getHeight(), watermark, 0, wmImage.getWidth());
		String watermarkStr = watermarkUtils.readBinaryWatermark(watermark);
		LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
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
		
		/** converting U channel's DCT */
		int[][] dct = ImageUtils.to2D(u, imageHeight, imageWidth);
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(dct, imageWidth, imageHeight);		
		double[] dctResult1D = ImageUtils.to1D(dctResult, imageHeight, imageWidth);
		/** adding watermark to U channel's DWT co-efficients */
		DCTWatermarker icarWatermarker = new DCTWatermarker();
		double[] wdct = icarWatermarker.watermarkBlock(dctResult1D,imageHeight, imageWidth, watermarkStr, policy);
		/** inverse DWT of U channel */
		int[] idct = ImageUtils.to1D(dctTransformUtil.applyIDCTImproved(ImageUtils.to2D(wdct, imageWidth, imageHeight), imageWidth, imageHeight), imageWidth, imageHeight);
		
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[] resYUV = ImageUtils.mergeChannels(y, idct, v);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[r.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		//String outputFileName = inputImage.substring(0, inputImage.lastIndexOf("."))+"_rgb_wmavg.bmp";
		new File(WATERMARKED_IMAGES +"dct/"+ outputImage + ".jpg").delete();
		new File(WATERMARKED_IMAGES +"dct/"+ outputImage + ".bmp").delete();
		BufferedImage outputBufferedImage = ImageUtils.toBufferedImage(convertedRgb, imageWidth, imageHeight);
		ImageIO.write(outputBufferedImage, "jpg", new File(outputImage + ".jpg"));
		ImageIO.write(outputBufferedImage, "BMP", new File(outputImage + ".bmp"));
		LOG.info("saving watermarked file to "+outputImage);
	}
	
	@Test
	public void testRecoverDCTWatermark() throws IOException {
		//String outputImage = RESOURCE_IMAGES + SHIP;
		String outputImage = WATERMARKED_IMAGES +"dct/"+ inputImageName.replaceFirst(".bmp", "_wm.jpg");
		
		BufferedImage wmImage = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
		watermarkWidth = wmImage.getWidth();
		watermarkHeight = wmImage.getHeight();
		int[] watermark = new int[wmImage.getHeight()*wmImage.getWidth()];
		wmImage.getRGB(0, 0, wmImage.getWidth(), wmImage.getHeight(), watermark, 0, wmImage.getWidth());
		String watermarkStr = watermarkUtils.readBinaryWatermark(watermark);
		LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
		//we would consider watermark length as power of 2 only
		
		//set the policy
		
		// RECOVERY
		// read image
		init(outputImage);

		/** converting RGB to YUV starts */
		int[] yuv = new int[r.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** converting RGB to YUV ends */

		/** getting seperate Y, U, V channels */
		int[] y = new int[rgb.length];
		int[] u = new int[rgb.length];
		int[] v = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		/** getting seperate Y, U, V channels ends */


		/** converting U channel's DCT */
		int[][] dct = ImageUtils.to2D(u, imageHeight, imageWidth);
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(dct, imageWidth, imageHeight);		
		double[] dctResult1D = ImageUtils.to1D(dctResult, imageHeight, imageWidth);
		/** adding watermark to U channel's DWT co-efficients */

		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, imageHeight, imageWidth, policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		watermarkUtils.writeBinaryWatermark(recoveredWatermark, watermarkWidth, watermarkHeight);
	}
}
