package com.tedu.common.constant;

public enum SendMsgTypeEnum {
	
	INFO_LIKE(0, "资讯被点赞"),
	INFO_REVIEW(1, "资讯被评论"),
	INFO_REVIEW_LIKE(2, "资讯评论被点赞"),
	INFO_REVIEW_REVIEW(3, "资讯评论被回复"),
	ACTIVITY_NOTICE(10, "活动通知");
	
	private Integer code;// 方向
	private String desc;// 描述
	
	SendMsgTypeEnum(Integer code, String desc){
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
		for (SendMsgTypeEnum c : SendMsgTypeEnum.values()) {
			if (c.getCode() == code) {
				return c.getDesc();
			}
		}
		return null;
	}

	
}
