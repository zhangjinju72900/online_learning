package com.tedu.common.constant;

public enum FileTypeEnum {
	OTHERS("0", "普通文件"),
	SCORM("14", "scorm课件"),
	PPT("15", "ppt转H5课件");
	
	private String code;// 方向
	private String desc;// 描述
	
	FileTypeEnum(String code, String desc){
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
