package com.ju.it.pratik.img;

import java.util.HashMap;
import java.util.Map.Entry;

public class FeaturePoint implements Comparable<FeaturePoint> {

	private int x;
	private int y;
	private int center_x;
	private int center_y;
	private double value;
	private double centerDist;
	private HashMap<String, Integer> neighbours = new HashMap<String, Integer>();
	
	public FeaturePoint(int x,int y,int center_x,int center_y, double value) {
		this.center_x = center_x;
		this.center_y = center_y;
		this.x = x;
		this.y = y;
		this.value = value;
		centerDist = Math.round(Math.sqrt(Math.pow(this.center_x-x,2) + Math.pow(this.center_y-y,2)));
	}
	
	public boolean match(FeaturePoint featurePoint) {
		boolean match = true;
		if(centerDist != featurePoint.getCenterDist()) {
			match = false;
			return match;
		}
		else if(neighbours.isEmpty()) {
			match = false;
			return match;
		}
		System.out.println("---------------");
		for(Entry<String, Integer> entry : neighbours.entrySet()) {
			System.out.println(entry);
			if(!featurePoint.neighbours.containsKey(entry.getKey())) {
				return false;
			}
			System.out.println(entry +""+ featurePoint.neighbours.containsKey(entry.getKey()));
			if(!entry.getValue().equals(featurePoint.neighbours.get(entry.getKey()))) {
				return false;
			}
		}
		return match;
	}
	
	public double calcDist(FeaturePoint fp1, FeaturePoint fp2) {
		return Math.round(Math.sqrt(Math.pow(fp1.getX()-fp2.getX(),2) + Math.pow(fp1.getY()-fp2.getY(),2)));
	}
	
	public void addNeighbour(int nx, int ny, int value) {
		String key = Math.round(Math.sqrt(Math.pow(this.center_x-nx,2) + Math.pow(this.center_y-ny,2)))+"";
		key = key + "_" + Math.round(Math.sqrt(Math.pow(x-nx,2) + Math.pow(y-ny,2)));
		key += "_"+value;
		
		if(neighbours.containsKey(key)) {
			neighbours.put(key, (neighbours.get(key)+1));
		}
		else {
			neighbours.put(key, 1);
		}
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getCenterDist() {
		return centerDist;
	}
	public void setCenterDist(double centerDist) {
		this.centerDist = centerDist;
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

	public int getCenter_x() {
		return center_x;
	}

	public void setCenter_x(int center_x) {
		this.center_x = center_x;
	}
	
	@Override
	public int compareTo(FeaturePoint o) {
		if(value > o.getValue()) {
			return -1;
		}
		else if(value == o.getValue()) {
			return 0;
		}
		else {
			return 1;
		}
	}

	public int getCenter_y() {
		return center_y;
	}

	public void setCenter_y(int center_y) {
		this.center_y = center_y;
	}

	@Override
	public String toString() {
		return "FeaturePoint [x=" + x + ", y=" + y + ", value=" + value + "]";
	}
	
	
}
