package com.tedu.plugin.teacher.classes.manage.info;


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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("getStudentOtherInfoPlugin")
public class GetStudentOtherInfoPlugin implements ILogicPlugin {
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
		Map<String, Object> map = (Map<String, Object>) formEngineResponse.getData();
		
		List<Map<String, Object>> attendClassTables = queryBySqlId("khTeacher/classManage/classInfo/QryClassAttendClassCount", map);//已签到数量
		
		List<Map<String, Object>> signTables = queryBySqlId("khTeacher/classManage/classInfo/QryStudentSignCount", map);
		
		int attendClassCount = attendClassTables == null ? 0 : attendClassTables.size();
		
		int studentSignCount = signTables == null ? 0 : signTables.size();
		
		BigDecimal signRate = attendClassCount == 0 ? new BigDecimal(100) : new BigDecimal(studentSignCount).divide(new BigDecimal(attendClassCount), 2, BigDecimal.ROUND_HALF_UP);
		
		map.put("signRate", signRate+"%");

		formEngineResponse.setData(map);
	}

	private List<Map<String, Object>> queryBySqlId(String string, Map<String, Object> map) {
		
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(string);
		sqlQuery.setDataByMap(map);
		return formMapper.query(sqlQuery);
	}

}
