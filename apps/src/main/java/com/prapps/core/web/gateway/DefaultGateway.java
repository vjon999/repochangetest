package com.prapps.core.web.gateway;

import org.springframework.integration.annotation.Header;

import com.prapps.core.web.vo.AppRequest;

public interface DefaultGateway {
	
	/*@Gateway(requestChannel="inRequest")*/
	public Object send(AppRequest appRequest, @Header("appCode") String appCode, @Header("action") String action);
}
