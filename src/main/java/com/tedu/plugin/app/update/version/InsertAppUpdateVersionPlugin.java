package com.tedu.plugin.app.update.version;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.validation.ValidationError;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.AppUpdateFileVersionEnum;
import com.tedu.common.constant.BannerEnum;

@Service("insertAppUpdateVersionPlugin")
public class InsertAppUpdateVersionPlugin implements ILogicPlugin{
	
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String file=map.get("file").toString();
		
		
	     if(!(file.contains(".png"))){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"错误提示","文件类型不符合格式要求");
			}
			
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String type = map.get("type")==null?"":map.get("type").toString();
		if(type.equals(BannerEnum.APP_START_PAGE.getCode()) || type.equals(BannerEnum.TEACHING_GUIDE_PAGE.getCode())){
			
			if(type.equals(BannerEnum.APP_START_PAGE.getCode())){
				map.put("baseType", AppUpdateFileVersionEnum.APP_START_PAGE.getCode());
			}else{
				map.put("baseType", AppUpdateFileVersionEnum.TEACHING_GUIDE_PAGE.getCode());
			}
			
			String mode = map.get("Mode")==null?"":map.get("Mode").toString();
			if("Add".equals(mode)){
				map.put("baseId", formModel.getPrimaryFieldValue());
			}else{
				map.put("baseId", map.get("id"));
			}
			
			execute("khApp/updateVsersion/InsertAppUpdateVersion", map);
		}
	}

	
	private String execute(String sqlId, Map<String, Object> data){
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
	}
}
