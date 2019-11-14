package com.tedu.plugin.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("DeleteProfessionPlugin")
public class DeleteProfessionPlugin implements ILogicPlugin{
	
	public final Logger log = Logger.getLogger(this.getClass());

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
		CustomFormModel cModel = new CustomFormModel();
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String pid = map.get("eq_id").toString();
		try {
			QueryPage sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("zhongdeprofession/QryFileList");
			sqlQuery.setSqlId("zhongdeprofession/QryFileList");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", pid);
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> tables = formMapper.query(sqlQuery);
			
			if(tables != null && tables.size() > 0){
				responseObj.setMsg("当前项目或其子项目下包含资源，不可删除");
			}else{
				formModel.getData().put("id", pid);
				cModel.setSqlId("khAdmin/resourcesManage/DeleteProfession");
				cModel.setData(formModel.getData());
				formMapper.saveCustom(cModel);
			}
			
		} catch (Exception e) {
			log.error("删除栏目失败，请查看日志", e);
			responseObj.setMsg("删除栏目失败，请查看日志");
			e.printStackTrace();
		}
	}

}
