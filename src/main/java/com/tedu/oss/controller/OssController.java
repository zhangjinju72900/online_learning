package com.tedu.oss.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssDeleteService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

@Controller
public class OssController {
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private OssDeleteService ossDeleteServiceImpl;
	
	@Autowired
	private OssQueryService ossQueryServiceImpl;
	
	@Autowired
	private OssUploadService ossUploadServiceImpl;
	
	@Autowired
	private CustomResourcesService customResourcesServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	//@Resource
	//private OSS ossPriClient;
	@Value("${oss.access_id}")
	private String ossAccess_id;
	@Value("${oss.access_key}")
	private String ossAccess_key;
	@Value("${oss.oss_endpoint}")
	private String oss_endpoint;
	
	/**
	 * @Title: deleteObjectByKey   
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @author: qun 
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年7月31日 下午2:19:06     
	 * @throws
	 */
	@RequestMapping("/deleteObjectByKey")
	@ResponseBody
	public FormEngineResponse deleteObjectByKey(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			Map <String, Object> param = requestObj.getData();
			if(param.get("key")==null || StringUtils.isBlank(param.get("key").toString())){
				response.setCode("999999");
				response.setMsg("参数错误");
			}else{
				result = ossDeleteServiceImpl.deleteObjectByKey(param.get("key").toString(), ossClient, BUCKET_NAME1);
				response.setData(result);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode("999999");
			response.setMsg(e.getMessage());
		}	
		return response;
	}
	
	
	/**
	 * @Title: queryObjectByKey   
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @author: qun 
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年7月31日 下午2:19:06     
	 * @throws
	 */
	@RequestMapping("/queryObjectByKey")
	@ResponseBody
	public FormEngineResponse queryObjectByKey(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		FormEngineResponse response = new FormEngineResponse(result);
		OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
		try {
			Map <String, Object> param = requestObj.getData();
			if(param.get("key")==null || StringUtils.isBlank(param.get("key").toString())){
				response.setCode("999999");
				response.setMsg("参数错误");
			}else{
				result = ossQueryServiceImpl.queryObjectByKey(param.get("key").toString(), ossClient, BUCKET_NAME1);
				response.setData(result);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode("999999");
			response.setMsg(e.getMessage());
		}	
		return response;
	}

	
	
	@RequestMapping("/queryObjectById")
	@ResponseBody
	public FormEngineResponse queryObjectById(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		Map <String, Object> result = new HashMap<>();
		FormEngineResponse response = new FormEngineResponse(result);
		OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
		try {
			Map <String, Object> param = requestObj.getData();
			if(param.get("customerResourcesId")==null || StringUtils.isBlank(param.get("customerResourcesId").toString())){
				response.setCode("999999");
				response.setMsg("参数错误");
			}else{
				result = ossQueryServiceImpl.queryObjectById(param.get("customerResourcesId").toString(), param.get("module")==null?"":param.get("module").toString(), 
						ossClient, BUCKET_NAME1);
				response.setData(result);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode("999999");
			response.setMsg(e.getMessage());
		}	
		return response;
	}
	
	
	@RequestMapping("/uploadObjectByFileId")
	@ResponseBody
	public FormEngineResponse uploadObjectByFileId(HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
			String coding = request.getParameter("coding");
			String fileId = request.getParameter("fileId");
			String id = request.getParameter("id");
			String originalFileType = request.getParameter("originalFileType");//文件类型
			String isScorm = request.getParameter("isScorm");//是否scorm，0是，1否
			Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileId);
			String path = fileMap.get("path");
			String fullPath = fileMap.get("fullpath");
			String fileName = fileMap.get("fileName");
			String fileType = fileMap.get("fileType");
				
			String key = ossUploadServiceImpl.getOssKey(fileType);
			ossUploadServiceImpl.uploadOss(key, fullPath, ossClient, BUCKET_NAME1);
			
			ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileId, key, BUCKET_NAME1);
			
			int resourceFileType = ResourceTypeEnum.OTHERS.getCode();
			if("0".equals(isScorm)){
				resourceFileType = ResourceTypeEnum.SCORM.getCode();
			}
			
			if(StringUtils.isNotBlank(id)){//存resource表
				Long resourcesId = customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, fileType, fullPath), key, Long.parseLong(id), fileId, 
						BUCKET_NAME1, "1.0", resourceFileType, CustomerResourcesSourceEnum.TFILEINDEX.getCode());
				result.put("resourcesId", resourcesId);
			}
			String url = ossQueryServiceImpl.queryObjectByKey(key, ossClient, BUCKET_NAME1);
			result.put("key", key);
			result.put("url", url);
			response.setData(result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode("999999");
			response.setMsg(e.getMessage());
		}	
		return response;
	}
	
	
}
