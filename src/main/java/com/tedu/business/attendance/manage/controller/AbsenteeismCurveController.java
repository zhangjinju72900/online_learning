package com.tedu.business.attendance.manage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.business.attendance.manage.service.AbsenteeismCurveService;

@Controller
public class AbsenteeismCurveController {

	@Resource
	private AbsenteeismCurveService absenteeismCurveServiceImpl;
	
	
	/**
	 * @Title: getTelAuthCode   
	 * @Description: TODO 获取曲线图数据接口
	 * @author: qun 
	 * @param: @param requestObj
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2019年1月5日 下午2:31:42     
	 * @throws
	 */
	@RequestMapping("/getCurveData")
	@ResponseBody
	public FormEngineResponse getCurveData(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
//	public FormEngineResponse getCurveData(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<>();
		FormEngineResponse response = new FormEngineResponse(result);
		
		Map <String, Object> param = requestObj.getData();
		String startDate = param.get("ge_startTime")==null?"":param.get("ge_startTime").toString();
		String endDate = param.get("le_startTime")==null?"":param.get("le_startTime").toString();
		String classId = param.get("eq_class")==null?"":param.get("eq_class").toString();
		String userId = param.get("userId")==null?"":param.get("userId").toString();
		
		absenteeismCurveServiceImpl.getCurveData(startDate,  endDate, classId, userId, result);
//		absenteeismCurveServiceImpl.getCurveData("2019-01-05",  "2019-06-06", "35,1429", result);
		
		return response;
	}
}
