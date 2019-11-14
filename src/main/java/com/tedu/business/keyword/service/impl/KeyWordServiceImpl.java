package com.tedu.business.keyword.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.business.keyword.service.KeyWordService;

@Service("keyWordServiceImpl")
public class KeyWordServiceImpl implements KeyWordService{

	@Resource
	private FormMapper formMapper;
	
	@Override
	public boolean checkTitle(String title) {
			
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khApp/discover/information/QryKeyword");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khApp/discover/information/QryKeyword");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("title", title);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		if(tables != null && tables.size() > 0){
			return true;
		}
		return false;
	}

}
