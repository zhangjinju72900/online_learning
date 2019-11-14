package com.tedu.plugin.teacher.homework.homeworkDistribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
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
 * 保存草稿明细信息
 * 
 * @author quancong
 *
 */
@Service("saveDraftPlugin")
public class SaveDraftPlugin implements ILogicServicePlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		
		if(map.get("courseId")==null || StringUtils.isEmpty(map.get("courseId").toString())){
			map.put("courseId", 0);
		}
		 
		if(map.get("sectionId")==null || StringUtils.isEmpty(map.get("sectionId").toString())){
			map.put("sectionId", 0);
		}
		
		if(map.get("source")==null || StringUtils.isEmpty(map.get("source").toString())){
			map.put("source", 0);
		}
		
		  String classId = map.get("classId").toString();
		  String homeworkType = map.get("homeworkType").toString();
		  String difficultyLevel = map.get("difficultyLevel").toString();
		  String endTime = map.get("endTime").toString();
		  String questionIdScore = map.get("questionIdScore").toString();
		  if(classId.equals("")||homeworkType.equals("")||difficultyLevel.equals("")||endTime.equals("")||questionIdScore.equals("")){
		   throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"信息填写不完整 ","请将信息填写完整");
		 }
		
		
		// 查询当前用户的学校id
		QueryPage queryPage = new QueryPage();
		String schoolId = "";
		queryPage.setDataByMap(map);
		queryPage.setQueryParam("khTeacher/homework/homeworkDistribute/QryTeacherSchool");
		List<Map<String, Object>> schoolList = formMapper.query(queryPage);
		if (schoolList.size() > 0) {
			schoolId = schoolList.get(0).get("schoolId") + "";
		}
		map.put("schoolId", schoolId);
		return formModel;
	}

	/*@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
	}*/

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		log.info(formModel.getData());
		// 获取主表id
		String homeworkId = formModel.getPrimaryFieldValue();
		Map<String, Object> map = (Map<String, Object>) requestObj.getData();
		// 获得题目id和分数字符串如"1-5,2-5"
		String[] ids = map.get("questionIdScore").toString().split(",");
		String source = map.get("source")==null||org.apache.commons.lang3.StringUtils.isBlank(map.get("source").toString())?"1":map.get("source").toString();//默认导入
		// 判断homeworkTemplateId是否为空
		if (StringUtils.isEmpty(map.get("homeworkTemplateId").toString())) {
			map.put("homeworkTemplateId", 0);
		}
		map.put("homeworkId", homeworkId);
		log.info("ids: " + ids.toString());
		CustomFormModel cModel = new CustomFormModel();
		cModel.setData(map);
		cModel.setSqlId("khTeacher/homework/homeworkDistribute/InsertHomeworkDetail");
		// 保存作业模板详情信息
		for (String questionIdScores : ids) {
			String[] questionIdScore = questionIdScores.split("-");
			String questionId = questionIdScore[0];
			String score = "0";
			if (questionIdScore.length > 1) {
				if (!StringUtils.isEmpty(questionIdScore[1])) {
					score = questionIdScore[1];
				}

			}
			map.put("source", source);
			map.put("questionId", questionId);
			map.put("score", score);
			formMapper.saveCustom(cModel);
		}
		
	}

}
