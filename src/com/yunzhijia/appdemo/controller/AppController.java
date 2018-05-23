/**
* 版权所有：深圳云之家网络科技有限公司
* Copyright 2018 yunzhijia.com Inc.
* All right reserved. 
*====================================================
* 文件名称: AppController.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    May 23, 2018	wangzy, yz(创建:创建文件)
*====================================================
* 类描述：Controller 逻辑控制
*/
package com.yunzhijia.appdemo.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.yunzhijia.appdemo.controller.base.BaseController;
import com.yunzhijia.appdemo.service.AppService;
import com.yunzhijia.appdemo.service.OutRecordService;
import com.yunzhijia.appdemo.vo.UserContext;

@Controller
@RequestMapping("/app")
public class AppController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AppController.class);
	@Autowired
	private AppService appService;
	@Autowired
	private OutRecordService outRecordService;
	
	private enum REQUEST_TYPE {
		GETUSERINFO, SENDTODO, GETPERSON, GETORGPERSONS, GETCOMPANY, GETALLORGS, GETORG, TODOACTION, 
		LISTRECORDS, ADDRECORD, SELECTRECORDS, SELECTONERECORD, DELRECORD,ADDCLOCKIN,
		RECALLRECORD
	}
	@RequestMapping(value = "/request", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object doRequest(HttpServletRequest request) {
		Map<String, Object> response = this.initMessage();
		// 请求类型
		REQUEST_TYPE requestType = REQUEST_TYPE.valueOf(request.getParameter("reqName").toUpperCase());
		// 外出登记事由
		String outreason = request.getParameter("condition");
		String recordid = request.getParameter("recordid");
		UserContext userContext = (UserContext) request.getAttribute("userContext");
		try {
			switch (requestType) {
			case GETUSERINFO:
				response.put("data", appService.getUserInfo(userContext));
				break;
			case LISTRECORDS:
				response.put("data",appService.listOutRecords(userContext,request));
				break;
			case SELECTRECORDS:
				response.put("data",appService.selectRecords(outreason, userContext));
				break;
			case SELECTONERECORD:
				response.put("data",appService.selectOneRecord(userContext, recordid));
				break;
			case ADDRECORD:
				response.put("data",appService.addOutRecords(userContext,request));
				break;
			case ADDCLOCKIN:
				response.put("data",appService.addClockIn(userContext, request));
				break;
			case DELRECORD:
				appService.delRecord(recordid);
				break;
			case RECALLRECORD:
				response.put("data",appService.recallRecord(recordid));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			String errMsg = "信息处理异常！";
			if(StringUtils.isNotEmpty(e.getLocalizedMessage())) errMsg = e.getMessage();
			logger.error(errMsg, e);
			this.ERROR(errMsg, 1001, response);
		} finally {
			logger.info("结果输出：".concat(JSONObject.toJSONString(response)));
		}
		return response;
	}
}