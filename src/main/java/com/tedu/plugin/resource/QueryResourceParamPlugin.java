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
@Service("queryResourceParam")
public class QueryResourceParamPlugin implements ILogicPlugin {

	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Resource
	private MaintainManualService maintainManualServiceImpl;
	
	@Resource
	private FormMapper formMapper;
	
	@Resource
	private OSS ossPriClient;

	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Value("${ftp.uploadPath}")
	private String UPLOAD_PATH;
	
	@Value("${base.website}")
	private String website;

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
		String createBy = requestMap.get("createBy")==null?"":requestMap.get("createBy").toString();
		String module = requestMap.get("module")==null?"":requestMap.get("module").toString();
		/*String courseId = requestMap.get("courseId")==null?"":requestMap.get("courseId").toString();
		String sectionId = requestMap.get("resourceId")==null?"":requestMap.get("sectionId").toString();
		String labelId = requestMap.get("resourceId")==null?"":requestMap.get("labelId").toString();*/
		if(StringUtils.isNotBlank(csrId)){
			try {
				Map<String, Object> map = new HashMap<>();
				if(HtmlResourceModuleEnum.MAINTAIN_MANUAL.getCode().equals(module)){//用户手册
					map = maintainManualServiceImpl.queryFileById(csrId);
				}else{//用户资源
					map = getCourseSectionResourcesById(csrId);
				}
				if(map != null){
					String filePath = map.get("filePath")==null?"":map.get("filePath").toString();
					String courseId = map.get("courseId")==null?"":map.get("courseId").toString();
					String sectionId = map.get("sectionId")==null?"":map.get("sectionId").toString();
					String labelId = map.get("labelId")==null?"":map.get("labelId").toString();
					String resourcesType = map.get("resourcesType")==null?"":map.get("resourcesType").toString();
					String resourceId = map.get("id")==null?"":map.get("id").toString();
					
					String token = filePath.substring(0, filePath.lastIndexOf("."));
					String url = AuthCode.encrypt(website);
					
					Map<String, Object> mapResponse = (Map<String, Object>) responseObj.getData();
					mapResponse = mapResponse == null ? new HashMap<String, Object>() : mapResponse;
					mapResponse.put("token", AuthCode.encrypt(token));
					mapResponse.put("url", url);
					mapResponse.put("resourceId", AuthCode.encrypt(resourceId));
					mapResponse.put("courseId", AuthCode.encrypt(courseId));
					mapResponse.put("sectionId", AuthCode.encrypt(sectionId));
					mapResponse.put("labelId", AuthCode.encrypt(labelId));
					mapResponse.put("userId", AuthCode.encrypt(createBy));
					mapResponse.put("resourcesType", resourcesType);//0：普通文件，14:scorm文件，15：ppt转H5文件
					responseObj.setData(mapResponse);
				}else{
					responseObj.setCode(ExErrorCode.RESOURCE_NOT_FOUND.getCode());
					responseObj.setMsg(ExErrorCode.RESOURCE_NOT_FOUND.getMsg());
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("获取打开scorm参数失败", e);
			}
		}

	}

	private Map<String, Object> getCourseSectionResourcesById(String resourceId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourcesManage/QryCourseResById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/resourcesManage/QryCourseResById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", resourceId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			return tables.get(0);
			
		}
		return null;
	}


}
