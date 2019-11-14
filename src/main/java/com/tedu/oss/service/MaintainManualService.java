package com.tedu.oss.service;

import java.util.Map;

import com.tedu.plugin.resource.vo.ResourcesFile;

public interface MaintainManualService {

	Long insertMaintainManual(ResourcesFile resourcesFile, String key, Long parentId, String fileId, String bucketName, int resourcesType, int source);

	Map<String, Object> queryFileById(String string);

}
