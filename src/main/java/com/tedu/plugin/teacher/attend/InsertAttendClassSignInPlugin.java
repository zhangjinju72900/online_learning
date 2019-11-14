package com.tedu.plugin.teacher.attend;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.error.ExErrorCode;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service("insertAttendClassSignIn")
public class InsertAttendClassSignInPlugin implements ILogicPlugin {
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
		
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khTeacher/attendClass/QryDataByClassIdAndUserId");
		sqlQuery.setDataByMap(map);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		if(tables == null || tables.size() < 1){
			formEngineResponse.setCode(ExErrorCode.ATTEND_CLASS_NOT_RIGHT_STUDENT.getCode());
			formEngineResponse.setMsg(ExErrorCode.ATTEND_CLASS_NOT_RIGHT_STUDENT.getMsg());
			return ;
		}
		
		cModel.setSqlId("khTeacher/attendClass/UpdateAttendClassRealCount");
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		
		cModel.setSqlId("khTeacher/attendClass/UpdateAttendClassSignByStudentIdAndAttId");
		formMapper.saveCustom(cModel);
	}
	

}
