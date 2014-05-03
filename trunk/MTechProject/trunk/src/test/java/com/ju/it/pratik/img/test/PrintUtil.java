package com.ju.it.pratik.img.test;

import java.text.DecimalFormat;

public class PrintUtil {

	private static DecimalFormat df = new DecimalFormat("##.##");
	
	public static String print2DArray(int[][] arr) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[0].length;j++) {
				sb.append(arr[i][j]+"\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static String print2DArray(int[] arr) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<arr.length;i++) {
			sb.append(arr[i]+"\t");
		}
		return sb.toString();
	}
	
	public static String print2DArray(double[][] arr) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[0].length;j++) {
				sb.append(Math.round(arr[i][j]*100)/100+"\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static String print2DArray(double[] arr) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<arr.length;i++) {
			sb.append(arr[i]+"\t");
		}
		return sb.toString();
	}
	
	public static String print2DArray(double[][] arr, int startY, int startX, int wlen) {
		StringBuilder sb = new StringBuilder();
		for(int i=startY;i<startY+wlen;i++) {
			for(int j=startX;j<startX+wlen;j++) {
				sb.append(df.format(arr[i][j])+"\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
