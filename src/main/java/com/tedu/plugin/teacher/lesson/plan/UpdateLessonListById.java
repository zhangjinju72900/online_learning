package com.tedu.plugin.teacher.lesson.plan;

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

@Service("UpdateLessonListById")
public class UpdateLessonListById implements ILogicPlugin {
   
	@Autowired(required=false)
    private FormMapper formMapper;

	public final Logger log = Logger.getLogger(this.getClass());

    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        return null;
    }


	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String, Object> map = (Map<String, Object>) requestObj.getData();
        String [] ids = map.get("lessonId").toString().split(",");
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> updateMap = new HashMap<>();
		for(String id:ids) {
			updateMap.put("id", id);
			cModel.setData(updateMap);
			cModel.setSqlId("khTeacher/lessonPlan/UpdateLessonListById");
			formMapper.saveCustom(cModel);
		}
		
	}
}
