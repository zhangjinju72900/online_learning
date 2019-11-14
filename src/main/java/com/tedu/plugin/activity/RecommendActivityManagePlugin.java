package com.tedu.plugin.activity;

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
@Service("RecommendActivityManagePlugin")
public class RecommendActivityManagePlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	private String sqlTemplate = "khAdmin/activityManage/RecommendActivityManage";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String[] ids=map.get("id").toString().split(",");
		  
		  log.info(ids.toString());
		  
		  for(String id:ids){
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				Map<String, Object> logData = formModel.getData();
				
				logData.put("id", id);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);
				
		}
	}
}

