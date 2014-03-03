package com.ju.it.pratik.img.util;

import java.awt.Color;
import java.util.logging.Logger;

import com.ju.it.pratik.img.CHANNEL_TYPE;
import com.ju.it.pratik.img.ComplexNum;

public class TransformUtils {

	private static Logger LOG = Logger.getLogger(TransformUtils.class.getName());
	
	public static final double[][] RGB_TO_YUV_MATRIX = new double[][] { { 0.299f, 0.587f, 0.114f }, { -0.14713f, -0.28886f, 0.436f },
			{ -0.615f, -0.51499f, 0.10001f }, };

	public static final double[][] YUV_TO_RGB_MATRIX = new double[][] { { 1, 0, 1.13983f }, { 1, -0.39465f, 0.58060f }, { 1f, 2.03211f, 0 }, };

	/*public static int convertRGBToYUV(int pixel) {
		int yuv[] = new int[3];
		CHANNEL_TYPE[] channelTypes = { CHANNEL_TYPE.RED_CHANNEL, CHANNEL_TYPE.GREEN_CHANNEL, CHANNEL_TYPE.BLUE_CHANNEL };
		int r = getChannelValue(pixel, channelTypes[0]);
		int g = getChannelValue(pixel, channelTypes[1]);
		int b = getChannelValue(pixel, channelTypes[2]);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(RGB_TO_YUV_MATRIX[i][j] + "*" + getChannelValue(pixel, channelTypes[j]) + " = " + RGB_TO_YUV_MATRIX[i][j]
						* ((double) getChannelValue(pixel, channelTypes[j])) + " / ");
				yuv[i] += RGB_TO_YUV_MATRIX[i][j] * ((double) getChannelValue(pixel, channelTypes[j]));
			}
			System.out.println(" Result=" + yuv[i]);
		}
		int finalVal = ((((pixel & 0xFF000000) | (yuv[0] << 16)) | (yuv[1] << 8)) | yuv[2]);
		return finalVal;
	}

	*//**
	 * @param pixel
	 * @return
	 *//*
	public static int convertYUVToRGB(int pixel) {
		int rgb[] = new int[3];
		CHANNEL_TYPE[] channelTypes = { CHANNEL_TYPE.Y, CHANNEL_TYPE.U, CHANNEL_TYPE.V };

		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 3; j++) {
				System.out.print(YUV_TO_RGB_MATRIX[i][j] + "*" + getChannelValue(pixel, channelTypes[j]) + " = " + YUV_TO_RGB_MATRIX[i][j]
						* ((double) getChannelValue(pixel, channelTypes[j])) + " / ");
				rgb[i] += YUV_TO_RGB_MATRIX[i][j] * ((double) getChannelValue(pixel, channelTypes[j]));
			}
			System.out.println(rgb[i]);
		}

		int finalVal = ((((pixel & 0xFF000000) | (rgb[0] << 16)) | (rgb[1] << 8)) | rgb[2]);
		return finalVal;
	}*/

	public static int getChannelValue(int pixel, CHANNEL_TYPE channelType) {
		return (pixel >> channelType.getChannelType()) & 0x000000FF;

	}

	/*public static void rgb2yuv(int r, int g, int b, int[] yuv) {
		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int u = (int) ((b - y) * 0.492f);
		int v = (int) ((r - y) * 0.877f);

		yuv[0] = y;
		yuv[1] = u;
		yuv[2] = v;
	}*/
	
	public static void rgb2yuv(int[] rgb, int[] y, int[] u, int[] v) {
		int temp;
		for(int i=0;i<rgb.length;i++) {
			temp = rgb2yuv(rgb[i]);
			y[i] = getChannelValue(temp, CHANNEL_TYPE.Y);
			u[i] = getChannelValue(temp, CHANNEL_TYPE.U);
			v[i] = getChannelValue(temp, CHANNEL_TYPE.V);
		}
	}

