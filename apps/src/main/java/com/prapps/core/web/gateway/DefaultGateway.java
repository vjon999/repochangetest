package com.prapps.core.web.gateway;

import org.springframework.integration.annotation.Gateway;

import com.prapps.core.web.vo.AppRequest;

public interface DefaultGateway {
	
	@Gateway(requestChannel="inRequest")
	public void send(AppRequest appRequest);
}
