package com.tedu.plugin.teacher.system.notice;

import com.google.common.collect.Lists;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.EasemobSendObjectEnum;
import com.tedu.common.constant.EasemobSendTypeEnum;
import com.tedu.component.EasemobComponent;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insertMessageRecord")
public class InsertMessageRecord implements ILogicPlugin {
	@Autowired(required = false)
	FormLogService formLogService;
	@Autowired(required = false)
	private FormMapper formMapper;

	@Resource
	com.tedu.component.EasemobComponent easemobComponent;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		/*
		 * Map<String , Object> map = formModel.getData(); map.put("source", "1");
		 * map.put("status", "1"); map.put("validFlag", "0"); formModel.setData(map);
		 */
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		CustomFormModel cModel = new CustomFormModel();
		System.out.println(map);
		long a = 0;

		String noticeId = formModel.getPrimaryFieldValue().toString();

		String title = map.get("title") == null ? "" : map.get("title").toString();
		String context = map.get("context") == null ? "" : map.get("context").toString();
		String classId = map.get("className").toString();
		if (classId.contains("[")) {
			classId = classId.replace("[", "").replace("]", "");
		}

		String[] classIds = classId.split(",");
		// 获取附件ID
		String [] fileIds = map.get("fileId") != null ? map.get("fileId").toString().split(",") :"".split(",");
		for(String fileId:fileIds) {
			if (!"".equals(fileId)) {
				map.put("noticeId", noticeId);
				map.put("fileId", fileId);
				cModel.setData(map);
				cModel.setSqlId("khTeacher/systemNotice/InsertNoticeFile");
				formMapper.saveCustom(cModel);
			}
		}
		
		

		List<String> classArr = new ArrayList<String>();
		for (int i = 0; i < classIds.length; i++) {
			classArr.add(classIds[i]);
			String classId1 = classIds[i];
			map.put("className", classId1);
			
			QueryPage q = new QueryPage();
			q.setDataByMap(map);
			/*q.setQueryParam("khTeacher/systemNotice/selectNoticeId");
			List<Map<String, Object>> id = formMapper.query(q);*/
			/*a = Long.parseLong(id.get(0).get("id").toString());*/
			q.setQueryParam("khTeacher/systemNotice/selectStuId");
			List<Map<String, Object>> list = formMapper.query(q);
			for (int j = 0; j < list.size(); j++) {
				list.get(j).put("baseId", noticeId);
				list.get(j).put("senderId", map.get("createBy"));
				cModel.setData(list.get(j));
				cModel.setSqlId("khTeacher/systemNotice/insertMessageRecord");
				formMapper.saveCustom(cModel);
			}
			// 插入学校班级数据
			cModel.setData(map);
			cModel.setSqlId("khTeacher/systemNotice/insertNoticeSchool");
			formMapper.saveCustom(cModel);
		}

		easemobComponent.sendNotice1(noticeId, classArr, title, context);
	}

}
