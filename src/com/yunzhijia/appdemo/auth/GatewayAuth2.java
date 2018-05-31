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
* 类描述：基于oauth2.0请求提交
*/
package com.yunzhijia.appdemo.auth;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.yunzhijia.appdemo.service.TokenService;
import com.yunzhijia.appdemo.util.HttpHelper;
import com.yunzhijia.appdemo.vo.UserContext;

@Component
public class GatewayAuth2 {
	private static final String APPLICATION_JSON = "application/json";
	private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	private static final int timeoutMillis = 3000;
	@Value("${APP.SECRET}")
	// 工作圈内用于erp同步的secret
	private String appSecret;
	@Value("${APP.ERPSECRET}")
	private String erpSecret;
	@Value("${YUNZHIJIA.GATEWAY.HOST}")
	private String gatewayHost;
	
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 校验用户上下文是否失效
	 * @param userContext
	 * @throws Exception
	 */
	public void checkValid(UserContext userContext) throws Exception {
		if(StringUtils.isEmpty(userContext.getOpenid()) && StringUtils.isEmpty(userContext.getEid()))
			throw new RuntimeException("ticket已失效！");
	}
	
	 /**
	 * 获取当前云之家单点登陆用户上下文信息
     * @param ticket
     * @param appId
     * @return
     * @throws Exception
     */
    public UserContext getUserContext(String ticket, String appId) throws Exception {
    	String scope = "app";
		String url = gatewayHost.concat("/ticket/user/acquirecontext?accessToken=").concat(tokenService.getAccessToken(appId, appSecret, null, scope));
		Map parm = new HashMap(2);
		parm.put("appid", appId); parm.put("ticket", ticket);
		Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_JSON);
		UserContext userContext = JSONObject.toJavaObject(JSONObject.parseObject(gatewayRequestJson(url, 
				JSONObject.toJSONString(parm))).getJSONObject("data"), UserContext.class);
		return userContext;
	}
    
    /**
     * 根据资源类型获取accessToken
     * @param appId
     * @param secret(appSecret或resGroupSecret)
     * @param eid
     * @param scope
     * @return
     * @throws Exception
     */
    @Deprecated
    public String getAccessToken(String appId, String secret, String eid, String scope) throws Exception {
    	//String nonceStr = Long.toString(Long.valueOf(String.valueOf(Math.random()).substring(2)), 36), 
    			//timestamp = String.valueOf(System.currentTimeMillis());
    	final String[] SCOPES = {"app", "team", "resGroupSecret"};
    	String timestamp = String.valueOf(System.currentTimeMillis());
    	Map parm = new HashMap(5);
    	parm.put("scope", scope); parm.put("timestamp", timestamp);
    	if(scope.equals(SCOPES[0])) {
    		parm.put("appId", appId);
    	} else if(scope.equals(SCOPES[1])) {
    		parm.put("eid", eid);
    	}if(scope.equals(SCOPES[2])) {
    		// 获取resGroupSecret秘钥
    		parm.put("eid", eid);
    		secret = erpSecret;
    	}
    	parm.put("secret", secret);
    	String url = gatewayHost.concat("/oauth2/token/getAccessToken");
    	return JSONObject.parseObject(gatewayRequestJson(url, JSONObject.toJSONString(parm))).getJSONObject("data").getString("accessToken");
    }
    
    /**
     * gateway发送json参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public static String gatewayRequestJson(String url, String parm) throws Exception {
		Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_JSON);
		return HttpHelper.post(headers, parm, url, timeoutMillis);
    }
    
    /**
     * gateway发送application/x-www-form-urlencoded参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public String gatewayRequest(String url, Map parm) throws Exception {
		Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_X_WWW_FORM_URLENCODED);
		return HttpHelper.post(headers, parm, url, timeoutMillis);
    }
}