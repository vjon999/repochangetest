package com.ju.it.pratik.img.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.ju.it.pratik.img.Image;

public class ImageUtils {
	
	private static final Logger LOG = Logger.getLogger(ImageUtils.class.getName());

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
				image.setRGB(j, i, rgb[i*width+j]);
			}
		}
		LOG.fine("Saving file at fName: "+fName);
		ImageIO.write(image, "BMP", new File(fName));
	}
	
	public static void saveJPGImage(BufferedImage bufferedImage, String fName) throws IOException {
		LOG.fine("Saving file at fName: "+fName);
		ImageIO.write(bufferedImage, "jpg", new File(fName));
	}
	
	public static BufferedImage toBufferedImage(int[] pixels, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image.setRGB(j, i, pixels[i*width+j]);
			}
		}
		return image;
	}
	public static void saveImage(int[] pixels, int width, int height, File file, String format) throws IOException {
		boolean success = ImageIO.write(toBufferedImage(pixels, width, height), format, file);
		if(success) {
			LOG.info("Saving file: "+file);
		}
		else {
			LOG.severe("cannot save image at "+file);
		}
	}
	
	public static void getChannels(int rgb[], int r[], int[] g, int[] b) {
		for(int i=0;i<rgb.length;i++) {
			r[i] = (rgb[i]>>16) & 0x00ff;
			g[i] = (rgb[i]>>8) & 0x00ff;
			b[i] = rgb[i] & 0x00ff;
		}
	}
	
	public static void getChannels(int rgb[][], int r[][], int[][] g, int[][] b) {
		for(int i=0;i<rgb.length;i++) {
			for(int j=0;j<rgb[0].length;j++) {
				r[i][j] = (rgb[i][j]>>16) & 0x00ff;
				g[i][j] = (rgb[i][j]>>8) & 0x00ff;
				b[i][j] = rgb[i][j] & 0x00ff;
			}
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
	
	public static double[] lineWatermark(double[] input, int[] watermark, int bitLen) {
		int tmpIntVal;// int variable to store convert the float bits to int bits and do XOR operation
		double[] output = new double[input.length]; // output variable to store the result
		
		/* looping input array's length divided by 4 as we plan to XOR Least Significant 2 bits of input with watermark's last 2 bits  */
		for(int i=0;i<input.length;i=i+8/bitLen) {
			for(int k=0;k<(8/bitLen);k++) {
				//converting float bits to int bits so that we can do XOR operation
				tmpIntVal = (int) Double.doubleToLongBits(input[i+k]);
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
	
	public static String recoverLineWatermark(float[] input, int[] watermark, int bitLen) {
		int in;// int variable to store convert the float bits to int bits and do XOR operation
		float[] output = new float[input.length]; // output variable to store the result
		int[] extractedWatermark = new int[watermark.length];
		StringBuilder sb = new StringBuilder();
		int rc=0,w;
		int pLen = watermark.length*8/bitLen;
		for(int i=0;i<pLen;i++) {
			int byt = 0;
			for(int k=0;k<8;k++) {
				in = Float.floatToIntBits(input[i+k]);
				w = watermark[i];
				byt = (in ^ ((w>>k) & 0x1));
				byt = ((byt <<31-k)>>31-k);
				System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(in)) +" ---- "+ Integer.toBinaryString(byt));
				byt = byt <<1;
			}
			sb.append((char)byt);
		}
		System.out.print(sb.toString());
		return sb.toString();
	}
	
	public static String recoverLineWatermark(float[] input, int bitLen) {
		return null;
	}

	public NoiseAnalysisResult calculatePSNR(int[][] originalImage, int[][] watermarkedImage) {
		int nrows = originalImage[0].length, ncols = originalImage.length;
		long signal = 0, noise = 0, peak = 0;
		NoiseAnalysisResult result = new NoiseAnalysisResult();

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				signal += originalImage[i][j] * originalImage[i][j];
				noise += (watermarkedImage[i][j] - watermarkedImage[i][j]) * (watermarkedImage[i][j] - watermarkedImage[i][j]);
				if (peak < watermarkedImage[i][j])
					peak = watermarkedImage[i][j];
			}
		}
		double mse = (double)noise/(double)(nrows*ncols);
		
		result.setMse(mse);
		result.setPsnr(10*Math.log10(255*255/mse));
		result.setPsnrMax(10*Math.log10((peak*peak)/mse));
		result.setSnr(10*Math.log10(signal/noise));
		
		System.out.println(result);
		return result;
	}
	
	public int[] rotate(int[] imgArr, int width, int height) {
		int[] resultImg = new int[imgArr.length];
		int ctr = 0;
		for(int i=0;i<width;i++) {
			for(int j=height-1;j>=0;j--) {
				resultImg[ctr++] = imgArr[j*width+i];
			}
		}
		return resultImg;
	}
	
	public double[] rotate(double[] imgArr, int width, int height) {
		double[] resultImg = new double[imgArr.length];
		int ctr = 0;
		for(int i=0;i<width;i++) {
			for(int j=height-1;j>=0;j--) {
				resultImg[ctr++] = imgArr[j*width+i];
			}
		}
		return resultImg;
	}
	
	public double[] rotateLeft(double[] imgArr, int width, int height) {
		double[] resultImg = new double[imgArr.length];
		int ctr = 0;
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				resultImg[ctr++] = imgArr[j*width+i];
			}
		}
		return resultImg;
	}
	
	public double[] rotateInvert(double[] imgArr) {
		double[] result = new double[imgArr.length];
		int ctr = 0;
		for(int i=imgArr.length-1;i>=0;i--) {
			result[ctr++] = imgArr[i]; 
		}
		return result;
	}
	
	public static int[][] to2D(int[] image, int height, int width) {
		int[][] image2D = new int[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image2D[i][j] = image[i*width+j];					
			}
		}
		return image2D;
	}
	
	public static double[][] to2D(double[] image, int height, int width) {
		double[][] image2D = new double[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image2D[i][j] = image[i*width+j];					
			}
		}
		return image2D;
	}
	
	public static double[] to1D(double[][] image, int height, int width) {
		double[] image1D = new double[height*width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image1D[i*width+j] = image[i][j];					
			}
		}
		return image1D;
	}
	
	public static int[] to1D(int[][] image, int height, int width) {
		int[] image1D = new int[height*width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				image1D[i*width+j] = image[i][j];					
			}
		}
		return image1D;
	}

}
