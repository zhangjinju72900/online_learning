package com.tedu.oss.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.common.constant.HtmlResourceModuleEnum;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;

@Service("ossQueryServiceImpl")
public class OssQueryServiceImpl implements OssQueryService{
	
	@Autowired
	private FormMapper formMapper;
	
	@Resource
	private MaintainManualService maintainManualServiceImpl;
	
	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private com.tedu.business.file.service.FileService fileServiceImpl;
	
	@Resource
	private OSS ossPubClient;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;
	
	private String HTTP = "http://";
	
	private String PUBLIC_OSS_URL_PREFIX = "know-how001.oss-cn-beijing.aliyuncs.com";
	
	private String PUBLIC_CDN_URL_PREFIX = "know-how001-cdn.knowhowedu.cn";
	
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	
	@Override
	public String queryObjectByKey(String key, OSS ossClient, String BUCKET_NAME) {
		 Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24);
		 GeneratePresignedUrlRequest generatePresignedUrlRequest =new GeneratePresignedUrlRequest(BUCKET_NAME, key);
        generatePresignedUrlRequest.setExpiration(expiration);

		String url = ossClient.generatePresignedUrl(generatePresignedUrlRequest).toString();
		
		return url;
	}

	@Override
	public Map<String, Object> queryObjectById(String customerResourcesId, String module, OSS ossClient, String BUCKET_NAME) {
		
		Map<String, Object> param = new HashMap<>();
		
		if(HtmlResourceModuleEnum.MAINTAIN_MANUAL.getCode().equals(module)){
			param = maintainManualServiceImpl.queryFileById(customerResourcesId);
		}else{
			param = getResourceFileById(customerResourcesId);
		}
		
		Map <String, Object> result = new HashMap<>();
		
		if(param != null && param.get("ossKey") != null){
			String key = param.get("ossKey").toString();
			String fileId = param.get("fileId").toString();
			if(fileId.contains("http://know-how001")) {
				result.put("url", fileId);
			}else {
				String url = queryObjectByKey(key, ossClient, BUCKET_NAME);
				result.put("url", url);
			}
			result.put("key", key);
			
			
			
			return result;
		}
			
		return null;
	}
	
	
	public Map<String, Object> getResourceFileById(String customerResourcesId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourcesManage/QryCustomerResourcesById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/resourcesManage/QryCustomerResourcesById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", customerResourcesId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			Map<String, Object> result = tables.get(0);
			return result;
		}
		return null;
	}
	
	@Override
	public String queryPublicUrlByKey(String key, String BUCKET_NAME, String OSS_ENDPOINT) {
		StringBuffer sb = new StringBuffer();
		sb.append(HTTP).append(BUCKET_NAME).append(".").append(OSS_ENDPOINT).append("/").append(key);
		return sb.toString();
		
	}

	@Override
	public InputStream queryDownPdfByKey(String key, OSS ossPriClient, String bUCKET_NAME1) throws IOException {
		OSSObject object  = ossPriClient.getObject(new GetObjectRequest(bUCKET_NAME1, key));
        System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
        return object.getObjectContent();
	}
	
	private static String displayTextInputStream(InputStream input) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		try {
			while (true) {
				String line = reader.readLine();
				if (line == null){
					break;	
				}
				sb.append(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			reader.close();
		}
		return sb.toString();
    }

	@Override
	public String queryCDNUrlByOssUrl(String fileId, String fileOssUrl) {
		if(StringUtils.isNotBlank(fileId) && !"0".equals(fileId) && fileOssUrl.contains(PUBLIC_OSS_URL_PREFIX)){
			fileOssUrl = fileOssUrl.replace(PUBLIC_OSS_URL_PREFIX, PUBLIC_CDN_URL_PREFIX);
		}else if(StringUtils.isNotBlank(fileId) && !"0".equals(fileId) && StringUtils.isBlank(fileOssUrl)){
			if(fileId.contains(",")){
				String[] fileIdArray = fileId.split(",");
				for (int i=0; i<fileIdArray.length; i++) {
					if(StringUtils.isNotBlank(fileIdArray[i]) && !"0".equals(fileIdArray[i])){
						executorService.execute(new ThreadUploadPicToOss(fileIdArray[i]));
					}
				}
			}else{
				executorService.execute(new ThreadUploadPicToOss(fileId));
			}
		}
		return fileOssUrl;
	}
	
	
	private class ThreadUploadPicToOss implements Runnable{
		
		private String fileId;

		public ThreadUploadPicToOss(String fileId){
			this.fileId = fileId;
		}
		
		@Override
		public void run() {
			Map<String, String> map = ossUploadServiceImpl.getFilePathById(fileId);
			String key = ossUploadServiceImpl.getOssKey(map.get("fileType"));
			ossUploadServiceImpl.uploadOss(key, map.get("fullpath"), ossPubClient, BUCKET_NAME2);
			String url = queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
			fileServiceImpl.updateFileOssUrl(Integer.parseInt(fileId), url, key);
		}
		
	}
	

	@Override
	public String getNewContentByContent(String content) {
		if(StringUtils.isNotBlank(content) && content.contains(PUBLIC_OSS_URL_PREFIX)){
			content = content.replace(PUBLIC_OSS_URL_PREFIX, PUBLIC_CDN_URL_PREFIX);
		}
		return content;
	}

}
