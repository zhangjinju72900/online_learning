package com.tedu.oss.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.oss.OSS;
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
public class OssController2 {
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private OssDeleteService ossDeleteServiceImpl2;
	
	@Resource
	private OssQueryService ossQueryServiceImpl2;
	
	@Resource
	private OssUploadService ossUploadServiceImpl2;
	
	@Resource
	private CustomResourcesService customResourcesServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;
	
	@Resource
	private OSS ossPubClient;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;
	
	
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
	@RequestMapping("/deleteObjectByKey2")
	@ResponseBody
	public FormEngineResponse deleteObjectByKey(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			Map <String, Object> param = requestObj.getData();
			if(param.get("key")==null || StringUtils.isBlank(param.get("key").toString())){
				response.setCode("999999");
				response.setMsg("参数错误");
			}else{
				result = ossDeleteServiceImpl2.deleteObjectByKey(param.get("key").toString(), ossPubClient, BUCKET_NAME2);
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
	@RequestMapping("/queryObjectByKey2")
	@ResponseBody
	public FormEngineResponse queryObjectByKey(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			Map <String, Object> param = requestObj.getData();
			if(param.get("key")==null || StringUtils.isBlank(param.get("key").toString())){
				response.setCode("999999");
				response.setMsg("参数错误");
			}else{
				result = ossQueryServiceImpl2.queryObjectByKey(param.get("key").toString(), ossPubClient, BUCKET_NAME2);
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

	
	
	@RequestMapping("/queryObjectById2")
	@ResponseBody
	public FormEngineResponse queryObjectById(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		Map <String, Object> result = new HashMap<>();
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			Map <String, Object> param = requestObj.getData();
			if(param.get("customerResourcesId")==null || StringUtils.isBlank(param.get("customerResourcesId").toString())){
				response.setCode("999999");
				response.setMsg("参数错误");
			}else{
				result = ossQueryServiceImpl2.queryObjectById(param.get("customerResourcesId").toString(), param.get("module")==null?"":param.get("module").toString(),ossPubClient, BUCKET_NAME2);
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
	
	
	@RequestMapping("/uploadObjectByFileId2")
	@ResponseBody
	public FormEngineResponse uploadObjectByFileId(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		
		Date stDate = new Date();
		
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			String coding = request.getParameter("coding");
			String fileId = request.getParameter("fileId");
			String id = request.getParameter("id");
			String originalFileType = request.getParameter("originalFileType");//文件类型
			String isScorm = request.getParameter("isScorm");//是否scorm，0是，1否
			if(StringUtils.isBlank(fileId)){
				Map <String, Object> param = requestObj.getData();
				coding = param.get("coding")==null?"":param.get("coding").toString();
				fileId = param.get("fileId")==null?"":param.get("fileId").toString();
				id = param.get("id")==null?"":param.get("id").toString();
				originalFileType = param.get("originalFileType")==null?"":param.get("originalFileType").toString();
				isScorm = param.get("isScorm")==null?"":param.get("isScorm").toString();
			}
			
			Map<String, String> fileMap = ossUploadServiceImpl2.getFilePathById(fileId);
			String path = fileMap.get("path");
			String fullPath = fileMap.get("fullpath");
			String fileName = fileMap.get("fileName");
			String fileType = fileMap.get("fileType");

			String key = ossUploadServiceImpl2.getOssKey(fileType);
			
			System.out.println("11111 耗时毫秒："+(new Date().getTime()-stDate.getTime()));
			
			ossUploadServiceImpl2.uploadOss(key, fullPath, ossPubClient, BUCKET_NAME2);
			
			System.out.println("22222 耗时毫秒："+(new Date().getTime()-stDate.getTime()));
			
			ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileId, key, BUCKET_NAME2);
			
			int resourceFileType = ResourceTypeEnum.OTHERS.getCode();
			if("0".equals(isScorm)){
				resourceFileType = ResourceTypeEnum.SCORM.getCode();
			}
			
			System.out.println("33333 耗时毫秒："+(new Date().getTime()-stDate.getTime()));
			
			if(StringUtils.isNotBlank(id)){//存resource表
				Long resourcesId = customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, fileType, fullPath), key, Long.parseLong(id), fileId, 
						BUCKET_NAME2, "1.0", resourceFileType, CustomerResourcesSourceEnum.TFILEINDEX.getCode());
				result.put("resourcesId", resourcesId);
			}
			
			System.out.println("44444 耗时毫秒："+(new Date().getTime()-stDate.getTime()));
			
			String url = ossQueryServiceImpl2.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
			result.put("key", key);
			result.put("url", url);
			
			System.out.println("55555 耗时毫秒："+(new Date().getTime()-stDate.getTime()));
			
			Date endDate = new Date();
			
			System.out.println("uploadObjectByFileId2 耗时毫秒："+(endDate.getTime()-stDate.getTime()));
			
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
