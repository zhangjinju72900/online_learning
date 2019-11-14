package com.tedu.plugin.address;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service("addDefaultAddress")
public class AddDefaultAddress implements ILogicPlugin {
	@Resource
	FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		QueryPage queryPage = new QueryPage();
		queryPage.setQueryParam("khApp/mine/mall/QryAddressUserId");
        queryPage.getData().put("id",formModel.getPrimaryFieldValue());
        List<Map<String, Object>> result = formMapper.query(queryPage);
		 CustomFormModel cModel = new CustomFormModel();
		 Map<String, Object> data = formModel.getData();
			cModel.setData(data);
			cModel.setSqlId("khApp/mine/mall/updateAddress1");
			data.put("userId", result.get(0).get("userId"));
			log.info(formModel.getData());
			formMapper.saveCustom(cModel);
			return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		

	}

	

}
