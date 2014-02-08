package com.ju.it.pratik.img.test;

import java.util.logging.Logger;

import org.junit.Test;

import com.ju.it.pratik.img.util.MatrixTransformer;

public class MatrixTransformerTest {

	private static final Logger LOG = Logger.getLogger(MatrixTransformerTest.class.getName());
	
	@Test
	public void testMatrixTransform() {
		int[][] input =  new int[][]{{1,2,3},{4,5,6},{7,8,9}};
		MatrixTransformer.transpose(input);
		LOG.fine(PrintUtil.print2DArray(input));
		MatrixTransformer.transpose(input);
		LOG.fine(PrintUtil.print2DArray(input));
	}
}
