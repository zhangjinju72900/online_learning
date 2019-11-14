package com.tedu.plugin.customUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
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
 * 在保存客户账号信息后将对应的角色关系更新到t_customer_user_role表中
 * 
 * @author quancong
 *
 */
@Service("InsertCustomUserRolePlugin")
public class InsertCustomUserRolePlugin implements ILogicPlugin {
	
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
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String id = map.get("id") == null ? "" : map.get("id").toString();
		String tel = map.get("tel") == null ? "" : map.get("tel").toString();
		String cardNum = map.get("cardNum") == null ? "" : map.get("cardNum").toString();
		
		QueryPage qp = new QueryPage();
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("tel", StringUtils.isBlank(tel)?"--":tel);
		requestMap.put("cardNum", StringUtils.isBlank(cardNum)?"--":cardNum);
		if(StringUtils.isBlank(id) || "0".equals(id)){
			qp.setQueryParam("khAdmin/userManage/QryUserByCardNumOrTel");
			qp.setSqlId("khAdmin/userManage/QryUserByCardNumOrTel");
		}else{
			requestMap.put("id", id);
			qp.setQueryParam("khAdmin/userManage/QryUserByCardNumOrTelAndId");
			qp.setSqlId("khAdmin/userManage/QryUserByCardNumOrTelAndId");
		}
		qp.setDataByMap(requestMap);
		List qlist = formService.queryBySqlId(qp);
		if(qlist != null && qlist.size() > 0){
			throw new ValidException(ErrorCode.MAIL_EXCEPTION, "身份证号或手机号重复", "");
		}
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		// 获取主表的id
		String userId = formModel.getPrimaryFieldValue().toString();
		// 获取roleid并解析
		String ides = map.get("eq_roleId") == null ? "" : map.get("eq_roleId").toString();
		String[] ids = ides.startsWith("[") ? ides.substring(1, ides.length() - 1).split(",") : ides.split(",");
		String mode = map.get("Mode") == null ? "" : map.get("Mode").toString();
		// 为编辑模式时先将原来的主表id对应的记录然后重新插入
		if ("Edit".equals(mode)) {
			String updateSql = "deleteCustomUserRole1";
			CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId(updateSql);
			cModel.setData(formModel.getData());
			Map<String, Object> logData = formModel.getData();
			logData.put("userId", userId);
			LogUtil.info(cModel.getData().toString());
			formMapper.saveCustom(cModel);
		}
		// 新增模式时将角色关系插入t_customer_user中
		CustomFormModel cModel = new CustomFormModel();
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

		if ("Add".equals(mode)) {//生成环信账号
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

}
