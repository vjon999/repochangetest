package com.pratik.img.util;

import java.awt.Color;

import com.pratik.img.CHANNEL_TYPE;
import com.pratik.img.ComplexNum;

public class TransformUtils {

	public static final float[][] RGB_TO_YUV_MATRIX = new float[][] { { 0.299f, 0.587f, 0.114f }, { -0.14713f, -0.28886f, 0.436f },
			{ -0.615f, -0.51499f, 0.10001f }, };

	public static final float[][] YUV_TO_RGB_MATRIX = new float[][] { { 1, 0, 1.13983f }, { 1, -0.39465f, 0.58060f }, { 1f, 2.03211f, 0 }, };

	public static int convertRGBToYUV(int pixel) {
		int yuv[] = new int[3];
		CHANNEL_TYPE[] channelTypes = { CHANNEL_TYPE.RED_CHANNEL, CHANNEL_TYPE.GREEN_CHANNEL, CHANNEL_TYPE.BLUE_CHANNEL };
		int r = getChannelValue(pixel, channelTypes[0]);
		int g = getChannelValue(pixel, channelTypes[1]);
		int b = getChannelValue(pixel, channelTypes[2]);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(RGB_TO_YUV_MATRIX[i][j] + "*" + getChannelValue(pixel, channelTypes[j]) + " = " + RGB_TO_YUV_MATRIX[i][j]
						* ((float) getChannelValue(pixel, channelTypes[j])) + " / ");
				yuv[i] += RGB_TO_YUV_MATRIX[i][j] * ((float) getChannelValue(pixel, channelTypes[j]));
			}
			System.out.println(" Result=" + yuv[i]);
		}
		int finalVal = ((((pixel & 0xFF000000) | (yuv[0] << 16)) | (yuv[1] << 8)) | yuv[2]);
		return finalVal;
	}

	/**
	 * @param pixel
	 * @return
	 */
	public static int convertYUVToRGB(int pixel) {
		int rgb[] = new int[3];
		CHANNEL_TYPE[] channelTypes = { CHANNEL_TYPE.Y, CHANNEL_TYPE.U, CHANNEL_TYPE.V };

		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 3; j++) {
				System.out.print(YUV_TO_RGB_MATRIX[i][j] + "*" + getChannelValue(pixel, channelTypes[j]) + " = " + YUV_TO_RGB_MATRIX[i][j]
						* ((float) getChannelValue(pixel, channelTypes[j])) + " / ");
				rgb[i] += YUV_TO_RGB_MATRIX[i][j] * ((float) getChannelValue(pixel, channelTypes[j]));
			}
			System.out.println(rgb[i]);
		}

		int finalVal = ((((pixel & 0xFF000000) | (rgb[0] << 16)) | (rgb[1] << 8)) | rgb[2]);
		return finalVal;
	}

	public static int getChannelValue(int pixel, CHANNEL_TYPE channelType) {
		return (pixel >> channelType.getChannelType()) & 0x000000FF;

	}

	public static void rgb2yuv(int r, int g, int b, int[] yuv) {
		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int u = (int) ((b - y) * 0.492f);
		int v = (int) ((r - y) * 0.877f);

		yuv[0] = y;
		yuv[1] = u;
		yuv[2] = v;
	}

	public static void rgb2yuv(int rgb, int[] yuv) {
		int r = TransformUtils.getChannelValue(rgb, CHANNEL_TYPE.RED_CHANNEL);
		int g = TransformUtils.getChannelValue(rgb, CHANNEL_TYPE.GREEN_CHANNEL);
		int b = TransformUtils.getChannelValue(rgb, CHANNEL_TYPE.BLUE_CHANNEL);

		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int u = (int) ((b - y) * 0.492f);
		int v = (int) ((r - y) * 0.877f);

		yuv[0] = y;
		yuv[1] = u;
		yuv[2] = v;
	}

	void rgb2ycbcr(int r, int g, int b, int[] ycbcr) {
		int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		int cb = (int) (-0.16874 * r - 0.33126 * g + 0.50000 * b);
		int cr = (int) (0.50000 * r - 0.41869 * g - 0.08131 * b);

		ycbcr[0] = y;
		ycbcr[1] = cb;
		ycbcr[2] = cr;
	}

	void rgb2hsb(int r, int g, int b, int[] hsb) {
		float[] hsbvals = new float[3];
		Color.RGBtoHSB(r, g, b, hsbvals);
	}

	void rgb2hmmd(int r, int g, int b, int[] hmmd) {

		float max = (int) Math.max(Math.max(r, g), Math.max(g, b));
		float min = (int) Math.min(Math.min(r, g), Math.min(g, b));
		float diff = (max - min);
		float sum = (float) ((max + min) / 2.);

		float hue = 0;
		if (diff == 0)
			hue = 0;
		else if (r == max && (g - b) > 0)
			hue = 60 * (g - b) / (max - min);
		else if (r == max && (g - b) <= 0)
			hue = 60 * (g - b) / (max - min) + 360;
		else if (g == max)
			hue = (float) (60 * (2. + (b - r) / (max - min)));
		else if (b == max)
			hue = (float) (60 * (4. + (r - g) / (max - min)));

		hmmd[0] = (int) (hue);
		hmmd[1] = (int) (max);
		hmmd[2] = (int) (min);
		hmmd[3] = (int) (diff);
	}

	private void rgb2hsl(int r, int g, int b, int hsl[]) {

		float var_R = (r / 255f);
		float var_G = (g / 255f);
		float var_B = (b / 255f);

		float var_Min; // Min. value of RGB
		float var_Max; // Max. value of RGB
		float del_Max; // Delta RGB value

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

		float H = 0, S, L;
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

			float del_R = (((var_Max - var_R) / 6f) + (del_Max / 2f)) / del_Max;
			float del_G = (((var_Max - var_G) / 6f) + (del_Max / 2f)) / del_Max;
			float del_B = (((var_Max - var_B) / 6f) + (del_Max / 2f)) / del_Max;

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

		float H = 0, S;
		float V = max;

		if (delMax == 0) {
			H = 0;
			S = 0;
		} else {
			S = delMax / 255f;
			if (r == max)
				H = ((g - b) / (float) delMax) * 60;
			else if (g == max)
				H = (2 + (b - r) / (float) delMax) * 60;
			else if (b == max)
				H = (4 + (r - g) / (float) delMax) * 60;
		}

		hsv[0] = (int) (H);
		hsv[1] = (int) (S * 100);
		hsv[2] = (int) (V * 100);
	}

	public void rgb2xyY(int R, int G, int B, int[] xyy) {
		// http://www.brucelindbloom.com

		float rf, gf, bf;
		float r, g, b, X, Y, Z;

		// RGB to XYZ
		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		if (r <= 0.04045)
			r = r / 12;
		else
			r = (float) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (float) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (float) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		float x;
		float y;

		float sum = X + Y + Z;
		if (sum != 0) {
			x = X / sum;
			y = Y / sum;
		} else {
			float Xr = 0.964221f; // reference white
			float Yr = 1.0f;
			float Zr = 0.825211f;

			x = Xr / (Xr + Yr + Zr);
			y = Yr / (Xr + Yr + Zr);
		}

		xyy[0] = (int) (255 * x + .5);
		xyy[1] = (int) (255 * y + .5);
		xyy[2] = (int) (255 * Y + .5);

	}

	public void rgb2xyz(int R, int G, int B, int[] xyz) {
		float rf, gf, bf;
		float r, g, b, X, Y, Z;

		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		if (r <= 0.04045)
			r = r / 12;
		else
			r = (float) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (float) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (float) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		xyz[1] = (int) (255 * Y + .5);
		xyz[0] = (int) (255 * X + .5);
		xyz[2] = (int) (255 * Z + .5);
	}

	public void rgb2lab(int R, int G, int B, int[] lab) {
		// http://www.brucelindbloom.com

		float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
		float Ls, as, bs;
		float eps = 216.f / 24389.f;
		float k = 24389.f / 27.f;

		float Xr = 0.964221f; // reference white D50
		float Yr = 1.0f;
		float Zr = 0.825211f;

		// RGB to XYZ
		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		// assuming sRGB (D65)
		if (r <= 0.04045)
			r = r / 12;
		else
			r = (float) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (float) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (float) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		// XYZ to Lab
		xr = X / Xr;
		yr = Y / Yr;
		zr = Z / Zr;

		if (xr > eps)
			fx = (float) Math.pow(xr, 1 / 3.);
		else
			fx = (float) ((k * xr + 16.) / 116.);

		if (yr > eps)
			fy = (float) Math.pow(yr, 1 / 3.);
		else
			fy = (float) ((k * yr + 16.) / 116.);

		if (zr > eps)
			fz = (float) Math.pow(zr, 1 / 3.);
		else
			fz = (float) ((k * zr + 16.) / 116);

		Ls = (116 * fy) - 16;
		as = 500 * (fx - fy);
		bs = 200 * (fy - fz);

		lab[0] = (int) (2.55 * Ls + .5);
		lab[1] = (int) (as + .5);
		lab[2] = (int) (bs + .5);
	}

	public void rgb2luv(int R, int G, int B, int[] luv) {
		// http://www.brucelindbloom.com

		float rf, gf, bf;
		float r, g, b, X_, Y_, Z_, X, Y, Z, fx, fy, fz, xr, yr, zr;
		float L;
		float eps = 216.f / 24389.f;
		float k = 24389.f / 27.f;

		float Xr = 0.964221f; // reference white D50
		float Yr = 1.0f;
		float Zr = 0.825211f;

		// RGB to XYZ

		r = R / 255.f; // R 0..1
		g = G / 255.f; // G 0..1
		b = B / 255.f; // B 0..1

		// assuming sRGB (D65)
		if (r <= 0.04045)
			r = r / 12;
		else
			r = (float) Math.pow((r + 0.055) / 1.055, 2.4);

		if (g <= 0.04045)
			g = g / 12;
		else
			g = (float) Math.pow((g + 0.055) / 1.055, 2.4);

		if (b <= 0.04045)
			b = b / 12;
		else
			b = (float) Math.pow((b + 0.055) / 1.055, 2.4);

		X = 0.436052025f * r + 0.385081593f * g + 0.143087414f * b;
		Y = 0.222491598f * r + 0.71688606f * g + 0.060621486f * b;
		Z = 0.013929122f * r + 0.097097002f * g + 0.71418547f * b;

		// XYZ to Luv

		float u, v, u_, v_, ur_, vr_;

		u_ = 4 * X / (X + 15 * Y + 3 * Z);
		v_ = 9 * Y / (X + 15 * Y + 3 * Z);

		ur_ = 4 * Xr / (Xr + 15 * Yr + 3 * Zr);
		vr_ = 9 * Yr / (Xr + 15 * Yr + 3 * Zr);

		yr = Y / Yr;

		if (yr > eps)
			L = (float) (116 * Math.pow(yr, 1 / 3.) - 16);
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
	
	public static float[] discreteWaveletTransform(int[] input) {
		float[] output = new float[input.length];
		float[] dblInput = new float[input.length];
		for(int i=0;i<input.length;i++) {
			dblInput[i] = input[i];
		}
		
		for(int len = dblInput.length>>1; len>=1; len >>= 1) {
			float mean=0, diff=0;
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
	
	public static int[] inverseDiscreteWaveletTransform(float[] input) {
		float[] output = new float[input.length];
		System.arraycopy(input, 0, output, 0, input.length);
		for(int len = 1;len <= input.length>>1; len *= 2) {			
			float sum=0, diff=0;
			for(int i=0;i<len; i++) {
				sum = input[i] + input[len+i];
				diff = input[i] - input[len+i];
				output[2*i] = sum;
				output[2*i+1] = diff;
			}
			System.arraycopy(output, 0, input, 0, input.length);
			
		}
		int[] finalOutput = new int[output.length];
		for(int i=0;i<output.length;i++) {
			finalOutput[i] = (int) output[i];
		}
		return finalOutput;
	}
}
