package com.tedu.business.user.service;

import java.util.List;
import java.util.Map;

public interface CustomUserService {

	List<String> selectUserByRolesAndSch(String roles, String schools);

	List<String> selectUserByRolesAndRegions(String roles, String regions);
	
	/*查询班级用户接口*/
	List<String> selectUserByclasses(String classess);

	List<Map<String, Object>> selectUnregisterUser();

	void updateEasemobUser(String id, String cardNum);

}
