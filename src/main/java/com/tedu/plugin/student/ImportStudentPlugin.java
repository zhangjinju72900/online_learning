package com.tedu.plugin.student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.alibaba.druid.sql.visitor.functions.If;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

@Service("ImportStudentPlugin")
public class ImportStudentPlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	@Resource
	FormService formService;
	@Resource
	private EasemobComponent easemobComponent;
	
	@Resource
	private CustomUserService customUserServiceImpl;
	
	
	private String studentSql = "khAdmin/schoolManage/student/QryStudentByUUID";

	/**
	 * 导入时数据校验
	 */

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		Map<String, Object> maps = (Map<String, Object>) formModel.getData();
		QueryPage qp = new QueryPage();

		System.out.println(maps.toString() + "===============");

		String value = (String) maps.get("sex");
		if ("男".equals(value)) {
			value = "0";
		} else {
			value = "1";
		}
		maps.put("sex",0);



				String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
				maps.put("uuid", uuid);
				CustomFormModel cModel = new CustomFormModel();
				cModel.setData(maps);
				//默认密码
				String password = "123456";
				String salt = TokenUtils.genUUID().toUpperCase();
				password = MD5Util.MD5Encode(password).toUpperCase();
				String saltPassword = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, salt, password);
			
				maps.put("password", saltPassword);
				
				cModel.setSqlId("khAdmin/schoolManage/student/insertStudent");
				formMapper.saveCustom(cModel);
				
				qp.getData().put("uuid", uuid);
				qp.setQueryParam(studentSql);
				qp.setDataByMap(maps);
				List<Map<String, Object>> userId = formMapper.query(qp);
				maps.put("userId", userId.get(0).get("userId").toString());
				cModel.setData(maps);
				cModel.setSqlId("khAdmin/schoolManage/student/insertStudentClass");
				formMapper.saveCustom(cModel);
				
				cModel.setSqlId("khAdmin/schoolManage/student/insertStudentRole");
				formMapper.saveCustom(cModel);
				
				easemobComponent.register(easemobComponent.getToken(),userId.get(0).get("userId").toString(),
						userId.get(0).get("userId").toString());
				customUserServiceImpl.updateEasemobUser(userId.get(0).get("userId").toString(), userId.get(0).get("userId").toString());

	}

}
