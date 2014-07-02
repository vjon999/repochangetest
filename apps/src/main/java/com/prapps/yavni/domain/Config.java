package com.prapps.yavni.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MASTER_CONFIG")
public class Config {

	@Id
	@Column(name="ID")
	private long id;
	@Column(name="DESCRIPTION")
	private String desc;
	@Column(name="CODE")
	private String code;
	@Column(name="CONFIG_TYPE")
	private String type;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
