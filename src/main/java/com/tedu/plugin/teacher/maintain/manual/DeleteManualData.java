package com.tedu.plugin.teacher.maintain.manual;

import com.aliyun.oss.OSS;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
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
import java.util.Map;

@Service("deleteManualData")
public class DeleteManualData implements ILogicServicePlugin {
   
	@Autowired(required=false)
    private FormMapper formMapper;
    
    @Resource
	private OssUploadService ossUploadServiceImpl;
    
    @Resource
	private OssRecordService ossRecordServiceImpl;
    
    @Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;
	
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
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
        Map<String, Object> map = (Map<String, Object>) formModel.getData();
        
		if(map.get("id") != null && StringUtils.isNotBlank(map.get("id").toString())){//编辑
		Map<String, Object> mmMap = maintainManualServiceimpl.queryFileById(map.get("id").toString());
			String fullPath = mmMap.get("filePath")==null?"":mmMap.get("filePath").toString();
			File f = new File(fullPath);
			f.delete();//删除原文件
			
			String ossKey = mmMap.get("ossKey")==null?"":mmMap.get("ossKey").toString();
			try {
				if(StringUtils.isNotBlank(ossKey)){
					ossDeleteServiceImpl.deleteObjectByKey(ossKey, ossPriClient, BUCKET_NAME1);
				}
			} catch (Exception e) {
				log.error("删除oss文件失败", e);
				e.printStackTrace();
			}
		}
		
    }
}
