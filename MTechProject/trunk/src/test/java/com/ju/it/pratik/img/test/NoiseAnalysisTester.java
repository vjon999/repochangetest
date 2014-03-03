package com.ju.it.pratik.img.test;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import com.ju.it.pratik.img.WMConsts;
import com.ju.it.pratik.img.util.NoiseAnalysisResult;
import com.ju.it.pratik.img.util.NoiseAnalysisUtil;

public class NoiseAnalysisTester implements WMConsts {

	private static Logger LOG = Logger.getLogger(NoiseAnalysisTester.class.getName());
	
	@Test
	public void computeNoiseAnalysis() throws IOException {
		NoiseAnalysisUtil util = new NoiseAnalysisUtil();
		/*String src = RESOURCE_IMAGES + LENA;
		String watermarked = WATERMARKED_IMAGES + "wavelet/lena_512_wm.bmp";*/
		String src = WATERMARK_LOGO;
		String watermarked = WATERMARKED_IMAGES + "dct/recovered/lena_512_wm_20.bmp";
		NoiseAnalysisResult result = util.calculatePSNR(src, watermarked);
		LOG.fine(result+"");
	}
}
