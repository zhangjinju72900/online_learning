package com.tedu.business.classes.manage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.business.classes.manage.service.ClassManageScormService;

@Service("classManageScormServiceImpl")
public class ClassManageScormServiceImpl implements ClassManageScormService{

	@Resource
	private FormMapper formMapper;
	
	@Override
	public List<Map<String, Object>> selectCourseListByClassId(String classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		return queryBySqlId("khAdmin/schoolManage/class/ListCourseName", map);
	}
	
	
	@Override
	public List<Map<String, Object>> selectResourceColumnByCourseId(String courseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("courseId", courseId);
		return queryBySqlId("khAdmin/schoolManage/class/ListResourceColumnByClassId", map);
	}
	
	
	private List<Map<String, Object>> queryBySqlId(String string, Map<String, Object> map) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(string);
		sqlQuery.setDataByMap(map);
		return formMapper.query(sqlQuery);
	}


	@Override
	public List<Map<String, Object>> selectScormResourceData(String courseId, String classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		map.put("courseId", courseId);
		List<Map<String, Object>> list = queryBySqlId("khTeacher/classManage/QryClassScromScore", map);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(list != null && list.size() > 0){
			
			String customerResourcesIds = list.get(0).get("customerResourcesIds").toString();
			
			if(StringUtils.isNotBlank(customerResourcesIds)){
				
				String[] resourceArrayIds = customerResourcesIds.split(",");
			
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				for(int i=0; i<list.size(); i++){
					
					resultMap = new HashMap<String, Object>();
				
					String scores = list.get(i).get("scores").toString();
					
					String avgScoreShow = list.get(i).get("avgScoreShow").toString();
					
					String customerUserName = list.get(i).get("customerUserName") != null ? list.get(i).get("customerUserName").toString() : "";
					
					String[] scoreArrays = scores.split(",");
					
					for(int j=0; j<resourceArrayIds.length; j++){
						
						resultMap.put("scorm-"+resourceArrayIds[j], scoreArrays[j]);
					}
					
					resultMap.put("avgScore", avgScoreShow);
					
					resultMap.put("stdName", customerUserName);
					
					resultList.add(resultMap);
				}
				
				resultMap = new HashMap<String, Object>();
				
				for(int j=0; j<resourceArrayIds.length; j++){
					
					map.put("resourceId", resourceArrayIds[j]);
					List<Map<String, Object>> resList = queryBySqlId("khTeacher/classManage/QryClassScormResourceAvgScore", map);
					if(resList != null && resList.size() > 0 && resList.get(0) != null && resList.get(0).get("resourceAvgScore") != null){
						resultMap.put("scorm-"+resourceArrayIds[j], resList.get(0).get("resourceAvgScore"));
					}else{
						resultMap.put("scorm-"+resourceArrayIds[j], "-");
					}
				}
				resultMap.put("avgScore", "-");
				resultMap.put("stdName", "平均分");
				resultList.add(resultMap);
				
			}	
		}
		return resultList;
	}


	

}
