package com.ju.it.pratik.img.test;

public class PrintUtil {

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
}
