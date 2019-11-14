package com.tedu.plugin.teacher.attend;


import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("UpdateAttendClassRecord")
public class UpdateAttendClassRecord implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		CustomFormModel cModel = new CustomFormModel();
//		QueryPage sqlQuery = new QueryPage();
		String attendClassRecordId = map.get("attendClassRecordId").toString();
		String ids = map.get("ids")!=null?map.get("ids").toString():"";
		String[] idsArray = StringUtils.isNotBlank(ids)?ids.split(","):new String[]{};
		map.put("attendClassRecordId", attendClassRecordId);
		
		Arrays.asList(idsArray).forEach(a ->{
			map.put("id", a);
			cModel.setSqlId("khTeacher/attendClass/UpdateAttendClassSignRecord");
			cModel.setData(map);
			formMapper.saveCustom(cModel);
		});
		
		/*sqlQuery.setQueryParam("khTeacher/attendClass/QryStudent");
		sqlQuery.setDataByMap(map);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);

		map.put("studentCount",tables.size());*/
		map.put("realStudentCount",idsArray.length);

		cModel.setSqlId("khTeacher/attendClass/UpdateAttendClassRecord");
		cModel.setData(map);
		formMapper.saveCustom(cModel);

		formEngineResponse.setData(map);
	}

}
