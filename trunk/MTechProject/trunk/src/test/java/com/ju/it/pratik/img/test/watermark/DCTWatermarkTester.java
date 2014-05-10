package com.ju.it.pratik.img.test.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

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
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class DCTWatermarkTester implements WMConsts {

	private static final Logger LOG = Logger.getLogger(DCTWatermarkTester.class.getName());
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private String inputImageName;
	private NoiseAnalysisUtil noiseAnalysisUtil = new NoiseAnalysisUtil();
	private String outputImage;
	private String folderName;
	private Image srcImg;
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
	public void setUp() throws IOException {
		inputImageName = BARBARA;
		folderName = inputImageName.substring(0, inputImageName.indexOf("_"))+"/";
		outputImage = WATERMARKED_IMAGES +"dct/"+folderName+ inputImageName.substring(0, inputImageName.lastIndexOf("."))+"_wm.jpg";
		init(RESOURCE_IMAGES+inputImageName);
	}
	
	public void init(String fileName) throws IOException {
		LOG.info("inputFile: "+fileName);
		srcImg = new Image(fileName);
		origLogo = new Image(WMConsts.WATERMARK_LOGO);
	}
	
	@Test
	public void testEmbedDCTWatermark() throws IOException {
		String inputImage = RESOURCE_IMAGES + inputImageName;
		String outputImage = WATERMARKED_IMAGES +"dct/"+folderName+ inputImageName.substring(0, inputImageName.lastIndexOf("."))+"_wm";				
		LOG.info("watermarkStr len: "+origLogo.getBinaryImage1D().length+"\twatermarkStr: "+Arrays.toString(origLogo.getBinaryImage1D()));
		//we would consider watermark length as power of 2 only
		
		//read image
		init(inputImage);
				
		/** converting U channel's DCT */
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(srcImg.getU(), srcImg.getHeight(), srcImg.getWidth());		
		/** adding watermark to U channel's DWT co-efficients */
		DCTWatermarker icarWatermarker = new DCTWatermarker();
		double[][] wdct = icarWatermarker.watermarkBlock(dctResult, origLogo.getBinaryImage1D(), policy);
		/** inverse DWT of U channel */
		int[][] idct = dctTransformUtil.applyIDCTImproved(wdct, srcImg.getHeight(), srcImg.getWidth());		
		
		/** merging 3 seperate Y, U, V channels to one single int array */
		int[][] resYUV = ImageUtils.mergeChannels(srcImg.getY(), idct, srcImg.getV());
		int[] resYUV1D = ImageUtils.to1D(resYUV);
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[resYUV1D.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV1D[i]);
		}
		/** converting YUV to RGB ends */
		
		/** saving the image */
		//String outputFileName = inputImage.substring(0, inputImage.lastIndexOf("."))+"_rgb_wmavg.bmp";
		File jpg = new File(outputImage + ".jpg");jpg.delete();
		File bmp = new File(outputImage + ".bmp");bmp.delete();
		BufferedImage outputBufferedImage = ImageUtils.toBufferedImage(convertedRgb, srcImg.getWidth(), srcImg.getHeight());
		ImageIO.write(outputBufferedImage, "jpg", jpg);
		ImageIO.write(outputBufferedImage, "BMP", bmp);
		LOG.info("saving watermarked file to "+outputImage+".bmp");
		
		NoiseAnalysisResult noiseAnalysisResult = noiseAnalysisUtil.calculatePSNR(inputImage, outputImage.replace(".bmp", "_wm.bmp") + ".bmp");
		LOG.info("noiseAnalysisResult: "+noiseAnalysisResult);
	}
	
	@Test
	public void testRecoverDCTWatermark() throws IOException {
		String watermarkedImageName = inputImageName.replaceFirst(".bmp", "_wm.jpg");
		Image watermarkedImage = new Image(WATERMARKED_IMAGES+"dct/"+folderName+watermarkedImageName);
		DCTTransformUtil dctTransformUtil = new DCTTransformUtil(8);
		double[][] dctResult = dctTransformUtil.applyDCTImproved(watermarkedImage.getU(), watermarkedImage.getWidth(), watermarkedImage.getHeight());		
		double[] dctResult1D = ImageUtils.to1D(dctResult, watermarkedImage.getHeight(), watermarkedImage.getWidth());
		/** adding watermark to U channel's DWT co-efficients */
		DCTWatermarker icarWatermarker = new DCTWatermarker();
		String recoveredWatermark = icarWatermarker.recoverWatermark(dctResult1D, watermarkedImage.getHeight(), watermarkedImage.getWidth(), policy, origLogo.getBinaryImage1D());
		LOG.info("watermarkStr len: " + recoveredWatermark.length() + "\t watermarkStr: " + recoveredWatermark);
		//watermarkUtils.writeBinaryWatermark(recoveredWatermark, watermarkWidth, watermarkHeight);
		File recoveredWatermarkFile = new File(outputImage.replace("dct/", "dct/recovered/").replace(".jpg", ".bmp"));
		int[] recoveredImage = watermarkUtils.toBWImageArray(recoveredWatermark, origLogo.getHeight(), origLogo.getWidth());
		ImageUtils.saveImage(recoveredImage, origLogo.getWidth(), origLogo.getHeight(), recoveredWatermarkFile, "bmp");
		
		NoiseAnalysisResult result = noiseAnalysisUtil.calculatePSNR(WMConsts.WATERMARK_LOGO, recoveredWatermarkFile.getAbsolutePath());
		LOG.info(result+"");
	}
}
