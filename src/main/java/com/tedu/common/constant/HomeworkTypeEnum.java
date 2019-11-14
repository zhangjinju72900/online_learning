package com.tedu.common.constant;

public enum HomeworkTypeEnum {
	OBJECTIVE("0", "客观题"),
	SUBJECTIVE("1", "主观题");
	
	private String code;
	private String desc;
	
	HomeworkTypeEnum(String code, String desc){
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
