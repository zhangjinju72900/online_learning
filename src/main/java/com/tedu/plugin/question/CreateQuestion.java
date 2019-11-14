package com.tedu.plugin.question;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.task.SpringUtils;

@Service("CreateQuestion")
public class CreateQuestion  implements ILogicReviser{
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Override
	public FormModel beforeLogic(FormEngineRequest requestObj) {
		
		return null;
	}

	@Override
	public void afterLogic(FormEngineRequest requestObj, FormEngineResponse responseObj) {
		 FormLogger.info("Transform逻辑后置方法FlowList" + requestObj.getData());
		Map<String,Object> map = (Map<String,Object>)requestObj.getData();
	
		List<Map<String, Object>> resourceList = new ArrayList<Map<String, Object >>();
	    for(int i=0;i<6;i++){
	    	Map<String, Object> row = new HashMap<String, Object>();
	    	row.put("id", i);
	    	row.put("questionId", "");
	    	row.put("title", "");
	    	row.put("content", "");
	    	resourceList.add(row);
	    }

    	DataGrid resourceDataGrid = new DataGrid(resourceList);
    	resourceDataGrid.setTotal(resourceList.size());
    	responseObj.setData(resourceDataGrid);
    	LogUtil.info("--------doAfter执行完毕------------");
	}

}
