package com.ju.it.pratik.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.ju.it.pratik.img.util.ImageRotationUtil;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;

public class Image {
	
	private static Logger LOG = Logger.getLogger(Image.class.getName());

	private int[] red;
	private int[] green;
	private int[] blue;
	private int rgb[];
	private int rgb2D[][];
	private int height;
	private int width;
	private int[][] y;
	private int[][] u;
	private int[][] v;
	
	public Image(String path) throws IOException {
		File in = new File(path);
		if(!in.exists())
			throw new FileNotFoundException("file not found: "+path);
		LOG.fine("Reading Image: "+in);
		BufferedImage bufImg = ImageIO.read(in);
		rgb = bufImg.getRGB(0, 0, bufImg.getWidth(), bufImg.getHeight(), rgb, 0, bufImg.getWidth());
		height = bufImg.getHeight();
		width = bufImg.getWidth();
		init();
	}
	
	public Image(String path, int startX, int startY, int endX, int endY) throws IOException {
		File in = new File(path);
		if(!in.exists())
			throw new FileNotFoundException("file not found: "+path);
		BufferedImage bufImg = ImageIO.read(in);
		height = endY - startY;
		width = endX - startX;
		rgb = new int[height*width];
		bufImg.getRGB(startX, startY, width, height, rgb, 0, width);
		init();
	}
	
	public Image(int[] arr, int height, int width) {
		rgb = arr;
		this.height = height;
		this.width = width;
		init();
	}
	
	private void init() {
		rgb2D = new int[height][];
		red = new int[width*height];
		green = new int[width*height];
		blue = new int[width*height];
		for(int i=0;i<height;i++) {
			rgb2D[i] = new int[width];
			for(int j=0;j<width;j++) {
				rgb2D[i][j] = rgb[i*width+j];
				red[i*width+j] = (rgb[i*width+j] >> 16) & 0xFF;
				green[i*width+j] = (rgb[i*width+j] >> 8) & 0xFF;
				blue[i*width+j] = (rgb[i*width+j]) & 0xFF;
			}
		}
		int[] y2 = new int[width*height];
		int[] u2 = new int[width*height];
		int[] v2 = new int[width*height];
		TransformUtils.rgb2yuv(rgb, y2, u2, v2);
		
		y = new int[height][];
		u = new int[height][];
		v = new int[height][];
		for(int i=0;i<height;i++) {
			y[i] = new int[width];
			u[i] = new int[width];
			v[i] = new int[width];
			for(int j=0;j<width;j++) {
				y[i][j] = y2[i*width+j];
				u[i][j] = u2[i*width+j];
				v[i][j] = v2[i*width+j];
			}
		}
	}

	public Image(int len) {
		red = new int[len];
		green = new int[len];
		blue = new int[len];
	}
	
	public void save(File file, String format) throws IOException {
		ImageUtils.saveImage(rgb, width, height, file, format);
	}
	
	public void rotate(int angle) {
		rgb = new ImageRotationUtil(this).rotate(angle);
		init();
	}
	
	public int[][] getBinaryImage() {
		int[][] binImg = new int[y.length][];
		for(int i=0;i<y.length;i++) {
			binImg[i] = new int[y[i].length];
			for(int j=0;j<y[i].length;j++) {
				binImg[i][j] = (y[i][j]>128)?1:0;
			}
		}
		return binImg;
	}
	
	public int[] getBinaryImage1D() {
		int[] binImg = new int[y.length*y[0].length];
		for(int i=0;i<y.length;i++) {
			for(int j=0;j<y[i].length;j++) {
				binImg[i*y[i].length+j] = (y[i][j]>128)?1:0;
			}
		}
		return binImg;
	}
	
	public int[] getRed() {
		return red;
	}
	public void setRed(int[] red) {
		this.red = red;
	}
	public int[] getGreen() {
		return green;
	}
	public void setGreen(int[] green) {
		this.green = green;
	}
	public int[] getBlue() {
		return blue;
	}
	public void setBlue(int[] blue) {
		this.blue = blue;
	}

	public int[] getRgb() {
		return rgb;
	}

	public void setRgb(int[] rgb) {
		this.rgb = rgb;
	}

	public int[][] getRgb2D() {
		return rgb2D;
	}

	public void setRgb2D(int[][] rgb2d) {
		rgb2D = rgb2d;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int[][] getY() {
		return y;
	}

	public void setY(int[][] y) {
		this.y = y;
	}

	public int[][] getU() {
		return u;
	}

	public void setU(int[][] u) {
		this.u = u;
	}

	public int[][] getV() {
		return v;
	}

	public void setV(int[][] v) {
		this.v = v;
	}
}
