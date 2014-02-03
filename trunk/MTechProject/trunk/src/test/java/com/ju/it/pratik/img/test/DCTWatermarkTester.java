package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.ju.it.pratik.img.ICARWatermarker;
import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.DCTTransformUtil;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class DCTWatermarkTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(DCTWatermarkTester.class.getName());
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private BufferedImage bufferedImage;
	private int imageHeight;
	private int imageWidth;
	private int[] rgb;
	private int r[];
	private int g[];
	private int b[];
	
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
		String inputImage = RESOURCE_IMAGES + LENA;
		String outputImage = WATERMARKED_IMAGES + LENA.substring(0, LENA.lastIndexOf("."))+"_wm";		
		
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
		
		/** converting U channel's DCT */
		int[][] dct = ImageUtils.to2D(u, imageHeight, imageWidth);
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(dct, imageWidth, imageHeight);		
		double[] dctResult1D = ImageUtils.to1D(dctResult, imageHeight, imageWidth);
		/** adding watermark to U channel's DWT co-efficients */
		ICARWatermarker icarWatermarker = new ICARWatermarker();
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
		new File(WATERMARKED_IMAGES + outputImage + ".jpg").delete();
		new File(WATERMARKED_IMAGES + outputImage + ".bmp").delete();
		BufferedImage outputBufferedImage = ImageUtils.toBufferedImage(convertedRgb, imageWidth, imageHeight);
		ImageIO.write(outputBufferedImage, "jpg", new File(outputImage + ".jpg"));
		ImageIO.write(outputBufferedImage, "BMP", new File(outputImage + ".bmp"));
		LOG.info("saving watermarked file to "+outputImage);
	}
	
	@Test
	public void testRecoverDCTWatermark() throws IOException {
		String outputImage = WATERMARKED_IMAGES + LENA.replaceFirst(".bmp", "_wm.jpg");		
		
		BufferedImage wmImage = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
		int[] watermark = new int[wmImage.getHeight()*wmImage.getWidth()];
		wmImage.getRGB(0, 0, wmImage.getWidth(), wmImage.getHeight(), watermark, 0, wmImage.getWidth());
		String watermarkStr = watermarkUtils.readWatermark(watermark);
		LOG.info("watermarkStr len: "+watermarkStr.length()+"\twatermarkStr: "+watermarkStr);
		//we would consider watermark length as power of 2 only
		
		//set the policy
		Location[] policy = new Location[] {new Location(5,1), new Location(4,2), new Location(1,4), new Location(1,2)};
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

		ICARWatermarker icarWatermarker = new ICARWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, imageHeight, imageWidth, policy, watermarkStr);
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		watermarkUtils.writeWatermark(recoveredWatermark);
	}
}
