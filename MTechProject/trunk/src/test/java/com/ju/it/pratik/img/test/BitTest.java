package com.ju.it.pratik.img.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.WatermarkUtils;

public class BitTest {

	@Test
	public void test() {
		
		System.out.println(Integer.toBinaryString(Float.floatToIntBits(5.0f)));
		System.out.println(Integer.toBinaryString(3));
		int f1 = (Float.floatToRawIntBits(5.0f) & 0x007fffff);
		System.out.println(Integer.toBinaryString(f1));
		
		System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(5.0f)));
		System.out.println(Integer.toBinaryString((Float.floatToRawIntBits(5.0f)) >> 23));
	}
	
	@Test
	public void testImageToBit() throws IOException {
		BufferedImage bufImg = ImageIO.read(new File(WMConsts.WATERMARK_LOGO));
		int[] img = new int[bufImg.getHeight()*bufImg.getWidth()];
		bufImg.getRGB(0, 0, bufImg.getWidth(), bufImg.getHeight(), img, 0, bufImg.getWidth());
		WatermarkUtils util = new WatermarkUtils();
		String watermark = util.readWatermark(img);
		System.out.println(watermark.length());
		System.out.println(watermark);
	}
}
