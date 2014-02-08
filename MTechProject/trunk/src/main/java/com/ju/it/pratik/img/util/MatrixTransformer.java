package com.ju.it.pratik.img.util;

public class MatrixTransformer {

	
	public static void transpose(int[][] input) {
		int tmp;
		for(int i=0;i<input.length;i++) {
			for(int j=i+1;j<input[0].length;j++) {
				tmp = input[j][i];
				input[j][i] = input[i][j];
				input[i][j] = tmp;
			}
		}
	}
	
	public static void transpose(double[][] input) {
		double tmp;
		for(int i=0;i<input.length;i++) {
			for(int j=i+1;j<input[0].length;j++) {
				tmp = input[j][i];
				input[j][i] = input[i][j];
				input[i][j] = tmp;
			}
		}
	}
	
	public double[][] multiply(double[][] A, double[][] B) {
		double[][] result = new double[A.length][];
		for(int i=0;i<A.length;i++) {
			for(int j=0;j<A[0].length;j++) {
				
			}
		}
		return result;
	}
}
