package com.tedu.plugin.question;

import com.aliyun.oss.OSS;
import com.itextpdf.text.log.SysoLogger;
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

@Service("SaveQuestionAns2")
public class SaveQuestionAns2 implements ILogicPlugin {

	
	@Resource
	FormMapper formMapper;
	
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		
		String id = map.get("id")+"";
		List l = (List)map.get("pTable");
		
		for(int i=0;i<l.size();i++){
			Map<String,Object> mmm = (Map<String, Object>) l.get(i);
			String del = mmm.get("delete")+"";
			String acontent = mmm.get("content")+"";
			String correctFlag = mmm.get("correctFlag")+"";
			if(!del.equals("1")){
				if(!correctFlag.equals("")&&!acontent.equals("")){
					if(!correctFlag.equals("0")&&!correctFlag.equals("1")){
						throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "试题选项答案只能添加0-是或1-否", "");
					}
				}else if(correctFlag.equals("")&&acontent.equals("")){
					
				}else if(correctFlag.equals("")||acontent.equals("")){
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写完整选项数据", "");
				}
			}
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		System.out.println(formModel.getData());
		String[] arr = {"A","B","C","D","E","F"};
		String mode = map.get("Mode").toString();
		String id = map.get("id")+"";
		String questionClassifyId = map.get("questionClassifyId")+"";
		String questionType = map.get("questionType")+"";
		String difficultyLevel = map.get("difficultyLevel")+"";
		String enableStatus = map.get("enableStatus")+"";
		String content = map.get("content")+"";
		String updateBy = map.get("updateBy")+"";
		int u = Integer.parseInt(updateBy);
		CustomFormModel cModel = new CustomFormModel();
		
		System.out.println("pTable:::::::::::::::"+map.get("pTable"));
		List l = (List)map.get("pTable");
		System.out.println("llllllllllllllllll::::::"+l.size());
		
		Map<String,Object> data=new HashMap<String,Object>();
		QueryPage queryPage = new QueryPage();
		data.put("questionId", id);
		queryPage.setDataByMap(data);
		queryPage.setQueryParam("khAdmin/question/QryAnserDetal");
		List<Map<String, Object>> list = formMapper.query(queryPage);
		
		System.out.println("list:::::::"+list);
		
