package com.ju.it.pratik.img.util;

import com.ju.it.pratik.img.test.PrintUtil;

public class WaveletTransformer {

	public static double[] discreteWaveletTransform(int[] input) {
		double[] output = new double[input.length];
		double[] dblInput = new double[input.length];
		for(int i=0;i<input.length;i++) {
			dblInput[i] = input[i];
		}
		
		for(int len = dblInput.length>>1; len>=1; len >>= 1) {
			double mean=0, diff=0;
			for(int i=0;i<len;i++) {
				mean = (dblInput[2*i] + dblInput[2*i+1])/2;
				diff = dblInput[2*i] - mean;
				output[i] = mean;
				output[len + i] = diff;
			}
			if(len == 1)
				return output;
			
			System.arraycopy(output, 0, dblInput, 0, dblInput.length);
		}
		
		return output;
	}
	
	public static double[] discreteWaveletTransform(double[] input) {
		double[] output = new double[input.length];
		double[] dblInput = new double[input.length];
		for(int i=0;i<input.length;i++) {
			dblInput[i] = input[i];
		}
		
		for(int len = dblInput.length>>1; len>=1; len >>= 1) {
			double mean=0, diff=0;
			for(int i=0;i<len;i++) {
				mean = (dblInput[2*i] + dblInput[2*i+1])/2;
				diff = dblInput[2*i] - mean;
				output[i] = mean;
				output[len + i] = diff;
			}
			if(len == 1)
				return output;
			
			System.arraycopy(output, 0, dblInput, 0, dblInput.length);
		}
		
		return output;
	}
	
	public static double[][] discreteWaveletTransform(int[][] input) {
		double[][] output = new double[input.length][];
		for(int i=0;i<input.length;i++) {
			output[i] = discreteWaveletTransform(input[i]);
		}
		MatrixTransformer.transpose(output);
		for(int i=0;i<input.length;i++) {
			output[i] = discreteWaveletTransform(output[i]);
		}
		MatrixTransformer.transpose(output);
		return output;
	}
	
	public static double[] inverseDiscreteWaveletTransform(double[] input) {
		double[] output = new double[input.length];
		System.arraycopy(input, 0, output, 0, input.length);
		for(int len = 1;len <= input.length>>1; len *= 2) {			
			double sum=0, diff=0;
			for(int i=0;i<len; i++) {
				sum = input[i] + input[len+i];
				diff = input[i] - input[len+i];
				output[2*i] = sum;
				output[2*i+1] = diff;
			}
			System.arraycopy(output, 0, input, 0, input.length);
			
		}
		return output;
	}
	
	public static int[][] inverseDiscreteWaveletTransform(double[][] input) {
		double[][] output = new double[input.length][];
		System.arraycopy(input, 0, output, 0, input.length);
		MatrixTransformer.transpose(output);
		for(int i=0;i<input.length;i++) {
			output[i] = inverseDiscreteWaveletTransform(output[i]);
		}
		MatrixTransformer.transpose(output);
		for(int i=0;i<output.length;i++) {
			double[] temp = new double[output[i].length];
			for(int j=0;j<output.length;j++) {
				temp[j] = output[i][j];
			}
			output[i] = inverseDiscreteWaveletTransform(temp);
		}
		int[][] finalOutput = new int[input.length][];
		for(int i=0;i<input.length;i++) {
			finalOutput[i] = new int[input[i].length];
			for(int j=0;j<input[0].length;j++) {
				finalOutput[i][j] = (int)output[i][j];
			}
		}
		return finalOutput;
	}
	


	
	
	/** Wavelet transform based on level */
	
	public static double[] discreteWaveletTransform(int[] input, int level) {
		double[] output = new double[input.length];
		double[] dblInput = new double[input.length];
		for(int i=0;i<input.length;i++) {
			dblInput[i] = input[i];
		}
		
		for(int len = dblInput.length>>1; len>=1 && level>0; len >>= 1, level--) {
			double mean=0, diff=0;
			for(int i=0;i<len;i++) {
				mean = (dblInput[2*i] + dblInput[2*i+1])/2;
				diff = dblInput[2*i] - mean;
				output[i] = mean;
				output[len + i] = diff;
			}
			if(len == 1)
				return output;
			
			System.arraycopy(output, 0, dblInput, 0, dblInput.length);
		}
		
		return output;
	}
	
	public static double[] discreteWaveletTransform(double[] input, int level) {
		double[] output = new double[input.length];
		double[] dblInput = new double[input.length];
		for(int i=0;i<input.length;i++) {
			dblInput[i] = input[i];
		}
		for(int len = dblInput.length>>1; len>=1 && level>0; len >>= 1, level--) {
			double mean=0, diff=0;
			for(int i=0;i<len;i++) {
				mean = (dblInput[2*i] + dblInput[2*i+1])/2;
				diff = dblInput[2*i] - mean;
				output[i] = mean;
				output[len + i] = diff;
			}
			if(len == 1)
				return output;
			
			System.arraycopy(output, 0, dblInput, 0, dblInput.length);
		}
		
		return output;
	}
	
	public static double[][] discreteWaveletTransform(int[][] input, int level) {
		double[][] output = new double[input.length][];
		for(int i=0;i<input.length;i++) {
			output[i] = discreteWaveletTransform(input[i], level);
		}
		MatrixTransformer.transpose(output);
		for(int i=0;i<input.length;i++) {
			output[i] = discreteWaveletTransform(output[i], level);
		}
		MatrixTransformer.transpose(output);
		return output;
	}
	
	public static double[] inverseDiscreteWaveletTransform(double[] input, int level) {
		double[] output = new double[input.length];
		System.arraycopy(input, 0, output, 0, input.length);
		for(int len = input.length>>level;len <= input.length>>1 && level>0; len = len*2, level--) {			
			double sum=0, diff=0;
			for(int i=0;i<len; i++) {
				sum = input[i] + input[len+i];
				diff = input[i] - input[len+i];
				output[2*i] = sum;
				output[2*i+1] = diff;
			}
			System.arraycopy(output, 0, input, 0, input.length);
			
		}
		return output;
	}
	
	public static int[][] inverseDiscreteWaveletTransform(double[][] input, int level) {
		double[][] output = new double[input.length][];
		System.arraycopy(input, 0, output, 0, input.length);
		MatrixTransformer.transpose(output);
		for(int i=0;i<input.length;i++) {
			output[i] = inverseDiscreteWaveletTransform(output[i], level);
		}
		MatrixTransformer.transpose(output);
		for(int i=0;i<output.length;i++) {
			double[] temp = new double[output[i].length];
			for(int j=0;j<output.length;j++) {
				temp[j] = output[i][j];
			}
			output[i] = inverseDiscreteWaveletTransform(temp, level);
		}
		int[][] finalOutput = new int[input.length][];
		for(int i=0;i<input.length;i++) {
			finalOutput[i] = new int[input[i].length];
			for(int j=0;j<input[0].length;j++) {
				finalOutput[i][j] = (int)output[i][j];
			}
		}
		return finalOutput;
	}
	
	public static void main(String[] args) {
		int[][] test = new int[][] {
				{1,2,3,4},
				{5,6,7,8},
				{9,10,11,12},
				{13,14,15,16},
		};
		double[][] dwt = discreteWaveletTransform(test, 2);
		System.out.println(PrintUtil.print2DArray(dwt));
		int[][] idwt = inverseDiscreteWaveletTransform(dwt, 2);
		System.out.println(PrintUtil.print2DArray(idwt));
	}
}
