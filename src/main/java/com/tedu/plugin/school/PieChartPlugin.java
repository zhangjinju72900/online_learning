package com.tedu.plugin.school;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

@Service("pieChartPlugin")
public class PieChartPlugin implements ILogicPlugin {
	

	@Resource
	private FormService formService;
	
	
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(requestObj.getData().toString());
		Map<String, Object> map =  requestObj.getData();
		
		String createTime = map.get("createTime")==null?"":map.get("createTime").toString();
		createTime = createTime.trim();
		QueryPage qp = new QueryPage();
		Map<String, Object> map1 = new HashMap<String,Object>();
		map1.put("createTime", createTime);
		
		
		qp.setDataByMap(map1);
		qp.setQueryParam("college/QryNumTest");
		
		List<Map<String,Object>> list= formService.queryBySqlId(qp);
		if(list.size()>0){
			responseObj.setData(list.get(0));
		}
		
	
		//Map<String, Object> requestMap = new HashMap<>();
		//requestMap.put("updateaTime", updateTime);
		//qp.setDataByMap(requestMap);
		//List qlist = formService.queryBySqlId(qp);
	}

}
