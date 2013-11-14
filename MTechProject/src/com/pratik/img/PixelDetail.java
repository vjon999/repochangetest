package com.pratik.img;

public class PixelDetail {

	private int red;
	private int green;
	private int blue;
	private int alpha;
	
	public PixelDetail() {	}
	public PixelDetail(int pixel) {
		blue = (pixel & 0xFF);
		green = ((pixel >> 8) & 0xFF);
		red = ((pixel >> 16) & 0xFF);
	}
	
	public String toString() {
		return "("+red+","+green+","+blue+")";
	}

}
