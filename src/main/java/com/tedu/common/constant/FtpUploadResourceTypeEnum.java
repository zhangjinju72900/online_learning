package com.tedu.common.constant;

public enum FtpUploadResourceTypeEnum {

	CUSTOMER_USER_RESOURCE("0", "用户资源模块"),
	MAINTAIN_MANUAL("1", "维修手册模块");
	
	private String code;// 方向
	private String desc;// 描述
	
	FtpUploadResourceTypeEnum(String code, String desc){
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
