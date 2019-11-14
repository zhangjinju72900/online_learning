package com.tedu.plugin.question;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("SaveQuestion1")
public class SaveQuestion1 implements ILogicServicePlugin {

	
	@Resource
	FormMapper formMapper;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		// TODO Auto-generated method stub
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String questionId = map.get("questionId")==null?formModel.getPrimaryFieldValue().toString():map.get("questionId").toString()+"";
		String mode = map.get("Mode").toString();
		String questionClassifyId = map.get("questionClassifyId")+"";
		String questionType = map.get("questionType")+"";
		String difficultyLevel = map.get("difficultyLevel")+"";
		String enableStatus = map.get("enableStatus")+"";
		String content = map.get("content")+"";
		String updateBy = map.get("updateBy")+"";
		String createBy = map.get("createBy")+"";
		int b = Integer.parseInt(createBy);
		String answerThought = map.get("answerThought")+"";
		CustomFormModel cModel = new CustomFormModel();
		
		//獲取題目選項信息
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        boolean flag= true;//判断是否有正确答案
		for(int i=1;i<=6;i++){
			Map<String,Object> map1 = new HashMap<String,Object>();
			String title =  map.get("title"+i)+"";
			String correctFlag = map.get("correctFlag"+i)+"";
			String acontent = map.get("content"+i)+"";
			
			if(!title.equals("")&&!correctFlag.equals("")&&!acontent.equals("")){
				map1.put("questionId", questionId);
				map1.put("content", content);
				map1.put("questionClassifyId", questionClassifyId);
				map1.put("title",title);
				int c = Integer.parseInt(correctFlag);
				map1.put("correctFlag", c);
				map1.put("acontent", acontent);
				map1.put("createBy", b);
				list.add(map1);
			}	
			if(!correctFlag.equals("")&&Integer.parseInt(correctFlag)==0&&!acontent.equals("")) {
				flag = false;
			}
		}
        if(flag) {
        	
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写时至少有一个正确选项", "");
        }
        
		Map<String,Object> data=new HashMap<String,Object>();
		data.put("questionClassifyId", questionClassifyId);
		data.put("questionType", questionType);
		data.put("difficultyLevel", difficultyLevel);
		data.put("enableStatus", enableStatus);
		data.put("answerThought", answerThought);
		data.put("content", content);
		data.put("updateBy", updateBy);
		data.put("createBy", SessionUtils.getUserInfo().getEmpId());
map.put("questionId", questionId);
		
		//CustomFormModel cModel = new CustomFormModel();
		/*cModel = new CustomFormModel();
		cModel.setData(map);
		cModel.setSqlId("khAdmin/question/saveQuestion");		
		formMapper.saveCustom(cModel);
		*/
		//如果是编辑那就删除选项并且新增，修改题目主体信息
		if("Edit".equals(mode)) {
			String primaryId = formModel.getPrimaryFieldValue().toString();
			map.put("primaryId", primaryId);
			cModel = new CustomFormModel();
			cModel.setData(map);
			cModel.setSqlId("khTeacher/myquestion/UpdateQuestionAndDeleteAnswer");		
			formMapper.saveCustom(cModel);
		}
		for(int i=0;i<list.size();i++){
			cModel.setData(list.get(i));
			cModel.setSqlId("khTeacher/myquestion/InsertQuestionAnser");		
			formMapper.saveCustom(cModel);
		}
		
		
	}
	

	

}
