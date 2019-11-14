package com.tedu.oss.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;

public interface OssQueryService {

	String queryObjectByKey(String key, OSS ossClient, String BUCKET_NAME);

	Map<String, Object> queryObjectById(String customerResourcesId, String module, OSS ossClient, String BUCKET_NAME);

	Map<String, Object> getResourceFileById(String fileId);
	
	String queryPublicUrlByKey(String key, String BUCKET_NAME, String OSS_ENDPOINT);

	InputStream queryDownPdfByKey(String key, OSS ossPriClient, String bUCKET_NAME1)  throws IOException ;

	String queryCDNUrlByOssUrl(String fileId, String fileOssUrl);

	String getNewContentByContent(String content);
	

}
