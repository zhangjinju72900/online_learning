package com.tedu.base.auth.login.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tedu.base.auth.login.model.UserModel;

public interface CustomLoginDao {

	List<UserModel> getCustomUserInfoByName(String username);

	void updateCustomPwd(UserModel user);

	void updateCustomPwdByMobile(UserModel user);

	List<Map<String, String>> getResList(String userName);

	List<Map<String, String>> getAuthorization(@Param("userName")String userName, @Param("menu")String menu);

	void updateLoginData(long id);

	List<String> selectUserByRolesAndRegions(@Param("roles")String roles, @Param("regions")String regions);

	List<String> selectUserByRolesAndSch(@Param("roles")String roles, @Param("schools")String schools);

	/* 班级用户查询接口 */
	List<String> selectUserByclasses( @Param("classess")String classess);
	
	List<Map<String, Object>> getUnregisterUser();

	void updateCustomEasemobUser(@Param("id")String id, @Param("cardNum")String cardNum);

	List<UserModel> getEmployeeInfoByName(String username);

	List<UserModel> getTeacherInfoByName(String username);

}
