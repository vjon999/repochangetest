package com.ju.it.pratik.img;

public class Location implements Comparable<Location> {

	private int x;
	private int y;
	private double value;
	
	public Location() {}
	
	public Location(int y, int x) {
		this.x = x;
		this.y = y;
	}
	
	public Location(int y, int x, double value) {
		this(y,x);
		this.value = value;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public int compareTo(Location o) {
		if(value < o.getValue()) {
			return 1;
		}
		else if(value > o.getValue()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + ", value=" + value + "]";
	}
	
}
