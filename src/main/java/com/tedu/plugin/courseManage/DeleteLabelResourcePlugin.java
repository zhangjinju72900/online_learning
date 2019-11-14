package com.tedu.plugin.courseManage;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("DeleteLabelResourcePlugin")
public class DeleteLabelResourcePlugin implements ILogicPlugin{
	public final Logger log = Logger.getLogger(this.getClass());
	private String sqlTemplate = "zhongdeprofession/DeleteLabelResource";
	@Resource
	private FormMapper formMapper;
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		CustomFormModel cModel = new CustomFormModel();
		cModel.setData(formModel.getData());
		cModel.setSqlId(sqlTemplate);
		formMapper.saveCustom(cModel);
	}

}
