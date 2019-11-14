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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SaveLessonPlanPlugin")
public class SaveLessonPlanPlugin implements ILogicPlugin {
   
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
		CustomFormModel cModel = new CustomFormModel();
		//获取主键id
		String id = formModel.getPrimaryFieldValue();
		String model = map.get("Mode").toString();
		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("planId", id);
		List<Map<String, Object>> steps = map.get("step")!=null? (ArrayList) map.get("step"):new ArrayList();
		if("Add".equals(model)) {
			for(Map<String, Object> stepMap:steps) {
				stepMap.put("planId", id);
				cModel.setData(stepMap);
				cModel.setSqlId("khTeacher/lessonPlan/InsertLessonStep");
				formMapper.saveCustom(cModel);
			}
		}else {
			//删除教学步骤
			cModel.setData(updateMap);
			cModel.setSqlId("khTeacher/lessonPlan/DeleteLessonStep");
			formMapper.saveCustom(cModel);
			for(Map<String, Object> stepMap:steps) {
				stepMap.put("planId", id);
				cModel.setData(stepMap);
				cModel.setSqlId("khTeacher/lessonPlan/InsertLessonStep");
				formMapper.saveCustom(cModel);
			}
		}
		
		/*Map<String, Object> updateMap = new HashMap<>();
		for(String id:ids) {
			updateMap.put("id", id);
			cModel.setData(updateMap);
			cModel.setSqlId("khTeacher/lessonPlan/UpdateLessonListById");
			formMapper.saveCustom(cModel);
		}*/
		
	}
}