	public static int rgb2yuv(int rgb) {
		int r = TransformUtils.getChannelValue(rgb, CHANNEL_TYPE.RED_CHANNEL);
		int g = TransformUtils.getChannelValue(rgb, CHANNEL_TYPE.GREEN_CHANNEL);
		int b = TransformUtils.getChannelValue(rgb, CHANNEL_TYPE.BLUE_CHANNEL);
		LOG.finest("TransformUtils.rgb2yuv R:"+r+", G:"+g+", B: "+b);

		/*int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		y = (y+128)>>8;
		int u = (int) ((b - y) * 0.492f);
		u=(u+128)>>8;
		int v = (int) ((r - y) * 0.877f);
		v=(v+128)>>8;
		y=y+16;u=u+128;v=v+128;
		LOG.fine("TransformUtils.rgb2yuv Y:"+y+", U:"+u+", V: "+v);
		int yuv = y;
		yuv = (yuv << 8) + u;
		yuv = (yuv << 8) + v;*/
		
		int y = ( (  66 * r + 129 * g +  25 * b + 128) >> 8) +  16;
		y = Math.min(255, y);
		y = Math.max(0, y);
		int u = ( ( -38 * r -  74 * g + 112 * b + 128) >> 8) + 128;
		u = Math.min(255, u);
		u = Math.max(0, u);
		int v = ( ( 112 * r -  94 * g -  18 * b + 128) >> 8) + 128;
		v = Math.min(255, v);
		v = Math.max(0, v);
		LOG.finest("TransformUtils.rgb2yuv Y:"+y+", U:"+u+", V: "+v);
		
		
		/*int C = y - 16;
		int D = u - 128;
		int E = v - 128;
		int R = (( 298 * C           + 409 * E + 128) >> 8);
		int G = (( 298 * C - 100 * D - 208 * E + 128) >> 8);
		int B = (( 298 * C + 516 * D           + 128) >> 8);
		LOG.fine("TransformUtils.rgb2yuv R:"+R+", G:"+G+", B: "+B);*/
		
		int finalVal = (y << 16) | (u << 8)  | (v & 0xFF);
		return finalVal;
	}
	
	public static int yuv2rgb(int yuv) {		
		int y = TransformUtils.getChannelValue(yuv, CHANNEL_TYPE.RED_CHANNEL);
		int u = TransformUtils.getChannelValue(yuv, CHANNEL_TYPE.GREEN_CHANNEL);
		int v = TransformUtils.getChannelValue(yuv, CHANNEL_TYPE.BLUE_CHANNEL);
		
		int C = y - 16;
		int D = u - 128;
		int E = v - 128;
		int r = (( 298 * C           + 409 * E + 128) >> 8);
		r = Math.min(255, r);
		r = Math.max(0, r);
		int g = (( 298 * C - 100 * D - 208 * E + 128) >> 8);
		g = Math.min(255, g);
		g = Math.max(0, g);
		int b = (( 298 * C + 516 * D           + 128) >> 8);
		b = Math.min(255, b);
		b = Math.max(0, b);
		LOG.finest("TransformUtils.rgb2yuv R:"+r+", G:"+g+", B: "+b);
		
		int finalVal = ((r << 16) | (g << 8) | (b & 0xFF));
		return finalVal;
	}
	
	/*public static int yuv2rgb(int yuv) {		
		int y = TransformUtils.getChannelValue(yuv, CHANNEL_TYPE.RED_CHANNEL);
		int u = TransformUtils.getChannelValue(yuv, CHANNEL_TYPE.GREEN_CHANNEL);
		int v = TransformUtils.getChannelValue(yuv, CHANNEL_TYPE.BLUE_CHANNEL);
		
		int r = (int) (y + v / 0.877f);
		int g = (int) (y - 0.395f * u -0.581f * v);
		int b = (int) (y + u / 0.492f);
		
		int finalVal = ((r << 16) | (g << 8) | b);
		return finalVal;
	}*/

	int rgb2ycbcr(int r, int g, int b, int[] ycbcr) {
		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int cb = (int) (-0.16874 * r - 0.33126 * g + 0.50000 * b);
		int cr = (int) (0.50000 * r - 0.41869 * g - 0.08131 * b);

		ycbcr[0] = y;
		ycbcr[1] = cb;
		ycbcr[2] = cr;
		return y<<16|cb<<8|cr&0xFF;
	}

	void rgb2hsb(int r, int g, int b, int[] hsb) {
		float[] hsbvals = new float[3];
		Color.RGBtoHSB(r, g, b, hsbvals);
	}

