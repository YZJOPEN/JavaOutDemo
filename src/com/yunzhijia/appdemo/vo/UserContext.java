/**
* 版权所有：深圳云之家网络科技有限公司
* Copyright 2018 yunzhijia.com Inc.
* All right reserved. 
*====================================================
* 文件名称: BaseController.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    May 20, 2018	wangzy,yz(创建:创建文件)
*====================================================
* 类描述：用户上下文信息缓存类
*/
package com.yunzhijia.appdemo.vo;

public class UserContext {
	private String appid;
	private String eid;
	private String openid;
	private String username;
	private String uid;
	private String tid;
	private String userid;
	
	public String getAppid() {
		return appid;
	}
	
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getEid() {
		return eid;
	}
	
	public void setEid(String eid) {
		this.eid = eid;
	}
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getTid() {
		return tid;
	}
	
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
}