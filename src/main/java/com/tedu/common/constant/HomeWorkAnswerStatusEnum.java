package com.tedu.common.constant;

public enum HomeWorkAnswerStatusEnum {
	HAVE_BEEN_DISTRIBUTED(0, "已分发"),
	HAVE_BEEN_ANSWER(1, "已回答"),
	HAVE_BEEN_SUBMIT(2, "已提交"),
	HAVE_BEEN_CORRECT(3, "已批改");
	
	private Integer code;// 方向
	private String desc;// 描述
	
	HomeWorkAnswerStatusEnum(Integer code, String desc){
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

	public static String getIntegral(Integer code) {
		for (HomeWorkAnswerStatusEnum c : HomeWorkAnswerStatusEnum.values()) {
			if (c.getCode() == code) {
				return c.getDesc();
			}
		}
		return null;
	}

	
}
