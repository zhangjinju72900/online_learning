package com.tedu.common.constant;

public enum ActivityTypeEnum {
	OFFLINE("0", "线下活动"),
	ONLINE("1", "线上活动"),
	LIVE("2", "直播");
	
	private String code;
	private String desc;
	
	ActivityTypeEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
