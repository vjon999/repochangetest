package com.ju.it.pratik.img.util;

public class NoiseAnalysisResult {

	private double mse;
	private double psnr;
	private double psnrMax;
	private double snr;
	private double peak;
	private double ncc;
	
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
	
	
	public double getNcc() {
		return ncc;
	}
	public void setNcc(double ncc) {
		this.ncc = ncc;
	}
	@Override
	public String toString() {
		return "NoiseAnalysisResult [mse=" + mse + ", psnr=" + psnr + ", psnrMax=" + psnrMax + ", snr=" + snr + ", NCC: " + ncc + "]";
	}
}
