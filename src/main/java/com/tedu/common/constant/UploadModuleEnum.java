package com.tedu.common.constant;

import java.util.Arrays;

public enum UploadModuleEnum {
	
	COURSE_ERROR_RECOVERY("0", "课程纠错", 0),
	
	QUESTION_SET("1", "题库", 0),
	
	TEACHER_NOTICE("2", "教师端通知", 0),
	
	TEACHING_ASSIST("3", "教辅资料", 1);
	
	private String code;// 来源模块
	private String desc;// 描述
	private Integer space;//0公有空间，1私有空间
	
	UploadModuleEnum(String code, String desc, Integer space){
		this.code = code;
		this.desc = desc;
		this.space = space;
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

	public Integer getSpace() {
		return space;
	}

	public void setSpace(Integer space) {
		this.space = space;
	}

	public static Integer getSpaceByCode(String code) {
		for (UploadModuleEnum c : UploadModuleEnum.values()) {
			if(c.getCode().equals(code)){
				return c.getSpace();
			}
		}
		return -1;
	}
	

}
