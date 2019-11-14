package com.tedu.oss.service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.aliyun.oss.OSS;
import com.tedu.plugin.resource.vo.ResourcesFile;

public interface OssUploadService {

	String getOssKey(String fileType);

	void uploadOss(String key, String publicFullPath, OSS ossClient, String BUCKET_NAME);

	Map<String, String> getFilePathById(String fileId);

//	void fuck(List<ResourcesFile> l, Map<String, List<ResourcesFile>> resourcesMap, Map<String, Long> im,
//			String fileId, OSS ossClient, String BUCKET_NAME, String coding);

	void clearUpData(String fileId, String id, String coding, String path, String zipFilePath, OSS ossClient, String BUCKET_NAME, String resourceType)throws IOException;
	
	Map<String, Object> getCustomerFilePathById(String id);

}
