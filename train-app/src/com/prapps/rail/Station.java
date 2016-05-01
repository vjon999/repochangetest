package com.prapps.rail;

import java.util.Collection;

public class Station {

	private int id;
	private String code;
	private String name;
	private Collection<Station> nextStation;
	private Collection<Integer> trains;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return name;
	}
	public void setDesc(String desc) {
		this.name = desc;
	}
	public Collection<Station> getNextStation() {
		return nextStation;
	}
	public void setNextStation(Collection<Station> nextStation) {
		this.nextStation = nextStation;
	}
	public Collection<Integer> getTrains() {
		return trains;
	}
	public void setTrains(Collection<Integer> trains) {
		this.trains = trains;
	}
	@Override
	public String toString() {
		return "Station [id=" + id + ", code=" + code + ", name=" + name + "]";
	}
	
}
