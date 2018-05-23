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
* 类描述：http请求帮助类
*/
package com.yunzhijia.appdemo.util;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpClientHelper {
	private static Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);
	private static HttpClientHelper instance = null;
	private static Lock lock = new ReentrantLock();
	private CloseableHttpClient httpClient;

	private HttpClientHelper() {
		instance = this;
	}

	public static HttpClientHelper getHttpClient() {
		if(instance == null) {
			lock.lock();
			try {
				if(instance == null) {
					instance = new HttpClientHelper();
					instance.init();
				}
			} finally {
				lock.unlock();
			}
		}
		return instance;
	}

	private final static int MAX_TOTEL = 1000;
	private final static int MAX_CONNECTION_PER_ROUTE = 100;

	private void init() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(MAX_TOTEL);
		cm.setDefaultMaxPerRoute(MAX_CONNECTION_PER_ROUTE);
		httpClient = HttpClientBuilder.create().setConnectionManager(cm).build();
	}

	public byte[] executeAndReturnByte(HttpRequestBase request) {
		HttpEntity entity = null;
		CloseableHttpResponse resp = null;
		byte[] rtn = new byte[0];
		if(request == null) return rtn;
		try {
			lock.lock();
			try {
				if(httpClient == null) init();
			} finally {
				lock.unlock();
			}
			if(httpClient == null) {
				logger.error("{}\nreturn error {}", request.getURI().toString(), "httpclient连接获取异常！");
				return rtn;
			}
			resp = httpClient.execute(request);
			entity = resp.getEntity();
			int statusCode = resp.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				logger.info("{}\nreturn correctly httpstatus code:{}", request.getURI().toString(), statusCode);
				String encoding = ("" + resp.getFirstHeader("Content-Encoding")).toLowerCase();
				if(encoding.indexOf("gzip") > 0) entity = new GzipDecompressingEntity(entity);
				rtn = EntityUtils.toByteArray(entity);
			} else if(statusCode == 400) {
				rtn = EntityUtils.toByteArray(entity);
				logger.error("{}\nreturn error httpstatus code:{}", request.getURI().toString(), statusCode);
			} else {
				logger.error("{}\nreturn error httpstatus code:{}", request.getURI().toString(), statusCode);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			EntityUtils.consumeQuietly(entity);
			if(resp != null) {
				try {
					resp.close();
				} catch (Exception e) {
					logger.error("httpclient连接释放异常！", e);
				}
			}
		}
		return rtn;
	}

	public String execute(HttpRequestBase request, String charset) throws UnsupportedEncodingException {
		byte[] bytes = executeAndReturnByte(request);
		if(bytes == null || bytes.length == 0) return null;
		return new String(bytes, charset);
	}
}