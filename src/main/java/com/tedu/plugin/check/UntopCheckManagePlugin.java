package com.tedu.plugin.check;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
/**
 * 审核管理中的取消置顶功能
 * @author quancong
 *
 */
@Service("untopCheckManagePlugin")
public class UntopCheckManagePlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	
	@Resource
	private IntegralChangeComponent integralChangeComponent;
	
	private String sqlTemplate = "khAdmin/checkManage/UpdateCheckManageTop";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  //解析获得的id值
		  String[] concatId=(map.get("id") == null ? "":map.get("id").toString()).split("-");
		  String tableName=concatId[0];
		  String id=concatId[1];
		  //判断获取的tableName并得到真正的表名
		  if(tableName.equals("info") || tableName.equals("ac_join_pic")){
			  tableName="t_information";
		  }
		  else if(tableName.equals("info_review")){
			  tableName="t_information_review";
		  }else if(tableName.equals("live_review")){
			  tableName="t_live_review";
		  }else{
			  return;
		  }
		  //根据表名和id值更新valid_flag字段值
		  CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId(sqlTemplate);
			cModel.setData(formModel.getData());
			Map<String, Object> logData = formModel.getData();
			logData.put("id", id);
			logData.put("tableName", tableName);
			LogUtil.info(cModel.getData().toString());
			formMapper.saveCustom(cModel);
	}
}

