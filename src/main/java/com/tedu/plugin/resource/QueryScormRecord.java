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
@Service("queryScormRecord")
public class QueryScormRecord implements ILogicPlugin {

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
		String userId = requestMap.get("userId")==null?"":requestMap.get("userId").toString();
		if(StringUtils.isNotBlank(csrId)){
			try {
				Map<String, Object> paramMap = new HashMap<>();
				Map<String, Object> resultMap = new HashMap<>();
				paramMap.put("csrId", csrId);
				paramMap.put("userId", userId);
				Map<String, Object> map = getCourseSectionResourcesById(paramMap, "khApp/study/section/QryLastStudyRecord");
				if(map != null){
					resultMap.putAll(map);
				}else{
					resultMap.put("percentScore", "0");
				}
				
				map = getCourseSectionResourcesById(paramMap, "khApp/study/section/QryStudyCount");
				if(map != null){
					resultMap.put("count", map.get("count"));
				}
				responseObj.setData(resultMap);
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
