package com.ju.it.pratik.img.util;

public class NoiseAnalysisResult {

	private double mse;
	private double psnr;
	private double psnrMax;
	private double snr;
	private double peak;
	
	public double getSnr() {
		return snr;
	}
	public void setSnr(double snr) {
		this.snr = snr;
	}
	public double getMse() {
		return mse;
	}
	public void setMse(double mse) {
		this.mse = mse;
	}
	public double getPsnr() {
		return psnr;
	}
	public void setPsnr(double psnr) {
		this.psnr = psnr;
	}
	public double getPsnrMax() {
		return psnrMax;
	}
	public void setPsnrMax(double psnrMax) {
		this.psnrMax = psnrMax;
	}
	
	public String toString() {
		return "MSE: " + mse +"\tSNR: " + snr + "\tPSNR: " + psnr + "\tPSNR(max=" + peak +") "+ psnrMax;
	}
}
