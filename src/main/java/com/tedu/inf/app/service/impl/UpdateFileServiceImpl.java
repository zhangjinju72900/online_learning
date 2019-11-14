package com.tedu.inf.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.AppUpdateFileVersionEnum;
import com.tedu.common.constant.BannerEnum;
import com.tedu.inf.app.service.UpdateFileService;
import com.tedu.inf.app.vo.UpdateFileVo;

@Service("updateFileServiceImpl")
public class UpdateFileServiceImpl implements UpdateFileService{

	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	@Override
	public UpdateFileVo selectAppStartPage(Long versionCode, UpdateFileVo vo) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("baseType", AppUpdateFileVersionEnum.APP_START_PAGE.getCode());
		
		List<Map<String, Object>> versionCodeList = queryDataBySqlIdAndParams("khApp/updateVsersion/QryMaxVersionCodeByType", param);
		
		if(versionCodeList != null && versionCodeList.size() > 0 && Long.parseLong(versionCodeList.get(0).get("id").toString()) > versionCode){
		
			vo.setUpdateFlag(0);

			vo.setVersionId(Long.parseLong(versionCodeList.get(0).get("id").toString()));
			
			List<String> list = new ArrayList<>();
			
			param.put("bannerType", BannerEnum.APP_START_PAGE.getCode());
			
			List<Map<String, Object>> urlList = queryDataBySqlIdAndParams("khApp/updateVsersion/QryAppUpdateFileByType", param);
			urlList = urlList == null ? new ArrayList<Map<String, Object>>() : urlList.stream().filter(m ->{
				return m.get("ossUrl") != null && StringUtils.isNotBlank(m.get("ossUrl").toString());
			}).collect(Collectors.toList());
			
			for (Map<String, Object> map : urlList) {
				list.add(map.get("ossUrl").toString());
			}
			
			vo.setUrlList(list);
		}else if(versionCodeList != null && versionCodeList.size() > 0){
			vo.setVersionId(Long.parseLong(versionCodeList.get(0).get("id").toString()));
		}
		return vo;
	}

	@Override
	public UpdateFileVo selectTeachingGuidePage(Long versionCode, UpdateFileVo vo) {
		Map<String, Object> param = new HashMap<>();
		param.put("baseType", AppUpdateFileVersionEnum.TEACHING_GUIDE_PAGE.getCode());
		
		List<Map<String, Object>> versionCodeList = queryDataBySqlIdAndParams("khApp/updateVsersion/QryMaxVersionCodeByType", param);
		
		if(versionCodeList != null && versionCodeList.size() > 0 && Long.parseLong(versionCodeList.get(0).get("id").toString()) > versionCode){
		
			vo.setUpdateFlag(0);

			vo.setVersionId(Long.parseLong(versionCodeList.get(0).get("id").toString()));
			
			List<String> list = new ArrayList<>();
			
			param.put("bannerType", BannerEnum.TEACHING_GUIDE_PAGE.getCode());
			
			List<Map<String, Object>> urlList = queryDataBySqlIdAndParams("khApp/updateVsersion/QryAppUpdateFileByType2", param);
			urlList = urlList == null ? new ArrayList<Map<String, Object>>() : urlList.stream().filter(m ->{
				return m.get("ossUrl") != null && StringUtils.isNotBlank(m.get("ossUrl").toString());
			}).collect(Collectors.toList());
			
			for (Map<String, Object> map : urlList) {
				list.add(map.get("ossUrl").toString());
			}
			
			vo.setUrlList(list);
		}else if(versionCodeList != null && versionCodeList.size() > 0){
			vo.setVersionId(Long.parseLong(versionCodeList.get(0).get("id").toString()));
		}
		return vo;
	}
	
	
	private List<Map<String, Object>> queryDataBySqlIdAndParams(String sqlId, Map<String, Object> paramMap) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(sqlId);
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId(sqlId);
		sqlQuery.setDataByMap(paramMap);
		return formMapper.query(sqlQuery);
	}

}
