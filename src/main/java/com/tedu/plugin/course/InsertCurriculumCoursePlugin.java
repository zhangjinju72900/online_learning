package com.tedu.plugin.course;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
/**
 * 在保存课表信息之后将数据保存到t_class_curriculum & t_curriculum_course表中
 * @author gonghaoxin
 *
 */
@Service("insertCurriculumCoursePlugin")
public class InsertCurriculumCoursePlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		//log.info(formModel.getData().get("explain").toString());
		return null;
	}
	
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  //获取主表的id
		  String curriculumId=formModel.getPrimaryFieldValue().toString();
		  //获取roleid并解析
		  String ides = map.get("courseId")==null?"":map.get("courseId").toString();
		  String schoolId = map.get("schoolId")==null?"":map.get("schoolId").toString();
		  String[] ids=ides.startsWith("[")?ides.substring(1, ides.length()-1).split(","):ides.split(",");
		  
		  String mode = map.get("Mode")==null?"":map.get("Mode").toString();
		  //为编辑模式时先将原来的主表id对应的记录然后重新插入
		  if("Edit".equals(mode)){
			  String updateSql = "DeleteCurriculumCourse";
			  CustomFormModel cModel = new CustomFormModel();
			  cModel.setSqlId(updateSql);
			  cModel.setData(formModel.getData());		
			  Map<String, Object> logData = formModel.getData();
			  logData.put("id", curriculumId);
			  logData.put("school_id", schoolId);
			  LogUtil.info(cModel.getData().toString());
			  formMapper.saveCustom(cModel);
		  }
		  
		  //新增课表把数据插入到t_curriculum_course表和t_class_curriculum表中
		  CustomFormModel cModel = new CustomFormModel();
		  cModel.setSqlId("InsertCurriculumCourse");
		  cModel.setData(formModel.getData());
		  Map<String, Object> logData = formModel.getData();
		  //当未选取roleId时，不执行插入
		  for(String courseId : ids) {
			  if(!(courseId.length()<=0)){
				  logData.put("curriculum_id", curriculumId);
				  logData.put("course_id", courseId);
				  logData.put("school_id", schoolId);
				  //logData.put("class_id", classId);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }	
		  }
		
	}

}
