package com.tedu.common.constant;

public enum AppUpdateFileVersionEnum {

	APP_START_PAGE(0, "APP启动页"),
	TEACHING_GUIDE_PAGE(1, "教学引导页");
	
	private Integer code;
	private String desc;
	
	AppUpdateFileVersionEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
