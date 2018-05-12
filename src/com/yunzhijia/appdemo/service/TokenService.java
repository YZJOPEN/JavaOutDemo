package com.yunzhijia.appdemo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunzhijia.appdemo.auth.GatewayAuth2;
import com.yunzhijia.appdemo.dao.TokenDao;
import com.yunzhijia.appdemo.util.JsonUtil;
import com.yunzhijia.appdemo.vo.TokenBean;

/**
 * 通用accessToken
 * @author
 */
@Service
public class TokenService {
	
	private Logger logger = LoggerFactory.getLogger(TokenService.class);
	private static final String APPLICATION_JSON = "application/json";
	private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	private static final int timeoutMillis = 3000;
	@Value("${APP.SECRET}")
	private String appSecret;
	
	@Value("${APP.ERPSECRET}")
	private String erpSecret;
	
	@Value("${YUNZHIJIA.GATEWAY.HOST}")
	private String gatewayHost;
	
	@Autowired
	private TokenDao tokenDao;
	
	
	/**
	 * 获取accessToken
	 * @author
	 * @return
	 */
	public String getAccessToken(String appId, String secret, String eid, String scope) {
		TokenBean tokenBean = tokenDao.getToken();
		// 判断当前token是否在有效期内
		if (tokenBean!=null && tokenBean.getAccessToken()!=null && scope.equals(tokenBean.getScope())) { 
			if((System.currentTimeMillis()-tokenBean.getUpdateTime().getTime())/1000 < (tokenBean.getExpireIn()-300)){
				logger.debug("返回有效期内的access_token: {}", tokenBean.getAccessToken());
				return tokenBean.getAccessToken();
			}
		}
		// 如果没有token信息或者已经过期, 重新从api获取
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
		
		logger.debug("获取access_token请求地址: {}", url);
		String result = null;
		try {
			result = JSONObject.parseObject(GatewayAuth2.gatewayRequestJson(url, JSONObject.toJSONString(parm))).getJSONObject("data").toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取access_token信息失败!, 返回null");
		}
		
		logger.debug("获取access_token返回数据: {}", result);
		tokenBean = JsonUtil.toObject(result, TokenBean.class);
		if (tokenBean!=null && tokenBean.getAccessToken()!=null) {
			tokenBean.setUpdateTime(new Date());
			tokenBean.setScope(scope);
			tokenDao.setToken(tokenBean); // 缓存获取的token信息
			logger.debug("返回新获取的access_token: {}", tokenBean.getAccessToken());
			return tokenBean.getAccessToken();
		}
		logger.error("获取access_token信息失败!, 返回null");
		return null;
	}
	
}
