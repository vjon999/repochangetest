package com.prapps.rail;

public class Stop implements Comparable<Stop> {

	private int id;
	private int trainId;
	private int stationId;
	private String code;
	private String desc;
	private String arr;
	private String dep;
	private String halt;
	private String dist;
	private String day;
	private int seq;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getTrainId() {
		return trainId;
	}
	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getArr() {
		return arr;
	}
	public void setArr(String arr) {
		this.arr = arr;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getHalt() {
		return halt;
	}
	public void setHalt(String halt) {
		this.halt = halt;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	@Override
	public int compareTo(Stop o) {
		return Integer.parseInt(dist.trim())-Integer.parseInt(o.getDist().trim());
	}
	@Override
	public String toString() {
		return "Stop [id=" + id + ", trainId=" + trainId + ", stationId=" + stationId + ", code=" + code + ", desc=" + desc + ", arrival=" + arr + ", dep="
				+ dep + ", halt=" + halt + ", dist=" + dist + ", day=" + day + ", seq=" + seq + "]";
	}
	
}
