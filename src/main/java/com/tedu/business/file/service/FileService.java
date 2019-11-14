package com.tedu.business.file.service;

public interface FileService {

	void updateFileDescription(Integer id, String url1);
	
	void updateFileOssUrl(Integer id, String url1, String key);

}
