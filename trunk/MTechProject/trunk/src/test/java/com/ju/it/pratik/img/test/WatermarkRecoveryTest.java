package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.ju.it.pratik.img.DCTWatermarker;
import com.ju.it.pratik.img.Location;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.CorrelationCalculator;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class WatermarkRecoveryTest extends DefaultImageLoader {

	public static Logger LOG = Logger.getLogger(WatermarkRecoveryTest.class.getName());

	private WatermarkUtils watermarkUtils = new WatermarkUtils();
	private ImageUtils utils = new ImageUtils();
	private CorrelationCalculator calculator = new CorrelationCalculator();

	/*@Before
	public void init() throws IOException {
		super.init(OUTPUT_FOLDER + LENA_CROPPED_WM);
	}*/

	@Test
	public void compareYChannel() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER + "lena_256_cropped_rgb_wm2.bmp"));
		int size1 = img.getHeight() * img.getWidth();
		LOG.fine("size1: " + size1);
		int[] rgb = new int[img.getHeight() * img.getWidth()];
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

		img = ImageIO.read(new File(OUTPUT_FOLDER + "lena_512_rgb_wm.bmp"));
		int size2 = img.getHeight() * img.getWidth();
		LOG.fine("size2: " + size2);
		rgb = new int[img.getHeight() * img.getWidth()];
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
		int maxY = 0, minY = 0;
		for (int i = 0; i < size2 - size1; i++) {
			sum_product = 0;
			for (int j = 0; j < size1; j++) {
				sum_product += (y1[j] - 115) * (y2[j + i] - 115);
				if (maxY < y2[j + i])
					maxY = y2[j + i];
				if (minY > y2[j + i])
					minY = y2[j + i];
			}
			if (max_sum_product < sum_product) {
				max_sum_product = sum_product;
				LOG.fine("i: " + i + " prod: " + max_sum_product);
			}
		}
		LOG.fine("maxY: " + maxY + " minY: " + minY);
	}

	@Test
	public void compareYChannelNCC() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER + "lena_256_cropped_rgb_wm2.bmp"));
		int h1 = img.getHeight();
		int w1 = img.getWidth();
		int size1 = img.getHeight() * img.getWidth();
		LOG.fine("size1: " + size1);
		int[] rgb = new int[img.getHeight() * img.getWidth()];
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

		img = ImageIO.read(new File(OUTPUT_FOLDER + "lena_512_rgb_wm.bmp"));
		int h2 = img.getHeight();
		int w2 = img.getWidth();
		int size2 = img.getHeight() * img.getWidth();
		LOG.fine("size2: " + size2);
		rgb = new int[img.getHeight() * img.getWidth()];
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
		for (int i = 0; i < h2 - h1 && !done; i++) {
			for (int j = 0; j < w2 - w1; j++) {
				int[] y3 = new int[y1.length];
				for (int p = i; p < h1 + i; p++) {
					for (int q = j; q < w1 + j; q++) {
						y3[(p - i) * w1 + (q - j)] = y2[p * w2 + q];
					}
				}
				res = calculator.calcNormalizedCrossCorrelation(y1, y3);
				if (res > maxRes) {
					maxRes = res;
					LOG.fine("maxRes: " + maxRes);
				}
				if (maxRes == 1) {
					LOG.fine("i: " + i + " j: " + j);
					done = true;
					break;
				}
			}
		}
	}

	@Test
	public void compareYChannels() throws IOException {
		BufferedImage img = ImageIO.read(new File(OUTPUT_FOLDER + "lena_512_rgb_wm.bmp"));
		int h1 = img.getHeight();
		int w1 = img.getWidth();
		int size1 = img.getHeight() * img.getWidth();
		LOG.fine("size1: " + size1);
		int[] rgb = new int[img.getHeight() * img.getWidth()];
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

		img = ImageIO.read(new File(OUTPUT_FOLDER + "lena_512_rgb_wm.jpg"));
		h1 = img.getHeight();
		w1 = img.getWidth();
		int size2 = img.getHeight() * img.getWidth();
		LOG.fine("size1: " + size1);
		rgb = new int[img.getHeight() * img.getWidth()];
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

		for (int i = 0; i < 32; i++) {
			// if(y1[i] != y2[i]) {
			System.err.println(y1[i] + "\t" + y2[i] + "\t\t" + u1[i] + "\t" + u2[i] + "\t\t" + v1[i] + "\t" + v2[i]);
			// }
		}
	}
	
	

}
