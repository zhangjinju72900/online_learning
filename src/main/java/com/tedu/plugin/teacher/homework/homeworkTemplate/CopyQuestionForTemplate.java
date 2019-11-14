package com.tedu.plugin.teacher.homework.homeworkTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.oss.service.OssQueryService;
/**
 * 判斷題目數量
 * @author quancong
 *
 */
@Service("CopyQuestionForTemplate")
public class CopyQuestionForTemplate implements ILogicPlugin {
	@Resource
	FormService formService;
	@Resource
	private OssQueryService ossQueryServiceImpl;
	private  String sql = "khTeacher/homework/homeworkTemplate/QryTemplateDetail";
	private  String copySql = "khTeacher/homework/homeworkTemplate/CopyQuestionList";
	private  String copyQFileSql = "khTeacher/homework/homeworkTemplate/CopyQuestionFileList";
	private  String copyQOptionSql = "khTeacher/homework/homeworkTemplate/CopyQuestionOptionList";
	private  String questionSql = "khTeacher/homework/homeworkTemplate/QryQuestionByUUID";
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   Map<String, Object> map = (Map<String, Object>) requestObj.getData();
		   String  homeworkTemplateId =map.get("homeworkTemplateId")!=null?map.get("homeworkTemplateId").toString():"";
		   //获取模板所有题目
		   List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		   //从模板导入
		   if(!"".equals(homeworkTemplateId)) {
			   HashMap paramMap = new HashMap<>();
			    QueryPage qp = new QueryPage();
				qp.setParamsByMap(paramMap);
				qp.getData().put("homeworkTemplateId", homeworkTemplateId);
				qp.setQueryParam(sql);
				qp.setDataByMap(paramMap);
			   //获取模板所有题目
			   List<Map<String, Object>> questionList =  formService.queryBySqlId(qp);
			   //获取uuid 
			   //创建新的题目，不用模板中的题目
			   Map<String, Object> rowsMap = new HashMap<String, Object>();
			   if(questionList.size()>0){
				   resultMap.put("total", questionList.size());
				   for(int i=0;i<questionList.size();i++) {
					   String uuid = UUID.randomUUID().toString().replace("-", "").substring(0,32);
					   String questionId =questionList.get(i).get("questionId")+"";
					   CustomFormModel cModel = new CustomFormModel();
					   map.put("uuid", uuid);
					   map.put("questionId", questionId);
					   //复制题目
					   cModel.setData(map);
					   cModel.setSqlId(copySql);
					   formMapper.saveCustom(cModel);
					   
					   //获取创建好的题目
					   qp.getData().put("uuid", uuid);
					   qp.setQueryParam(questionSql);
					   qp.setDataByMap(paramMap);
					   //获取模板所有题目
					   List<Map<String, Object>> questions = formMapper.query(qp);
					   if(questions.size()>0) {
						   resultList.add(questions.get(0));
						   String questionType = questionList.get(0).get("questionType").toString();
						   //复制模板中的附件
						   //主观题包含附件
						   if("3".equals(questionType)) {
							   map.put("qId", questions.get(0).get("questionId").toString());
							   cModel.setSqlId(copyQFileSql);
							   formMapper.saveCustom(cModel);
						   }else {
							   //客观题需要复制选项
							   map.put("qId", questions.get(0).get("questionId").toString());
							   cModel.setSqlId(copyQOptionSql);
							   formMapper.saveCustom(cModel);
						   }
					   }
				   }
				  
				  
			   }
		   }
		   //客观题选择
		   else {
			    HashMap paramMap = new HashMap<>();
			    QueryPage qp = new QueryPage();
				qp.setParamsByMap(paramMap);
				qp.setQueryParam(sql);
				qp.setDataByMap(paramMap);
			 
			   //获取uuid 
			   //创建新的题目，不用模板中的题目
			   Map<String, Object> rowsMap = new HashMap<String, Object>();
			   String [] questionArray =  map.get("questionId").toString().split(",");
			   if(questionArray.length>0){
				   resultMap.put("total", questionArray.length);
				   for(int i=0;i<questionArray.length;i++) {
					   String uuid = UUID.randomUUID().toString().replace("-", "").substring(0,32);
					   String questionId =questionArray[i];
					   CustomFormModel cModel = new CustomFormModel();
					   map.put("uuid", uuid);
					   map.put("questionId", questionId);
					   //复制题目
					   cModel.setData(map);
					   cModel.setSqlId(copySql);
					   formMapper.saveCustom(cModel);
					   
					   //获取创建好的题目
					   qp.getData().put("uuid", uuid);
					   qp.setQueryParam(questionSql);
					   qp.setDataByMap(paramMap);
					   List<Map<String, Object>> questions = formMapper.query(qp);
					   if(questions.size()>0) {
						   resultList.add(questions.get(0));
						   String questionType = questions.get(0).get("questionType").toString();
							   //客观题需要复制选项
						   map.put("qId", questions.get(0).get("questionId").toString());
						   cModel.setSqlId(copyQOptionSql);
						   formMapper.saveCustom(cModel);
					   }
				   }
				  
				  
			   }
		   }
		 
		
		   resultMap.put("rows", resultList);
		   responseObj.setData(resultMap);
		
  		
	}

}
