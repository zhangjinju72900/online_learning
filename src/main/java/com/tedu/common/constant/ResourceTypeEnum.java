package com.tedu.common.constant;

public enum ResourceTypeEnum {
	FIELD_STANDARD(1, "领域标准"),
	KNOWLEDGE_ASSEMBLY(2, "知识汇编"),
	MAINTENANCE_MANUALS(3, "维修手册"),
	WORKBOOK(4, "工作页"),
	TEACHING_LESSON_PLANS(5, "教学教案"),
	NOTES(6, "笔记"),
	COURSES_PREPARE(7, "课程准备"),
	ASK_QUESTIONS(8, "提问"),
	ANSWERING_QUESTIONS(9, "答疑"),
	FEEDBACK(10, "反馈&建议"),
	TEACHER_WORK_PAGE(11, "教师工作页"),
	DOUBT_ANSWERING(12, "疑惑答疑"),
	TEACHING_DESIGN(13, "教学设计"),
	SCORM(14, "scorm课件"),
	PPT(15, "ppt转H5课件"),
	OTHERS(0, "其他");
	
	private Integer code;// 方向
	private String desc;// 描述
	
	ResourceTypeEnum(Integer code, String desc){
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
