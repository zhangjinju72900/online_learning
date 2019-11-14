package com.tedu.plugin.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.model.DataGrid;
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
 * 在保存课表信息之后将数据保存到t_class_curriculum & t_curriculum_course表中
 * @author gonghaoxin
 *
 */
@Service("courseInfoPlugin")
public class CourseInfoPlugin implements ILogicPlugin {
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
		Map<String,Object> data = requestObj.getData();
		DataGrid dgData = (DataGrid) responseObj.getData();
		List<Map<String, Object>> courses = (List<Map<String, Object>>) dgData.getRows();
		
		QueryPage sqlQuery2 = new QueryPage();
		sqlQuery2.setQueryParam("khApp/study/QryRecord");
		sqlQuery2.setQueryType("");
		sqlQuery2.setSingle(1);
		sqlQuery2.setSqlId("khApp/study/QryRecord");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("courseId", data.get("courseId").toString());
		sqlQuery2.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery2);
		
		if(tables != null && tables.size() > 0 && courses != null && courses.size() > 0){
			
			List<Map<String, Object>> fff = (List<Map<String, Object>>)courses.get(0).get("children");
			
			String type = "cou-section-"+tables.get(0).get("sectionid").toString();
			boolean first = false;
			
			for (Map<String, Object> map : fff) {
				if("course-section".equals(map.get("type").toString()) && type.equals(map.get("id").toString())){
					map.put("fileType", 1);
					
					first = true;
					break;
				}
			}
			
			if(!first){
				for (Map<String, Object> map : fff) {
					if("course-section".equals(map.get("type").toString())){
						map.put("fileType", 1);
						
						break;
					}
				}
			}
		}
		
		if((tables == null || tables.size() == 0) && courses != null && courses.size() > 0){
			List<Map<String, Object>> fff = (List<Map<String, Object>>)courses.get(0).get("children");
			for (Map<String, Object> map : fff) {
				if("course-section".equals(map.get("type").toString())){
					map.put("fileType", 1);
					break;
				}
			}
		}
		
		/*DataGrid dgDataRes = new DataGrid(courses);
		dgDataRes.setTotal(courses.size());
		responseObj.setData(dgDataRes);*/
		System.out.println(1);
	}

}
