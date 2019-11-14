package com.tedu.common.model;

public class IntegralChangeModel {
	private String userId; //用户ID
	private String id; //对应业务主表ID;如线下活动报名，该字段为 t_activity_apply 表id
	private String integral; //维护积分数
	private String remark; //备注
	
	public IntegralChangeModel(String id) {
		super();
		this.id = id;
	}
	
	public IntegralChangeModel(String userId, String id, String integral, String remark) {
		super();
		this.userId = userId;
		this.id = id;
		this.integral = integral;
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
