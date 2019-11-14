package com.tedu.business.file.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aliyun.oss.OSS;
import com.tedu.base.common.utils.ImageUtil;
import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.service.FileService;
import com.tedu.base.file.util.UploadFile;
import com.tedu.common.constant.UploadModuleEnum;
import com.tedu.common.error.ExErrorCode;
import com.tedu.common.util.PicUtil;
import com.tedu.common.util.VideoUtil;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;

@Controller
public class IOSFileController {
	
	@Resource
	private FileService fileService ;
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;
	
	@Resource
	private OSS ossPubClient;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;

	@Resource
	private com.tedu.business.file.service.FileService fileServiceImpl;
	
	
	
	
	
	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;
	
	@Value("${oss.oss_endpoint}")
	private String OSS_ENDPOINT1;
	
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 *
	 * @Description: 手机端上传使用 公有
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * @throws IOException 
	 */
	@RequestMapping("/localUploadIOS")
	public void localUploadIOS(HttpServletRequest request,HttpServletResponse response) throws IOException{
			
		Date stDate = new Date();
		
		ResponseJSON responseJSON = null;
		String hintMessage = "";	//提示消息
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String, Object> fileData = new HashMap<>();
			//上传结果信息
		responseJSON = new ResponseJSON();
		try {
			//创建文件上传对象
			UploadFile upfiles = new UploadFile(request,""){
				//生成文件新名字
				@Override
				public String offerUploadFileNames() {
					return fileUUID;
				}
			};
			String uploadUrl = upfiles.getParameter("uploadUrl");
			String accessType = upfiles.getParameter("accessType");
			String module = upfiles.getParameter("module");
			//只能上传单文件判断
			
			map = fileService.localUpload(upfiles,fileUUID,accessType,uploadUrl,module);
			hintMessage = (String) map.get("hintMessage");
			if (StringUtils.isEmpty(hintMessage)) {
				//获取上传后文件信息对象
				FileModel fileModel = (FileModel)map.get("fileModel");
				//上传成功则返回id
				hintMessage = "上传成功";
				responseJSON.setStatus(fileModel.getId()==null?0:fileModel.getId());
				
				Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileModel.getId()+"");
				System.out.println("fileMap"+fileMap);
				String fullPath = fileMap.get("fullpath");
				String fileName = fileMap.get("fileName");
				String fileType = fileMap.get("fileType");
				
				ExecutorService executorService = Executors.newFixedThreadPool(2);
				Runnable syncRunnable = new Runnable(){
	                @Override
	                public void run() {
	                	
	                	try {
							
		                	String key = ossUploadServiceImpl.getOssKey(fileType);
		    				ossUploadServiceImpl.uploadOss(key, fullPath, ossPubClient, BUCKET_NAME2);
		    				
		    				ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key, BUCKET_NAME2);
		    				
		    				String url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
		    				
		    				if("jpg,png,bmp,jpeg".contains(fileModel.getFileType()) && Double.parseDouble(fileModel.getLength()) > 0.5*1024*1024){
		    					url = url + PicUtil.OSS_PIC_EXECUTE_SUFFIX;
		    					url = URLEncoder.encode(url, "utf-8");
		    				}
		    				
		    				fileData.put("url", url);
		    				fileData.put("error",0);
		    				fileData.put("fileId", fileModel.getId()==null?0:fileModel.getId());
		    				
		    				fileServiceImpl.updateFileOssUrl(fileModel.getId(), url, key);
	    				
	                	} catch (Exception e) {
	                		e.printStackTrace();
	                	}
	                }
				};
				executorService.execute(syncRunnable);
				
				if("mp4".equals(fileType)){
					Runnable syncRunnable1 = new Runnable(){
		                @Override
		                public void run() {
		                	try {
								String imgPath = VideoUtil.fetchFrame(fullPath, null);
								String key1 = ossUploadServiceImpl.getOssKey("jpg");
			    				ossUploadServiceImpl.uploadOss(key1, imgPath, ossPubClient, BUCKET_NAME2);
			    				String url1 = ossQueryServiceImpl.queryPublicUrlByKey(key1, BUCKET_NAME2, OSS_ENDPOINT2);
			    				
			    				fileData.put("imgUrl", url1);
			    				
			    				fileServiceImpl.updateFileDescription(fileModel.getId(), url1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                	
		                }

		            };

		            executorService.execute(syncRunnable1);
				}    
				
	            executorService.shutdown();
	            while (!executorService.awaitTermination(1, TimeUnit.SECONDS));
			}
			
	//		request.getHeader("Accept");
			FormLogger.logFlow("header.Accept =" + request.getHeader("Accept"), FormLogger.LOG_TYPE_INFO);
			responseJSON.setMsg(hintMessage);
			responseJSON.setData(fileData);
			
			log.info(hintMessage);
			log.info("fileData"+fileData);
			log.info(responseJSON);
			response.setContentType("application/json;charset=utf-8");  
			response.getWriter().write(FormUtil.toJsonString(responseJSON));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("手机端上传文件失败", e);
			responseJSON.setCode(ExErrorCode.APP_UPLOAD_FAIL.getCode());
			responseJSON.setMsg(ExErrorCode.APP_UPLOAD_FAIL.getMsg());
		}	
	}
	
	
	
