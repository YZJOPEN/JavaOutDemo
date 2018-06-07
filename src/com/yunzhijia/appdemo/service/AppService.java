/**
* 版权所有：深圳云之家网络科技有限公司
* Copyright 2018 yunzhijia.com Inc.
* All right reserved. 
*====================================================
* 文件名称: BaseController.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    May 20, 2018	yz(创建:创建文件)
*====================================================
* 类描述：外出登记业务逻辑实现
*/

package com.yunzhijia.appdemo.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzhijia.appdemo.auth.GatewayAuth2;
import com.yunzhijia.appdemo.pojo.ClockIn;
import com.yunzhijia.appdemo.pojo.OutRecord;
import com.yunzhijia.appdemo.util.MobileRandomNum;
import com.yunzhijia.appdemo.vo.UserContext;

@Service
public class AppService {
	@Autowired
	private GatewayAuth2 gatewayAuth2;
	@Autowired
	private TokenService tokenService;
	@Autowired 
	private OutRecordService outRecordService;
	@Autowired
	private ClockInService clockInService;
	@Value("${YUNZHIJIA.GATEWAY.HOST}")
	private String gatewayHost;
	@Value("${LOCAL.HOST}")
	private String localHost;
	@Value("${APP.SECRET}")
	private String appSecret;
	@Value("${APP.ERPSECRET}")
	private String erpSecret;
	
	/**
	 * 获取用户信息
	 * 注意：手机号系用户隐私信息，故此处手机号用于随机产生，正式投入使用时，请联合通讯录API以及云之家通讯录同步秘钥获取当前用户手机号，参考方法getErpPerson()
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject getUserInfo(UserContext userContext) throws Exception {
		String phone = MobileRandomNum.getRandomMobile();
		JSONObject personInfo = this.getPerson(userContext);
		JSONObject orgInfo = this.getOrg(userContext, personInfo.getString("orgId"));
		boolean isDeptInCharge = isDeptInCharge(userContext, orgInfo.getJSONArray("inChargers"));
		gatewayAuth2.checkValid(userContext);
		JSONObject ret = new JSONObject();
		ret.put("openId", userContext.getOpenid());
		ret.put("userId", userContext.getUserid());
		ret.put("userName", personInfo.getString("name"));
		ret.put("deptName", orgInfo.getString("name"));
		ret.put("isDeptInCharge", isDeptInCharge);
		ret.put("phone", phone);
		ret.put("inChargers", orgInfo.getJSONArray("inChargers"));
		return ret;
	}

	/**
	 * 获取外出登记记录
	 * @return
	 * @throws Exception
	 */
	public JSONObject listOutRecords(UserContext userContext,HttpServletRequest request) throws Exception {
		gatewayAuth2.checkValid(userContext);
		String stage = request.getParameter("stage");
		String choice = request.getParameter("choice"); // 区分我的（1）团队（0）
		Integer stageTime = stage!=null ? Integer.parseInt(stage) : 0;//时间
		List<OutRecord> outRecords = new ArrayList<OutRecord>();
		JSONObject jsonObject = this.getPerson(userContext);
		jsonObject = this.getOrg(userContext, jsonObject.getString("orgId"));
		boolean isDeptInCharge = isDeptInCharge(userContext, jsonObject.getJSONArray("inChargers"));
		String orgId = jsonObject.getString("orgId");
		String openId = userContext.getOpenid();
		if (isDeptInCharge) {
			if (choice!=null && choice.equals("0")) {
				outRecords = outRecordService.listOutRecrds(openId,stageTime);
			}else {
				outRecords = outRecordService.listOutRecordsAdmin(orgId,stageTime,openId);
			}
		}else {
			outRecords = outRecordService.listOutRecrds(openId,0);
		}
		JSONObject rets = new JSONObject();
		JSONArray ja=new JSONArray();
		Iterator<OutRecord> it = outRecords.iterator(); 
		while(it.hasNext()) {
			JSONObject ret = new JSONObject();
			OutRecord outRecord = (OutRecord) it.next();
			ret.put("recordid", outRecord.getRecordid());
			ret.put("openid", outRecord.getOpenid());
			ret.put("username", outRecord.getUsername());
			ret.put("outtime", outRecord.getOuttime());
			ret.put("backtime", outRecord.getBacktime());
			ret.put("outreason", outRecord.getOutreason());
			ret.put("photoUrl", outRecord.getPhotoUrl());
			ret.put("isMyRecord", outRecord.getOpenid().equals(userContext.getOpenid()));//区分管理员和其他成员
			ret.put("state", outRecord.getState());
			ja.add(ret);
		}
		rets.put("isDeptInCharge", isDeptInCharge);
		rets.put("outrecords", ja);
		return rets;
	}
	
