package com.tedu.plugin.goods;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service("addGoodsCollect")
public class AddGoodsCollect implements ILogicPlugin {
	@Resource
	FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> data = formModel.getData();
		cModel.setData(data);
		log.info(data);
		cModel.setSqlId("khApp/mine/mall/SaveGoodsCollect");
		formMapper.saveCustom(cModel);
	}

	

}
