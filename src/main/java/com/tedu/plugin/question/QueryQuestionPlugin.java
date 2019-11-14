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
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.task.SpringUtils;

@Service("QueryQuestionPlugin")
public class QueryQuestionPlugin  implements ILogicPlugin{
	@Resource
	FormService formService;
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		formModel = new FormModel();
		Map<String,Object> map = requestObj.getData();
		String questionId = map.get("id")==null?"":map.get("id").toString();
		if(!"".equals(questionId)) {
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(map);
			qp.getData().put("questionId", questionId);
			qp.setQueryParam("khAdmin/question/QryQuestionAndOption");
			List<Map<String, Object>> questionList = formService.queryBySqlId(qp);
			if(questionList.size()>0) {
				formModel.setData(questionList.get(0));
			}
			
			qp.setQueryParam("khAdmin/question/QryOptionList");
			List<Map<String, Object>> optionList = formService.queryBySqlId(qp);
			for(int i=0;i<optionList.size();i++) {
				Map<String, Object> optionMap = optionList.get(i);
				formModel.getData().put("content"+(i+1), optionMap.get("content"));
				formModel.getData().put("correctFlag"+(i+1), optionMap.get("correctFlag"));
				formModel.getData().put("title"+(i+1), optionMap.get("title"));
			}
		}
		
		
		return formModel;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		Map<String,Object> map = requestObj.getData();
		String questionId = map.get("id")==null?"":map.get("id").toString();
		if(!"".equals(questionId)) {
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(map);
			qp.getData().put("questionId", questionId);
			qp.setQueryParam("khAdmin/question/QryQuestionAndOption");
			List<Map<String, Object>> questionList = formService.queryBySqlId(qp);
			
			if(questionList.size()>0) {
				map = questionList.get(0);
			}
			
			qp.setQueryParam("khAdmin/question/QryOptionList");
			List<Map<String, Object>> optionList = formService.queryBySqlId(qp);
			for(int i=0;i<optionList.size();i++) {
				Map<String, Object> optionMap = optionList.get(i);
				map.put("content"+(i+1), optionMap.get("content"));
				map.put("correctFlag"+(i+1), optionMap.get("correctFlag"));
				map.put("title"+(i+1), optionMap.get("title"));
			}
		}
		
		responseObj.setData(map);
		
	}



}
