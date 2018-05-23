/**
* 版权所有：深圳云之家网络科技有限公司
* Copyright 2017 yunzhijia.com Inc.
* All right reserved. 
*====================================================
* 文件名称: GatewayAuth2.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    Dec 21, 2017	wangzy(创建:创建文件)
*====================================================
* 类描述：获取用户上下文信息
*/
package com.yunzhijia.appdemo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.yunzhijia.appdemo.auth.GatewayAuth2;
import com.yunzhijia.appdemo.vo.UserContext;

public class ContextInterceptor implements HandlerInterceptor, ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		String ticket = arg0.getParameter("ticket"), appId = arg0.getParameter("appid");
		if(StringUtils.isEmpty(ticket) || StringUtils.isEmpty(appId)) throw new RuntimeException("ticket、appId必要参数不能为空！");
		UserContext userContext = null;
		try {
			userContext = applicationContext.getBean(GatewayAuth2.class).getUserContext(ticket, appId);
		} catch (Exception e) {
			throw new RuntimeException("用户上下文件获取异常，请检查APP.SECRET配置参数！");
		}
		arg0.setAttribute("userContext", userContext);
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}
}