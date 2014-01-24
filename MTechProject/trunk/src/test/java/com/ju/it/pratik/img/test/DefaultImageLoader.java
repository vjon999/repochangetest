package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.ImageUtils;

public class DefaultImageLoader implements WMConsts {

	public static Logger LOG = Logger.getLogger(DefaultImageLoader.class.getName());

	protected BufferedImage bufferedImage;
	protected int imageHeight;
	protected int imageWidth;
	protected int[] rgb;
	protected int r[];
	protected int g[];
	protected int b[];

	public void init(String fileName) throws IOException {
		File file = new File(fileName);
		LOG.info("fileName: "+fileName);
		bufferedImage = ImageIO.read(file);
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
