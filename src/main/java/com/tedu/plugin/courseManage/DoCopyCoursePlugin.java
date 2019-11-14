package com.tedu.plugin.courseManage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tedu.base.engine.aspect.ILogicServicePlugin;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
/**
 * 课程管理-专业管理    操作-复制功能的实现
 * @author zhangzhiming
 * 
 */
@Service("DoCopyCoursePlugin")
public class DoCopyCoursePlugin implements ILogicServicePlugin{
	public final Logger log = Logger.getLogger(this.getClass());
	@Resource
	private FormMapper formMapper;
	@Resource
	FormService formService;
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
	}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		log.info(formModel.getData());
		CustomFormModel cModel = new CustomFormModel();
		
		Map<String, Object> map = formModel.getData();
		map.put("id", formModel.getPrimaryFieldValue());
		//复制课程
		cModel.setSqlId("zhongdeprofession/InsertCopyCourse");
		cModel.setData(map);
		int r=formMapper.saveCustom(cModel);
		map.put("", "");
		//获取新增课程Id
		String courseId=cModel.getPrimaryFieldValue();
		map.put("courseId", courseId);
		map.put("pid", "cou-"+map.get("id"));
		//复制课程-领域标签
		insertCopyCourse("zhongdeprofession/InsertCopyCourseLabel",map);
		insertCopyCourse("zhongdeprofession/InsertCopyCourseSectionRes",map);
		if(r==1){
			
			
			// 获取原课程下所有小节
			QueryPage query = new QueryPage();
			query.setDataByMap(formModel.getData());
			query.getData().put("courseId", map.get("id"));
			query.setQueryParam("zhongdeprofession/QrySectionByCourseId");
			List<Map<String, Object>> secList=formService.queryBySqlId(query);
			//循环遍历小节并复制小节
			for(Map<String, Object> secMap:secList){
				secMap.put("course_id",courseId);
				//复制对应小节并获取新增小节Id
				String secId =insertCopyCourse("zhongdeprofession/InsertCopyCourseSection",secMap).getPrimaryFieldValue();
				//获取原小节下所有标签
				
				query.setQueryParam("zhongdeprofession/QryLabelBySecId");
				query.getData().put("id", secMap.get("id"));
				query.setDataByMap(secMap);
				List<Map<String, Object>> LabList=formService.queryBySqlId(query);
				for(Map<String, Object> labMap:LabList){
					labMap.put("courseId",formModel.getPrimaryFieldValue());
					labMap.put("secId", secId);
					//复制小节标签
					String labId = insertCopyCourse("zhongdeprofession/InsertCopyCourseSectionLabel",labMap).getPrimaryFieldValue();
					//获取标签下资源
					query.setQueryParam("zhongdeprofession/QryCourseResourceByLabId");
					query.setDataByMap(labMap);
					query.getData().put("label_id", labMap.get("label_id"));
					query.getData().put("section_id", labMap.get("section_id"));
					
					List<Map<String, Object>> resourceList=formService.queryBySqlId(query);
					for(Map<String, Object> sourceMap:resourceList){
						sourceMap.put("course_id", courseId);
						sourceMap.put("section_id", secId);
						//复制标签下资源
						insertCopyCourse("zhongdeprofession/InsertCopyCourseResource",sourceMap);
					}
				}
				
			}
			
			/*
			//System.out.println("courseId------------"+courseId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", formModel.getData("id"));
			map.put("courseId", courseId);
			//复制课程-标签
			insertCopyCourse("zhongdeprofession/",map);
			//复制课程-小节
			QueryPage query = new QueryPage();
			query.setQueryParam("zhongdeprofession/QryCourseSection");
			query.setDataByMap(map);
			List<Map<String, Object>> secList=formMapper.query(query);
			String id="";
			String name="";
			String remark="";
			String showOrder="";
			String validFlag="";
			map.put("name", name);
			map.put("remark", remark);
			map.put("showOrder", showOrder);
			map.put("validFlag", validFlag);
			Map<String, Object> idMap=new HashMap<String, Object>();
			for (int i = 0; i < secList.size(); i++) {
				id=secList.get(i).get("id").toString();
				name=secList.get(i).get("name").toString();
				remark=secList.get(i).get("remark").toString();
				showOrder=secList.get(i).get("showOrder").toString();
				validFlag=secList.get(i).get("validFlag").toString();
				map.replace("name", name);
				map.replace("remark", remark);
				map.replace("showOrder", showOrder);
				map.replace("validFlag", validFlag);
				idMap.put(id,insertCopyCourse("zhongdeprofession/InsertCopyCourseSection",map).getPrimaryFieldValue());
			}
			//复制课程-小节-标签
			insertCopyCourse("zhongdeprofession/InsertCopyCourseSectionLabel",map);
			//复制课程-标签-资源、课程-小节-标签-资源
			QueryPage sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("zhongdeprofession/QryCourseResource");
			sqlQuery.setDataByMap(map);
			List<Map<String, Object>> list=formMapper.query(sqlQuery);
			String resourceId="";
			String sectionId="";
			String labelId="";
			map.put("sectionId", sectionId);
			map.put("resourceId", resourceId);
			map.put("labelId", labelId);
			for (int i = 0; i < list.size(); i++) {
				resourceId= list.get(i).get("resourceId").toString();
				sectionId= list.get(i).get("sectionId").toString();
				labelId= list.get(i).get("labelId").toString();
				if(!sectionId.equals("0")){
					if(!idMap.containsKey(sectionId)){
						continue;
					}else{
						sectionId=idMap.get(sectionId).toString();
					}
				}
				map.replace("sectionId", sectionId);
				map.replace("resourceId", resourceId);
				map.replace("labelId", labelId);
				insertCopyCourse("zhongdeprofession/InsertCopyCourseResource",map);
			}
		*/}
	}
	
	public CustomFormModel insertCopyCourse(String str,Map<String, Object> map){
		CustomFormModel cModel = new CustomFormModel();
		cModel=new CustomFormModel();
		cModel.setSqlId(str);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		return cModel;
	}

}