	/**
	 *
	 * @Description: 上传使用 私有
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * @throws IOException 
	 */
	@RequestMapping("/localUploadTeacher")
	public void localUploadTeacher(HttpServletRequest request,HttpServletResponse response) throws IOException{
			
		ResponseJSON responseJSON = null;
		String hintMessage = "";	//提示消息
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String, Object> fileData = new HashMap<>();
			//上传结果信息
		responseJSON = new ResponseJSON();
		try {
			//创建文件上传对象
			UploadFile upfiles = new UploadFile(request,""){
				//生成文件新名字
				@Override
				public String offerUploadFileNames() {
					return fileUUID;
				}
			};
			String uploadUrl = upfiles.getParameter("uploadUrl");
			String accessType = upfiles.getParameter("accessType");
			String module = upfiles.getParameter("module");
			//只能上传单文件判断
			
			map = fileService.localUpload(upfiles,fileUUID,accessType,uploadUrl,module);
			hintMessage = (String) map.get("hintMessage");
			if (StringUtils.isEmpty(hintMessage)) {
				//获取上传后文件信息对象
				FileModel fileModel = (FileModel)map.get("fileModel");
				//上传成功则返回id
				hintMessage = "上传成功";
				responseJSON.setStatus(fileModel.getId()==null?0:fileModel.getId());
				
				Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileModel.getId()+"");
				System.out.println("fileMap"+fileMap);
				String fullPath = fileMap.get("fullpath");
				String fileName = fileMap.get("fileName");
				String fileType = fileMap.get("fileType");
				
				String code = request.getParameter("code");
				
				ExecutorService executorService = Executors.newFixedThreadPool(2);
				Runnable syncRunnable = new Runnable(){
	                @Override
	                public void run() {
	                	
	                	try {
							
		                	String key = ossUploadServiceImpl.getOssKey(fileType);
		                	
		                	Integer space = UploadModuleEnum.getSpaceByCode(code);
		                	
		                	String url = "";
		                	
		                	if(0 == space){

		                		ossUploadServiceImpl.uploadOss(key, fullPath, ossPubClient, BUCKET_NAME2);
		                		
		                		ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key, BUCKET_NAME2);
		                		
		                		url = ossQueryServiceImpl.queryObjectByKey(key, ossPubClient, BUCKET_NAME2);
		                		
		                	}else if (1 == space){
		                		
		                		ossUploadServiceImpl.uploadOss(key, fullPath, ossPriClient, BUCKET_NAME1);
		                		
		                		ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key, BUCKET_NAME1);
		                		
		                		url = ossQueryServiceImpl.queryObjectByKey(key, ossPriClient, BUCKET_NAME1);
		                		
		                	}
		    				
		    				fileData.put("url", url);
		    				fileData.put("error",0);
		    				fileData.put("fileId", fileModel.getId()==null?0:fileModel.getId());
		    				
		    				fileServiceImpl.updateFileOssUrl(fileModel.getId(), space==1?"":url, key);
	    				
	                	} catch (Exception e) {
	                		e.printStackTrace();
	                	}
	                }
				};
				executorService.execute(syncRunnable);
				
				if("mp4".equals(fileType)){
					Runnable syncRunnable1 = new Runnable(){
		                @Override
		                public void run() {
		                	try {
								String imgPath = VideoUtil.fetchFrame(fullPath, null);
								String key1 = ossUploadServiceImpl.getOssKey("jpg");
			    				ossUploadServiceImpl.uploadOss(key1, imgPath, ossPubClient, BUCKET_NAME2);
			    				String url1 = ossQueryServiceImpl.queryPublicUrlByKey(key1, BUCKET_NAME2, OSS_ENDPOINT2);
			    				
			    				fileData.put("imgUrl", url1);
			    				
			    				fileServiceImpl.updateFileDescription(fileModel.getId(), url1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                	
		                }

		            };

		            executorService.execute(syncRunnable1);
				}    
				
	            executorService.shutdown();
	            while (!executorService.awaitTermination(1, TimeUnit.SECONDS));
			}
			
	//		request.getHeader("Accept");
			FormLogger.logFlow("header.Accept =" + request.getHeader("Accept"), FormLogger.LOG_TYPE_INFO);
			responseJSON.setMsg(hintMessage);
			responseJSON.setData(fileData);
			
			log.info(hintMessage);
			log.info("fileData"+fileData);
			log.info(responseJSON);
			response.setContentType("application/json;charset=utf-8");  
			response.getWriter().write(FormUtil.toJsonString(responseJSON));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("手机端上传文件失败", e);
			responseJSON.setCode(ExErrorCode.APP_UPLOAD_FAIL.getCode());
			responseJSON.setMsg(ExErrorCode.APP_UPLOAD_FAIL.getMsg());
		}	
	}
	

}
