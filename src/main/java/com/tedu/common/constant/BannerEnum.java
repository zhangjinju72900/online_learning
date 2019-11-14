package com.tedu.common.constant;

public enum BannerEnum {

	STUDY("0", "学习"),
	INTEGRAL_MALL("1", "积分商城"),
	APP_START_PAGE("2", "启动页"),
	TEACHING_GUIDE_PAGE("3", "教学引导页");
	
	private String code;
	private String desc;
	
	BannerEnum(String code, String desc){
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
