package com.tedu.common.constant;

public enum QuestionSwitchEnum {
	NEXT("next", "下一题"),
	PREVIOUS("previous", "上一题");
	
	private String code;
	private String desc;
	
	QuestionSwitchEnum(String code, String desc){
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
