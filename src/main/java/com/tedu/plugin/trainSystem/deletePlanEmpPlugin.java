package com.tedu.plugin.trainSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.tedu.base.task.SpringUtils;

@Service("deletePlanEmpPlugin")
public class deletePlanEmpPlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String, Object> map = formModel.getData();
		QueryPage qp = new QueryPage();
		qp.getData().put("id", formModel.getData().get("id"));
		qp.setQueryParam("trainSystem/QryPlanIdEmpId");
		List<Map<String, Object>> list = formService.queryBySqlId(qp);
		
			Map sqlMap = new HashMap();
			sqlMap.put("planId", list.get(0).get("plan_id"));
			sqlMap.put("traineeId", list.get(0).get("emp_id"));
			CustomFormModel csmd = new CustomFormModel("", "", sqlMap);
			csmd.setSqlId("trainSystem/deleteCourseEmp");
			formMapper.saveCustom(csmd);
		
		
		return formModel;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		
		
	}

}
