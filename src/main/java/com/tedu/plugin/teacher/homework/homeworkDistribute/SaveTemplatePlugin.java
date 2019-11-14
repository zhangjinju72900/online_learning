package com.tedu.plugin.teacher.homework.homeworkDistribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
/**
 * 保存为作业模板
 * @author quancong
 *
 */
@Service("saveTemplatePlugin")
public class SaveTemplatePlugin implements ILogicServicePlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 log.info(formModel.getData());
		 Map<String,Object> map=(Map<String, Object>) formModel.getData();
		 
		 if(map.get("courseId")==null || StringUtils.isBlank(map.get("courseId").toString())){
			 map.put("courseId", 0);
		 }
		 
		 if(map.get("sectionId")==null || StringUtils.isBlank(map.get("sectionId").toString())){
			 map.put("sectionId", 0);
		 }
		 
		  String templateName = map.get("templateName").toString();
		  String homeworkType = map.get("homeworkType").toString();
		  String difficultyLevel = map.get("difficultyLevel").toString();
		  String questionIds = map.get("questionIds").toString();
		  if(templateName.equals("")||homeworkType.equals("")||difficultyLevel.equals("")||questionIds.equals("")){
		   throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"信息填写不完整 ","请将信息填写完整");
		  }
		  
		//查询当前用户的学校id
		  QueryPage queryPage=new QueryPage();
		  String schoolId ="";
		  queryPage.setDataByMap(map);
		  queryPage.setQueryParam("khTeacher/homework/homeworkDistribute/QryTeacherSchool");
		  List<Map<String,Object>> schoolList=formMapper.query(queryPage);
		  if(schoolList.size()>0){
			  schoolId =schoolList.get(0).get("schoolId")+"";
		  }
		  formModel.getData().put("schoolId", schoolId);
		return formModel;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		  log.info(formModel.getData());
		  //获取主表id
		  String templateId = formModel.getPrimaryFieldValue();
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String[] ids=map.get("questionIds").toString().split(",");
		  map.put("templateId", templateId);
		  log.info("ids: "+ids.toString());
		  CustomFormModel cModel = new CustomFormModel();
		  cModel.setData(map);
		  cModel.setSqlId("khTeacher/homework/homeworkDistribute/InsertTemplateDetail");
		  //保存作业模板详情信息
		  for(String questionId:ids){
				map.put("questionId", questionId);
				formMapper.saveCustom(cModel);
			}
		

	}

}
