package com.tedu.plugin.sale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.task.SpringUtils;

@Service("cluesLogPlugin")
public class CluesLogPlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
		}


	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String,String> empStatus = new HashMap<String,String>();	
	    empStatus.put("potentialbusiness", "1-潜在商机");
	    empStatus.put("presales", "2-售前");
	    empStatus.put("bid", "3-投标");
	    empStatus.put("approve", "4-合同审批");
	    empStatus.put("signing", "5-合同签订");
	    empStatus.put("close", "6-线索关闭");
		Map<String,Object> mapNewData = formMapper.selectById("t_sales_clues", "id",formModel.getPrimaryFieldValue());
		
		String name = SessionUtils.getUserInfo().getEmpName();
		Map<String, Object> data = new HashMap<>();
		data.put("cluesId", mapNewData.get("id"));
		data.put("createTime", new Date());
		data.put("createBy", SessionUtils.getUserInfo().getEmpId());
		Boolean flag = false;
		if (mapNewData.get("stage").equals("presales")) {
			data.put("logContent","("+name+")将本商机建档且已升级为(" + empStatus.get(ObjectUtils.toString(mapNewData.get("stage")))+")");
			flag=true;
		}
		if (!flag) {
			data.put("logContent","("+name+")将本线索阶段升级为(" + empStatus.get(ObjectUtils.toString(mapNewData.get("stage")))+")");
			flag=true;
		}
		
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("saveCluesLog");
		cModel.setData(data);
		int n = 0;
		if (flag) {
			n = formMapper.saveCustom(cModel);
		}
		if(n!=1){
			FormLogger.logFlow(String.format("记录salesLog失败", formModel.getPrimaryFieldValue()), FormLogger.LOG_TYPE_ERROR);
		}
	
 	}

}
