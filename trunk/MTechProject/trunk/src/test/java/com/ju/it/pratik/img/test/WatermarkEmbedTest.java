package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.ImageUtils;
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
	
	
}
