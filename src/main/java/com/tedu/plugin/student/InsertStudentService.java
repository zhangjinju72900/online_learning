package com.tedu.plugin.student;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

@Service("insertStudentService")
public class InsertStudentService implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	@Resource
	private FormService formService;

	@Resource
	private EasemobComponent easemobComponent;

	@Resource
	private CustomUserService customUserServiceImpl;
	
	public final Logger log = Logger.getLogger(this.getClass());

	private String sqlTemplate = "khAdmin/schoolManage/student/insertRole";
	private String sql1 = "khAdmin/schoolManage/student/insertClass";
	private String sql2 = "khAdmin/schoolManage/student/deleteStudentClass";

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
			throw new ValidException(ErrorCode.MAIL_EXCEPTION, "身份证号或手机号重复", cardNum);
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		Map<String, Object> map = (Map<String, Object>) formModel.getData();

		String[] res = responseObj.getData().toString().split(",");

		Long userid = SessionUtils.getUserInfo().getEmpId();
		long l = System.currentTimeMillis();
		Date time = new Date(l);
		String id = formModel.getPrimaryFieldValue().toString();

		LogUtil.info("------------ id -----------" + id);
		String mode = map.get("Mode") == null ? "" : map.get("Mode").toString();
		String classId = map.get("classId") == null ? "" : map.get("classId").toString();
		String gradeName = map.get("gradeName") == null ? "" : map.get("gradeName").toString();
		LogUtil.info(mode + "----" + classId + "------" + gradeName);

		FormModel my = new FormModel("frmStudentNew", "pRole");
		FormModel ms = new FormModel("frmStudentNew", "pClass");
		my.getData().put("id", my.getPrimaryFieldValue());
		ms.getData().put("id", my.getPrimaryFieldValue());
		CustomFormModel cModel = new CustomFormModel();
		CustomFormModel cmods = new CustomFormModel();
		Map<String, Object> data = my.getData();
		Map<String, Object> dat = ms.getData();
		if ("Edit".equals(mode)) {
			cmods.setData(dat);
			cmods.setSqlId(sql2);
			dat.put("customerId", id);
			formMapper.saveCustom(cmods);
		}

		// 添加模式下 添加角色
		if ("Add".equals(mode)) {
			cModel.setData(data);
			cModel.setSqlId(sqlTemplate);
			data.put("customerId", id);
			data.put("roleId", 10);
			try {
				formMapper.saveCustom(cModel);
			} catch (Exception e) {
				LogUtil.info(e.toString());

			}
		}

		cmods.setData(dat);
		cmods.setSqlId(sql1);
		dat.put("customerId", id);
		dat.put("classId", classId);
		try {
			formMapper.saveCustom(cmods);
		} catch (Exception e) {
			LogUtil.info(e.toString());

		}
		
		if ("Add".equals(mode)) {//生成环信账号
			try {
				String userId = formModel.getPrimaryFieldValue().toString();
				easemobComponent.register(easemobComponent.getToken(),userId,
						userId);
				customUserServiceImpl.updateEasemobUser(id, userId);
			} catch (Exception e) {
				log.error("新建学生时生成环信账号失败", e);
				e.printStackTrace();
			}
		}

	}

}
