package com.ju.it.pratik.img.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.ComplexNum;
import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.DCTTransformUtil;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;

public class ImageTest implements WMConsts {

	public static final String WATER_MARK = "This is a test. ";
	public static final String DEFAULT_FOLDER = "resources/images/";
	public static final String DEFAULT_WATERMARK_INPUT_FILE_NAME = "Hydrangeas_512.jpg";
	public static final String DEFAULT_WATERMARKED_IMAGE = "Hydrangeas_512_rgb_wm.bmp";
	
	private BufferedImage bufferedImage;
	private int imageHeight;
	private int imageWidth;
	private int[] rgb;
	private int r[];
	private int g[];
	private int b[];
	
	@Before
	public void init() throws IOException {
		bufferedImage = ImageIO.read(new File(RESOURCE_IMAGES+SAMPLE_IMAGES[LENA_INDEX]));
		imageHeight = bufferedImage.getHeight();
		imageWidth = bufferedImage.getWidth();
		rgb = new int[imageWidth * imageHeight];
		r = new int[rgb.length];
		g = new int[rgb.length];
		b = new int[rgb.length];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, rgb, 0, imageWidth);
		ImageUtils.getChannels(rgb, r, g, b);
	}
	
	DCTTransformUtil util = new DCTTransformUtil(2);
	
	@Test
	public void testImprovedDCTPerformance() {
		long startTime = new Date().getTime();
		long endTime, diff;
		for(int i=0;i<1000;i++) {
			testDCT();
		}
		endTime = new Date().getTime();
		diff = endTime-startTime;
		System.out.print("diff: "+diff);
	}
	
	@Test
	public void testDCT() {
		
		//double input[][] =  new double[][] {{1d,2d,3d,4d},{5d,6d,7d,8d},{9d,10d,11d,12d},{13d,14d,15d,16d}};
		int input[][] =  new int[][] {{54,35},{128,185}};
		double output[][];
		output = util.applyDCTImproved(input);
		printArr(output);
		int idct[][];
		idct = util.applyIDCTImproved(output);
		printArr(idct);
	}
	
	@Test
	public void testRGB() throws IOException {
		int rgb[] = new int[imageWidth * imageHeight];
		bufferedImage.getRGB(0, 0, imageWidth, imageHeight, rgb, 0, imageWidth);
		int[] r = new int[rgb.length];
		int[] g = new int[rgb.length];
		int[] b = new int[rgb.length];
		ImageUtils.getChannels(rgb, r, g, b);
		int[] mergedRGB = ImageUtils.mergeChannels(r, g, b);
		assert(compareArray(rgb, mergedRGB));
		ImageUtils.saveRGBImage(imageWidth, imageHeight, mergedRGB, RESOURCE_IMAGES+"output/desert_crop_rgb.bmp");
	}
	
	@Test
	public void testWaveletTransform() throws IOException {
		//int[] inputImg = new int[]{56,40,8,24,48,48,40,16};		
				
		double[] inputImgTmp = new double[rgb.length];
		double[] dwt = TransformUtils.discreteWaveletTransform(rgb);
		System.arraycopy(dwt, 0, inputImgTmp, 0, rgb.length);
		int invDWT[] = TransformUtils.inverseDiscreteWaveletTransform(inputImgTmp);
		write("Original,InverDWT,DWT\r\n");
		boolean error = false;
		for(int i=0;i<rgb.length;i++) {
			/*System.out.print(inputImg[i]+" - "+output[i]);
			if(inputImg[i] != output[i]) {
				throw new IOException("no match");
			}*/
			write(rgb[i]+","+invDWT[i]+","+dwt[i]+",=A"+(i+2)+"-B"+(i+2)+"\r\n");
			if(rgb[i] != invDWT[i]) {
				error=true;
			}
		}
		System.err.print(error);
	}
	
	@Test
	public void testDFT() throws IOException {
		File input = new File("resources/images/Desert_crop.jpg");
		BufferedImage bufferedImage = ImageIO.read(input);
		int w = bufferedImage.getWidth();
		int h = bufferedImage.getHeight();
		System.out.print(h*w);
		int[] imgArr = new int[h*w];
		bufferedImage.getRGB(0, 0, w, h, imgArr, 0, w);
		for(int i=0;i<imgArr.length;i++) {
			imgArr[i]=new Color(imgArr[i]).getRed();
		}
		/*for(int x=0;x<h;x++) {
			for(int y=0;y<w;y++) {
				Color c = new Color(bufferedImage.getRGB(y, x));
				//System.out.print(c.getRed()+",");
				imgArr[y/w + (y%w)] = c.getRed();  
			}
		}*/
		//System.out.println();
		ComplexNum complexNums[] = TransformUtils.discreteFourierTransform(imgArr);	
		System.out.print("FT complete");
		StringBuilder sb = new StringBuilder();
		/*for(ComplexNum c : complexNums) {
			sb.append(c);
		}
		System.out.println("\n"+sb);*/
		
		
		ComplexNum[] invComplexNums = TransformUtils.inverseDiscreteFourierTransform(complexNums.clone());
		
		/**jwave starts **/
		/*Complex[] inputArr = new Complex[h*w];
		for(int i=0;i<h*w;i++) {
			inputArr[i] = new Complex();
			inputArr[i].setReal(imgArr[i]);
		}
		DiscreteFourierTransform transform = new DiscreteFourierTransform();
		Complex[] forRes = transform.forward(inputArr);
		
		Complex[] revRes = transform.reverse(forRes);*/
		/** jwave ends **/
		
		
		sb = new StringBuilder("Original \tFT \tFTJWAVE \tDiff\t RFT \t RTF JWAVE \tDiff\tDiffOrig\r\n");
		int ctr=0;
		for(ctr=0;ctr<h*w;ctr++) {
			sb.append(imgArr[ctr]+"\t"+complexNums[ctr].getReal()+"\t=B"+(ctr+2)+"-C"+(ctr+2)
					+"\t"+(int)invComplexNums[ctr].getReal()+"\t=D"+(ctr+2)+"-E"+(ctr+2)+"\t"+"=A"+(ctr+2)+"-E"+(ctr+2)+"\r\n");
		}
		
		//System.out.println(sb);
		
		File f = new File("d:/temp.txt");
		f.delete();
		f.createNewFile();
		new FileOutputStream(f).write(sb.toString().getBytes());
	}
	
	@Test
	public void testGrayScale() {
		toGrayscale(new File("resources/images/Desert.jpg"), new File("resources/images/output/Desert_grayscale.jpg"));
	}
	
	@Test
	public void testRGBToYUV() throws IOException {
		int pixel = bufferedImage.getRGB(0, 0);
		int r1[] = new int[rgb.length];
		int g1[] = new int[rgb.length];
		int b1[] = new int[rgb.length];
		ImageUtils.getChannels(new int[]{pixel}, r1, g1, b1);
		
		int yuv = TransformUtils.rgb2yuv(pixel);
		int resrgb = TransformUtils.yuv2rgb(yuv);
		
		int r2[] = new int[rgb.length];
		int g2[] = new int[rgb.length];
		int b2[] = new int[rgb.length];
		ImageUtils.getChannels(new int[]{resrgb}, r2, g2, b2);
		
		System.out.println(r1[0]+" - "+r2[0]);
		System.out.println(g1[0]+" - "+g2[0]);
		System.out.println(b1[0]+" - "+b2[0]);
	}
	
	@Test
	public void testWatermark() throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new File("resources/images/Desert.jpg"));
		watermark(bufferedImage, "Barsha");
		toBinary("barsha");
	}
	
	@Test
	public void toBinary() {
		boolean[] binaryArr = toBinary(128, 24,32);
		/*System.out.println(Integer.toBinaryString(255));*/
		System.out.println(Arrays.toString(binaryArr));
	}
	
	@Test
	public void testGetChannels() {
		try {
			BufferedImage bufferedImage = ImageIO.read(new File("resources/images/Desert_crop.jpg"));
			int w = bufferedImage.getWidth();
			int h = bufferedImage.getHeight();
			int rgb[] = new int[w*h];
			bufferedImage.getRGB(0, 0, w, h, rgb, 0, w);
			int[] r = new int[w*h];
			int[] g = new int[w*h];
			int[] b = new int[w*h];
			ImageUtils.getChannels(rgb, r, g, b);
			ImageUtils.saveRGBImage(w, h, r, g, b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void toGrayscale(File inputFile, File outputFile) {
		try {
			BufferedImage bufferedImage = ImageIO.read(inputFile);
			ImageIO.write(toGrayscale(bufferedImage), "BMP", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public BufferedImage toGrayscale(BufferedImage origBufferedImage) {
		BufferedImage newBufferedImage = new BufferedImage(origBufferedImage.getWidth(), origBufferedImage.getHeight(), origBufferedImage.getType());
		int ht = origBufferedImage.getHeight();
		int wid = origBufferedImage.getWidth();
		for(int i=0;i<wid;i++) {
			for(int j=0;j<ht;j++) {
				newBufferedImage.setRGB(i, j, toGrayscale(origBufferedImage.getRGB(i, j)));
			}
		}
		return newBufferedImage;
	}
	
	public int toGrayscale(int rgb) {
		int alpha = (rgb>>24) & 0xFF;
		int red = (rgb>>16) & 0xFF;
		int green = (rgb>>8) & 0xFF;
		int blue = rgb & 0xFF;
		int avg = ((red+green+blue)/3) & 0xFC;
		int finalVal = ((((alpha<<24) | (avg<<16)) | (avg<<8)) | avg); 
		return finalVal;
	}
	
	public BufferedImage watermark(BufferedImage bufferedImage, String noise) {
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(), bufferedImage.getType());
		System.out.println(noise.getBytes().length*2*8);
		System.out.println();
		for(int x=0;x<bufferedImage.getWidth();x++) {
			for(int y=0;y<bufferedImage.getHeight();y++) {
				newBufferedImage.setRGB(x, y, (bufferedImage.getRGB(x, y)));
			}
		}
		return newBufferedImage;
	}
	
	public int watermark(int rgb, boolean bit) {
		int alpha = (rgb>>24) & 0xFF;
		int red = (rgb>>16) & 0xFF;
		int green = (rgb>>8) & 0xFF;
		int blue = rgb & 0xFE;
		
		//watermarking
		if(bit) {
			red = red | 0xFF;
		}
		else {
			red = red | 0xFE;
		}
		int finalVal = ((((alpha<<24) | (red<<16)) | (green<<8)) | blue); 
		return finalVal;
	}

	
	public boolean[] toBinary(int input, int start, int end) {
		boolean[] binaryArr = new boolean[end-start];
		input = input << start;
		for(int i=end-start;i>0;i--) {
			int t = 1 << (start+i-1);
			binaryArr[end-start-i] = (input & (t)) != 0;
		}
		return binaryArr;
	}
	
	public void toBinary(String s) throws UnsupportedEncodingException {
		byte[] bytes = s.getBytes("UTF8");
		  StringBuilder binary = new StringBuilder();
		  for (byte b : bytes)
		  {
		     int val = b;
		     for (int i = 0; i < 8; i++)
		     {
		        binary.append((val & 128) == 0 ? 0 : 1);
		        val <<= 1;
		     }
		     binary.append(' ');
		  }
		  System.out.println("'" + s + "' to binary: " + binary);
	}
	
	FileOutputStream fout;
	
	//@Before
	public void setUp() {
		try {
			File f = new File("d:/temp.csv");
			fout = new FileOutputStream(f);
			f.delete();
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void write(String str) throws FileNotFoundException, IOException {
		fout.write(str.getBytes());
	}
	
	public boolean compareArray(int[] arr1, int[] arr2) {
		for(int i=0;i<arr1.length;i++) {
			if(arr1[i] != arr2[i])
				return false;
		}
		return true;
	}
	
	private void printArr(double[][] arr) {
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr.length;j++) {
				System.out.print(df.format(arr[i][j])+"\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private void printArr(int[][] arr) {
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr.length;j++) {
				System.out.print(df.format(arr[i][j])+"\t");
			}
			System.out.println();
		}
		System.out.println();
	}
}
