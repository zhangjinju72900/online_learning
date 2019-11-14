package com.tedu.business.user.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.business.user.service.PasswordChangeService;
import com.tedu.business.user.service.SaveCourseResoursesScoreService;
import com.tedu.common.error.ExErrorCode;

@Controller
public class SaveCourseResourceScoreController {
	@Resource
	private SaveCourseResoursesScoreService SaveCourseResourcesScoreServiceImpl;
	public final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * @Title:   
	 * @Description: TODO 保存课程资源分数
	 * @author: quancong
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年11月15日 上午10:04   
	 * @throws
	 */
	@RequestMapping(value="/saveCourseResourcesScore")
	@ResponseBody
	public FormEngineResponse changePassword(HttpServletRequest request){
		String result="";
		FormEngineResponse response = new FormEngineResponse(result);
		//获得页面的参数值
		String customerResourcesId = StringUtils.isBlank(request.getParameter("customerResourcesId")) || "null".equals(request.getParameter("customerResourcesId")) ? "0" 
				: request.getParameter("customerResourcesId");
		String courseId = StringUtils.isBlank(request.getParameter("courseId")) || "null".equals(request.getParameter("courseId")) ? "0" 
				: request.getParameter("courseId");
		String sectionId = StringUtils.isBlank(request.getParameter("sectionId")) || "null".equals(request.getParameter("sectionId")) ? "0" 
				: request.getParameter("sectionId");
		String labelId = StringUtils.isBlank(request.getParameter("labelId")) || "null".equals(request.getParameter("labelId")) ? "0" 
				: request.getParameter("labelId");
		String customerUserId = StringUtils.isBlank(request.getParameter("customerUserId")) || "null".equals(request.getParameter("customerUserId")) ? "0" 
				: request.getParameter("customerUserId");
		String score = StringUtils.isBlank(request.getParameter("score")) || "null".equals(request.getParameter("score")) ? "0" 
				: request.getParameter("score");
		String passScore = StringUtils.isBlank(request.getParameter("passScore")) || "null".equals(request.getParameter("passScore")) ? "0" 
				: request.getParameter("passScore");
		
		
		String percentScore = StringUtils.isBlank(request.getParameter("percentScore")) || "null".equals(request.getParameter("percentScore")) ? "0" 
				: request.getParameter("percentScore");
		String passPercent = StringUtils.isBlank(request.getParameter("passPercent")) || "null".equals(request.getParameter("passPercent")) ? "0" 
				: request.getParameter("passPercent");
		
		boolean isSaveScore = SaveCourseResourcesScoreServiceImpl.saveCourseResourcesScore(customerResourcesId, courseId, sectionId, labelId, customerUserId, score, passScore, percentScore, passPercent);
		
		if(isSaveScore){
			response.setMsg("保存成功");
			log.info("保存成功");
		}else{
			//返回错误信息
			response.setCode(ExErrorCode.SAVE_SCORE_FAIL.getCode());
			response.setMsg(ExErrorCode.SAVE_SCORE_FAIL.getMsg());
			log.info(ExErrorCode.SAVE_SCORE_FAIL.getMsg());
		}
		return response;
	}
	
	
	@RequestMapping(value="/testSession")
	@ResponseBody
	public String testSession(HttpServletRequest request){
		
		System.err.println("成功进入");
		
		return "";
	}
	
	

}
