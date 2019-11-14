package com.tedu.plugin.question;

import com.aliyun.oss.OSS;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.util.VideoUtil;
import com.tedu.component.BackEndVideoExecute;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

@Service("UpdateQuesAns")
public class UpdateQuesAns implements ILogicPlugin {

	
	@Resource
	FormMapper formMapper;
	
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
	
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		//FormMapper formMapper = SpringUtils.getBean("simpleDao");
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		System.out.println(formModel.getData());
		String mode = map.get("Mode").toString();
		String id = map.get("id")+"";
		String questionClassifyId = map.get("questionClassifyId")+"";
		String questionType = map.get("questionType")+"";
		String difficultyLevel = map.get("difficultyLevel")+"";
		String enableStatus = map.get("enableStatus")+"";
		String content = map.get("content")+"";
		String updateBy = map.get("updateBy")+"";
		String createBy = map.get("createBy")+"";
		int b = Integer.parseInt(createBy);
		int u = Integer.parseInt(updateBy);
		String answerThought = map.get("answerThought")+"";
		CustomFormModel cModel = new CustomFormModel();

		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(int i=1;i<=6;i++){
			Map<String,Object> map1 = new HashMap<String,Object>();
			String title =  map.get("title"+i)+"";
			String correctFlag = map.get("correctFlag"+i)+"";
			String acontent = map.get("content"+i)+"";
			int c = 0;
			if(correctFlag.equals("否")||correctFlag.equals("1")){
				c = c+1;
			}
			if(!title.equals("")&&!correctFlag.equals("")&&!acontent.equals("")){
				map1.put("id", id);
				map1.put("content", content);
				map1.put("questionClassifyId", questionClassifyId);
				map1.put("title",title);
				map1.put("correctFlag", c);
				map1.put("acontent", acontent);
				map1.put("updateBy", u);
				list.add(map1);
			}else if(title.equals("")&&correctFlag.equals("")&&acontent.equals("")){
			}
		}
        
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("id", id);
		data.put("questionClassifyId", questionClassifyId);
		data.put("questionType", questionType);
		data.put("difficultyLevel", difficultyLevel);
		data.put("enableStatus", enableStatus);
		data.put("answerThought", answerThought);
		data.put("content", content);
		data.put("updateBy", SessionUtils.getUserInfo().getEmpId());
		cModel.setData(data);
		cModel.setSqlId("khAdmin/question/deleteQuestion");		
		formMapper.saveCustom(cModel);
		
		for(int i=0;i<list.size();i++){
			cModel.setData(list.get(i));
			System.out.println(list);
			cModel.setSqlId("khAdmin/question/saveQuestionAnser2");		
			formMapper.saveCustom(cModel);
		}
		
		
	}
	

}
