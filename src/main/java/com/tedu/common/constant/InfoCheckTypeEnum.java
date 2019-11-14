package com.tedu.common.constant;

public enum InfoCheckTypeEnum {
	INFO("info", "资讯"),
	INFO_REVIEW("info_review","资讯评论"),
	INFO_REVIEW_REVIEW("info_review_review","资讯评论-评论"),
	AC_JOIN_PIC("ac_join_pic","活动晒图"),
	LIVE_REVIEW("live_review","直播评论");
	
	private String code;
	private String desc;// 描述
	
	private InfoCheckTypeEnum(String code, String desc) {
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
