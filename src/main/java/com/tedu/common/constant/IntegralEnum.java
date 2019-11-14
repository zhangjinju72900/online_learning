package com.tedu.common.constant;

public enum IntegralEnum {
	SIGN_IN(0, "签到", -1),
	INFO_FORWARD(1, "分享资讯", 10),
	TOP_INFO_REVIEW(2, "资讯评论被置顶", 20),
	TOP_INFO(3, "资讯被置顶", 50),
	APPLY_ACTIVITY(4, "报名活动", -1),
	JOIN_ACTIVITY(5, "参加活动", -1),
	ACTIVITY_PIC(5, "活动晒图", -1),
	BINDING_TEL(6, "绑定手机", 100),
	PERFECT_INFO(7, "完善个人资料", 20),
	BUY_GOODS(8, "购买商品", -1),
	RECOMMEND_INFO(9, "资讯被推荐", 50),
	MAINTAIN(10, "积分维护", -1);
	
	private Integer code;// 方向
	private String desc;// 描述
	private Integer integral; //变动积分数
	
	IntegralEnum(Integer code, String desc, Integer integral){
		this.code = code;
		this.desc = desc;
		this.integral = integral;
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

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
	public static Integer getIntegral(Integer code) {
		for (IntegralEnum c : IntegralEnum.values()) {
			if (c.getCode() == code) {
				return c.getIntegral();
			}
		}
		return null;
	}

	
}
