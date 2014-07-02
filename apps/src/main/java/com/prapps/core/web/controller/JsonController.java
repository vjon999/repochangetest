package com.prapps.core.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prapps.core.web.consts.KeyConsts;
import com.prapps.core.web.gateway.DefaultGateway;
import com.prapps.core.web.vo.AppRequest;

@Controller
@RequestMapping("/json")
@Transactional
public class JsonController {

	public static Logger LOG = Logger.getLogger(JsonController.class);
	@Autowired
	private DefaultGateway defaultGateway;
	
	@RequestMapping(value="/{action}", method={RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Object handleJsonRequest(@PathVariable String action, @RequestParam("appCode") String appCode, HttpServletRequest req) {
		LOG.info("action: "+action+"\tappCode: "+appCode);
		AppRequest appRequest = createAppRequest(req, action, appCode);
		Object response = defaultGateway.send(appRequest, appCode, action);
		LOG.debug("response: "+response);
		return response;
	}
	
	@RequestMapping(value="/*.jsp", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) {Object object=null;
		LOG.debug("handleRequest");
		System.out.println(object);
		return new ModelAndView("LoadCities.jsp");
	}
	
	public AppRequest createAppRequest(Object object) {
		AppRequest appRequest = new AppRequest();
		appRequest.add(KeyConsts.ROOT_OBJECT.toString(), object);
		return appRequest;
	}
	
	public AppRequest createAppRequest(HttpServletRequest req, String action, String appCode) {
		AppRequest appRequest = new AppRequest();
		appRequest.setAction(action);
		appRequest.setAppCode(appCode);
		for(Object key : req.getParameterMap().keySet()) {
			appRequest.add((String) key, req.getParameterMap().get(key));
		}
		return appRequest;
	}
}
