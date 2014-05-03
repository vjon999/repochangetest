package com.ju.it.pratik.img.util;

import java.text.DecimalFormat;

public class NoiseAnalysisResult {

	private double mse;
	private double psnr;
	private double psnrMax;
	private double snr;
	private double peak;
	private double ncc;
	private double ber;
	private DecimalFormat df = new DecimalFormat("##.##");
	
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
	
	public double getBer() {
		return ber;
	}
	public void setBer(double ber) {
		this.ber = ber;
	}
	@Override
	public String toString() {
		return "NoiseAnalysisResult [NCC: " + df.format(ncc) + " mse=" + df.format(mse) + ", "
				+ "psnr=" + df.format(psnr) + ", psnrMax=" + df.format(psnrMax) + ", snr=" + df.format(snr) + " ber: " + df.format(ber) + "]";
	}
}
