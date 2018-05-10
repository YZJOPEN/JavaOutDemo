package com.yunzhijia.appdemo.service;

import java.util.List;

import com.yunzhijia.appdemo.pojo.OutRecord;

public interface OutRecordService {
	/**
	 * 新增外出登记
	 * @param ouRecord
	 */
	void addoutRecord(OutRecord ouRecord);
	
	/**
	 * 新增外出登记
	 * @param ouRecord
	 */
	int updateoutRecord(OutRecord ouRecord);
	 /**
	  * 获取所有登记记录
	  * @return
	  */
	List<OutRecord> listOutRecrdsAll();
	/**
	 * 获取用户登记记录
	 * @param openid
	 * @param stage
	 * @return
	 */
	List<OutRecord> listOutRecrds(String openid,Integer stage);
	/**
	 * 部门负责获取登记记录
	 * @param orgid
	 * @param stage
	 * @param openid
	 * @return
	 */
	List<OutRecord> listOutRecordsAdmin(String orgid,Integer stage,String openid);
	/**
	 * 搜索
	 * @param reason
	 * @param orgid
	 * @return
	 */
	List<OutRecord> selectByReason(String outreason);
	
	
	List<OutRecord> selectByCondition(String outreason,String condition);
	/**
	 * 
	 * @param stage
	 * @return
	 */
	List<OutRecord> selectByTime(Integer stage);
	/**
	 * 
	 * @param recordid
	 * @param condition
	 * @return
	 */
	OutRecord selectByPrimaryKey(String recordid,Boolean condition);
	/**
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(OutRecord record);
	/**
	 * 删除记录
	 * @param recordid
	 * @return
	 */
	int delRecord(String recordid);
	
	/**
	 * 撤回记录
	 * @param recordid
	 * @return
	 */
	int recallRecord(OutRecord record);
}
