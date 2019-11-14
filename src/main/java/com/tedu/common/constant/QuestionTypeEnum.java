package com.tedu.common.constant;

public enum QuestionTypeEnum {
	SINGLE_CHOICE("0", "单项选择"),
	MULTIPLE_CHOICE("1", "多项选择"),
	ESTIMATE("2", "判断题"),
	SUBJECTIVE("3", "主观题");
	
	private String code;
	private String desc;
	
	QuestionTypeEnum(String code, String desc){
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
