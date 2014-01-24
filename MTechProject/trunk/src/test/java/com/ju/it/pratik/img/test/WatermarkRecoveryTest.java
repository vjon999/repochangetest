package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.util.CorrelationCalculator;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class WatermarkRecoveryTest extends DefaultImageLoader {

	public static Logger LOG = Logger.getLogger(WatermarkRecoveryTest.class.getName());
	
	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private ImageUtils utils = new ImageUtils();
	private CorrelationCalculator calculator = new CorrelationCalculator();

	@Before
	public void init() throws IOException {
		super.init(OUTPUT_FOLDER+LENA_CROPPED_WM);
	}

	@Test
	public void testRecoverWatermark() {
		int[] watermark = new int[WatermarkEmbedTest.WATER_MARK.getBytes().length];
		double maxCorrelationRatio = 0;
		int ctr = 0;		
		for (byte b : WatermarkEmbedTest.WATER_MARK.getBytes()) {
			watermark[ctr++] = b;
		}
		// we would consider watermark length as power of 2 only

		/** converting RGB to YUV starts */
		int yuv[] = new int[r.length];
		for (int i = 0; i < rgb.length; i++) {
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
		int[] wdwt = watermarkUtils.recoverBlockWatermark(dwt, imageHeight, imageWidth, watermark);
		double correlationRatio = calculator.getCorrelationCoefficient(wdwt, watermark);
		LOG.fine("Correlation Ratio: " + correlationRatio);
		maxCorrelationRatio = correlationRatio;
		
		/** first rotation */
		double transformedU[] = utils.rotate(dwt, imageWidth, imageHeight);
		wdwt = watermarkUtils.recoverBlockWatermark(transformedU, imageHeight, imageWidth, watermark);		
		correlationRatio = calculator.getCorrelationCoefficient(wdwt, watermark);
		LOG.fine("Correlation Ratio: " + correlationRatio);
		if(maxCorrelationRatio < correlationRatio)
			maxCorrelationRatio = correlationRatio;
		
		/** second rotation */
		transformedU = utils.rotateInvert(dwt);
		wdwt = watermarkUtils.recoverBlockWatermark(transformedU, imageHeight, imageWidth, watermark);		
		correlationRatio = calculator.getCorrelationCoefficient(wdwt, watermark);
		LOG.fine("Correlation Ratio: " + correlationRatio);
		if(maxCorrelationRatio < correlationRatio)
			maxCorrelationRatio = correlationRatio;
		
		/** third rotation */
		transformedU = utils.rotate(transformedU, imageWidth, imageHeight);
		wdwt = watermarkUtils.recoverBlockWatermark(transformedU, imageHeight, imageWidth, watermark);		
		correlationRatio = calculator.getCorrelationCoefficient(wdwt, watermark);
		LOG.fine("Correlation Ratio: " + correlationRatio);
		if(maxCorrelationRatio < correlationRatio)
			maxCorrelationRatio = correlationRatio;
		
		LOG.fine("Max Correlation Ratio: " + maxCorrelationRatio);
		
		//for(int i=0;i<64;i++) { System.out.println((char)wdwt[i]); }
		
				
	}
	
	@Test
	public void testCheckWatermarkRecovery() throws IOException {
		
		/*BufferedImage orig = ImageIO.read(new java.io.File(OUTPUT_FOLDER+LENA_WM));
		int origData[] = new int[orig.getHeight()*orig.getWidth()];
		orig.getRGB(0, 0, 512, 512, origData, 0, 512);
		for(int i=0;i<512-imageHeight+1;i++) {
			for(int j=0;j<512-imageWidth+1;j++) {
				boolean match = true;
				for(int k=0;k<imageHeight;k++) {
					for(int l=0;l<imageHeight;l++) {
						if(orig.getRGB(j+l, i+k) != bufferedImage.getRGB(l, k)) {
							match = false;
							break;
						}
						else {
							//LOG.fine(match+" ("+i+","+j+")");
						}
					}
					if(!match)
						break;
				}
				if(match) {				
					LOG.fine("matched at "+i);
					break;
				}
			}			
		}*/
		
		int[] watermark = new int[WatermarkEmbedTest.WATER_MARK.getBytes().length];
		double maxCorrelationRatio = 0;
		int ctr = 0;		
		for (byte b : WatermarkEmbedTest.WATER_MARK.getBytes()) {
			watermark[ctr++] = b;
		}
		// we would consider watermark length as power of 2 only

		/** converting RGB to YUV starts */
		int yuv[] = new int[r.length];
		for (int i = 0; i < rgb.length; i++) {
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
		//int[] wdwt = watermarkUtils.recoverBlockWatermark(dwt, imageHeight, imageWidth, watermark);
		double finalRes = 0f;
		double interRes = 0f;
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				interRes = watermarkUtils.checkWatermarkRecovery(dwt, imageHeight, imageWidth, watermark, i, j);
				LOG.fine("("+i+","+j+") interRes: "+interRes);
				if(interRes > finalRes) {
					finalRes = interRes;
				}
			}
		}
		LOG.fine("finalRes: "+finalRes);
	}
	
	@Test
	public void testCompareTransforDimaonCroppedImage() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_256_cropped_rgb_wm2.bmp"));
		int h1 = img.getHeight(), w1=img.getWidth();
		int[] rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		int yuv[] = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		double[] dwt1 = TransformUtils.discreteWaveletTransform(u);
		
		img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_512_rgb_wm.bmp"));
		int h2 = img.getHeight(), w2=img.getWidth();
		rgb = new int[img.getHeight()*img.getWidth()];
		yuv = new int[rgb.length];
		y = new int[rgb.length];
		u = new int[rgb.length];
		v = new int[rgb.length];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		ImageUtils.getChannels(yuv, y, u, v);
		double[] dwt2 = TransformUtils.discreteWaveletTransform(u);
		//DecimalFormat format = new DecimalFormat("####.##");
		double sum_product = 0;
		double max_sum_product = 0;
		int x1=0,y1=0;
		for(int k = 0;k<h2-h1;k++) {
			for(int l = 0;l<w2-w1;l++) {
				sum_product = 0;
				for(int i=0;i<h1;i++) {
					for(int j=0;j<w1;j++) {
						sum_product += dwt1[i*w1+j] * dwt2[(i+k)*w2+(j+l)];
					}
				}				
				if(sum_product > max_sum_product) {
					LOG.fine(" x1: "+l+", y1: "+k+"\tsum_product: "+sum_product);
					max_sum_product = sum_product;
					x1 = l;
					y1 = k;
				}
			}
		}
		System.out.println(x1+","+y1);
	}
	
	@Test
	public void compareYChannel() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_256_cropped_rgb_wm2.bmp"));
		int size1 = img.getHeight()*img.getWidth();
		LOG.fine("size1: "+size1);
		int[] rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		int yuv[] = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y1[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y1, u, v);
		
		img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_512_rgb_wm.bmp"));
		int size2 = img.getHeight()*img.getWidth();
		LOG.fine("size2: "+size2);
		rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		yuv = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y2[] = new int[size2];
		u = new int[rgb.length];
		v = new int[rgb.length];
		ImageUtils.getChannels(yuv, y2, u, v);
		
		double sum_product = 0;
		double max_sum_product = 0;
		int maxY=0, minY =0;
		for(int i=0;i<size2-size1;i++) {
			sum_product = 0;
			for(int j=0;j<size1;j++) {
				sum_product += (y1[j]-115)*(y2[j+i]-115);
				if(maxY < y2[j+i])
					maxY = y2[j+i];
				if(minY > y2[j+i])
					minY = y2[j+i];
			}
			if(max_sum_product<sum_product) {
				max_sum_product = sum_product;
				LOG.fine("i: "+i+" prod: "+max_sum_product);
			}
		}
		LOG.fine("maxY: "+maxY+" minY: "+minY);
	}
	
	@Test
	public void compareYChannelNCC() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_256_cropped_rgb_wm2.bmp"));
		int h1 = img.getHeight();
		int w1 = img.getWidth();
		int size1 = img.getHeight()*img.getWidth();
		LOG.fine("size1: "+size1);
		int[] rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		int yuv[] = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y1[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y1, u, v);
		
		img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_512_rgb_wm.bmp"));
		int h2 = img.getHeight();
		int w2 = img.getWidth();
		int size2 = img.getHeight()*img.getWidth();
		LOG.fine("size2: "+size2);
		rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		yuv = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y2[] = new int[size2];
		u = new int[rgb.length];
		v = new int[rgb.length];
		ImageUtils.getChannels(yuv, y2, u, v);
		
		CorrelationCalculator calculator = new CorrelationCalculator();
		double res = 0;
		double maxRes = 0;
		boolean done = false;
		for(int i=0;i<h2-h1 && !done;i++) {		
			for(int j=0;j<w2-w1;j++) {
				int[] y3 = new int[y1.length];
				for(int p=i;p<h1+i;p++) {
					for(int q=j;q<w1+j;q++) {
						y3[(p-i)*w1+(q-j)] = y2[p*w2+q];
					}
				}
				res = calculator.calcNormalizedCrossCorrelation(y1, y3);
				if(res > maxRes) {
					maxRes = res;
					LOG.fine("maxRes: "+maxRes);
				}
				if(maxRes == 1) {
					LOG.fine("i: "+i+" j: "+j);
					done = true;
					break;
				}
			}
		}
	}
	
	@Test
	public void compareYChannels() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_512_rgb_wm.bmp"));
		int h1 = img.getHeight();
		int w1 = img.getWidth();
		int size1 = img.getHeight()*img.getWidth();
		LOG.fine("size1: "+size1);
		int[] rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		int yuv[] = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y1[] = new int[rgb.length];
		int u1[] = new int[rgb.length];
		int v1[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y1, u1, v1);
		
		img = ImageIO.read(new File(OUTPUT_FOLDER+"lena_512_rgb_wm.jpg"));
		h1 = img.getHeight();
		w1 = img.getWidth();
		int size2 = img.getHeight()*img.getWidth();
		LOG.fine("size1: "+size1);
		rgb = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		yuv = new int[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** getting seperate Y, U, V channels */
		int y2[] = new int[rgb.length];
		int[] u2 = new int[rgb.length];
		int[] v2 = new int[rgb.length];
		ImageUtils.getChannels(yuv, y2, u2, v2);
		
		for(int i=0;i<32;i++) {
			//if(y1[i] != y2[i]) {
				System.err.println(y1[i]+"\t"+y2[i]+"\t\t"+u1[i]+"\t"+u2[i]+"\t\t"+v1[i]+"\t"+v2[i]);
			//}
		}
	}

}
