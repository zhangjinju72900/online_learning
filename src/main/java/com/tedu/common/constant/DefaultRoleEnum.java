package com.tedu.common.constant;

public enum DefaultRoleEnum {

	ADMIN("1", "管理员"),
	STUDENT("10", "学生"),
	TEACHER("11", "教师");
	
	private String code;
	private String desc;
	
	DefaultRoleEnum(String code, String desc){
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
