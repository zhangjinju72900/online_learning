package com.tedu.business.user.service;

public interface PasswordChangeService {

	boolean checkOldPassword(String oldPassWord);
	void updateUserPassword(String newPassword);

}
