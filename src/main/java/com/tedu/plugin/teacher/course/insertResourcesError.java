package com.tedu.plugin.teacher.course;


import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("insertResourcesError")
public class insertResourcesError implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		log.info(formModel.getData());
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		CustomFormModel cModel = new CustomFormModel();
		QueryPage sqlQuery = new QueryPage();

		sqlQuery.setQueryParam("khTeacher/course/QryCourseSectionResources");
		sqlQuery.setDataByMap(map);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		String fileId = map.get("fileId")!=null?map.get("fileId").toString():"";
		map.put("customerResourcesId", tables.get(0).get("customerResourcesId"));
		map.put("courseResourcesName", tables.get(0).get("courseResourcesName"));
		map.put("courseId", tables.get(0).get("courseId"));
		map.put("sectionId", tables.get(0).get("sectionId"));
		map.put("labelId", tables.get(0).get("labelId"));
		map.put("schoolId", tables.get(0).get("schoolId"));
		map.put("fileId","".equals(fileId)?tables.get(0).get("fileId").toString(): fileId);
		//纠错没有上传附件
		if("".equals(fileId)) {
			cModel.setSqlId("khTeacher/course/InsertResNoFileError");
		}else {
			cModel.setSqlId("khTeacher/course/InsertResourcesError");
		}
		


		
		cModel.setData(map);
		formMapper.saveCustom(cModel);

		formEngineResponse.setData(map);
	}

}
