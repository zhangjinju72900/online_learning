package com.tedu.plugin.labelManage;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("insertRolePlugin")
public class InsertRolePlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	private String sqlTemplate = "classlabel/InsertRole";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String labelId=formModel.getPrimaryFieldValue().toString();
		  
		  String ides = map.get("eq_role")==null?"":map.get("eq_role").toString();
		  String[] ids=ides.startsWith("[")?ides.substring(1, ides.length()-1).split(","):ides.split(",");
		  String mode = map.get("Mode")==null?"":map.get("Mode").toString();
		  //map.get("eq_role");
		  
		  if("Edit".equals(mode)){
			  //编辑
			  String deleteSql = "classlabel/deleteByLabelId";
			  CustomFormModel cModel = new CustomFormModel();
			  cModel.setSqlId(deleteSql);
			  cModel.setData(formModel.getData());
			  Map<String, Object> logData = formModel.getData();
			
			  logData.put("labelId", labelId);
			  LogUtil.info(cModel.getData().toString());
			  formMapper.saveCustom(cModel);
		  }
		  
		  log.info(ids.toString());
		  
		  for(String roleId:ids){
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				Map<String, Object> logData = formModel.getData();
				
				logData.put("labelId", labelId);
				logData.put("roleId", roleId);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);
				
					}
	}
}