	void rgb2hmmd(int r, int g, int b, int[] hmmd) {

		double max = (int) Math.max(Math.max(r, g), Math.max(g, b));
		double min = (int) Math.min(Math.min(r, g), Math.min(g, b));
		double diff = (max - min);
		double sum = (double) ((max + min) / 2.);

		double hue = 0;
		if (diff == 0)
			hue = 0;
		else if (r == max && (g - b) > 0)
			hue = 60 * (g - b) / (max - min);
		else if (r == max && (g - b) <= 0)
			hue = 60 * (g - b) / (max - min) + 360;
		else if (g == max)
			hue = (double) (60 * (2. + (b - r) / (max - min)));
		else if (b == max)
			hue = (double) (60 * (4. + (r - g) / (max - min)));

		hmmd[0] = (int) (hue);
		hmmd[1] = (int) (max);
		hmmd[2] = (int) (min);
		hmmd[3] = (int) (diff);
	}

	private void rgb2hsl(int r, int g, int b, int hsl[]) {

		double var_R = (r / 255f);
		double var_G = (g / 255f);
		double var_B = (b / 255f);

		double var_Min; // Min. value of RGB
		double var_Max; // Max. value of RGB
		double del_Max; // Delta RGB value

		if (var_R > var_G) {
			var_Min = var_G;
			var_Max = var_R;
		} else {
			var_Min = var_R;
			var_Max = var_G;
		}

		if (var_B > var_Max)
			var_Max = var_B;
		if (var_B < var_Min)
			var_Min = var_B;

		del_Max = var_Max - var_Min;

		double H = 0, S, L;
		L = (var_Max + var_Min) / 2f;

		if (del_Max == 0) {
			H = 0;
			S = 0;
		} // gray
		else { // Chroma
			if (L < 0.5)
				S = del_Max / (var_Max + var_Min);
			else
				S = del_Max / (2 - var_Max - var_Min);

			double del_R = (((var_Max - var_R) / 6f) + (del_Max / 2f)) / del_Max;
			double del_G = (((var_Max - var_G) / 6f) + (del_Max / 2f)) / del_Max;
			double del_B = (((var_Max - var_B) / 6f) + (del_Max / 2f)) / del_Max;

			if (var_R == var_Max)
				H = del_B - del_G;
			else if (var_G == var_Max)
				H = (1 / 3f) + del_R - del_B;
			else if (var_B == var_Max)
				H = (2 / 3f) + del_G - del_R;
			if (H < 0)
				H += 1;
			if (H > 1)
				H -= 1;
		}
		hsl[0] = (int) (360 * H);
		hsl[1] = (int) (S * 100);
		hsl[2] = (int) (L * 100);
	}

	private void rgb2hsv(int r, int g, int b, int hsv[]) {

		int min; // Min. value of RGB
		int max; // Max. value of RGB
		int delMax; // Delta RGB value

		if (r > g) {
			min = g;
			max = r;
		} else {
			min = r;
			max = g;
		}
		if (b > max)
			max = b;
		if (b < min)
			min = b;

		delMax = max - min;

		double H = 0, S;
		double V = max;

		if (delMax == 0) {
			H = 0;
			S = 0;
		} else {
			S = delMax / 255f;
			if (r == max)
				H = ((g - b) / (double) delMax) * 60;
			else if (g == max)
				H = (2 + (b - r) / (double) delMax) * 60;
			else if (b == max)
				H = (4 + (r - g) / (double) delMax) * 60;
		}

		hsv[0] = (int) (H);
		hsv[1] = (int) (S * 100);
		hsv[2] = (int) (V * 100);
	}

	public void rgb2xyY(int R, int G, int B, int[] xyy) {
		// http://www.brucelindbloom.com

		double rf, gf, bf;
		double r, g, b, X, Y, Z;

		// RGB to XYZ
		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		if (r <= 0.04045)
			r = r / 12;
		else
			r = (double) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (double) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (double) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		double x;
		double y;

		double sum = X + Y + Z;
		if (sum != 0) {
			x = X / sum;
			y = Y / sum;
		} else {
			double Xr = 0.964221f; // reference white
			double Yr = 1.0f;
			double Zr = 0.825211f;

			x = Xr / (Xr + Yr + Zr);
			y = Yr / (Xr + Yr + Zr);
		}

		xyy[0] = (int) (255 * x + .5);
		xyy[1] = (int) (255 * y + .5);
		xyy[2] = (int) (255 * Y + .5);

	}

	public void rgb2xyz(int R, int G, int B, int[] xyz) {
		double rf, gf, bf;
		double r, g, b, X, Y, Z;

		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		if (r <= 0.04045)
			r = r / 12;
		else
			r = (double) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (double) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (double) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		xyz[1] = (int) (255 * Y + .5);
		xyz[0] = (int) (255 * X + .5);
		xyz[2] = (int) (255 * Z + .5);
	}