		//判断题型验证
		if(questionType.equals("2")&&list.size()!=2){
			for(int i=0;i<list.size();i++){
				if(list.get(i).get("title").equals("")){
					data.put("id", list.get(i).get("id")+"");
					cModel = new CustomFormModel();
					cModel.setData(data);
					cModel.setSqlId("khAdmin/question/deleteOption");		
					formMapper.saveCustom(cModel);
				}
			}
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "当前题型只能添加一对一错两个选项", "");
		}else if(questionType.equals("2")&&list.size()==2){
			if(list.get(0).get("content").equals(list.get(1).get("content"))){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("title").equals("")){
						data.put("id", list.get(i).get("id")+"");
						cModel = new CustomFormModel();
						cModel.setData(data);
						cModel.setSqlId("khAdmin/question/deleteOption");		
						formMapper.saveCustom(cModel);
					}
				}
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "选项内容应不能一致", "");
			}
		}
		
		if(!questionType.equals("2")&&(list.size()<2||list.size()>6)){
			for(int i=0;i<list.size();i++){
				if(list.get(i).get("title").equals("")){
					data.put("id", list.get(i).get("id")+"");
					cModel = new CustomFormModel();
					cModel.setData(data);
					cModel.setSqlId("khAdmin/question/deleteOption");		
					formMapper.saveCustom(cModel);
				}
			}
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请添加两个到六个选项", "");
		}
		
		List<Map<String,Object>> list2=new ArrayList<Map<String,Object>>();
		boolean flag= true;//判断是否有正确答案
		int t = 0;
		for(int i=0;i<list.size();i++){
			Map<String,Object> data1=new HashMap<String,Object>();
			for(int j=0;j<list.size();j++){
				if(i!=j&&list.get(i).get("content").equals(list.get(j).get("content"))){
					for(int k=0;k<list.size();k++){
						if(list.get(k).get("title").equals("")){
							data.put("id", list.get(k).get("id")+"");
							cModel = new CustomFormModel();
							cModel.setData(data);
							cModel.setSqlId("khAdmin/question/deleteOption");		
							formMapper.saveCustom(cModel);
						}
					}
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "选项内容应不能一致", "");
				}
			}
			/*if(!list.get(i).get("correctFlag").equals("")&&!list.get(i).get("content").equals("")){
				int c = 0;
				if(list.get(i).get("correctFlag").equals("否")){
					c = c+1;
				}
				data1.put("questionId", id);
				data1.put("title", arr[i]);
				data1.put("acontent", list.get(i).get("content"));
				data1.put("correctFlag", c);
				data1.put("createBy", u);
				list2.add(data1);
			}else if(list.get(i).get("correctFlag").equals("")&&list.get(i).get("content").equals("")){
			}else if(list.get(i).get("correctFlag").equals("")||list.get(i).get("content").equals("")){
				for(int k=0;k<list.size();k++){
					if(list.get(k).get("title").equals("")){
						data.put("id", list.get(k).get("id")+"");
						cModel = new CustomFormModel();
						cModel.setData(data);
						cModel.setSqlId("khAdmin/question/deleteOption");		
						formMapper.saveCustom(cModel);
					}
				}
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写完整选项数据", "");
			}
			if(list.get(i).get("correctFlag").equals("是")&&!list.get(i).get("correctFlag").equals("")) {
				flag = false;
				t = t+1;
				if(t>1&&(questionType.equals("0")||questionType.equals("2"))){
					for(int k=0;k<list.size();k++){
						if(list.get(k).get("title").equals("")){
							data.put("id", list.get(k).get("id")+"");
							cModel = new CustomFormModel();
							cModel.setData(data);
							cModel.setSqlId("khAdmin/question/deleteOption");		
							formMapper.saveCustom(cModel);
						}
					}
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "当前题型只能有一个正确选项", "");
				}
			}*/
		}
		
		int j = -1;
		for(int i=0;i<l.size();i++){
			Map<String,Object> data1=new HashMap<String,Object>();
			Map<String,Object> mmm = (Map<String, Object>) l.get(i);
			String del = mmm.get("delete")+"";
			String acontent = mmm.get("content")+"";
			String correctFlag = mmm.get("correctFlag")+"";
			if(!del.equals("1")){
				j = j+1;
				if(!correctFlag.equals("")&&!acontent.equals("")){
					data1.put("questionId", id);
					data1.put("title", arr[j]);
					data1.put("acontent", acontent);
					data1.put("correctFlag", correctFlag);
					data1.put("createBy", u);
					list2.add(data1);
				}else if(correctFlag.equals("")&&acontent.equals("")){
					
				}else if(correctFlag.equals("")||acontent.equals("")){
					for(int k=0;k<list.size();k++){
						if(list.get(k).get("title").equals("")){
							data.put("id", list.get(k).get("id")+"");
							cModel = new CustomFormModel();
							cModel.setData(data);
							cModel.setSqlId("khAdmin/question/deleteOption");		
							formMapper.saveCustom(cModel);
						}
					}
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写完整选项数据", "");
				}
				if(correctFlag.equals("0")&&!correctFlag.equals("")) {
					flag = false;
					t = t+1;
					if(t>1&&(questionType.equals("0")||questionType.equals("2"))){
						for(int k=0;k<list.size();k++){
							if(list.get(k).get("title").equals("")){
								data.put("id", list.get(k).get("id")+"");
								cModel = new CustomFormModel();
								cModel.setData(data);
								cModel.setSqlId("khAdmin/question/deleteOption");		
								formMapper.saveCustom(cModel);
							}
						}
						throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "当前题型只能有一个正确选项", "");
					}
				}
			}
		}
		
		if(flag) {
			for(int k=0;k<list.size();k++){
				if(list.get(k).get("title").equals("")){
					data.put("id", list.get(k).get("id")+"");
					cModel = new CustomFormModel();
					cModel.setData(data);
					cModel.setSqlId("khAdmin/question/deleteOption");		
					formMapper.saveCustom(cModel);
				}
			}
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "请填写时至少有一个正确选项", "");
        }
		
		cModel = new CustomFormModel();
		cModel.setData(data);
		cModel.setSqlId("khAdmin/question/deleteOpt");		
		formMapper.saveCustom(cModel);
		
		for(int i=0;i<list2.size();i++){
			cModel.setData(list2.get(i));
			cModel.setSqlId("khAdmin/question/saveQuestionOption");		
			formMapper.saveCustom(cModel);
		}
		
		
	}
	

}
