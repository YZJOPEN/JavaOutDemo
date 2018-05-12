package com.yunzhijia.appdemo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenBean {
	
	@JsonProperty("accessToken")
	private String accessToken;
	@JsonProperty("expireIn")
	private int expireIn;
	private Date updateTime; // 更新时间
	private String scope;  //授权级别
	
	
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public int getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
