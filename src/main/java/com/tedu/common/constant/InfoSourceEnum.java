package com.tedu.common.constant;

public enum InfoSourceEnum {
	USER_INFO("0", "用户动态"),
	OFFICIAL_REVIEW("1","官方发布"),
	ACTIVITY_PIC("2","活动晒图");
	
	private String code;
	private String desc;// 描述
	
	private InfoSourceEnum(String code, String desc) {
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
