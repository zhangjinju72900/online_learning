package com.tedu.plugin.operate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("SourceIpPlugin")
public class SourceIpPlugin implements ILogicPlugin{
	
	@Resource
	private FormMapper formMapper;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		String sourceIp = requestObj.getClientIp();
		Map<String,Object> map = formModel.getData();
		map.put("sourceIp", sourceIp);
		formModel.setData(map);
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
	}
}
