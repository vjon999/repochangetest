package com.pratik.img.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.pratik.img.Image;

public class ImageUtils {

	public void saveGrayScaleImage(int width, int height, int[] data) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image.setRGB(i, j, data[(i*width)+j]);
			}
		}
		ImageIO.write(image, "BMP", new File("resources/images/output/Desert_crop_grayscale.jpg"));
	}
	
	public static void saveRGBImage(int width, int height, int[] red, int[] green, int[] blue) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int rgb, index;
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				index = (i*width)+j;
				rgb = (red[index]<<16) | (green[index]<<8) | blue[index];
				image.setRGB(i, j, rgb);
			}
		}
		ImageIO.write(image, "BMP", new File("resources/images/output/Desert_crop_rgb.jpg"));
	}
	
	public static void saveRGBImage(int width, int height, int[] rgb, String fName) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image.setRGB(i, j, rgb[i*width+j]);
			}
		}
		ImageIO.write(image, "BMP", new File(fName));
	}
	
	public static void getChannels(int rgb[], int r[], int[] g, int[] b) {
		for(int i=0;i<rgb.length;i++) {
			r[i] = (rgb[i]>>16) & 0x00ff;
			g[i] = (rgb[i]>>8) & 0x00ff;
			b[i] = rgb[i] & 0x00ff;
		}
	}
	
	public static Image getChannels(int rgb[]) {
		Image img = new Image(rgb.length);
		for(int i=0;i<rgb.length;i++) {
			img.getRed()[i] = (rgb[i]>>16) & 0x00ff;
			img.getGreen()[i] = (rgb[i]>>8) & 0x00ff;
			img.getBlue()[i] = rgb[i] & 0x00ff;
		}
		return img;
	}
	
	public static int[] mergeChannels(int r[], int[] g, int[] b) {
		int rgb[] = new int[r.length];
		for(int i=0;i<r.length;i++) {
			rgb[i] = (r[i]<<16) | (g[i]<<8) | b[i];
		}
		return rgb;
	}
	
	public static float[] lineWatermark(float[] input, int[] watermark, int bitLen) {
		int tmpIntVal;// int variable to store convert the float bits to int bits and do XOR operation
		float[] output = new float[input.length]; // output variable to store the result
		
		/* looping input array's length divided by 4 as we plan to XOR Least Significant 2 bits of input with watermark's last 2 bits  */
		for(int i=0;i<input.length;i=i+8/bitLen) {
			for(int k=0;k<(8/bitLen);k++) {
				//converting float bits to int bits so that we can do XOR operation
				tmpIntVal = Float.floatToIntBits(input[i+k]);
				//doing XOR operation with last 2 bits of input and watermark
				switch(bitLen) {
					case 1: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0x1);
					break;
					case 2: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0x3);
					break;
					case 3: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0x7);
					break;
					case 4: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF);
					break;
					case 5: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF1);
					break;
					case 6: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF3);
					break;
					case 7: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF7);
					break;
					case 8: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xFF);
					break;
				}
				//converting int bits to float and store in output variable
				output[i+k] = Float.intBitsToFloat(tmpIntVal);
			}
		}
		//return the watermarked result
		return output;
	}
	
	public static String recoverLineWatermark(float[] input, int bitLen) {/*
		int tmpIntVal;// int variable to store convert the float bits to int bits and do XOR operation
		for(int i=0;i<input.length;i=i+8/bitLen) {
			for(int k=0;k<(8/bitLen);k++) {
				//converting float bits to int bits so that we can do XOR operation
				tmpIntVal = Float.floatToIntBits(input[i+k]);
				//doing XOR operation with last 2 bits of input and watermark
				switch(bitLen) {
					case 1: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0x1);
					break;
					case 2: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0x3);
					break;
					case 3: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0x7);
					break;
					case 4: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF);
					break;
					case 5: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF1);
					break;
					case 6: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF3);
					break;
					case 7: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xF7);
					break;
					case 8: tmpIntVal = tmpIntVal ^ ((watermark[i%watermark.length]>>(k*bitLen)) & 0xFF);
					break;
				}
				//converting int bits to float and store in output variable
				output[i+k] = Float.intBitsToFloat(tmpIntVal);
			}
		}
	*/
		return null;
	}
}
