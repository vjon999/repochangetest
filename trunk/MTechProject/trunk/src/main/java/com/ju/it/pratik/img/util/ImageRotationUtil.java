package com.ju.it.pratik.img.util;

import java.io.IOException;

import com.ju.it.pratik.img.Image;

public class ImageRotationUtil {

	public ImageRotationUtil(int[] input, int height, int width) {
		this.pixels = new int[input.length];
		System.arraycopy(input, 0, this.pixels, 0, input.length);
		this.height = height;
		this.width = width;
	}
	
	public ImageRotationUtil(Image image) {
		this(image.getRgb(), image.getHeight(), image.getWidth());
	}

	private final int width, height;
	private int[] pixels;

	public int[] rotate(double angle) {
		final double radians = Math.toRadians(angle), cos = Math.cos(radians), sin = Math.sin(radians);
		final int[] pixels2 = new int[pixels.length];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				final int centerx = this.width / 2;
				final int centery = this.height / 2;
				final int m = x - centerx;
				final int n = y - centery;
				final int j = ((int) (m * cos + n * sin)) + centerx;
				final int k = ((int) (n * cos - m * sin)) + centery;
				
				if (j >= 0 && j < width && k >= 0 && k < this.height)
					pixels2[(y * width + x)] = pixels[(k * width + j)];
			}
		System.arraycopy(pixels2, 0, pixels, 0, pixels.length);
		return pixels;
	}

	/*public ImageRotationUtil print() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				System.out.print(pixels[width * y + x]);
			System.out.println();
		}
		System.out.println();
		return this;
	}*/

	public static void main(String[] args) throws IOException {
		System.out.println(Math.atan2(2.5, 5)*360/Math.PI);
		/*BufferedImage bufImg = ImageIO.read(new File(WMConsts.WATERMARKED_IMAGES + "wavelet/lena_512_wm.bmp"));
		int pixels[] = new int[bufImg.getHeight() * bufImg.getWidth()];
		int h = bufImg.getHeight();
		int w = bufImg.getWidth();
		bufImg.getRGB(0, 0, w, h, pixels, 0, w);
		ImageRotationUtil i = new ImageRotationUtil(pixels, h, w);
		i.rotate(10);
		ImageUtils.saveImage(i.pixels, w, h, new File("test.bmp"), "bmp");*/
	}
}
