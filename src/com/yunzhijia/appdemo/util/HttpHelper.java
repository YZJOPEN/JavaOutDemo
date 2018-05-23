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
* 类描述：http 请求帮助类
*/
package com.yunzhijia.appdemo.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

public final class HttpHelper {
	private final static String UTF8 = "UTF-8";

	public static String post(Map<String, String> params, String url) throws Exception {
		return post(params, url, 0);
	}

	public static String post(Map<String, String> params, String url, int timeOutInMillis) throws Exception {
		return post(null, params, url, timeOutInMillis);
	}

	public static String post(Map<String, String> headers, Map<String, String> params, String url) throws Exception {
		return post(headers, params, url, 0);
	}

	public static String post(Map<String, String> headers, Map<String, String> params, String url, 
			int timeOutInMillis) throws Exception {
		HttpPost post = null;
		try {
			post = getHttpPost(headers, params, url, timeOutInMillis);
			return HttpClientHelper.getHttpClient().execute(post, UTF8);
		} finally {
			if(post != null) post.abort();
		}
	}

	private static HttpPost getHttpPost(Map<String, String> headers, Map<String, String> params, 
			String url, int timeOutInMillis) throws Exception {
		HttpPost post = null;
		post = new HttpPost(url);
		if(headers != null) {
			Set<String> set = headers.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()) {
				String key = it.next();
				post.addHeader(key, headers.get(key));
			}
		}
		if(params != null) {
			List<NameValuePair> uvp = new LinkedList<NameValuePair>();
			Set<String> set = params.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()) {
				String key = it.next();
				uvp.add(new BasicNameValuePair(key, params.get(key)));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(uvp, UTF8);
			post.setEntity(entity);
		}
		if(timeOutInMillis > 0) {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOutInMillis).setConnectTimeout(timeOutInMillis).build();
			post.setConfig(requestConfig);
		}
		return post;
	}

	public static String post(Map<String, String> headers, String jsonObject, String url, int timeOutInMillis) throws Exception {
		HttpPost post = null;
		try {
			post = new HttpPost(url);
			if(headers != null) {
				Set<String> set = headers.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String key = it.next();
					post.addHeader(key, headers.get(key));
				}
			}
			if(null == jsonObject || jsonObject.isEmpty()) throw new Exception("json参数为空！");
			StringEntity entity = new StringEntity(jsonObject, UTF8);
			if(timeOutInMillis > 0) {
				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOutInMillis).setConnectTimeout(timeOutInMillis).build();
				post.setConfig(requestConfig);
			}
			post.setEntity(entity);
			return HttpClientHelper.getHttpClient().execute(post, UTF8);
		} finally {
			if(post != null) post.abort();
		}
	}
}