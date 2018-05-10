package com.yunzhijia.appdemo.service;

import java.util.List;

import com.yunzhijia.appdemo.pojo.ClockIn;

public interface ClockInService {
	/**
	 * 新增签到记录
	 * @param clockIn
	 */
	void addClockIn(ClockIn clockIn);
	
	/**
	 * 获取签到记录
	 * @param recordid 外出登记ID
	 * @return
	 */
	List<ClockIn> selectByOutRecordId(String recordid);
}
