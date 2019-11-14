package com.tedu.plugin.sale;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

@Service("contactsAddSave")
public class ContactsAddSavePlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String,Object> map = new HashMap<String,Object>();
		QueryPage qp = new QueryPage();
        log.info(formModel.getData().get("ctlMobile"));
        map.put("eq_tel", formModel.getData().get("ctlMobile"));
        map.put("eq_CustomerId", formModel.getData().get("eq_CustomerId"));
        qp.setParamsByMap(map);
        qp.setQueryParam("ListIssuePriority1");
//        qp.getData().put("gsid", formModel.getData().get("ctlMobile"));
        List<Map<String,Object>> list = formService.queryBySqlId(qp);
        log.info(list);
        if(!list.isEmpty()||list.size()!=0){
        	throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "数据业务校验失败", "联系人手机号已存在，请废除后重新添加！");
        }
/*        if (qlists.size()>2) {
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "数据业务校验失败", "不能超过3条");
		}
*/		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		// TODO Auto-generated method stub

	}
}
