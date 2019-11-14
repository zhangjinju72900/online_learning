package com.tedu.plugin.order;

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

@Service("buyAgainOrder")
public class BuyAgainOrder implements ILogicPlugin{
	
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
			sqlQuery.setQueryParam("zhongdeorder/QryDefaultAddress");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId("zhongdeorder/QryDefaultAddress");
			Map<String, Object> paramMap = new HashMap<>();
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> tables = formMapper.query(sqlQuery);
			if(tables != null && tables.size() > 0){
				map.putAll(tables.get(0));
			}
			
			sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("zhongdeorder/BuyAgainByOrderId");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId("zhongdeorder/BuyAgainByOrderId");
			paramMap = new HashMap<>();
			paramMap.put("id", data.get("id"));
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> t = formMapper.query(sqlQuery);
			if(t != null && t.size() > 0){
				map.putAll(t.get(0));
				
				if(tables == null || tables.size() == 0){
					map.put("address", "");
					map.put("addressDetail", "");
					map.put("name", "");
					map.put("tel", "");
				}
			}
			
			if(map.get("goodId") != null){
				sqlQuery = new QueryPage();
				sqlQuery.setQueryParam("zhongdeorder/QryGoodPayDeatil");
				sqlQuery.setQueryType("");
				sqlQuery.setSingle(1);
				sqlQuery.setSqlId("zhongdeorder/QryGoodPayDeatil");
				paramMap = new HashMap<>();
				paramMap.put("goodId", map.get("goodId"));
				sqlQuery.setDataByMap(paramMap);
				List<Map<String, Object>> ads = formMapper.query(sqlQuery);
				map.put("payDetail", ads);
			}
			
			responseObj.setData(map);
			
		}
	}
}
