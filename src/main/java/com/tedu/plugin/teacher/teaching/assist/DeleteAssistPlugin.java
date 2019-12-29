package com.tedu.plugin.teacher.teaching.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
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
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
/**
 * 
 * @author bj
 *
 */
@Service("DeleteAssistPlugin")
public class DeleteAssistPlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		 return  null;
	}
		
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		 Map<String,Object> map=(Map<String, Object>) requestObj.getData();
		 String [] assistIds = map.get("id").toString().split(",");
		 CustomFormModel cModel = new CustomFormModel();
		 cModel.setSqlId("khAdmin/teachingAssist/DeleteTeachingAssist");
		 for(String id:assistIds) {
			 map.put("id", id);
			 cModel.setData(map);
			 formMapper.saveCustom(cModel);
		 }
		
	}

}