	/**
	 * 登记删除
	 * @param recordid 外出登记ID
	 * @return
	 * @throws Exception
	 */
	public JSONObject delRecord(String recordid) throws Exception {
		JSONObject ret = new JSONObject();
		int deleted  = 0;
		try {
			deleted = outRecordService.delRecord(recordid);
		} catch (Exception e) {
			ret.put("deleted", String.valueOf(deleted));
		}
		return ret;
	}
	
	/**
	 * 登记撤回
	 * @param recordid 外出登记ID
	 * @return
	 * @throws Exception
	 */
	public JSONObject recallRecord(String recordid) throws Exception {
		JSONObject ret = new JSONObject();
		int recalled  = 0;
		boolean hasClockIns = false;
		try {
			List<ClockIn> clockIns = clockInService.selectByOutRecordId(recordid);
			hasClockIns = !clockIns.isEmpty();
			if (!hasClockIns) {
				OutRecord outRecord = outRecordService.selectByPrimaryKey(recordid, hasClockIns);
				outRecord.setState(0);//撤回记录
				recalled = outRecordService.recallRecord(outRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ret.put("hasClockIns", hasClockIns);
			ret.put("recalled", String.valueOf(recalled));
		}
		return ret;
	}
	
	/**
	 * 外出登记
	 * @param userContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JSONObject addOutRecords(UserContext userContext, HttpServletRequest request) throws Exception {
		JSONObject ret = new JSONObject();
		OutRecord outRecord = new OutRecord();
		String recordId = request.getParameter("recordId");
		String outreason = request.getParameter("outreason");
		// for mysql
//		String outtime = request.getParameter("outtime");
//		String backtime = request.getParameter("backtime");
		// to match h2 db
		String outtime = request.getParameter("outtime").concat(":00");
		String backtime = request.getParameter("backtime").concat(":00");
		JSONObject jsonObject = this.getPerson(userContext);
		String photoUrl = jsonObject.getString("photoUrl").concat("&spec=100");
		jsonObject = this.getOrg(userContext, jsonObject.getString("orgId"));
		String orgId = jsonObject.getString("orgId");
		String deptname = jsonObject.getString("name");
		String phone = request.getParameter("phone");
		int updated = 0;
		outRecord.setUsername(userContext.getUsername());
		outRecord.setOrgid(orgId);
		outRecord.setOpenid(userContext.getOpenid());
		outRecord.setPhotoUrl(photoUrl);
		try {
			if (recordId!=null) {
				outRecord = outRecordService.selectByPrimaryKey(recordId, false);
				outRecord.setState(1);
				outRecord.setOuttime(outtime);
				outRecord.setBacktime(backtime);
				outRecord.setOutreason(outreason);
				outRecord.setDeptname(deptname);
				outRecord.setContact(phone);
				updated = outRecordService.updateoutRecord(outRecord);
			}else {
				recordId = UUID.randomUUID().toString();
				outRecord.setState(1);
				outRecord.setRecordid(recordId);
				outRecord.setOuttime(outtime);
				outRecord.setBacktime(backtime);
				outRecord.setOutreason(outreason);
				outRecord.setDeptname(deptname);
				outRecord.setContact(phone);
				outRecordService.addoutRecord(outRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("updated", updated);
		ret.put("recordId", recordId);
		return ret;
	}
	
	/**
	 * 签到
	 * @param userContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JSONObject addClockIn(UserContext userContext, HttpServletRequest request) throws Exception {
		String position = request.getParameter("position");
		String recordid = request.getParameter("recordid");
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String clockintime = dateFormat.format(date);
		String clockInId = UUID.randomUUID().toString();
		ClockIn clockIn = new ClockIn();
		clockIn.setClockinid(clockInId);
		clockIn.setState(1);
		clockIn.setClockintime(clockintime );
		clockIn.setPosition(position);
		clockIn.setRecordId(recordid);
		try {
			clockInService.addClockIn(clockIn);
		} catch (Exception e) {
		}
		JSONObject ret = new JSONObject();
		ret.put("clockInId", clockInId);
		ret.put("clockIn", clockIn);
		return ret;
	}
	
	/**
	 * 查看登记
	 * @param outreason
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject selectRecords(String outreason, UserContext userContext) throws Exception {
		gatewayAuth2.checkValid(userContext);
		JSONObject jsonObject = this.getPerson(userContext);
		jsonObject = this.getOrg(userContext, jsonObject.getString("orgId"));
		boolean isDeptInCharge = isDeptInCharge(userContext, jsonObject.getJSONArray("inChargers"));
		String orgId = jsonObject.getString("orgId");
		String openId = userContext.getOpenid();
		List<OutRecord> outRecords = new ArrayList<OutRecord>();;
		if (isDeptInCharge) {
			// 传入orgId
			outRecords = outRecordService.selectByCondition(outreason,orgId);
		}else {
			// 传入openId
			outRecords = outRecordService.selectByCondition(outreason,openId);
		}
		JSONObject rets = new JSONObject();
		JSONArray ja=new JSONArray();
		Iterator<OutRecord> it = outRecords.iterator();
		while(it.hasNext()) {
			JSONObject ret = new JSONObject();
			OutRecord outRecord = (OutRecord) it.next();
			ret.put("recordid", outRecord.getRecordid());
			ret.put("username", outRecord.getUsername());
			ret.put("outtime", outRecord.getOuttime());
			ret.put("backtime", outRecord.getBacktime());
			ret.put("outreason", outRecord.getOutreason());
			ret.put("photoUrl", outRecord.getPhotoUrl());
			ret.put("isMyRecord", outRecord.getOpenid().equals(userContext.getOpenid()));//区分管理员和其他成员
			ret.put("state", outRecord.getState());
			ja.add(ret);
		}
		rets.put("outrecords", ja);
		return rets;
	}
	/**
	 * 判断当前部门负责人
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public boolean isDeptInCharge(UserContext userContext, JSONArray jsonArray) throws Exception {
		boolean isDeptInCharge  = false;
		List<String> chargeOpenIds = new ArrayList<>();
		for(int i=0; i<jsonArray.size(); i++) {
			JSONObject joObject = (JSONObject) jsonArray.getJSONObject(i);
			chargeOpenIds.add(joObject.getString("openId"));
		}
		String openId = userContext.getOpenid();
		isDeptInCharge = chargeOpenIds.contains(openId);
		return isDeptInCharge;
	}
	
	/**
	 * 查看某条记录详情
	 * @param userContext
	 * @param recordid
	 * @return
	 * @throws Exception
	 */
	public JSONObject selectOneRecord(UserContext userContext,String recordid) throws Exception {
		List<ClockIn> clockIns = clockInService.selectByOutRecordId(recordid);
		boolean hasClockIns = !clockIns.isEmpty();
		JSONObject ret = new JSONObject();
		OutRecord outRecord = outRecordService.selectByPrimaryKey(recordid,hasClockIns);
		if (outRecord!=null) {
			ret.put("username", outRecord.getUsername());
			ret.put("openid", outRecord.getOpenid());
			ret.put("photoUrl", outRecord.getPhotoUrl());
			ret.put("outtime", outRecord.getOuttime());
			ret.put("backtime", outRecord.getBacktime());
			ret.put("outreason", outRecord.getOutreason());
			ret.put("phone", outRecord.getContact());
			ret.put("deptname", outRecord.getDeptname());
			ret.put("clonkIns", outRecord.getClockIns());
		}
		return ret;
	}
	
	@Deprecated
	public void todoAction(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		String url = gatewayHost.concat("/newtodo/open/action.json?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		String appId = userContext.getAppid(), openId = userContext.getOpenid();
		Map actiontype = new HashMap(3);
        actiontype.put("read", 1); actiontype.put("deal", 1);
        Map parameters = new HashMap(4);
        parameters.put("sourcetype", appId); parameters.put("sourceitemid", "jzyj2eeappdemo");
        List<String> openIds = new ArrayList<String>(1);
        openIds.add(openId); parameters.put("openids", openIds);
        parameters.put("actiontype", actiontype);
        System.out.println(JSONObject.toJSONString(parameters));
        gatewayAuth2.gatewayRequestJson(url, JSONObject.toJSONString(parameters));
	}
	
	@Deprecated
	public void sendTodo(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		JSONObject jsonObject = this.getPerson(userContext);
		String url = gatewayHost.concat("/newtodo/open/generatetodo.json?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		String appId = userContext.getAppid(), openId = userContext.getOpenid();
		Map parameters = new HashMap(9);
        Map status = new HashMap(2);
        status.put("READ", 0); status.put("DO", 0);
        Map target = new HashMap();
        target.put("openId", openId); target.put("status", status);
        List<Map> targets = new ArrayList<Map>(1);
        targets.add(target);
        parameters.put("sourceId", "jzyjavaoutdemo"); 
        parameters.put("appId", appId);
        parameters.put("senderId", openId);        
        parameters.put("content", "[轻应用Demo演示]".concat(jsonObject.getString("name")).concat("的外出登记需要您审批"));        
        parameters.put("url", localHost.concat("/approvalstatus.html?t=approval-result"));
        parameters.put("title", "轻应用Demo演示");
        parameters.put("itemtitle", null);
        parameters.put("headImg", "https://yunzhijia.com/appsys/download.action?filename=".concat(appId).concat(".png&type=9"));
        parameters.put("params", targets);
        System.out.println(JSONObject.toJSONString(parameters));
        gatewayAuth2.gatewayRequestJson(url, JSONObject.toJSONString(parameters));
	}
	
	/**
	 * 获取用户数据
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject getPerson(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		String url = gatewayHost.concat("/opendata-control/data/getperson?accessToken=")
				.concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		Map parameters = new HashMap(2);
		parameters.put("openId", userContext.getOpenid());
		parameters.put("eid", userContext.getEid());
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONArray("data").getJSONObject(0);
	}
	
	/**
	 * 获取ERP数据
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject getErpPerson(UserContext userContext) throws Exception {
		String scope = "resGroupSecret";
		List<String> openIds = new ArrayList<String>();
		openIds.add(userContext.getOpenid());
		gatewayAuth2.checkValid(userContext);
		String url = gatewayHost.concat("/openimport/open/person/get?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), erpSecret, userContext.getEid(), scope));
		Map parameters = new HashMap(2); 
		parameters.put("eid", userContext.getEid());
		JSONObject jo=new JSONObject();
		jo.put("eid", userContext.getEid());
		jo.put("type", 1);
		JSONArray ja=new JSONArray();
		ja.add(userContext.getOpenid());
		jo.put("array", ja);
		parameters.put("eid", userContext.getEid());
		parameters.put("nonce", UUID.randomUUID().toString());
		parameters.put("data", jo.toString());
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONArray("data").getJSONObject(0);
	}
	
	/**
	 * 
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject getOrgPersons(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		JSONObject jsonObject = this.getPerson(userContext);
		String url = gatewayHost.concat("/opendata-control/data/getorgpersons?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		Map parameters = new HashMap(2); parameters.put("orgId", jsonObject.getString("orgId"));
		parameters.put("eid", userContext.getEid()); parameters.put("begin", "0");
		parameters.put("count", "10");
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONObject("data");
	}
	
	public JSONObject getCompany(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		JSONObject jsonObject = this.getPerson(userContext);
		String url = gatewayHost.concat("/opendata-control/data/getcompany?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		Map parameters = new HashMap(2); parameters.put("orgId", jsonObject.getString("orgId"));
		parameters.put("eid", userContext.getEid());
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONObject("data");
	}
	
	public JSONArray getAllOrgs(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		String url = gatewayHost.concat("/opendata-control/data/getallorgs?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		Map parameters = new HashMap(2); parameters.put("eid", userContext.getEid());
		parameters.put("begin", "0"); parameters.put("count", "10");
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONArray("data");
	}
	
	/**
	 * 
	 * @param userContext
	 * @return
	 * @throws Exception
	 */
	public JSONObject getOrg(UserContext userContext, String orgId) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		String url = gatewayHost.concat("/opendata-control/data/getorg?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		if(StringUtils.isEmpty(orgId)) throw new RuntimeException("该员工要先移动到部门^^");
		Map parameters = new HashMap(2); parameters.put("orgId", orgId);
		parameters.put("eid", userContext.getEid());
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONObject("data");
	}
	
	public JSONObject getparentperson(UserContext userContext) throws Exception {
		String scope = "app";
		gatewayAuth2.checkValid(userContext);
		String url = gatewayHost.concat("/opendata-control/data/getparentperson?accessToken=").concat(tokenService.getAccessToken(userContext.getAppid(), appSecret, userContext.getEid(), scope));
		Map parameters = new HashMap(2); parameters.put("openId", userContext.getOpenid());
		parameters.put("eid", userContext.getEid());
		String ret = gatewayAuth2.gatewayRequest(url, parameters);
		return JSONObject.parseObject(ret).getJSONObject("data");
	}
}