package com.tedu.plugin.student;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tedu.base.common.model.DataGrid;
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
import com.tedu.base.msg.SendMessage;

@Service("deleteStudentService")
public class deleteStudentService implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	@Resource
	private FormService formService;

	@Autowired
	SendMessage sendMessage;

	@Value("${base.website}")
	private String baseSite;
	private final String NEW = "new";
	private final String MODIFY = "modify";
	
	private String sqlTemplate = "khAdmin/schoolManage/student/deleteStudentRole";
	private String sql1 = "khAdmin/schoolManage/student/deleteStudentClass";
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
			
			Map<String,Object> map=(Map<String, Object>) formModel.getData();
			
			String[] res = responseObj.getData().toString().split(",");
			
			Long userid = SessionUtils.getUserInfo().getEmpId();
			long l = System.currentTimeMillis();
			Date time=new Date(l);
			String id =formModel.getPrimaryFieldValue().toString();
			LogUtil.info("------------ id -----------"+id);
			String mode = map.get("Mode")==null?"":map.get("Mode").toString();
			String classId = map.get("classId")==null?"":map.get("classId").toString();
			String gradeName = map.get("gradeName")==null?"":map.get("gradeName").toString();
			LogUtil.info(mode+"----"+classId+"------"+gradeName);
			
			FormModel my = new FormModel("frmStudentNew","pRole");
			FormModel ms = new FormModel("frmStudentNew","pClass");
			my.getData().put("id", my.getPrimaryFieldValue());
			ms.getData().put("id", my.getPrimaryFieldValue());
			CustomFormModel cModel = new CustomFormModel();
			CustomFormModel cmods = new CustomFormModel();
			Map<String, Object> data = my.getData();
			Map<String, Object> dat = ms.getData();
				cModel.setData(data);
				cmods.setData(dat);
				cModel.setSqlId(sqlTemplate);
				cmods.setSqlId(sql1);
				data.put("customerId",id);
				dat.put("customerId", id);
				
				try {
					formMapper.saveCustom(cModel);
					formMapper.saveCustom(cmods);
				} catch (Exception e) {
					LogUtil.info(e.toString());
					
				}
			  }
		
		


}
