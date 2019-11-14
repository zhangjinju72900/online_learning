package com.tedu.oss.service;

import com.aliyun.oss.OSS;

public interface OssDeleteService {

	String deleteObjectByKey(String key, OSS ossClient, String BUCKET_NAME)throws Exception;

}
