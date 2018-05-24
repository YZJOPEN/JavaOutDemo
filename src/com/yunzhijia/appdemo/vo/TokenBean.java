package com.yunzhijia.appdemo.vo;

import java.util.Date;

/**
 * accessToken bean
 * @author Administrator
 *
 */
public class TokenBean {
	private String accessToken;
	private int expireIn;
	private Date updateTime; // 更新时间
	private String scope;  //授权级别
	private String eid;
	
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
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
}