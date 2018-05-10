package com.yunzhijia.appdemo.pojo;

import java.util.List;
import org.apache.ibatis.type.Alias;

@Alias("tOutRecord")
public class OutRecord {
	//登记外出记录ID
    private String recordid;
    
    //openId
    private String openid;
    
    // 登记人	
    private String username;
    
    private String contact;
    // 出发时间
    private String outtime;
    // 返回时间
    private String backtime;
    // 外出事由    
    private String outreason;
    // 所属部门
    private String deptname;
    // 部门id
    private String orgid;
    
    private String photourl;
    
    private Integer state;
    //时间阶段
    private Integer stage;
    // 外出签到数据
    private List<ClockIn> clockIns;

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid == null ? null : recordid.trim();
    }
    
    public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }
    
    public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getBacktime() {
        return backtime;
    }

    public void setBacktime(String backtime) {
        this.backtime = backtime;
    }

    public String getOutreason() {
        return outreason;
    }

    public void setOutreason(String outreason) {
        this.outreason = outreason == null ? null : outreason.trim();
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname == null ? null : deptname.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    
	public List<ClockIn> getClockIns() {
		return clockIns;
	}

	public void setClockIns(List<ClockIn> clockIns) {
		this.clockIns = clockIns;
	}
	

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	
	public String getPhotoUrl() {
		return photourl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photourl = photoUrl;
	}

	@Override
	public String toString() {
		return "OutRecord [recordid=" + recordid + ", openid=" + openid + ", username=" + username + ", contact="
				+ contact + ", outtime=" + outtime + ", backtime=" + backtime + ", outreason=" + outreason
				+ ", deptname=" + deptname + ", orgid=" + orgid + ", photourl=" + photourl + ", state=" + state
				+ ", stage=" + stage + ", clockIns=" + clockIns + "]";
	}
}