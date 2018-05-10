package com.yunzhijia.appdemo.pojo;

import org.apache.ibatis.type.Alias;

@Alias("tClockIn")
public class ClockIn {
    private String clockinid;

    private String position;

    private String clockintime;

    private Integer state;

    private String recordId;

    public String getClockinid() {
        return clockinid;
    }

    public void setClockinid(String clockinid) {
        this.clockinid = clockinid == null ? null : clockinid.trim();
    }
    
    public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getClockintime() {
        return clockintime;
    }

    public void setClockintime(String clockintime) {
        this.clockintime = clockintime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }
}