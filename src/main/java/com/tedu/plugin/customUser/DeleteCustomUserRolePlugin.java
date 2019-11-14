package com.tedu.plugin.customUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
/**
 * 在删除一条用户记录后，将对应在t_customer_user_role中的记录删除
 * @author quancong
 *
 */
@Service("deleteCustomUserRolePlugin")
public class DeleteCustomUserRolePlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		  	  log.info(formModel.getData());
			  String deleteSql = "deleteCustomUserRole";
			  CustomFormModel cModel = new CustomFormModel();
			  cModel.setSqlId(deleteSql);
			  cModel.setData(formModel.getData());
			  LogUtil.info(cModel.getData().toString());
			  Map<String, Object> logData = formModel.getData();
			  formMapper.saveCustom(cModel);
			  
	}

}
