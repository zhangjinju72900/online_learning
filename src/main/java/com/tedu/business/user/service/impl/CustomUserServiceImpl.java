package com.tedu.business.user.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.auth.login.dao.CustomLoginDao;
import com.tedu.business.user.service.CustomUserService;

@Service("customUserServiceImpl")
public class CustomUserServiceImpl implements CustomUserService{
	
	@Resource
	private CustomLoginDao customLoginDao;

	@Override
	public List<String> selectUserByRolesAndSch(String roles, String schools) {
		return customLoginDao.selectUserByRolesAndSch(roles, schools);
	}

	@Override
	public List<String> selectUserByRolesAndRegions(String roles, String regions) {
		return customLoginDao.selectUserByRolesAndRegions(roles, regions);
	}

	@Override
	public List<Map<String, Object>> selectUnregisterUser() {
		return customLoginDao.getUnregisterUser();
	}

	@Override
	public void updateEasemobUser(String id, String cardNum) {
		customLoginDao.updateCustomEasemobUser(id, cardNum);
	}

	/*根据班级查询用户*/
	@Override
	public List<String> selectUserByclasses(String classess) {
		return customLoginDao.selectUserByclasses(classess);
	}

	
}
