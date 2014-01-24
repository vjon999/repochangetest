package com.ju.it.pratik.img.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.ju.it.pratik.img.util.ImageUtils;

public class ImageRotationTester extends DefaultImageLoader {

	private static final Logger LOG = Logger.getLogger(ImageRotationTester.class.getName());
	private ImageUtils utils = new ImageUtils();
	
	@Test
	public void testImageRotation() {
		int[] orig = new int[]{0,1,2,3,7,6,5,4,8,9,10,11,15,14,13,12};
		int[] expecteds = new int[]{15,8,7,0,14,9,6,1,13,10,5,2,12,11,4,3};
		
		int[] result = utils.rotate(orig, 4, 4);
		LOG.fine("result: "+Arrays.toString(result));
		
		Assert.assertArrayEquals(expecteds, result);
	}
	
	@Test
	public void rotateImage() {
		int[] result = utils.rotate(rgb, imageWidth, imageHeight);
		try {
			utils.saveRGBImage(imageHeight, imageWidth, result, OUTPUT_FOLDER+LENA_ROTATED_90_WM);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
