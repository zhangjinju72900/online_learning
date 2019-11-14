package com.tedu.plugin.resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.common.constant.FtpUploadResourceTypeEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.common.util.FileUtil;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

/**
 * 
 * @ClassName: ImportExecute
 * @Description:TODO 导入资源处理类
 * @author: qun
 * @date: 2018年7月27日 下午1:11:12
 *
 */
@Service("ftpResourcePlugin")
public class FtpResourcePlugin implements ILogicPlugin {

	@Autowired
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Autowired
	private CustomResourcesService customResourcesServiceImpl;
	
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

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String, Object> map = requestObj.getData();
		
		Map<String, Object> result = getFtpUploadById(map.get("id").toString());
		
		String fileName = result.get("filename")==null?"":result.get("filename").toString();
		String fileType = result.get("file_type")==null?"":result.get("file_type").toString();
		String resourceType = result.get("resource_type")==null?"":result.get("resource_type").toString();
		String id = result.get("parent_id")==null?"":result.get("parent_id").toString();
		String codingFormat = result.get("coding_format")==null?"":result.get("coding_format").toString();
		String fullPath = UPLOAD_PATH+fileName;
		
		if(StringUtils.isNotBlank(fileName)){
			try {
				updateFtpUploadStatus(map.get("id").toString(), 1, "");
				String endType = fileName.substring(fileName.lastIndexOf(".")+1);
				String path = fullPath.substring(0, fullPath.lastIndexOf("."));
				
				ExecutorService executorService = Executors.newFixedThreadPool(1);
				
				executorService.execute(new ThreadFtpResource(endType, fileType, resourceType, fullPath, fileName, codingFormat, path, map.get("id").toString(), id));
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("执行ftp工具上传失败", e);
				updateFtpUploadStatus(map.get("id").toString(), 3, "执行ftp工具上传失败");
			}
		}

	}

	private void updateFtpUploadStatus(String id, int status, String remark) {

		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/resourceUpload/UpdateFtpResourceStatus");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", id);
		map1.put("result", status);
		map1.put("remark", remark);
		cModel.setData(map1);
		formMapper.saveCustom(cModel);
		
	}

	private Map<String, Object> getFtpUploadById(String id) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourceUpload/QryFtpResourceById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/resourceUpload/QryFtpResourceById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			return tables.get(0);
		}
		return null;
	}
	
	
	private class ThreadFtpResource implements Runnable{

		private String endType;
		
		private String fileType;
		
		private String resourceType;
		
		private String fullPath;
		
		private String fileName;
		
		private String codingFormat;
		
		private String path;
		
		private String ftpResourceId;
		
		private String id;
		
		public ThreadFtpResource(String endType, String fileType, String resourceType, String fullPath, String fileName, String codingFormat,
				String path, String ftpResourceId, String id) {
			super();
			this.endType = endType;
			this.fileType = fileType;
			this.resourceType = resourceType;
			this.fullPath = fullPath;
			this.fileName = fileName;
			this.codingFormat = codingFormat;
			this.path = path;
			this.ftpResourceId = ftpResourceId;
			this.id = id;
		}

		@Override
		public void run() {
			try {
				if("zip".equals(endType) && FileTypeEnum.SCORM.getCode().equals(fileType)){//单个scorm文件
					
					FileUtil.unzip(fullPath, path, true, "0".equals(codingFormat)?"GBK":"UTF-8");
					
					if(resourceType.equals(FtpUploadResourceTypeEnum.CUSTOMER_USER_RESOURCE.getCode())){
						
						customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, endType, fullPath), "", Long.parseLong(id), ftpResourceId, 
								"", "1.0", ResourceTypeEnum.SCORM.getCode(), CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
					}else if(resourceType.equals(FtpUploadResourceTypeEnum.MAINTAIN_MANUAL.getCode())){
						
						maintainManualServiceImpl.insertMaintainManual(new ResourcesFile(fileName, path, endType, fullPath), "", Long.parseLong(id), ftpResourceId, BUCKET_NAME1, Integer.parseInt(fileType), 
								CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
					}
				
				}else if("zip".equals(endType) && FileTypeEnum.PPT.getCode().equals(fileType)){//单个ppt转H5文件
					
					FileUtil.unzip(fullPath, path, true, "0".equals(codingFormat)?"GBK":"UTF-8");
					
					if(resourceType.equals(FtpUploadResourceTypeEnum.CUSTOMER_USER_RESOURCE.getCode())){
						
						customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, endType, fullPath), "", Long.parseLong(id), ftpResourceId, 
								"", "1.0", ResourceTypeEnum.PPT.getCode(), CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
					}else if(resourceType.equals(FtpUploadResourceTypeEnum.MAINTAIN_MANUAL.getCode())){
						
						maintainManualServiceImpl.insertMaintainManual(new ResourcesFile(fileName, path, endType, fullPath), "", Long.parseLong(id), ftpResourceId, "", Integer.parseInt(fileType), 
								CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
					}
						
				}else if(!"zip".equals(endType) && FileTypeEnum.OTHERS.getCode().equals(fileType)){//0普通 非scorm、非PPT转H5的单个文件
					
					String key = ossUploadServiceImpl.getOssKey(endType);
					ossUploadServiceImpl.uploadOss(key, fullPath, ossPriClient, BUCKET_NAME1);
					
					ossRecordServiceImpl.insertOssRecord(fileName, endType, ftpResourceId, key, BUCKET_NAME1);
					
					if(resourceType.equals(FtpUploadResourceTypeEnum.CUSTOMER_USER_RESOURCE.getCode())){
						
						customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, endType, fullPath), key, Long.parseLong(id), ftpResourceId, 
								BUCKET_NAME1, "1.0", ResourceTypeEnum.OTHERS.getCode(), CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
					}else if(resourceType.equals(FtpUploadResourceTypeEnum.MAINTAIN_MANUAL.getCode())){
						
						maintainManualServiceImpl.insertMaintainManual(new ResourcesFile(fileName, path, endType, fullPath), key, Long.parseLong(id), ftpResourceId, BUCKET_NAME1, Integer.parseInt(fileType), 
								CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
					}	
						
				} else if("zip".equals(endType) && FileTypeEnum.OTHERS.getCode().equals(fileType)){//多文件
					
					ossUploadServiceImpl.clearUpData(ftpResourceId, id, "0".equals(codingFormat)?"GBK":"UTF-8", path, fullPath, ossPriClient, BUCKET_NAME1, resourceType);
					
				}
				
				File entryFile = new File(fullPath);
				entryFile.delete();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				updateFtpUploadStatus(ftpResourceId, 3, "执行ftp工具上传失败");
			}
			
			updateFtpUploadStatus(ftpResourceId, 2, "");
			
		}
		
	}

}
