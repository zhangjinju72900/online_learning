package com.tedu.business.user.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

public interface BindingTelService {

	void bindingTelNum(String telNum, String userId) throws Exception;

	void resetPassWord(String telNum, String password, String confirmPassword);

	void insertSmsRecord(SendSmsResponse res, String telNum, String type, String code);

	boolean verifyAccountExists(String telNum);
}
