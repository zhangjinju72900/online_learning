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
import com.tedu.business.user.service.SaveCourseResoursesScoreService;

@Service
public class SaveCourseResourcesScoreServiceImpl implements SaveCourseResoursesScoreService{
	
	@Resource
	private FormMapper formMapper;	
	
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean saveCourseResourcesScore(String customerResourcesId, String courseId, String sectionId,
			String labelId, String customerUserId, String score, String passScore, String percentScore, String passPercent) {
		CustomFormModel cModel = new CustomFormModel();
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("customerResourcesId", customerResourcesId);
			map.put("courseId", courseId);
			map.put("labelId", labelId);
			map.put("sectionId", sectionId);
			map.put("customerUserId", customerUserId);
			map.put("score", score);
			map.put("passScore", passScore);
			
			map.put("percentScore", percentScore);
			map.put("passPercent", passPercent);
			cModel.setSqlId("khApp/study/InsertCourseResourcesScore");
			cModel.setData(map);
			int isSaved = formMapper.saveCustom(cModel);
			if(isSaved>-1){
				return true; 
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("scorm记录分数失败", e);
		}	
		return false;
	}
	
	
	
}
