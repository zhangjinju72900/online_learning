package com.tedu.plugin.resource;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

/**
 * @ClassName:  ArchiveService   
 * @Description:TODO 用户资源归档  
 * @author: qun
 * @date:   2018年7月25日 上午10:29:07   
 */

@Service("archiveService")
public class ArchiveService implements ILogicPlugin{
	
	@Resource
	private FormMapper formMapper;	

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		Map<String, Object> data = formModel.getData();
		String id = data.get("id")==null?"":data.get("id").toString();
		String backupId = data.get("backupId")==null?"":data.get("backupId").toString();
		formModel.getData().put("backupId", 0);
		formMapper.update(formModel);
		
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/resourcesManage/UpdateResorceById");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", backupId);
		map.put("backupId", id);
		map.put("backupType", 0);
		map.put("updateTime", data.get("updateTime")==null?"":data.get("updateTime").toString());
		map.put("updateBy", data.get("updateBy")==null?"":data.get("updateBy").toString());
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		
		cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/resourcesManage/UpdateResorceByBackupId");
		map = new HashMap<String, Object>();
		map.put("backupId", id);
		map.put("oldBackupId", backupId);
		map.put("updateTime", data.get("updateTime")==null?"":data.get("updateTime").toString());
		map.put("updateBy", data.get("updateBy")==null?"":data.get("updateBy").toString());
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		
		cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/resourcesManage/UpdateCourseSectionResorceByResourId");
		map = new HashMap<String, Object>();
		map.put("customerResourcesId", id);
		map.put("oldCustomerResourcesId", backupId);
		map.put("updateTime", data.get("updateTime")==null?"":data.get("updateTime").toString());
		map.put("updateBy", data.get("updateBy")==null?"":data.get("updateBy").toString());
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		
		
	}

}
