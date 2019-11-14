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

@Service("DeleteQuestion")
public class DeleteQuestion  implements ILogicReviser{
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Override
	public FormModel beforeLogic(FormEngineRequest requestObj) {
		
		return null;
	}

	@Override
	public void afterLogic(FormEngineRequest requestObj, FormEngineResponse responseObj) {
		System.out.println("data:"+requestObj.getData());
		JSONArray groupData = JSONArray.fromObject(requestObj.getData().get("empGroupDel"));
		System.out.println("groupData:"+groupData);
		String id = groupData.getJSONObject(0).getString("eq_Id");
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("id", id);
		System.out.println(id);
		CustomFormModel cModel = new CustomFormModel();
		cModel.setData(data);
		//删除
		cModel.setSqlId("khAdmin/question/deleteQuestion");
		formMapper.saveCustom(cModel);
		//修改vflag
		cModel.setSqlId("khAdmin/question/changeFlagQuestion");
		formMapper.saveCustom(cModel);
		
		
	}
	
	


}
