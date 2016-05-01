package com.prapps.rail;

import java.util.Collection;

public class Train {

	private String id;
	private String from;
	private String to;
	private String classes;
	private String rundays;
	private String type;
	private String arr;
	private String dep;
	private Collection<Stop> route;
	
	public Train() {}
	public Train(String id, String from, String to) {
		this.id = id;
		this.from = from;
		this.to = to;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Collection<Stop> getRoute() {
		return route;
	}
	public void setRoute(Collection<Stop> route) {
		this.route = route;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getRundays() {
		return rundays;
	}
	public void setRundays(String rundays) {
		this.rundays = rundays;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	@Override
	public String toString() {
		return "Train [id=" + id + ", from=" + from + ", to=" + to + ", classes=" + classes + ", rundays=" + rundays + ", type=" + type + ", arr=" + arr
				+ ", dep=" + dep + ", route=" + route + "]";
	}
	
}
