/**
* 版权所有：深圳云之家网络科技有限公司
* Copyright 2017 yunzhijia.com Inc.
* All right reserved. 
*====================================================
* 文件名称: BaseController.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    Dec 21, 2017	wangzy(创建:创建文件)
*====================================================
* 类描述：初始化接口返回数据结构体
*/
package com.yunzhijia.appdemo.controller.base;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseController {
	
	public Map<String, Object> initMessage(){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("success", true);
		map.put("error", null);
		map.put("errorCode", 100);
		map.put("data", null);
		return map;
	}
	
	/**
	 * 出错
	 * @param error
	 * @param errorCode
	 * @param map
	 */
	protected void ERROR(String error, int errorCode, Map<String, Object> map) {
		map.put("success", false);
		map.put("error", error);
		map.put("errorCode", errorCode);
	}
}