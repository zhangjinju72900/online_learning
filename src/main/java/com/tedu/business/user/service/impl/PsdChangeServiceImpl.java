package com.tedu.business.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.business.user.service.PasswordChangeService;

@Service
public class PsdChangeServiceImpl implements PasswordChangeService{
	
	@Resource
	private FormMapper formMapper;	
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	
	@Override
	public boolean checkOldPassword(String oldPassWord) {
		UserModel model=SessionUtils.getUserInfo();
		long userId=model.getUserId();
		Map<String, Object> map = new HashMap<String, Object>();
		QueryPage qp = new QueryPage();
		map.put("userId", userId);
		qp.setDataByMap(map);
		qp.setQueryParam("khApp/mine/QryPasswordById");
		List<Map<String, Object>> qlists = formMapper.query(qp);
		//获得查询到的原密码
		if ((qlists.size() != 0)) {
			String userPassword = (String) qlists.get(0).get("password");
			String salt = (String) qlists.get(0).get("salt");
			// 判断原密码与输入的旧密码是否相等
			String password = MD5Util.MD5Encode(new String(oldPassWord)).toUpperCase();
			String saltedPwd = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, salt, password);
			if (saltedPwd.equals(userPassword)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}




	@Override
	public void updateUserPassword(String newPassword) {
		UserModel model=SessionUtils.getUserInfo();
		long userId=model.getUserId();
		String salt = AviatorEvaluator.execute("Guid()").toString();
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/mine/UpdateUserPassword");
		Map<String, Object> map = new HashMap<String, Object>();
		String password = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR,salt, MD5Util.MD5Encode(new String(newPassword)).toUpperCase());
		map.put("newPassword", password);
		map.put("userId", userId);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
	}

	

}
