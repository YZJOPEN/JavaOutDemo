package com.yunzhijia.appdemo.dao;

import org.springframework.stereotype.Repository;

import com.yunzhijia.appdemo.vo.TokenBean;

/**
 * 单应用获取token可临时将token存储在内存
 * 如果多应用同时获取token会出现抢占失效问题
 * 可存储于共享缓存或数据库中
 * @author Administrator
 */
@Repository
public class TokenDao {
	
	/** 缓存Token信息 */
	private TokenBean tokenBean;

	
	/**
	 * 获取Token信息
	 * @param appid
	 * @return
	 */
	public TokenBean getToken(){
		return tokenBean;
	}
	
	/**
	 * 缓存新获得的token
	 * @param tokenBean
	 * @return
	 */
	public void setToken(TokenBean tokenBean){
		this.tokenBean = tokenBean;
	}
}
