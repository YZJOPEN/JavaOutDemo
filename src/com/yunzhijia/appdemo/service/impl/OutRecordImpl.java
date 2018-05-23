package com.yunzhijia.appdemo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunzhijia.appdemo.mapper.OutRecordMapper;
import com.yunzhijia.appdemo.pojo.OutRecord;
import com.yunzhijia.appdemo.service.OutRecordService;

@Service("outRecordService")
public class OutRecordImpl implements OutRecordService {
	
	@Autowired
	private OutRecordMapper outRecordMapper;
	
	@Override
	public void addoutRecord(OutRecord ouRecord) {
		outRecordMapper.insertSelective(ouRecord);
	}

	@Override
	public List<OutRecord> listOutRecrds(String openid, Integer stage) {
		return outRecordMapper.listOutRecords(openid,stage);
	}
	
	@Override
	public List<OutRecord> listOutRecrdsAll() {
		return outRecordMapper.listOutRecrdsAll();
	}
	
	@Override
	public List<OutRecord> selectByReason(String outreason) {
		return outRecordMapper.selectByReason(outreason);
	}
	
	@Override
	public List<OutRecord> selectByCondition(String outreason, String condition) {
		return outRecordMapper.selectByCondition(outreason,condition);
	}

	@Override
	public OutRecord selectByPrimaryKey(String recordid,Boolean condition) {
		return outRecordMapper.selectByPrimaryKey(recordid,condition);
	}

	@Override
	public int updateByPrimaryKeySelective(OutRecord record) {
		return outRecordMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delRecord(String recordid) {
		return outRecordMapper.deleteByPrimaryKey(recordid);
	}

	@Override
	public List<OutRecord> listOutRecordsAdmin(String orgid,Integer stage,String openid) {
		return outRecordMapper.listOutRecordsAdmin(orgid,stage,openid);
	}

	@Override
	public int recallRecord(OutRecord record) {
		return outRecordMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateoutRecord(OutRecord outRecord) {
		return outRecordMapper.updateByPrimaryKeySelective(outRecord);
	}
	
}