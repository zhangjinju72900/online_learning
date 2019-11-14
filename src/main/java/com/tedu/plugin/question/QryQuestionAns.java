package com.tedu.plugin.question;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service("QryQuestionAns")
public class QryQuestionAns implements ILogicPlugin {

	String querySql ="khAdmin/question/QryAnserDetal";
	@Resource
	FormMapper formMapper;
	
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
	
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,FormEngineResponse responseObj) {
		
		Map<String, Object> map = (Map<String, Object>)requestObj.getData();
		String questionId = (String)map.get("id");
		
		Map<String,Object> data=new HashMap<String,Object>();
		QueryPage queryPage = new QueryPage();
		data.put("questionId", questionId);
		queryPage.setDataByMap(data);
		queryPage.setQueryParam(querySql);
		List<Map<String, Object>> list = formMapper.query(queryPage);
		

		for(int i=0;i<list.size();i++){
			int j = i+1;
			String title ="title"+j;
			String content ="content"+j;
			String correctFlag ="correctFlag"+j;
			System.out.println(list.get(i).toString());
			map.put(title, list.get(i).get("title"));
			map.put(content, list.get(i).get("content"));
			map.put(correctFlag, list.get(i).get("correctFlag"));
			
		}
		System.out.println(map);
		/*map.put("content", "");
		map.put("answerThought", "");*/
		System.out.println(map);
		responseObj.setData(map);
	}
	

}
