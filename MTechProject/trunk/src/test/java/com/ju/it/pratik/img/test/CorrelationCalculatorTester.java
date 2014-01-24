package com.ju.it.pratik.img.test;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import org.junit.Test;

import com.ju.it.pratik.img.util.CorrelationCalculator;

public class CorrelationCalculatorTester {
	
	private static Logger LOG = Logger.getLogger(CorrelationCalculatorTester.class.getName());

	DecimalFormat decimalFormat = new DecimalFormat("###.##");
	CorrelationCalculator calculator = new CorrelationCalculator();
	
	@Test
	public void testGetCorrelation() {
		float[] x = new float[] {60,61,62,63,65};
		float[] y = new float[] {3.1f,3.6f,3.8f,4f,4.1f};
		float r = calculator.getCorrelationCoefficient(x, y);
		String result = decimalFormat.format(r);
		assert("0.91".equals(result));
		LOG.fine("correlation ratio: "+r);
	}
	
	@Test
	public void testCalcNormalizedCorrelationCoefficient() {
		int[] input1 = new int[] {1,2,3,4,5,6,7,8,9};
		int[] input2 = new int[] {2,4,6,8,10,12,14,16,18};
		double res = calculator.calcNormalizedCrossCorrelation(input1, input2);
		LOG.info("result: "+res);
		assert(res == 1);
	}
}
