package com.tedu.plugin.teacher.attend;

import com.aliyun.oss.OSS;
import com.google.zxing.WriterException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.common.util.FileUtil;
import com.tedu.common.util.QrCodeCreateUtil;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("CloseClassPlugin")
public class CloseClassPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	FormService formService;
	@Resource
	com.tedu.component.EasemobComponent easemobComponent;


	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String attendClassRecordId = map.get("attendClassRecordId")!=null?map.get("attendClassRecordId").toString():"";
		QueryPage qp = new QueryPage();
		qp.setParamsByMap(map);
		qp.getData().put("attendClassRecordId", attendClassRecordId);
		qp.setQueryParam("QryUserNotRecord");
		List<Map<String, Object>> userList = formService.queryBySqlId(qp);
		qp.setQueryParam("khTeacher/task/attendClass/QryAttendClassRecord");
		CustomFormModel cModel = new CustomFormModel();
		List<Map<String, Object>> classRecord = formService.queryBySqlId(qp);
		if (classRecord.size() > 0 && userList.size()>0) {
			String inTitle = classRecord.get(0).get("courseName") != null ? classRecord.get(0).get("courseName").toString()
					: "";
			String userName = classRecord.get(0).get("teacherName") != null? classRecord.get(0).get("teacherName").toString(): "";
			String createBy = classRecord.get(0).get("id") != null? classRecord.get(0).get("id").toString(): "";
			String startTime = classRecord.get(0).get("startTime") != null? classRecord.get(0).get("startTime").toString(): "";
			String courseName = classRecord.get(0).get("courseName") != null? classRecord.get(0).get("courseName").toString(): "";
			// 新增notice
			Map<String, Object> noticeMap = new HashMap<String, Object>();
			noticeMap.put("title","缺勤提醒");
			noticeMap.put("context","教师"+userName+startTime+"的课程《"+courseName+"》未签到！");
			noticeMap.put("source", 0);
			noticeMap.put("updateBy", SessionUtils.getUserInfo().getUserId());
			cModel.setSqlId("InsertNoticeInformation");
			cModel.setData(noticeMap);
			formMapper.saveCustom(cModel);
			// 获取该消息通知
			qp.setQueryParam("QryNoticeByMaxId");
			List<Map<String, Object>> noticeList = formService.queryBySqlId(qp);
			
			formEngineResponse.setData(map);
			formEngineResponse.setCode("0");
			formEngineResponse.setMsg("当前课程已结束");
			if (noticeList.size() > 0) {
				for(int i =0;i<userList.size();i++) {
					Map<String,Object> userMap = userList.get(i);
					
					String noticeId = noticeList.get(0).get("id").toString();
					noticeMap.put("baseId", noticeId);
					// 插入明细表
					cModel.setSqlId("khAdmin/noticeManage/insertMessageRecord");
					String array = "";
					String userId =userMap.get("id").toString();
					String easeUserName = userMap.get("easName").toString();
					noticeMap.put("receiverId", userId);
					noticeMap.put("userId", 2);
					cModel.setData(noticeMap);
					formMapper.saveCustom(cModel);
					// 发送通知
					String title ="缺勤提醒";
					String context = "教师"+userName+startTime+"的课程《"+courseName+"》未签到！";
					easemobComponent.sendNotice(noticeId, easeUserName.split(","), title, context);
				}
				
			}
		}
	}
	
	

}
