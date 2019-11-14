package com.tedu.common.error;
/**
 * 错误码
 * 1开头的是系统级错位
 * 第一位：分类码,2-3位错误码
 * 终止逻辑的执行
 * 
 * @author wangdanfeng
 *
 */
public enum ExErrorCode{
//	拦截器的通用错误代码的提示信息：系统错误(10YY)，请联系管理员 
//	- 未知错误
//	- 请求次数过于频繁
//	- 请求内容长度超过限制
//	- HTTP Header内容非法
//	- 请求参数错误
//	- JSON解析失败
//	- Header内容非法
//	- HMAC验证失败
//	- Token非法
//	- 重复请求
//	- 访问权限验证不通过
//	- 隐藏域必填项验证异常
//	- 隐藏域输入格式验证异常
//	- 服务超时
//	- 服务不存在
	//系统级
	KEY_WORD("801", "包含违禁词，请修改"),
	SMS_CODE_ERROR("802", "短信验证码错误"),
	BINDING_TEL_FAIL("803", "绑定手机号失败"),
	RESTE_PWD_FAIL("804", "重置密码失败"),

	ACCOUNT_NOT_EXIT("805","账号不存在"),
	PWD_INCONFORMITY("806", "密码不一致"),
	PWD_EMPTY("807", "密码为空"),
	PWD_OLDPWD_ERROR("808","旧密码错误"),
	ACCOUNT_ALREADY_EXIT("809","账号已存在"),
	
	
	JOIN_OR_ACTIVITY_EMPTY("809", "参加人ID或活动ID为空"),
	JOIN_ACTIVITY_FAIL("810", "参加活动失败"),
	JOIN_ACTIVITY_NO_PERMIS("811", "无权限报名"),
	JOIN_ACTIVITY_LACK_INTEGRAL("812", "可使用积分小于报名积分"),
	
	
	REGISTER_EASEMOB_FAIL("901", "注册环信失败"),
	SMS_CODE_FAIL("902", "获取短信验证码失败"),
	
	
	SAVE_SCORE_FAIL("903","分数保存失败"),
	
	SAVE_INFO_FAIL("1001","动态/晒图保存失败"),
	
	RESOURCE_NOT_FOUND("1101","找不到这个资源"),
	
	INFO_OR_ACTIVITY_NOT_FOUND("1101","活动或资讯已删除"),
	
	APP_UPLOAD_FAIL("1201","上传资源失败"),
	
	
	NOT_FOUND_NEXT_QUESTION("2001","无下一题"),
	
	NOT_FOUND_PREVIOUS_QUESTION("2002","无上一题"),
	
	SAVE_HOMEWORK_DATA_ERROR("3001","保存作业数据错误"),
	
	SUBMIT_HOMEWORK_DATA_ERROR("3002","提交作业数据错误"),
	
	HOMEWORK_HAVE_BEAN_SUBMIT_ERROR("3011","作业已提交或批改不能更改"),
	
	ATTEND_CLASS_NOT_RIGHT_STUDENT("4001","非本班学生,签到失败"),
	
	APP_UPDATE_FILE_PARAM_ERROR("9001", "获取APP文件地址参数错误")
	;

	private String code;
	private String msg;
	private ExErrorCode(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	 @Override
	  public String toString() {
	    return code + ": " + msg;
	  }
}
