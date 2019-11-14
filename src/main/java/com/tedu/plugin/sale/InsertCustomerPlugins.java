package com.tedu.plugin.sale;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;

@Service("insertCustomerPlugins")
public class InsertCustomerPlugins implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		if(formModel.getData().get("Mode").equals("Add")){
			String customerName = formModel.getData().get("customerName").toString();
			QueryPage qp = new QueryPage();
			qp.setQueryParam("customer/QryCustomer");
			List<Map<String,Object>> qlists = formMapper.query(qp);
			for (Map<String, Object> map : qlists) {
				if (customerName.equals(map.get("customerName"))) {
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "客户校验", "输入的客户名已存在");
				}
			}
		}
		
		return formModel;
		}


	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
	
 	}

}
