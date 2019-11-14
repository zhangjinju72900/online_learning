package com.tedu.plugin.resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssDeleteService;
import com.tedu.oss.service.OssQueryService;
/**
 * 删除专业
 *   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月30日 下午2:32:21   
 *
 */
@Service("deleteCustomerResources")
public class DeleteCustomerResourcesPlugin implements ILogicPlugin{
	
	public final Logger log = Logger.getLogger(this.getClass());

	@Resource
	private FormMapper formMapper;
	
	@Resource
	private CustomResourcesService customResourcesServiceImpl;
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		CustomFormModel cModel = new CustomFormModel();
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourcesManage/QryResourceSection");
		sqlQuery.setDataByMap(formModel.getData());
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		try {
			
			if(tables != null && tables.size() > 0){
				responseObj.setMsg("有课程/小节绑定该资源,不可删除");
			}else{
				cModel.setSqlId("khAdmin/resourcesManage/DeleteCustomerResources");
				cModel.setData(formModel.getData());
				formMapper.saveCustom(cModel);
			}
			
			customResourcesServiceImpl.deleteSourceFileAndOssFile(formModel.getData().get("id").toString());
			
		} catch (Exception e) {
			log.error("删除资源失败，请查看日志", e);
			responseObj.setMsg("删除资源失败，请查看日志");
			e.printStackTrace();
		}
		
	}
	
}
