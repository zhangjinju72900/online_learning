package com.tedu.common.constant;

public enum HtmlResourceModuleEnum {

	CUSTOMER_RESOURCES("1", "用户资源模块"),
	MAINTAIN_MANUAL("2","维修手册");
	
	private String code;
	private String desc;// 描述
	
	private HtmlResourceModuleEnum(String code, String desc) {
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
