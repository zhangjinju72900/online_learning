package com.tedu.plugin.homework;


import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service("UpdateHomeworkStatusPlugin")
public class UpdateHomeworkStatusPlugin implements ILogicPlugin {
	@Resource
	FormMapper formMapper;
	@Resource
	FormService formService;
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		//homeworkAnswerId: 2282
		Map map = requestObj.getData();
		String homeworkAnswerId = map.get("homeworkAnswerId").toString();
		QueryPage qp = new QueryPage();
		qp.getData().put("homeworkAnswerId", homeworkAnswerId);
		qp.setQueryParam("/khTeacher/homework/homeworkCorrect/QryHomeworkId");
		List<Map<String, Object>> homeworkList = formService.queryBySqlId(qp);
		if(homeworkList.size()>0) {
			String homeworkId = homeworkList.get(0).get("homeworkId").toString();
			qp.getData().put("homeworkId", homeworkId);
			qp.setQueryParam("/khTeacher/homework/homeworkCorrect/QryHomeworkDeailStatus");
			List<Map<String, Object>> statusList = formService.queryBySqlId(qp);
			if(statusList.size()<1) {
					CustomFormModel cModel = new CustomFormModel();
					cModel.setSqlId("/khTeacher/homework/homeworkCorrect/updataHomeWorkStatus");
					map.put("homeworkId", homeworkId);
					map.put("homeworkStatus", '3');
					cModel.setData(map);
					formMapper.saveCustom(cModel);
			}
		}
	}

	

}
