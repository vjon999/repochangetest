package com.pratik.img;

public class Image {

	private int[] red;
	private int[] green;
	private int[] blue;

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
}
