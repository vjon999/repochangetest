package com.pratik.img;

public class ComplexNum {

	private double real;
	private double imaginary;
	
	public ComplexNum() {}
	
	public ComplexNum(double real) {
		this.real = real;
	}
	
	public ComplexNum(double real, double imaginary) {
		this(real);
		this.imaginary = imaginary;
	}
	
	public double getReal() {
		return real;
	}
	public void setReal(double real) {
		this.real = real;
	}
	public double getImaginary() {
		return imaginary;
	}
	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
	
	public String toString() {
		return "("+real+","+imaginary+")";
	}
}
