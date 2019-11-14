package com.tedu.plugin.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;

/**
 * 直播参数配置
 * @author quancong
 *
 */
@Service("activityConfigPlugin")
public class ActivityConfigPlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	@Resource
	private IntegralChangeComponent integralChangeComponent;

	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		
		String activityId = map.get("activityId")==null?"":map.get("activityId").toString();
		String rtmpUrl = map.get("rtmpUrl")==null?"":map.get("rtmpUrl").toString();
		String flvUrl = map.get("flvUrl")==null?"":map.get("flvUrl").toString();
		String muUrl = map.get("muUrl")==null?"":map.get("muUrl").toString();
		
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khApp/discover/activity/selectActivityConfig");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khApp/discover/activity/selectActivityConfig");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("activityId", activityId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> logData = new HashMap<>();
		
		logData.put("rtmpUrl", rtmpUrl);
		logData.put("flvUrl", flvUrl);
		logData.put("muUrl", muUrl);
		
		if(tables != null && tables.size() > 0){
			cModel.setSqlId("khApp/discover/activity/updateActivityConfig");
			logData.put("id", tables.get(0).get("id"));
			
			cModel.setData(logData);
			int inter=	formMapper.saveCustom(cModel);
		}else{
			cModel.setSqlId("khApp/discover/activity/insertActivityConfig");
			logData.put("activityId", activityId);
			
			cModel.setData(logData);
			int inter=	formMapper.saveCustom(cModel);
		}
	}
}

