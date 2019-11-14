package com.tedu.plugin.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.common.util.FileUtil;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssDeleteService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

/**
 * 
 * @ClassName: ImportExecute
 * @Description:TODO 用户资源管理，资源编辑
 * @author: qun
 * @date: 2018年7月27日 下午1:11:12
 *
 */
@Service("saveExecute")
public class SaveExecute implements ILogicPlugin {

	@Autowired
	private OssUploadService ossUploadServiceImpl;
	
	@Autowired
	private OssQueryService ossQueryServiceImpl;
	
	@Autowired
	private CustomResourcesService customResourcesServiceImpl;
	
	@Resource
	private OssDeleteService ossDeleteServiceImpl;
	
	@Resource
	private FormMapper formMapper;

	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		Map<String, Object> map = requestObj.getData();
		String fileId = map.get("fileId") == null ? "" : map.get("fileId").toString();
		String id = map.get("ctlId") == null ? "" : map.get("ctlId").toString();
		
		Map<String, String> result = ossUploadServiceImpl.getFilePathById(fileId);
		String path = result.get("path");
		String fullPath = result.get("fullpath");
		String fileName = result.get("fileName");
		String fileType = result.get("fileType");
		
		Map<String, Object> resourceMap = ossQueryServiceImpl.getResourceFileById(id);
		String oldFileId = resourceMap.get("fileId")==null?"":resourceMap.get("fileId").toString();

		if(!oldFileId.equals(fileId)){
			try {
				String oldParentId = resourceMap.get("parentId")==null?"0":resourceMap.get("parentId").toString();
				String resourcesType = resourceMap.get("resourcesType")==null?"0":resourceMap.get("resourcesType").toString();
				String versionCode = resourceMap.get("versionCode")==null?"0":resourceMap.get("versionCode").toString();
				
				String newVersionCode = "";
				try {
					newVersionCode = (Double.parseDouble(versionCode) + 1) + "";
				} catch (Exception e) {
					e.printStackTrace();
					newVersionCode = "3.0";
				}
				String key = "";
					
				if(resourcesType.equals(FileTypeEnum.OTHERS.getCode())){
					key = ossUploadServiceImpl.getOssKey(fileType);
					ossUploadServiceImpl.uploadOss(key, fullPath, ossPriClient, BUCKET_NAME1);
					
				}else{
					
					FileUtil.unzip(fullPath, path, true, "GBK");
				}
				
				customResourcesServiceImpl.deleteSourceFileAndOssFile(id);
				
				Long newId = customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, fileType, fullPath), key, Long.parseLong(oldParentId), fileId, 
						StringUtils.isBlank(key)?"":BUCKET_NAME1, newVersionCode, Integer.parseInt(resourcesType), CustomerResourcesSourceEnum.TFILEINDEX.getCode());
			
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId("khAdmin/resourcesManage/UpdateResorceById");
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("id", id);
				map1.put("backupId", newId);
				map1.put("backupType", 0);
				map1.put("updateTime", map.get("updateTime")==null?"":map.get("updateTime").toString());
				map1.put("updateBy", map.get("updateBy")==null?"":map.get("updateBy").toString());
				cModel.setData(map1);
				formMapper.saveCustom(cModel);
				
				cModel = new CustomFormModel();
				cModel.setSqlId("khAdmin/resourcesManage/UpdateResorceByBackupId");
				map1 = new HashMap<String, Object>();
				map1.put("backupId", newId);
				map1.put("oldBackupId", id);
				map1.put("updateTime", map.get("updateTime")==null?"":map.get("updateTime").toString());
				map1.put("updateBy", map.get("updateBy")==null?"":map.get("updateBy").toString());
				cModel.setData(map1);
				formMapper.saveCustom(cModel);
				
				//课程中的原资源更改为指向新的资源
				cModel = new CustomFormModel();
				cModel.setSqlId("khAdmin/resourcesManage/UpdateCourseSectionResorceByResourId");
				map1 = new HashMap<String, Object>();
				map1.put("customerResourcesId", newId);
				map1.put("oldCustomerResourcesId", id);
				map1.put("updateTime", map.get("updateTime")==null?"":map.get("updateTime").toString());
				map1.put("updateBy", map.get("updateBy")==null?"":map.get("updateBy").toString());
				cModel.setData(map1);
				formMapper.saveCustom(cModel);
			} catch (Exception e) {
				responseObj.setCode("999999");
				responseObj.setMsg("重新更改资源失败");
				e.printStackTrace();
			}	
		}
		

	}

}
