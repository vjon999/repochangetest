package com.ju.it.pratik.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ju.it.pratik.img.util.TransformUtils;

public class Image {

	private int[] red;
	private int[] green;
	private int[] blue;
	private int rgb[];
	private int rgb2D[][];
	private int height;
	private int width;
	int[][] y;
	int[][] u;
	int[][] v;
	
	public Image(String path) throws IOException {
		File in = new File(path);
		if(!in.exists())
			throw new FileNotFoundException("file not found: "+path);
		BufferedImage bufImg = ImageIO.read(in);
		height = bufImg.getHeight();
		width = bufImg.getWidth();
		rgb = bufImg.getRGB(0, 0, width, height, rgb, 0, width);
		rgb2D = new int[height][];
		
		int center_x = width/2;
		int center_y = height/2;
		/*for(int i=0;i<height;i++) {
			rgb2D[i] = new int[width];
			for(int j=0;j<width;j++) {
				rgb2D[i][j] = rgb[i*width+j];
			}
		}*/
		for(int i=128;i<center_y+128;i++) {
			rgb2D[i-128] = new int[256];
			for(int j=128;j<center_x+128;j++) {
				rgb2D[i-128][j-128] = rgb[i*width+j];
			}
		}
		int[] y2 = new int[width*height];
		int[] u2 = new int[width*height];
		int[] v2 = new int[width*height];
		TransformUtils.rgb2yuv(rgb, y2, u2, v2);
		
		/*y = new int[height][];
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
		}*/
		y = new int[height/2][];
		u = new int[height/2][];
		v = new int[height/2][];
		for(int i=128;i<center_y+128;i++) {
			y[i-128] = new int[width/2];
			u[i-128] = new int[width/2];
			v[i-128] = new int[width/2];
			for(int j=128;j<center_x+128;j++) {
				y[i-128][j-128] = y2[i*width+j];
				u[i-128][j-128] = u2[i*width+j];
				v[i-128][j-128] = v2[i*width+j];
			}
		}
	}

	public Image(int len) {
		red = new int[len];
		green = new int[len];
		blue = new int[len];
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
