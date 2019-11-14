package com.tedu.plugin.course;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
/**
 * 在保存课表信息之后将数据保存到 t_curriculum_course表中
 * 
 */
@Service("insertCurriculumCoursePlugin2")
public class InsertCurriculumCoursePlugin2 implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String mode = map.get("Mode")==null?"":map.get("Mode").toString();
		  String id = map.get("id")+"";
		  
			//根据专业id查询该专业下的课程
				 String proId = map.get("proId")+"";
				  QueryPage queryPage=new QueryPage(); 
				  queryPage.setQueryParam("course/QryCourseByProId");
				  queryPage.setDataByMap(map);
				  List<Map<String,Object>> list=formMapper.query(queryPage);
				  String curriculumName = map.get("curriculumName")==null?"":map.get("curriculumName").toString();
				  String schoolId = map.get("eq_schoolId")==null?"":map.get("eq_schoolId").toString();
				  
				//查询选择的课程
				  String ides1 = map.get("courseId1")==null?"":map.get("courseId1").toString();
				  String ides2 = map.get("courseId2")==null?"":map.get("courseId2").toString();
				  String ides3 = map.get("courseId3")==null?"":map.get("courseId3").toString();
				  String ides4 = map.get("courseId4")==null?"":map.get("courseId4").toString();
				  String ides5 = map.get("courseId5")==null?"":map.get("courseId5").toString();
				 
				  String[] ids1=ides1.startsWith("[")?ides1.substring(1, ides1.length()-1).split(","):ides1.split(",");
				  String[] ids2=ides2.startsWith("[")?ides2.substring(1, ides2.length()-1).split(","):ides2.split(",");
				  String[] ids3=ides3.startsWith("[")?ides3.substring(1, ides3.length()-1).split(","):ides3.split(",");
				  String[] ids4=ides4.startsWith("[")?ides4.substring(1, ides4.length()-1).split(","):ides4.split(",");
				  String[] ids5=ides5.startsWith("[")?ides5.substring(1, ides5.length()-1).split(","):ides5.split(",");
				//判断选择的课程是否属于选择的专业
				  checkCourse(ids1,list,1);
				  checkCourse(ids2,list,2);
				  checkCourse(ids3,list,3);
				  checkCourse(ids4,list,4);
				  checkCourse(ids5,list,5);
				  
				  //判断课表是否相同
				  QueryPage qp = new QueryPage();
				  qp.getData().put("schoolId", schoolId);

				  if("".equals(id)){
					  qp.getData().put("Id",0);
				  }else{
					  qp.getData().put("Id", Integer.parseInt(id));
				  }
				  
				  qp.setQueryParam("course/QrycurriculumById");
				  qp.setDataByMap(map);
				  List<Map<String, Object>> fileList = formMapper.query(qp);
				  boolean a1 = false;
				  for(int a =0;a<fileList.size();a++){
					  Map<String, Object> curriculumNameList = fileList.get(a);
					  String curriculumName1 = curriculumNameList.get("curriculumName").toString();
					 
					  if(curriculumName1.equals(curriculumName)){
						  a1 = true;
					  }
					  if(a1){
						  throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "课表名称存在", "请重新输入"); 
					  }
					  
				  }
				  for(int a =0;a<fileList.size();a++){
					  Map<String, Object> curriculumNameList = fileList.get(a);
					  String curriculumId = curriculumNameList.get("curriculumId").toString();
					  qp.getData().put("curriculumId", curriculumId);
					  qp.setQueryParam("course/QryCurriculumDetail2");
					  qp.setDataByMap(map);
					  List<Map<String, Object>> curriculumIdList = formMapper.query(qp);
					
						  Map<String, Object> curriculumIdMap = curriculumIdList.get(0);
						  String courseId11 = curriculumIdMap.get("courseId1")==null?"":curriculumIdMap.get("courseId1").toString();
						  String courseId22 = curriculumIdMap.get("courseId2")==null?"":curriculumIdMap.get("courseId2").toString();
						  String courseId33 = curriculumIdMap.get("courseId3")==null?"":curriculumIdMap.get("courseId3").toString();
						  String courseId44 = curriculumIdMap.get("courseId4")==null?"":curriculumIdMap.get("courseId4").toString();
						  String courseId55 = curriculumIdMap.get("courseId5")==null?"":curriculumIdMap.get("courseId5").toString();
						  boolean courseIdFlag1 = checkcourseId(courseId11,ids1);
						  boolean courseIdFlag2 = checkcourseId(courseId22,ids2);
						  boolean courseIdFlag3 = checkcourseId(courseId33,ids3);
						  boolean courseIdFlag4 = checkcourseId(courseId44,ids4);
						  boolean courseIdFlag5 = checkcourseId(courseId55,ids5);
						  
						  if("Add".equals(mode)){
							  if(courseIdFlag1&&courseIdFlag2&&courseIdFlag3&&courseIdFlag4&&courseIdFlag5){
								  throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "课表已经存在", "请重新选择"); 
							  }
						  }
						  
				  }
				  
				 
				  
				  
				 
		 

		
		return null;
	}
	
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  //获取主表的id
		  String curriculumId=formModel.getPrimaryFieldValue().toString();
		  //获取roleid并解析
		  String ides1 = map.get("courseId1")==null?"":map.get("courseId1").toString();
		  String ides2 = map.get("courseId2")==null?"":map.get("courseId2").toString();
		  String ides3 = map.get("courseId3")==null?"":map.get("courseId3").toString();
		  String ides4 = map.get("courseId4")==null?"":map.get("courseId4").toString();
		  String ides5 = map.get("courseId5")==null?"":map.get("courseId5").toString();
		 
		  String[] ids1=ides1.startsWith("[")?ides1.substring(1, ides1.length()-1).split(","):ides1.split(",");
		  String[] ids2=ides2.startsWith("[")?ides2.substring(1, ides2.length()-1).split(","):ides2.split(",");
		  String[] ids3=ides3.startsWith("[")?ides3.substring(1, ides3.length()-1).split(","):ides3.split(",");
		  String[] ids4=ides4.startsWith("[")?ides4.substring(1, ides4.length()-1).split(","):ides4.split(",");
		  String[] ids5=ides5.startsWith("[")?ides5.substring(1, ides5.length()-1).split(","):ides5.split(",");
		 
		  LogUtil.info(ides1);
		  
		  String mode = map.get("Mode")==null?"":map.get("Mode").toString();
		  if("Edit".equals(mode)){
			  String updateSql = "DeleteCurriculumCourse";
			  CustomFormModel cModel = new CustomFormModel();
			  cModel.setSqlId(updateSql);
			  cModel.setData(formModel.getData());		
			  Map<String, Object> logData = formModel.getData();
			  logData.put("id", curriculumId);
			  formMapper.saveCustom(cModel);
		  }
		  CustomFormModel cModel = new CustomFormModel();
		  
		  cModel.setSqlId("InsertCurriculumCourse1");
		  cModel.setData(formModel.getData());
		  Map<String, Object> logData = formModel.getData();
		 
		  for(String courseId1 : ids1) {
				  if(!(courseId1.length()<=0)){
					  logData.put("curriculum_id", curriculumId);
					  logData.put("course_id", courseId1);
					  logData.put("grade_type", 1);
					  //logData.put("class_id", classId);
					  LogUtil.info(cModel.getData().toString());
					  formMapper.saveCustom(cModel);
				  }	
		  } 
		  for(String courseId2 : ids2) {
			  if(!(courseId2.length()<=0)){
				  logData.put("curriculum_id", curriculumId);
				  logData.put("course_id", courseId2);
				  logData.put("grade_type", 2);
				  //logData.put("class_id", classId);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }	
	  }
		  for(String courseId3 : ids3) {
			  if(!(courseId3.length()<=0)){
				  logData.put("curriculum_id", curriculumId);
				  logData.put("course_id", courseId3);
				  logData.put("grade_type", 3);
				  //logData.put("class_id", classId);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }	
	  }
		  for(String courseId4 : ids4) {
			  if(!(courseId4.length()<=0)){
				  logData.put("curriculum_id", curriculumId);
				  logData.put("course_id", courseId4);
				  logData.put("grade_type", 4);
				  //logData.put("class_id", classId);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }	
	  }
		  for(String courseId5 : ids5) {
			  if(!(courseId5.length()<=0)){
				  logData.put("curriculum_id", curriculumId);
				  logData.put("course_id", courseId5);
				  logData.put("grade_type", 5);
				  //logData.put("class_id", classId);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }	
	  }
		 
	}
	 public static boolean checkcourseId(String a,String[] courseIds ){
		 boolean flag =false;
		 int count =0;
		 String[] cId1 = a.split(",");
		  for(int s =0;s<courseIds.length;s++){
			  String cId = courseIds[s];
			  if(!a.equals("")){ 
				  if(courseIds.length==cId1.length){
					  for(int j =0;j<cId1.length;j++){
						  String cId2 = cId1[j];
						  if(cId.equals(cId2)){
							  count++;
						  }
					  } 
				  }else{
					  flag =false;
					  break;
				  }
				  
			  }else{
				  break;
			  }
		  }
		  if(count == cId1.length){
			  flag = true;
		  }else{
			  flag = false;
		  }
		  if(cId1[0].equals("")){
			  flag = true;
		  }
		return flag;
		  
	  }
	public static void checkCourse( String[] courseIds,List<Map<String,Object>> list,int index){
		
		String courseId = "";
	  for(String courseId1 : courseIds) {
		  if(null==courseId1 || "".equals(courseId1)){
			  return;
		  }
		  boolean flag = false;
		  if(list.size()>0 && courseId1.length()>0){
			  for(int i=0,size = list.size();i<size;i++){
				 courseId = list.get(i).get("courseId")+"";
				  if(courseId1.equals(courseId)){
					  flag = true;
					  break;
				  }
			  }  
		  }	
		  if(!flag){
			  throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "专业已改变", "请重新选择"+index+"年级课程");
		  }
  } 
	 
}

}
