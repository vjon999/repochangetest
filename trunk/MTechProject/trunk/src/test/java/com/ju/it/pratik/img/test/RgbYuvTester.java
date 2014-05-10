package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;

public class RgbYuvTester {
	
	private static Logger LOG = Logger.getLogger(RgbYuvTester.class.getName());

	public static final String WATER_MARK = "This is a test. ";
	public static final String DEFAULT_FOLDER = "src/main/resources/images/";
	public static final String DEFAULT_WATERMARK_INPUT_FILE_NAME = "Hydrangeas_512.jpg";
	public static final String DEFAULT_WATERMARKED_IMAGE = "Hydrangeas_512_rgb_wm.bmp";
	
	private BufferedImage bufferedImage;
	private int imageHeight;
	private int imageWidth;
	private int[] rgb;
	private int r[];
	private int g[];
	private int b[];
	
	public void init(String fileName) throws IOException {
		LOG.info("fine logging");
		bufferedImage = ImageIO.read(new File(DEFAULT_FOLDER+fileName));
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
	public void testRGBToYUV() throws IOException {
		init("Desert.jpg");
		int yuv,resrgb,r1,g1,b1,r2,g2,b2;
		int tolerance = 3;
		for(int i=0;i<rgb.length;i++) {
			if(i==264718)
				System.out.println();
			r1=(rgb[i]>>16)&0xFF;
			g1=(rgb[i]>>8)&0xFF;
			b1=rgb[i]&0xFF;
			yuv = TransformUtils.rgb2yuv(rgb[i]);
			resrgb = TransformUtils.yuv2rgb(yuv);
			r2=(resrgb>>16)&0xFF;
			g2=(resrgb>>8)&0xFF;
			b2=resrgb&0xFF;
			assert(Math.abs(r1-r2) < tolerance):"i="+i+" ---> r1: "+r1+" - r2:"+r2;
			assert(Math.abs(g1-g2) < tolerance):"i="+i+" ---> g1: "+g1+" - g2:"+g2;
			assert(Math.abs(b1-b2) < tolerance):"i="+i+" ---> b1: "+b1+" - b2:"+b2;
		}
	}
}
