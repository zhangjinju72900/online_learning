package com.tedu.oss.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.oss.service.OssDeleteService;

@Service("ossDeleteServiceImpl")
public class OssDeleteServiceImpl implements OssDeleteService{

	ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	@Override
	public String deleteObjectByKey(String key, OSS ossClient, String BUCKET_NAME) throws Exception {

		if(StringUtils.isBlank(key)){
			throw new Exception("key为空");
		}
		Runnable syncRunnable = new Runnable(){
            @Override
            public void run() {
            	ossClient.deleteObject(BUCKET_NAME, key);
            }
		};
		executorService.execute(syncRunnable);
		return "success";

	}

}
