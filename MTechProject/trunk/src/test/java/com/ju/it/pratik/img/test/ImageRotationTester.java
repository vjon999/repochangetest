package com.ju.it.pratik.img.test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.WaveletTransformer;

public class ImageRotationTester extends DefaultImageLoader {

	private static final Logger LOG = Logger.getLogger(ImageRotationTester.class.getName());
	private ImageUtils utils = new ImageUtils();
	DecimalFormat df = new DecimalFormat("##.#");
	
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
	
	@Test
	public void testGetRoationAngle() throws IOException {
		String src = WATERMARKED_IMAGES+"wavelet/lena_512_wm.jpg";
		String target = WATERMARKED_IMAGES+"wavelet/lena_512_wm_rotate_5.jpg";
		Image imgSrc = new Image(src);
		Image imgTarget = new Image(target);
		double[][] s = WaveletTransformer.discreteWaveletTransform(imgSrc.getU(), 5);
		double[][] t = WaveletTransformer.discreteWaveletTransform(imgTarget.getU(), 5);
		for(int i=16;i<32;i++) {
			for(int j=16;j<32;j++) {
				System.out.print(df.format(s[i][j])+"\t");//+"-"+df.format(t[i][j])+"  "
			}
			System.out.println();
		}
	}
}
