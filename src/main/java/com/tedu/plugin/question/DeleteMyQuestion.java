package com.tedu.plugin.question;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("DeleteMyQuestion")
public class DeleteMyQuestion  implements ILogicPlugin {

	
	@Resource
	FormMapper formMapper;
	
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
	
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String,Object> data=requestObj.getData();
		String [] ids = data.get("questionId")!=null?data.get("questionId").toString().split(","):"".split(",");
		for(String id:ids) {
			
			data.put("id",id);
			CustomFormModel cModel = new CustomFormModel();
			cModel.setData(data);
			//删除
			cModel.setSqlId("khTeacher/myquestion/DeleteQuestionM");
			formMapper.saveCustom(cModel);
		}
		
		
	}
	
	


}
