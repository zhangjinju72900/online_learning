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

@Service("SaveQuestionAns")
public class SaveQuestionAns implements ILogicPlugin {

	
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
		
		//獲取題目選項信息
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        boolean flag= true;//判断是否有正确答案
        int t = 0;
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
			else if(title.equals("")||correctFlag.equals("")||acontent.equals("")){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写完整数据", "");
				
			}	
			if(!correctFlag.equals("")&&c==0) {
				flag = false;
				t = t+1;
				if(t>1&&questionType.equals("0")){
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "当前题型只能有一个正确选项", "");
				}
			}
			
		}
		if(flag) {
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写时至少有一个正确选项", "");
        }
        
		for(int i=0;i<list.size();i++){
        	for(int j=0;j<list.size();j++){
        		if(i!=j&&(list.get(i).get("title").equals(list.get(j).get("title"))||
        				list.get(i).get("acontent").equals(list.get(j).get("acontent")))){
        			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "不能存在选项标题或选项内容一样的选项", "");
        		}
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
		
		
		if(list.size()<2){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请至少添加两个选项", "");
		}else{
			for(int i=0;i<list.size();i++){
				cModel.setData(list.get(i));
				System.out.println(list);
				cModel.setSqlId("khAdmin/question/saveQuestionAnser2");		
				formMapper.saveCustom(cModel);
			}
		}
		
		
	}
	

}
