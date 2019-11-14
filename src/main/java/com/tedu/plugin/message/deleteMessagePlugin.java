package com.tedu.plugin.message;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("deleteMessagePlugin")
public class deleteMessagePlugin implements ILogicServicePlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	private String sqlTemplate = "zhongdemessage/deleteMessage";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o) {

		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String[] ids=map.get("id").toString().split(",");

		log.info(ids.toString());

		for(String id:ids){
			CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId(sqlTemplate);
			cModel.setData(formModel.getData());
			Map<String, Object> logData = formModel.getData();

			logData.put("id", id);
			LogUtil.info(cModel.getData().toString());
			formMapper.saveCustom(cModel);

		}
	}
}
