package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.TransformUtils;
import com.ju.it.pratik.img.util.WaveletTransformer;

public class WaveletTransformTester implements WMConsts{

	private static final Logger LOG = Logger.getLogger(WaveletTransformTester.class.getName());
	
	private int[] sampleInput;
	private int[][] sample2DInput;
	private double[][] sample2DOutput;
	
	@Before
	public void setUp() {
		sampleInput = new int[64];
		sample2DInput = new int[8][8];
		sample2DOutput = new double[8][8];
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/freqInput.txt")));
			for(int i=0;i<64;i++) {
				sampleInput[i] = Integer.parseInt(reader.readLine());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("failed to setUp the testcase");
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/resources/freqInput.txt")));
			for(int i=0;i<8;i++) {
				String[] arr = reader.readLine().split(",");
				for(int j=0;j<8;j++) {
					sample2DInput[i][j] = Integer.parseInt(arr[j]);
				}
			}
			reader.readLine();
			for(int i=0;i<8;i++) {
				String[] arr = reader.readLine().split(",");
				for(int j=0;j<8;j++) {
					sample2DOutput[i][j] = Double.parseDouble(arr[j]);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("failed to setUp the testcase");
		}
	}
	
	@Test
	public void testForwardAndInverseDWT() throws IOException {
		double[] dwt = WaveletTransformer.discreteWaveletTransform(sampleInput);
		LOG.fine(Arrays.toString(dwt));		
		double[] idwt = WaveletTransformer.inverseDiscreteWaveletTransform(dwt);
		LOG.fine(Arrays.toString(idwt));
		for(int i=0;i<64;i++) {
			if(idwt[i] != sampleInput[i]) {
				LOG.log(Level.SEVERE, "expected "+sampleInput[i]+" but actual: "+idwt[i]);
				throw new AssertionError("expected "+sampleInput[i]+" but actual: "+idwt[i]);
			}
		}
	}
	
	@Test
	public void testForwardAndInverse2D_DWT() throws IOException {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<sample2DInput.length;i++) {			
			for(int j=0;j<sample2DInput.length;j++) {
				sb.append(sample2DInput[i][j]+"\t");
			}
			sb.append("\n");
		}
		sb.append("\n");
		double[][] dwt = WaveletTransformer.discreteWaveletTransform(sample2DInput);
		for(int i=0;i<8;i++) {
			sb.append(Arrays.toString(dwt[i]).replaceAll(", ", "\t").replaceFirst("\\[", "").replaceFirst("\\]", "")+"\n");
		}
		sb.append("\n");
		LOG.fine("Forward DWT complete");
		int[][] idwt = WaveletTransformer.inverseDiscreteWaveletTransform(dwt);
		for(int i=0;i<8;i++) {
			sb.append(Arrays.toString(idwt[i]).replaceAll(", ", "\t").replaceFirst("\\[", "").replaceFirst("\\]", "")+"\n");
		}
		LOG.fine("Reverse DWT complete");
		System.out.println(sb);
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				if(idwt[i][j] != sample2DInput[i][j]) {
					LOG.log(Level.SEVERE, "expected "+sample2DInput[i][j]+" but actual: "+idwt[i][j]);
					throw new AssertionError("expected "+sample2DInput[i][j]+" but actual: "+idwt[i][j]);
				}
			}
		}		
	}
	
	@Test
	public void testWaveletCompress() throws IOException {
		String inputImage = RESOURCE_IMAGES + LENA;
		//read image
		LOG.info("inputFile: "+inputImage);
		BufferedImage bufferedImage = ImageIO.read(new File(inputImage));
		int height = bufferedImage.getHeight();
		int width = bufferedImage.getWidth();
		int[] rgb = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
		int[] r = new int[rgb.length];
		int g[] = new int[rgb.length];
		int b[] = new int[rgb.length];
		bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), rgb, 0, bufferedImage.getHeight());
		ImageUtils.getChannels(rgb, r, g, b);
		
		/** converting RGB to YUV starts */
		int yuv[] = new int[r.length];
		for(int i=0;i<rgb.length;i++) {
			yuv[i] = TransformUtils.rgb2yuv(rgb[i]);
		}
		/** converting RGB to YUV ends */
		
		/** getting seperate Y, U, V channels */
		int y[] = new int[rgb.length];
		int u[] = new int[rgb.length];
		int v[] = new int[rgb.length];
		ImageUtils.getChannels(yuv, y, u, v);
		/** getting seperate Y, U, V channels ends */
		
		/** converting U channel's DCT */
		int[][] dct = ImageUtils.to2D(y, bufferedImage.getHeight(), bufferedImage.getHeight());
		double[][] res = WaveletTransformer.discreteWaveletTransform(dct);
		for(int i=0;i<res.length;i++) {
			for(int j=res[i].length/2;j<res[i].length;j++) {
				res[i][j] = 0;
			}
		}
		int[][] res2 = WaveletTransformer.inverseDiscreteWaveletTransform(res);		
		int[] wy = ImageUtils.to1D(res2);
		//for U channel
		dct = ImageUtils.to2D(u, bufferedImage.getHeight(), bufferedImage.getHeight());
		res = WaveletTransformer.discreteWaveletTransform(dct);
		for(int i=0;i<res.length;i++) {
			for(int j=res[i].length/2;j<res[i].length;j++) {
				res[i][j] = 0;
			}
		}
		res2 = WaveletTransformer.inverseDiscreteWaveletTransform(res);		
		int[] wu = ImageUtils.to1D(res2);
		//for V channel
		dct = ImageUtils.to2D(v, bufferedImage.getHeight(), bufferedImage.getHeight());
		res = WaveletTransformer.discreteWaveletTransform(dct);
		for(int i=0;i<res.length;i++) {
			for(int j=res[i].length/2;j<res[i].length;j++) {
				res[i][j] = 0;
			}
		}
		res2 = WaveletTransformer.inverseDiscreteWaveletTransform(res);		
		int[] wv = ImageUtils.to1D(res2);
		
		
		int[] resYUV = ImageUtils.mergeChannels(wy,wu, wv);
		
		/** converting YUV to RGB starts */
		int convertedRgb[] = new int[r.length];
		for(int i=0;i<convertedRgb.length;i++) {
			convertedRgb[i] = TransformUtils.yuv2rgb(resYUV[i]);
		}
		/** converting YUV to RGB ends */
		BufferedImage outputBufferedImage = ImageUtils.toBufferedImage(convertedRgb, width, height);
		ImageIO.write(outputBufferedImage, "BMP", new File("src/test/resources/" + "wavelet.bmp"));
	}
}
