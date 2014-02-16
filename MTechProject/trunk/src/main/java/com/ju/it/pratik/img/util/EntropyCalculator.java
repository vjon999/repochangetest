package com.ju.it.pratik.img.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class EntropyCalculator {

	static DecimalFormat decimalFormat = new DecimalFormat("##.#");

	@SuppressWarnings("boxing")
	public static double getShannonEntropy(double[][] arr, int startX, int startY, int blockSize) {
		Map<Double, Integer> occ = new HashMap<>();
		double value;
		for (int i = startY; i < startY + blockSize; i++) {
			for (int j = startX; j < startX + blockSize; j++) {
				value = Double.parseDouble(decimalFormat.format(arr[i][j]));
				// value = Math.round(arr[j]*100)/100;
				if (occ.containsKey(value)) {
					occ.put(arr[i][j], occ.get(value) + 1);
				} else {
					occ.put(value, 1);
				}
			}
		}

		// calculate the entropy
		Double result = 0.0;
		for (Double sequence : occ.keySet()) {
			Double frequency = (double) occ.get(sequence) / blockSize * blockSize;
			result -= frequency * (Math.log(frequency) / Math.log(2));
		}

		return result;
	}
}
