package com.tedu.plugin.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.tedu.common.constant.HtmlResourceModuleEnum;
import com.tedu.common.error.ExErrorCode;
import com.tedu.common.util.AuthCode;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.oss.service.OssRecordService;

/**
 * 查询打开scorm参数
 *
 */
@Service("saveResourceRecord")
public class SaveResourceRecord implements ILogicPlugin {

	@Resource
	private FormMapper formMapper;
	

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String, Object> requestMap = requestObj.getData();
		String csrId = requestMap.get("csrId")==null?"":requestMap.get("csrId").toString();
		String customeruserid = requestMap.get("customeruserid")==null?"":requestMap.get("customeruserid").toString();
		if(StringUtils.isNotBlank(csrId)){
			try {
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("csrId", csrId);
				paramMap.put("userId", customeruserid);
				Map<String, Object> map = getCourseSectionResourcesById(paramMap, "khApp/study/section/QryCustomerResourceById");
				
				if(map != null){
					CustomFormModel cModel = new CustomFormModel();
					cModel.setSqlId("khApp/study/section/InsertCustomerResource");
					paramMap.put("courseId", map.get("courseId")==null?"0":map.get("courseId").toString());
					paramMap.put("sectionId", map.get("sectionId")==null?"0":map.get("sectionId").toString());
					paramMap.put("labelId", map.get("labelId")==null?"0":map.get("labelId").toString());
					paramMap.put("resourcesId", map.get("resourcesId")==null?"0":map.get("resourcesId").toString());
					cModel.setData(paramMap);
					formMapper.saveCustom(cModel);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("获取打开scorm参数失败", e);
			}
		}

	}

	private Map<String, Object> getCourseSectionResourcesById(Map<String, Object> map, String sqlId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(sqlId);
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId(sqlId);
		sqlQuery.setDataByMap(map);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			return tables.get(0);
			
		}
		return null;
	}


}
