package com.tedu.business.file.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.business.file.service.FileService;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService{

	@Resource
	private FormMapper formMapper;
	
	/**
	 * 保存视频文件  截图的oss地址
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateFileDescription(Integer id, String url) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/discover/information/updateFileDes");
		Map data = new HashMap<>();
		data.put("description", url);
		data.put("id", id);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
	}
	
	
	/**
	 * 保存文件本身的 oss 地址
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateFileOssUrl(Integer id, String url, String key) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/discover/information/updateFileOssUrl");
		Map data = new HashMap<>();
		data.put("key", key);
		data.put("url", url);
		data.put("id", id);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
	}
	
}
