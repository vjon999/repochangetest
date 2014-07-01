package com.prapps.core.web.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.prapps.core.web.consts.KeyConsts;

public class AppRequest implements Serializable {
	
	private String appCode;
	private String action;
	private Map<String,Object> context;
	
	public AppRequest() {
		context = new HashMap<String, Object>();
	}
	
	public AppRequest(Object object) {
		this();
		add(KeyConsts.ROOT_OBJECT.toString(), object);
	}
	
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void add(String key, Object value) {
		context.put(key, value);
	}
	
	public void remove(String key) {
		context.remove(key);
	}

	@Override
	public String toString() {
		return "AppRequest [appCode=" + appCode + ", action=" + action + ", context=" + context + "]";
	}
	
}
