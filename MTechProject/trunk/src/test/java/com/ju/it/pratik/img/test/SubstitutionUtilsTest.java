package com.ju.it.pratik.img.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ju.it.pratik.img.util.SubstitutionUtils;

public class SubstitutionUtilsTest {

	private SubstitutionUtils substitutionUtils = new SubstitutionUtils();
	
	@Test
	public void testRijndaelForwardSubstitute() {
		assertEquals(99, substitutionUtils.rijndaelForwardSubstitute(0));
		/*assertEquals(0, substitutionUtils.rijndaelReverseSubstitute(99));*/
		
		assertEquals(124, substitutionUtils.rijndaelForwardSubstitute(20));
		/**assertEquals(20, substitutionUtils.rijndaelReverseSubstitute(124));*/
	}
	
	@Test
	public void testShiftColumns() {
		int[][] input = new int[][] {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
		int[][] expectedOutput = new int[][]{{1,2,3,4},{8,5,6,7},{11,12,9,10},{14,15,16,13}};
		int output[][] = substitutionUtils.shiftColumns(input);
		for(int i=0;i<output.length;i++) {
			for(int j=0;j<output[i].length;j++) {
				System.out.print(output[i][j]+",");
				assertEquals(expectedOutput[i][j], output[i][j]);
			}
			System.out.println();
		}
	}
	
	@Test
	public void testInvShiftColumns() {
		int[][] input = new int[][]{{1,2,3,4},{8,5,6,7},{11,12,9,10},{14,15,16,13}};
		int[][] expectedOutput= new int[][] {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
		int output[][] = substitutionUtils.invShiftColumns(input);
		for(int i=0;i<output.length;i++) {
			for(int j=0;j<output[i].length;j++) {
				System.out.print(output[i][j]+",");
				assertEquals(expectedOutput[i][j], output[i][j]);
			}
			System.out.println();
		}
	}
}
