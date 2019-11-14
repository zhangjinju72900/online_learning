package com.tedu.base.auth.login.service;

import java.util.List;
import java.util.Map;

import com.tedu.base.auth.login.model.UserModel;

public interface CustomLoginService {

	List<UserModel> getUserInfoByName(String username);

	void updateCustomPwd(UserModel user);

	List<Map<String, String>> getAuthorization(String userName, String menu);

	void updateLoginData(long id);

	List<UserModel> getEmployeeInfoByName(String username);

	List<UserModel> getTeacherInfoByName(String username);

}
