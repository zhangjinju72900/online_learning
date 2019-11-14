package com.tedu.business.user.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.auth.login.dao.CustomLoginDao;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.business.user.service.BindingTelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bindingTelServiceImpl")
public class BindingTelServiceImpl implements BindingTelService {

	@Resource
	private FormMapper formMapper;

	@Resource
	private CustomLoginDao customLoginDao;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public void bindingTelNum(String telNum, String userId) throws Exception {
		UserModel model = new UserModel();
		if (StringUtils.isNotBlank(userId)) {
			model.setUserId(Long.parseLong(userId));
		} else {
			model = SessionUtils.getUserInfo();
			if (model == null) {
				throw new Exception();
			}
		}
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/mine/setChangePwd/updateTelById");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", model.getUserId());
		map.put("tel", telNum);
		cModel.setData(map);
		formMapper.saveCustom(cModel);

	}

	@Override
	public void resetPassWord(String telNum, String password, String confirmPassword) {
		String salt = TokenUtils.genUUID().toUpperCase();
		// String salt = AviatorEvaluator.execute("Guid()").toString();
		password = MD5Util.MD5Encode(password).toUpperCase();
		String saltPassword = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, salt, password);
		UserModel user = new UserModel();
		user.setMobile(telNum);
		user.setSalt(salt);
		user.setPassword(saltPassword);

		customLoginDao.updateCustomPwdByMobile(user);
		new AviatorString(saltPassword);
	}

	@Override
	public void insertSmsRecord(SendSmsResponse res, String telNum, String type, String code) {
		// 发送短信后存储记录
		try {

			TableModel model = new TableModel() {

				@Override
				public String getTableName() {
					return "t_sendsms_record";
				}

				@Override
				public String[] getFields() {
					String[] str = { "tel", "code", "send_type", "result_code", "result_msg", "create_time",
							"create_by", "update_time", "update_by" };
					return str;
				}
			};
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("tel", telNum);
			map.put("code", code);
			map.put("sendType", type);
			map.put("resultCode", res.getCode());
			map.put("resultMsg", res.getMessage());
			map.put("createTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
			map.put("createBy", SessionUtils.getUserInfo() == null ? 0 : SessionUtils.getUserInfo().getUserId());
			map.put("updateTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
			map.put("updateBy", SessionUtils.getUserInfo() == null ? 0 : SessionUtils.getUserInfo().getUserId());
			SimpleFormModel simpleModel = new SimpleFormModel(model, map);
			int s = formMapper.insert(simpleModel);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("存储短信发送记录失败", e);
		}
	}

	@Override
	public boolean verifyAccountExists(String telNum) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khApp/mine/verifyAccountExists/verifyAccountExists");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khApp/mine/verifyAccountExists/verifyAccountExists");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tel", telNum);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		return tables != null && tables.size() > 0;
	}

}
