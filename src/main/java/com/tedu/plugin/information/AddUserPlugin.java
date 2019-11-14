package com.tedu.plugin.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

/**
 * 
 * 
 * @author
 *
 */
@Service("AddUserPlugin")
public class AddUserPlugin implements ILogicPlugin {
	
	@Resource
	FormService formService;

	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Resource
	private EasemobComponent easemobComponent;

	@Resource
	private CustomUserService customUserServiceImpl;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		cModel.setSqlId("InsertCustomerUser");
		cModel.setData(formModel.getData());
		String password = map.get("ctlNewPassword").toString();
		String salt = TokenUtils.genUUID().toUpperCase();
		// String salt = AviatorEvaluator.execute("Guid()").toString();
		password = MD5Util.MD5Encode(password).toUpperCase();
		String saltPassword = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, salt, password);
        map.put("password",saltPassword);
		formMapper.saveCustom(cModel);
		new AviatorString(saltPassword);
		// 获取主表的id
		String userId = cModel.getPrimaryFieldValue().toString();
		// 获取roleid并解析
		String ides = "10";
		String[] ids = ides.startsWith("[") ? ides.substring(1, ides.length() - 1).split(",") : ides.split(",");
		
		cModel.setSqlId("insertCustomUserRole");
		cModel.setData(formModel.getData());
		Map<String, Object> logData = formModel.getData();
		// 当未选取roleId时，不执行插入
		for (String roleId : ids) {
			if (!(roleId.length() <= 0)) {
				logData.put("userId", userId);
				logData.put("roleId", roleId);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);
			}

		}
			//生成环信账号
			try {
				easemobComponent.register(easemobComponent.getToken(),userId,
						userId);
				customUserServiceImpl.updateEasemobUser(userId, userId);
			} catch (Exception e) {
				log.error("新建账号时生成环信账号失败", e);
				e.printStackTrace();
			}

	}

}
