package com.tedu.plugin.courseManage;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
@Service("DoBreakUpIdPlugin")
public class DoBreakUpIdPlugin implements ILogicPlugin{
	public final Logger log = Logger.getLogger(this.getClass());
	private String sqlTemplate = "zhongdeprofession/QrySaveResources";
	@Resource
	private FormMapper formMapper;
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		CustomFormModel cModel = new CustomFormModel();
		String[] ids=String.valueOf(map.get("ids")).split(",");
		String courseId=ids[0];
		String labelId=ids[1];
		String sectionId=ids[2].substring(ids[2].indexOf("-")+1);
		String cusresourcesId = map.get("cusresourcesId").toString();
		if(cusresourcesId.contains(",")){
			formModel.getData().put("courseId", courseId);
			formModel.getData().put("labelId", labelId);
			if(ids[2].contains("cou-")){
				formModel.getData().put("sectionId", 0);
			}else{
				formModel.getData().put("sectionId", sectionId);
			}
			for (String string : cusresourcesId.split(",")) {
				formModel.getData().put("cusresourcesId", string);
				
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				formMapper.saveCustom(cModel);
			}
		}else{
			formModel.getData().put("courseId", courseId);
			formModel.getData().put("labelId", labelId);
			if(ids[2].contains("cou-")){
				formModel.getData().put("sectionId", 0);
			}else{
				formModel.getData().put("sectionId", sectionId);
			}
			
			cModel.setSqlId(sqlTemplate);
			cModel.setData(formModel.getData());
			formMapper.saveCustom(cModel);
		}
		
	}

}
