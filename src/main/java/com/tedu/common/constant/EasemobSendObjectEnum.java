package com.tedu.common.constant;

public enum EasemobSendObjectEnum {
	USERS("users", "给用户发送"),
	GROUPS("chatgroups","给群组发送"),
	ROOMS("chatrooms","聊天室发送");
	
	private String code;
	private String desc;// 描述
	
	private EasemobSendObjectEnum(String code, String desc) {
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
