package com.ju.it.pratik.img.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.ju.it.pratik.img.util.TransformUtils;

public class WaveletTransformTester {

	private static final Logger LOG = Logger.getLogger(WaveletTransformTester.class.getName());
	
	private int[] sampleInput;
	
	@Before
	public void setUp() {
		sampleInput = new int[64];
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/freqInput.txt")));
			for(int i=0;i<64;i++) {
				sampleInput[i] = Integer.parseInt(reader.readLine());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("failed to setUp the testcase");
		}
	}
	
	@Test
	public void testForwardAndInverseDWT() throws IOException {
		double[] dwt = TransformUtils.discreteWaveletTransform(sampleInput);		
		int[] idwt = TransformUtils.inverseDiscreteWaveletTransform(dwt);
		for(int i=0;i<64;i++) {
			if(idwt[i] != sampleInput[i]) {
				LOG.log(Level.SEVERE, "expected "+sampleInput[i]+" but actual: "+idwt[i]);
				throw new AssertionError("expected "+sampleInput[i]+" but actual: "+idwt[i]);
			}
		}
	}
}
