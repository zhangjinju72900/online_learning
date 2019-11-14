package com.tedu.plugin.question;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
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

@Service("SaveQuestion")
public class SaveQuestion  implements ILogicReviser{
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Override
	public FormModel beforeLogic(FormEngineRequest requestObj) {
		
		return null;
	}

	@Override
	public void afterLogic(FormEngineRequest requestObj, FormEngineResponse responseObj) {
		System.out.println("data:"+requestObj.getData());
		JSONArray groupData = JSONArray.fromObject(requestObj.getData().get("addGroup"));
		
		String id = groupData.getJSONObject(0).getString("id");
		String questionClassifyId = groupData.getJSONObject(0).getString("questionClassifyId");
		String questionType = groupData.getJSONObject(0).getString("questionType");
		String difficultyLevel = groupData.getJSONObject(0).getString("difficultyLevel");
		String enableStatus = groupData.getJSONObject(0).getString("enableStatus");
		String content = groupData.getJSONObject(0).getString("content");
		String updateBy = groupData.getJSONObject(0).getString("updateBy");
		String createBy = groupData.getJSONObject(0).getString("createBy");
		String answerThought = groupData.getJSONObject(0).getString("answerThought");
		
		//獲取題目選項信息
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

		for(int i=1;i<=6;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			String title = groupData.getJSONObject(0).getString("title"+i);
			String correctFlag = groupData.getJSONObject(0).getString("correctFlag"+i);
			String acontent = groupData.getJSONObject(0).getString("content"+i);
			if(!title.equals("")&&!correctFlag.equals("")&&!acontent.equals("")){
				map.put("id", id);
				map.put("title",title);
				map.put("correctFlag", correctFlag);
				map.put("acontent", acontent);
				map.put("createBy", createBy);
				map.put("createBy", createBy);
				list.add(map);
			}else if(title.equals("")&&correctFlag.equals("")&&acontent.equals("")){
				
			}
			else if(title.equals("")||correctFlag.equals("")||acontent.equals("")){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写完整数据", "");
			}	
		}
        
        
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("questionClassifyId", questionClassifyId);
		data.put("questionType", questionType);
		data.put("difficultyLevel", difficultyLevel);
		data.put("enableStatus", enableStatus);
		data.put("content", content);
		data.put("updateBy", updateBy);
		data.put("createBy", SessionUtils.getUserInfo().getEmpId());
		System.out.println("data:"+data);
		
		
		CustomFormModel cModel = new CustomFormModel();
		cModel = new CustomFormModel();
		cModel.setData(data);
		cModel.setSqlId("khAdmin/question/saveQuestion");		
		formMapper.saveCustom(cModel);
		
		
		for(int i=0;i<list.size();i++){
			cModel.setData(list.get(i));
			cModel.setSqlId("khAdmin/question/saveQuestionAnser");		
			formMapper.saveCustom(cModel);
		}
		
	}
}


