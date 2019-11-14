package com.tedu.plugin.message;

import com.tedu.base.common.utils.LogUtil;
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
import java.util.Map;

@Service("updateMessageReadFlagPlugin")
public class UpdateMessageReadFlagPlugin implements ILogicPlugin {
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

		log.info(formEngineRequest.getData());
		Map<String,Object> map=(Map<String, Object>) formEngineRequest.getData();
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("zhongdemessage/UpdateMessageFlagByReceiverId");
		cModel.setData(map);
		LogUtil.info(cModel.getData().toString());
		formMapper.saveCustom(cModel);
	}
}
