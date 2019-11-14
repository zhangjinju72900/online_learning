package com.tedu.business.classes.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.common.model.DataGrid;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.business.classes.manage.service.ClassManageScormService;

@Controller
public class ClassManageScormController {
	
	@Resource
	private ClassManageScormService classManageScormServiceImpl;
	
	@RequestMapping("courseList")
	@ResponseBody
	public FormEngineResponse queryItem(@RequestBody FormEngineRequest requestObj,ServerTokenData serverTokenData){
		String classId = requestObj.getData().get("classId")==null?"":requestObj.getData().get("classId").toString();
		List<Map<String,Object>> list = classManageScormServiceImpl.selectCourseListByClassId(classId);
		if(list==null){
			list= new ArrayList<>();
		}
		return new FormEngineResponse(list);
	}	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("getScormResourceColumn")
	@ResponseBody
	public FormEngineResponse getScormResourceColumn(@RequestBody FormEngineRequest requestObj,ServerTokenData serverTokenData){
		String courseId = requestObj.getData().get("courseId")==null?"":requestObj.getData().get("courseId").toString();
		List result = new ArrayList();
		List<Map<String,Object>> l1 = new ArrayList();
		List<Map<String,Object>> list = classManageScormServiceImpl.selectResourceColumnByCourseId(courseId);
		if(list==null){
			list= new ArrayList<>();
		}else if (list.size() > 0){
			
			list.stream().forEach(l ->{
				l.put("width", "100");
			});
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("field", "avgScore");
			map.put("title", "平均分");
			map.put("width", "100");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("field", "stdName");
			map.put("title", "姓名");
			map.put("width", "100");
			l1.add(map);
		}
		l1.addAll(list);
		result.add(l1);
		return new FormEngineResponse(result);
	}
	
	
	@RequestMapping("getScormResourceData")
	@ResponseBody
	public DataGrid getScormResourceData(HttpServletRequest request){
		String courseId = request.getParameter("courseId");
		String classId = request.getParameter("classId");
		List<Map<String,Object>> list = classManageScormServiceImpl.selectScormResourceData(courseId, classId);
		if(list==null){
			list= new ArrayList<>();
		}
		DataGrid dg = new DataGrid(list);
		dg.setTotal(list.size());
		return dg;
	}
}
