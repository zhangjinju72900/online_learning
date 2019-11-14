package com.tedu.oss.service;

import java.util.Map;

import com.tedu.plugin.resource.vo.ResourcesFile;

public interface CustomResourcesService {

	Long insertResources(ResourcesFile resourcesFile, String key, Long parentId, String fileId, String bucketName, String versionCode, int resourcesType, int source);

	void deleteSourceFileAndOssFile(String string);

}
