package com.yunzhijia.appdemo.mapper;

import java.util.List;

import com.yunzhijia.appdemo.pojo.ClockIn;

public interface ClockInMapper {
	
    int deleteByPrimaryKey(String clockinid);

    int insert(ClockIn record);

    int insertSelective(ClockIn record);

    ClockIn selectByPrimaryKey(String clockinid);
    
    List<ClockIn> selectByOutRecordId(String recordid);

    int updateByPrimaryKeySelective(ClockIn record);

    int updateByPrimaryKey(ClockIn record);
    
}