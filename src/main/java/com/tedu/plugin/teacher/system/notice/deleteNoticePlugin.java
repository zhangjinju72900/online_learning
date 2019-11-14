package com.tedu.plugin.teacher.system.notice;

import com.google.common.collect.Lists;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.EasemobSendObjectEnum;
import com.tedu.common.constant.EasemobSendTypeEnum;
import com.tedu.component.EasemobComponent;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("deleteNoticePlugin")
public class deleteNoticePlugin implements ILogicPlugin {
	@Autowired(required = false)
	FormLogService formLogService;
	@Autowired(required = false)
	private FormMapper formMapper;


	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String, Object> map = (Map<String, Object>) requestObj.getData();
		CustomFormModel cModel = new CustomFormModel();
		String type = map.get("type").toString();
		if("notice".equals(type)) {
			  String [] noticeIds = map.get("noticeId").toString().split(",");
			  Map<String, Object> paMap = new HashMap<String, Object>();
			  for(String id : noticeIds) {
				  paMap.put("id", id);
				  cModel.setData(paMap);
				  cModel.setSqlId("khTeacher/systemNotice/updateFlag");
				  formMapper.saveCustom(cModel);
			  }
		}else {
			 String [] messgageIds = map.get("messageId").toString().split(",");
			  Map<String, Object> paMap = new HashMap<String, Object>();
			  for(String id : messgageIds) {
				  paMap.put("id", id);
				  cModel.setData(paMap);
				  cModel.setSqlId("khTeacher/systemNotice/updateMessageFlag");
				  formMapper.saveCustom(cModel);
			  }
		}
	
	}

}
