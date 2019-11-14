package com.tedu.plugin.teacher;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
/**
 * 在保存t_notice_user_role、t_notice_region
 * @author 
 *
 */

@Service("insertPlanPlugin")
public class insertPlanPlugin implements ILogicPlugin {
	@Resource
	FormMapper formMapper;
	
	String lessonData="khTeacher/lessonPlan/deleteplandata";
	String stepData="khTeacher/lessonPlan/deletestepdata";
	String lessonDataAdd="khTeacher/lessonPlan/insertplandata";
	String stepDataAdd="khTeacher/lessonPlan/insertstepdata";
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 Map<String,Object> map=(Map<String, Object>) requestObj.getData();
		  System.out.println("map:"+map);
		  
		//删除步骤
		  CustomFormModel cModelStep = new CustomFormModel();
		  cModelStep.setSqlId(stepData);
		  cModelStep.setData(map);
		  formMapper.saveCustom(cModelStep);
		  System.out.println("步骤删除成功");
		  
		  //删除教案
		  CustomFormModel cModelLesson = new CustomFormModel();
		  cModelLesson.setSqlId(lessonData);
		  cModelLesson.setData(map);
		  formMapper.saveCustom(cModelLesson);
		  System.out.println("教案删除成功");
		  
		  
		 
		  //重新插入教案
		  CustomFormModel cModelLessonAdd = new CustomFormModel();
		  cModelLessonAdd.setSqlId(lessonDataAdd);
		  cModelLessonAdd.setData(map);
		  formMapper.saveCustom(cModelLessonAdd);
		  System.out.println("插入教案");
		  
		  int count =0;
		  //重新插入步骤
		  List<Map<String,Object>> stepMap =  (List<Map<String, Object>>) map.get("step");   //获取步骤中的数据
		  System.out.println(stepMap);
		  for(Map<String,Object> step: stepMap) {   //遍历每一个步骤，然后将其进行一一插入
			  count++;
			  CustomFormModel cModelStepAdd = new CustomFormModel();
			  cModelStepAdd.setSqlId(stepDataAdd);
			  cModelStepAdd.setData(step);
			  formMapper.saveCustom(cModelStepAdd);
			  System.out.println("插入第"+count+"步骤成功");  
		  }
		 
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		

	}

}
