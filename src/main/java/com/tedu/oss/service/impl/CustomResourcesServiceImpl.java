package com.tedu.oss.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssDeleteService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.plugin.resource.vo.ResourcesFile;

@Service("customResourcesServiceImpl")
public class CustomResourcesServiceImpl implements CustomResourcesService{
	
	@Autowired
	private FormMapper formMapper;
	
	@Autowired
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private OssDeleteService ossDeleteServiceImpl;
	
	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;
	
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Long insertResources(ResourcesFile resourcesFile, String key, Long parentId, String fileId, String bucketName, String versionCode, int resourcesType, int source) {

		TableModel model = new TableModel() {

			@Override
			public String getTableName() {
				return "t_customer_resources";
			}

			@Override
			public String[] getFields() {
				String[] str = { "name", "file_type", "file_path", "parent_id", "backup_type", "backup_id",
						"version_code", "resources_type", "file_id", "oss_key", "bucket_name", "source", "create_time",
						"create_by", "update_time", "update_by" };
				return str;
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();

		String newFileName = resourcesFile.getName().replace("_scorm", "").replace("_Scorm", "").replace("_SCORM", "").replace(".zip", "");
		
		map.put("name", newFileName);
		if (resourcesFile.isDirectory()) {
			map.put("fileType", "");
			map.put("filePath", resourcesFile.getPublicFullPath());
			map.put("bucketName", "");
		} else {
			map.put("fileType", resourcesFile.getType());
			map.put("filePath", resourcesFile.getPublicFullPath());
			map.put("bucketName", bucketName);

		}
		map.put("parentId", parentId);
		map.put("backupType", 1);
		map.put("backupId", 0);
		map.put("versionCode", versionCode);
		map.put("resourcesType", resourcesType);
		map.put("fileId", fileId);
		map.put("ossKey", key);
		map.put("source", source);
		map.put("createTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("createBy", SessionUtils.getUserInfo().getUserId());
		map.put("updateTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("updateBy", SessionUtils.getUserInfo().getUserId());
		SimpleFormModel simpleModel = new SimpleFormModel(model, map);
		int s = formMapper.insert(simpleModel);
		if (s == 1) {
			return Long.parseLong(simpleModel.getPrimaryFieldValue().toString());
		} else {
			log.error("新增全路径为" + resourcesFile.getFullpath() + ";fileId为" + fileId + "的t_customer_resources表失败！");
			return 0L;
		}
	}

	@Override
	public void deleteSourceFileAndOssFile(String string) {
		Map<String, Object> resourceMap = ossQueryServiceImpl.getResourceFileById(string);
		String resourcesType = resourceMap.get("resourcesType")==null?"0":resourceMap.get("resourcesType").toString();
		String oldFilePath = resourceMap.get("filePath")==null?"0":resourceMap.get("filePath").toString();
		String oldOssKey = resourceMap.get("ossKey")==null?"0":resourceMap.get("ossKey").toString();
		try {
			if(resourcesType.equals(FileTypeEnum.OTHERS.getCode())){
				ossDeleteServiceImpl.deleteObjectByKey(oldOssKey, ossPriClient, BUCKET_NAME1);
			}
		
			File f = new File(oldFilePath);
			f.delete();//删除旧文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
