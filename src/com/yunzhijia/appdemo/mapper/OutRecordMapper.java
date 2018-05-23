package com.yunzhijia.appdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunzhijia.appdemo.pojo.OutRecord;

public interface OutRecordMapper {
	
    int deleteByPrimaryKey(String recordid);

    int insert(OutRecord record);

    int insertSelective(OutRecord record);

    OutRecord selectByPrimaryKey(@Param("recordid")String recordid,@Param("condition")Boolean condition);
    
    List<OutRecord> selectByReason(String outreason);
    
    List<OutRecord> selectByCondition(@Param("outreason")String outreason,@Param("condition")String condtion);
    
    int updateByPrimaryKeySelective(OutRecord record);

    int updateByPrimaryKey(OutRecord record);
    
    List<OutRecord> listOutRecords(@Param("openid")String openid, @Param("stage")Integer stage);
    
    List<OutRecord> listOutRecrdsAll();
    
    List<OutRecord> listOutRecordsAdmin(@Param("orgid")String orgid, @Param("stage")Integer stage,@Param("openid")String openid);
}