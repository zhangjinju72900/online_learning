package com.tedu.plugin.app.update.version;

import java.util.Map;

import javax.annotation.Resource;

import com.tedu.base.engine.aspect.ILogicServicePlugin;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.AppUpdateFileVersionEnum;
import com.tedu.common.constant.BannerEnum;

@Service("insertAppDeleteVersionPlugin")
public class InsertAppDeleteVersionPlugin implements ILogicServicePlugin {
	
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
			
		execute("khApp/updateVsersion/InsertAppDeleteVersion", map);
	}

	
	private String execute(String sqlId, Map<String, Object> data){
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
	}
}
