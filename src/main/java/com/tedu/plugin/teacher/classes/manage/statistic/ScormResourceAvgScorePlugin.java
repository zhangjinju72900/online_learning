package com.tedu.plugin.teacher.classes.manage.statistic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("scormResourceAvgScorePlugin")
public class ScormResourceAvgScorePlugin implements ILogicPlugin{

	@Resource
	private FormMapper formMapper;
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		Map<String, Object> map = requestObj.getData();
		
		DataGrid dg = (DataGrid) responseObj.getData();
		if(dg != null && dg.getRows() != null && dg.getRows().size() > 0){
			
			List<Map<String, Object>> list = (List<Map<String, Object>>) dg.getRows();
			
			String customerResourcesIds = list.get(0).get("customerResourcesIds").toString();//资源树
			
			StringBuffer sb = new StringBuffer();
			
			Arrays.asList(customerResourcesIds.split(",")).stream().forEach(m ->{
				map.put("resourceId", m);
				List<Map<String, Object>> resList = queryBySqlId("khTeacher/classManage/QryClassScormResourceAvgScore", map);
				if(resList != null && resList.size() > 0 && resList.get(0) != null){
					sb.append(resList.get(0).get("resourceAvgScore")).append(",");
				}else{
					sb.append("-").append(",");
				}
			});
			
			list.get(0).put("resourceAvgScore", sb.toString().substring(0, sb.toString().length()-1));
		}
	}
	
	
	private List<Map<String, Object>> queryBySqlId(String string, Map<String, Object> map) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(string);
		sqlQuery.setDataByMap(map);
		return formMapper.query(sqlQuery);
	}

}
