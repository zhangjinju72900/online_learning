package com.tedu.common.constant;

public enum EasemobSendTypeEnum {
	TXT("txt", "文本消息"),
	IMG("img","图片消息"),
	RAUDIO("audio","语音消息"),
	VIDEO("video", "视频消息"),
	CMD("cmd", "透传消息");
	
	private String code;
	private String desc;// 描述
	
	private EasemobSendTypeEnum(String code, String desc) {
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