	public void rgb2lab(int R, int G, int B, int[] lab) {
		// http://www.brucelindbloom.com

		double r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
		double Ls, as, bs;
		double eps = 216.f / 24389.f;
		double k = 24389.f / 27.f;

		double Xr = 0.964221f; // reference white D50
		double Yr = 1.0f;
		double Zr = 0.825211f;

		// RGB to XYZ
		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		// assuming sRGB (D65)
		if (r <= 0.04045)
			r = r / 12;
		else
			r = (double) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (double) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (double) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		// XYZ to Lab
		xr = X / Xr;
		yr = Y / Yr;
		zr = Z / Zr;

		if (xr > eps)
			fx = (double) Math.pow(xr, 1 / 3.);
		else
			fx = (double) ((k * xr + 16.) / 116.);

		if (yr > eps)
			fy = (double) Math.pow(yr, 1 / 3.);
		else
			fy = (double) ((k * yr + 16.) / 116.);

		if (zr > eps)
			fz = (double) Math.pow(zr, 1 / 3.);
		else
			fz = (double) ((k * zr + 16.) / 116);

		Ls = (116 * fy) - 16;
		as = 500 * (fx - fy);
		bs = 200 * (fy - fz);

		lab[0] = (int) (2.55 * Ls + .5);
		lab[1] = (int) (as + .5);
		lab[2] = (int) (bs + .5);
	}

	public void rgb2luv(int R, int G, int B, int[] luv) {
		// http://www.brucelindbloom.com

		double rf, gf, bf;
		double r, g, b, X_, Y_, Z_, X, Y, Z, fx, fy, fz, xr, yr, zr;
		double L;
		double eps = 216.f / 24389.f;
		double k = 24389.f / 27.f;

		double Xr = 0.964221f; // reference white D50
		double Yr = 1.0f;
		double Zr = 0.825211f;

		// RGB to XYZ

		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		// assuming sRGB (D65)
		if (r <= 0.04045)
			r = r / 12;
		else
			r = (double) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (double) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (double) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		// XYZ to Luv

		double u, v, u_, v_, ur_, vr_;

		u_ = 4 * X / (X + 15 * Y + 3 * Z);
		v_ = 9 * Y / (X + 15 * Y + 3 * Z);

		ur_ = 4 * Xr / (Xr + 15 * Yr + 3 * Zr);
		vr_ = 9 * Yr / (Xr + 15 * Yr + 3 * Zr);

		yr = Y / Yr;

		if (yr > eps)
			L = (double) (116 * Math.pow(yr, 1 / 3.) - 16);
		else
			L = k * yr;

		u = 13 * L * (u_ - ur_);
		v = 13 * L * (v_ - vr_);

		luv[0] = (int) (2.55 * L + .5);
		luv[1] = (int) (u + .5);
		luv[2] = (int) (v + .5);
	}
	
	public static ComplexNum[] discreteFourierTransform(int[] original) {
		ComplexNum[] result = new ComplexNum[original.length];
		double realSum = 0, imgSum = 0;
		for(int frequency=0;frequency<original.length;frequency++) {
			realSum = 0;
			imgSum = 0;
			double temp = 2*Math.PI*frequency/original.length;
			for(int inputSignal=0; inputSignal<original.length; inputSignal++) {
				realSum += original[inputSignal] * Math.cos(temp*inputSignal);
				//imgSum += -original[inputSignal] * Math.sin(temp*inputSignal);
			}
			result[frequency] = new ComplexNum(realSum/original.length, imgSum/original.length);
		}
		return result;
	}
	
	public static ComplexNum[] inverseDiscreteFourierTransform(ComplexNum[] complexNums) {
		int len = complexNums.length;
		ComplexNum[] result = new ComplexNum[len];
		double realSum = 0, imgSum = 0;
		
		for(int outputSignal=0;outputSignal<len;outputSignal++) {
			realSum = 0;
			imgSum = 0;
			double temp = 2*Math.PI*outputSignal/len;
			for(int frequency=0; frequency<complexNums.length; frequency++) {
				double cos = Math.cos(temp*frequency);
				double sin = Math.sin(temp*frequency);
				realSum += complexNums[frequency].getReal() * cos
						-complexNums[frequency].getImaginary() * sin;
				imgSum += complexNums[frequency].getReal() * sin
						+complexNums[frequency].getImaginary() * cos;
			}
			result[outputSignal] = new ComplexNum(realSum, imgSum);
		}
		return result;
	}
	
	
	
}
