package com.tedu.plugin.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("goodView")
public class GoodView implements ILogicPlugin{


	
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
		Map<String, Object> data = formModel.getData();
		Map<String,Object> map = new HashMap<>();
		if(data != null){
			
			QueryPage sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("khApp/mine/mall/QryGoodById");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId("khApp/mine/mall/QryGoodById");
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", data.get("goodId"));
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> tables = formMapper.query(sqlQuery);
			if(tables != null && tables.size() > 0){
				map.putAll(tables.get(0));
			}
			
			if(map.get("id") != null){
				sqlQuery = new QueryPage();
				sqlQuery.setQueryParam("zhongdeorder/QryGoodCollect");
				sqlQuery.setQueryType("");
				sqlQuery.setSingle(1);
				sqlQuery.setSqlId("zhongdeorder/QryGoodCollect");
				paramMap = new HashMap<>();
				paramMap.put("goodId", map.get("id"));
				sqlQuery.setDataByMap(paramMap);
				List<Map<String, Object>> colls = formMapper.query(sqlQuery);
				if(colls != null && colls.size() > 0){
					map.put("isCollect", 1);
				}else{
					map.put("isCollect", 0);
				}
				
				sqlQuery = new QueryPage();
				sqlQuery.setQueryParam("zhongdeorder/QryGoodPayDeatil");
				sqlQuery.setQueryType("");
				sqlQuery.setSingle(1);
				sqlQuery.setSqlId("zhongdeorder/QryGoodPayDeatil");
				paramMap = new HashMap<>();
				paramMap.put("goodId", map.get("id"));
				sqlQuery.setDataByMap(paramMap);
				List<Map<String, Object>> ads = formMapper.query(sqlQuery);
				map.put("payDetail", ads);
			}
			
			responseObj.setData(map);
			
		}
	}

	
}
