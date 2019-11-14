package com.tedu.base.auth.login.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tedu.base.auth.login.dao.CustomLoginDao;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.CustomLoginService;

@Service("customLoginService")
public class CustomLoginServiceImpl implements CustomLoginService{
	
	@Resource
	private CustomLoginDao customLoginDao;

	@Override
	public List<UserModel> getUserInfoByName(String username) {
		return customLoginDao.getCustomUserInfoByName(username);
	}

	@Override
	public void updateCustomPwd(UserModel user) {
		customLoginDao.updateCustomPwd(user);
	}

	@Override
	public List<Map<String, String>> getAuthorization(String userName, String menu) {
		if(StringUtils.isEmpty(menu)){
			return customLoginDao.getResList(userName);
		} else {
			return customLoginDao.getAuthorization(userName, menu);
		}
	}

	@Override
	public void updateLoginData(long id) {
		customLoginDao.updateLoginData(id);
	}

	@Override
	public List<UserModel> getEmployeeInfoByName(String username) {
		return customLoginDao.getEmployeeInfoByName(username);
	}

	@Override
	public List<UserModel> getTeacherInfoByName(String username) {
		return customLoginDao.getTeacherInfoByName(username);
	}

}
