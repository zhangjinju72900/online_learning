package com.tedu.plugin.teacher.maintain.manual;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.oss.service.OssDeleteService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insertManualData")
public class InsertManualData implements ILogicPlugin {
   
	@Autowired(required=false)
    private FormMapper formMapper;
    
    @Resource
	private OssUploadService ossUploadServiceImpl;
    
    @Resource
	private OssRecordService ossRecordServiceImpl;
    
    @Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Value("${oss.access_id}")
	private String ossAccess_id;
	@Value("${oss.access_key}")
	private String ossAccess_key;
	@Value("${oss.oss_endpoint}")
	private String oss_endpoint;
	@Resource
	private OssDeleteService ossDeleteServiceImpl;
	
	@Resource
	private MaintainManualService maintainManualServiceimpl;
	
	public final Logger log = Logger.getLogger(this.getClass());

    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        return null;
    }

    @Override
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
        Map<String, Object> map = (Map<String, Object>) formModel.getData();
        System.out.println(map);
        String fileId = map.get("fileId") == null ? "" : map.get("fileId").toString();
		String parentId = map.get("parentId") == null ? "" : map.get("parentId").toString();
		
		OSSClient ossPriClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
		if(map.get("id") != null && StringUtils.isNotBlank(map.get("id").toString())){//编辑
			Map<String, Object> mmMap = maintainManualServiceimpl.queryFileById(map.get("id").toString());
			if(mmMap != null && mmMap.get("fileId") != null && StringUtils.isNotBlank(mmMap.get("fileId").toString()) && fileId.equals(mmMap.get("fileId").toString())){ //未改变附件
				return ;
			}else{//改变附件
				String fullPath = mmMap.get("filePath")==null?"":mmMap.get("filePath").toString();
				File f = new File(fullPath);
				f.delete();//删除原文件
				
				String ossKey = mmMap.get("ossKey")==null?"":mmMap.get("ossKey").toString();
				try {
					ossDeleteServiceImpl.deleteObjectByKey(ossKey, ossPriClient, BUCKET_NAME1);
				} catch (Exception e) {
					log.error("删除oss文件失败", e);
					e.printStackTrace();
				}
			}
		}
		
		
		Map<String, String> result = ossUploadServiceImpl.getFilePathById(fileId);
		String path = result.get("path");
		String fullPath = result.get("fullpath");
		String fileName = result.get("fileName");
		String fileType = result.get("fileType");
		
		try {
			String key = ossUploadServiceImpl.getOssKey(fileType);
			ossUploadServiceImpl.uploadOss(key, fullPath, ossPriClient, BUCKET_NAME1);
			
			ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileId, key, BUCKET_NAME1);
			
			if(map.get("id") != null && StringUtils.isNotBlank(map.get("id").toString())){//编辑
				CustomFormModel cModel = new CustomFormModel();
				Map<String, Object> updateMap = new HashMap<>();
				updateMap.put("id", map.get("id"));
				updateMap.put("name", fileName);
				updateMap.put("type", fileType);
				updateMap.put("path", path);
				updateMap.put("fileId", fileId);
				updateMap.put("fileName", fileName);
				updateMap.put("ossKey", key);
				updateMap.put("ossUrl", BUCKET_NAME1);
				cModel.setData(updateMap);
	        	cModel.setSqlId("khAdmin/maintainManual/updateManual");
	        	formMapper.saveCustom(cModel);
			}else{//新增
				maintainManualServiceimpl.insertMaintainManual(new ResourcesFile(fileName, path, fileType, fullPath), key, Long.parseLong(parentId), fileId, BUCKET_NAME1, 
						ResourceTypeEnum.OTHERS.getCode(), CustomerResourcesSourceEnum.TFILEINDEX.getCode());
			}	
		} catch (Exception e) {
			log.error("维修手册存储失败！", e);
			e.printStackTrace();
			responseObj.setCode("111111");
			responseObj.setMsg("维修手册存储失败！");
		}
		System.out.println(map);
        
        
        /*System.out.println(map);
    	QueryPage q = new QueryPage();
        q.setDataByMap(map);
        q.setQueryParam("khAdmin/maintainManual/selectManualDetails");
        List<Map<String,Object>> list = formMapper.query(q);
        System.out.println(list.get(0));
        if(map.get("id")==""){
        	list.get(0).put("pid", map.get("parentId"));
        	list.get(0).put("fileId", map.get("fileId"));
        	list.get(0).put("updateBy", map.get("updateBy"));
        	list.get(0).put("dataFlag", map.get("dataFlag"));
        	list.get(0).put("name", "");
        	cModel.setData(list.get(0));
        	cModel.setSqlId("khAdmin/maintainManual/insertManual");
        	formMapper.saveCustom(cModel);
        }else{
        	list.get(0).put("id", map.get("id"));
        	list.get(0).put("pid", map.get("parentId"));
        	list.get(0).put("fileId", map.get("fileId"));
        	list.get(0).put("updateBy", map.get("updateBy"));
        	list.get(0).put("dataFlag", map.get("dataFlag"));
        	list.get(0).put("name", "");
        	cModel.setData(list.get(0));
        	cModel.setSqlId("khAdmin/maintainManual/updateManual");
        	formMapper.saveCustom(cModel);
        }*/
    }
}
