package com.ju.it.pratik.img.test;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.ju.it.pratik.img.FeaturePoint;
import com.ju.it.pratik.img.Image;
import com.ju.it.pratik.img.util.CorrelationCalculator;
import com.ju.it.pratik.img.util.ImageMatcher;
import com.ju.it.pratik.img.util.ImageRotationUtil;
import com.ju.it.pratik.img.util.ImageUtils;
import com.ju.it.pratik.img.util.Sobel;

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
	public void testGetRoationAngle1() throws IOException {
		String src = WATERMARKED_IMAGES+"wavelet/lena_512_wm.jpg";
		String target = WATERMARKED_IMAGES+"wavelet/lena_512_wm_rotate_7.jpg";
		int threshold = 200;
		Image imgSrc = new Image(src);
		Image imgTarget = new Image(target);
		Sobel edgeDetector = new Sobel();		
		edgeDetector.setThreshold(threshold);
		
		int[] gradImgSrc = new int[imgSrc.getWidth()*imgSrc.getHeight()];
		edgeDetector.init(imgSrc.getBlue(), imgSrc.getWidth(), imgSrc.getHeight());
		gradImgSrc = edgeDetector.process();
		ImageUtils.saveImage(gradImgSrc, imgSrc.getWidth(), imgSrc.getWidth(), new File("src/test/resources/test1.bmp"), "BMP");
		edgeDetector.init(imgTarget.getBlue(), imgTarget.getWidth(), imgTarget.getHeight());
		int[] gradImgTarget = edgeDetector.process();
		ImageUtils.saveImage(gradImgTarget, imgTarget.getWidth(), imgTarget.getWidth(), new File("src/test/resources/test2.bmp"), "BMP");		
		int angle = new ImageMatcher().getBestRotationMatch(gradImgSrc, imgSrc.getHeight(), imgSrc.getWidth(), gradImgTarget, imgTarget.getHeight(), imgTarget.getWidth());
	}
	
	@Test
	public void testGetRoationAngle() throws IOException {
		String src = WATERMARKED_IMAGES+"wavelet/lena_512_wm.jpg";
		String target = WATERMARKED_IMAGES+"wavelet/lena_512_wm_rotate_5.jpg";
		int threshold = 200;
		Image imgSrc = new Image(src);
		Image imgTarget = new Image(target);
		/*double[][] s = WaveletTransformer.discreteWaveletTransform(imgSrc.getU(), 4);
		double[][] t = WaveletTransformer.discreteWaveletTransform(imgTarget.getU(), 4);
		for(int i=32;i<64;i++) {
			for(int j=32;j<64;j++) {
				System.out.print(df.format(t[i][j])+"\t");//+"-"+df.format(t[i][j])+"  "
			}
			System.out.println();
		}*/
		Sobel edgeDetector = new Sobel();
		edgeDetector.setThreshold(threshold);
		int[] gradImgSrc = new int[imgSrc.getWidth()*imgSrc.getHeight()];
		edgeDetector.init(imgSrc.getBlue(), imgSrc.getWidth(), imgSrc.getHeight());
		gradImgSrc = edgeDetector.process();
		List<FeaturePoint> srcPoints = new ArrayList<FeaturePoint>();
		FeaturePoint fp;int pixelValue;
		for(int i=0;i<imgSrc.getHeight();i++) {
			for(int j=0;j<imgSrc.getWidth();j++) {
				pixelValue = gradImgSrc[i*imgSrc.getWidth()+j]&0xFF;
				if(pixelValue > threshold/5) {
					fp = new FeaturePoint(j, i, imgSrc.getWidth()/2, imgSrc.getHeight()/2, pixelValue);
					srcPoints.add(fp);
					//System.out.println(fp);
				}
			}
		}
		/*double max = 0;int x=0,y=0;
		Location[] locSrc = new Location[imgSrc.getWidth()*imgSrc.getHeight()];
		
		double d;
		for(int i=0;i<imgSrc.getHeight();i++) {
			for(int j=0;j<imgSrc.getWidth();j++) {
				locSrc[i*imgSrc.getWidth()+j] = new Location(i,j,edgeDetector.getDirection()[i*imgSrc.getWidth()+j]);
				d = edgeDetector.getDirection()[i*imgSrc.getWidth()+j];
			}
		}
		Arrays.sort(locSrc);*/
		ImageUtils.saveImage(gradImgSrc, imgSrc.getWidth(), imgSrc.getWidth(), new File("src/test/resources/test1.bmp"), "BMP");
		List<FeaturePoint> tPoints = new ArrayList<FeaturePoint>();
		edgeDetector.init(imgTarget.getBlue(), imgTarget.getWidth(), imgTarget.getHeight());
		int[] gradImgTarget = edgeDetector.process();
		System.out.println("-------------------------------------------------------");
		for(int i=0;i<imgSrc.getHeight();i++) {
			for(int j=0;j<imgSrc.getWidth();j++) {
				pixelValue = gradImgTarget[i*imgSrc.getWidth()+j]&0xFF; 
				if(pixelValue > threshold/5) {
					fp = new FeaturePoint(j, i, imgSrc.getWidth()/2, imgSrc.getHeight()/2, pixelValue);
					tPoints.add(fp);
					//System.out.println(fp);
				}
			}
		}
		/*Location[] locTarget = new Location[imgSrc.getWidth()*imgSrc.getHeight()];
		max = 0;
		x=0;
		y=0;
		for(int i=0;i<imgSrc.getHeight();i++) {
			for(int j=0;j<imgSrc.getWidth();j++) {
				locTarget[i*imgSrc.getWidth()+j] = new Location(i,j,edgeDetector.getDirection()[i*imgSrc.getWidth()+j]);
				d = edgeDetector.getDirection()[i*imgSrc.getWidth()+j];
			}
		}
		Arrays.sort(locTarget);
		
		
		for(int i=0;i<locSrc.length;i++) {
			System.out.println(locSrc[i]+"\t"+locTarget[i]);
		}*/
		ImageUtils.saveImage(gradImgTarget, imgTarget.getWidth(), imgTarget.getWidth(), new File("src/test/resources/test2.bmp"), "BMP");
		
		for(FeaturePoint f : srcPoints) {
			int fx = f.getX();
			int fy = f.getY();
			for(int a=fy-3;a<fy+4;a++) {
				for(int b=fx-3;b<fx+4;b++) {
					if(a >= 0 && b >= 0 && a<imgSrc.getHeight() && b < imgSrc.getWidth()) {
						int pv = gradImgSrc[a*imgSrc.getWidth()+b]&0xFF;
						if(pv > threshold/5)
							f.addNeighbour(a, b, pv);
					}
				}
			}
		}
		
		for(FeaturePoint f : tPoints) {
			int fx = f.getX();
			int fy = f.getY();
			for(int a=fy-3;a<fy+4;a++) {
				for(int b=fx-3;b<fx+4;b++) {
					if(a >= 0 && b >= 0 && a<imgTarget.getHeight() && b < imgTarget.getWidth()) {
						int pv = gradImgTarget[a*imgTarget.getWidth()+b]&0xFF;
						if(pv > threshold/5)
							f.addNeighbour(a, b, pv);
					}
				}
			}
		}
		
		/*Collections.sort(srcPoints);
		Collections.sort(tPoints);*/
		for(int i=0;i<srcPoints.size();i++) {
			//System.out.println(srcPoints.get(i)+"\t\t"+tPoints.get(i));
			for(int j=0;j<tPoints.size();j++) {
				if(srcPoints.get(i).match(tPoints.get(j))) {
					System.err.println(srcPoints.get(i)+"\t"+tPoints.get(j));
				}
			}
		}
	}
}
