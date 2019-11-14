package com.tedu.plugin.teacher.homework.homeworkDistribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

/**
 * 保存作业明细信息
 * 
 * @author quancong
 *
 */
@Service("saveHomeworkPlugin")
public class SaveHomeworkPlugin implements ILogicServicePlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Resource
	com.tedu.component.EasemobComponent easemobComponent;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData());
		Map<String, Object> Modifiymap = (Map<String, Object>) requestObj.getData();
		// 获取主表id
		String homeworkId = Modifiymap.get("homeworkId") == null ? "" : Modifiymap.get("homeworkId").toString();
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		if (map.get("courseId") == null || StringUtils.isEmpty(map.get("courseId").toString())) {
			map.put("courseId", 0);
		}

		if (map.get("sectionId") == null || StringUtils.isEmpty(map.get("sectionId").toString())) {
			map.put("sectionId", 0);
		}
		String name = map.get("name").toString();
		String classId = map.get("classId").toString();
		String homeworkType = map.get("homeworkType").toString();
		String difficultyLevel = map.get("difficultyLevel").toString();
		String endTime = map.get("endTime").toString();
		String questionIdScore = map.get("questionIdScore").toString();
		if (name.equals("") || classId.equals("") || homeworkType.equals("") || difficultyLevel.equals("")
				|| endTime.equals("") || questionIdScore.equals("")) {
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "信息填写不完整 ", "请将信息填写完整");

		}
		// 查询当前用户的学校id
		QueryPage queryPage = new QueryPage();
		String schoolId = "";
		queryPage.setDataByMap(map);
		queryPage.setQueryParam("khTeacher/homework/homeworkDistribute/QryTeacherSchool");
		List<Map<String, Object>> schoolList = formMapper.query(queryPage);
		if (schoolList.size() > 0) {
			schoolId = schoolList.get(0).get("schoolId") + "";
		}
		formModel.getData().put("id", homeworkId);
		formModel.getData().put("schoolId", schoolId);
		return formModel;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		log.info(formModel.getData());
		Map<String, Object> Modifiymap = (Map<String, Object>) requestObj.getData();
		String id = Modifiymap.get("homeworkId") == null ? "" : Modifiymap.get("homeworkId").toString();
		// 获取主表id
		String homeworkId = "".equals(id) ? formModel.getPrimaryFieldValue() : id;
		String homeworkType = Modifiymap.get("homeworkType").toString();
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		// 获得题目id和分数字符串如"1-5,2-5"
		String[] ids = map.get("questionIdScore").toString().split(",");
		String source = map.get("source") == null
				|| org.apache.commons.lang3.StringUtils.isBlank(map.get("source").toString()) ? "1"
						: map.get("source").toString();// 默认导入
		// 判断homeworkTemplateId是否为空
		if (StringUtils.isEmpty(map.get("homeworkTemplateId").toString())) {
			map.put("homeworkTemplateId", 0);
		}
		map.put("homeworkId", homeworkId);
		log.info("ids: " + ids.toString());
		// 保存作业详情信息
		CustomFormModel cModel = new CustomFormModel();
		cModel.setData(map);

		// 先删除已有的作业详情记录
		/// cModel.setSqlId("khTeacher/homework/homeworkDistribute/DeleteHomeworkDetail");
		// formMapper.saveCustom(cModel);

		// 查询分发作业班级的学生id
		QueryPage queryPage = new QueryPage();
		queryPage.setDataByMap(map);
		queryPage.setQueryParam("khTeacher/homework/homeworkDistribute/QryHomeworkStudent");
		List<Map<String, Object>> studentList = formMapper.query(queryPage);
		String studentId = "";
		cModel.setSqlId("khTeacher/homework/homeworkDistribute/InsertHomeworkAnswer");
		String inTitle = Modifiymap.get("name") != null ? Modifiymap.get("name").toString() : "";
		long createBy = SessionUtils.getUserInfo().getUserId() ;
		String userName = SessionUtils.getUserInfo().getNickName()==null?"": SessionUtils.getUserInfo().getNickName();
		// 新增notice
		CustomFormModel cModelNotice = new CustomFormModel();
		Map<String, Object> noticeMap = new HashMap<String, Object>();
		noticeMap.put("title", userName + "发布了新作业《" + inTitle + "》");
		noticeMap.put("context", userName + "发布了新作业《" + inTitle + "》，赶快去完成吧！");
		noticeMap.put("source", 0);
		noticeMap.put("updateBy", SessionUtils.getUserInfo().getUserId());
		cModelNotice.setSqlId("InsertNoticeInformation");
		cModelNotice.setData(noticeMap);
		log.info(noticeMap.toString());
		formMapper.saveCustom(cModelNotice);
		// 获取该消息通知
		queryPage.setQueryParam("QryNoticeByMaxId");
		List<Map<String, Object>> noticeList = formService.queryBySqlId(queryPage);
		log.info(noticeList.size()>0?noticeList.toString():noticeList);
		for (Map<String, Object> sMap : studentList) {
			studentId = sMap.get("studentId") + "";
			String easName = sMap.get("easName") + "";
			map.put("studentId", studentId);
			cModel.setData(map);
			// 保存对应班级学生的作业信息t_homework_answer
			formMapper.saveCustom(cModel);
			log.info(easName);
		
			if (noticeList.size() > 0) {
				log.info("从表添加");
				String noticeId = noticeList.get(0).get("id").toString();
				noticeMap.put("baseId", noticeId);
				// 插入明细表
				cModelNotice.setSqlId("khAdmin/noticeManage/insertMessageRecord");
				noticeMap.put("receiverId", studentId);
				noticeMap.put("userId", 2);
				cModelNotice.setData(noticeMap);
				formMapper.saveCustom(cModelNotice);
				// 发送通知
				String title = userName + "发布了新作业《" + inTitle + "》";
				String context = userName + "发布了新作业《" + inTitle + "》，赶快去完成吧！";
				easemobComponent.sendNotice(noticeId, easName.split(","), title, context);
			}

		}
		// 获取被删除的id
		String deleteQuestionId = map.get("deleteId") != null ? map.get("deleteId").toString() : "";
		if (!"".equals(deleteQuestionId)) {
			String[] questionIds = deleteQuestionId.split(",");
			for (String qId : questionIds) {
				map.put("deleteQuestionId", qId);
				cModel.setSqlId("khTeacher/homework/homeworkDistribute/DeleteHomeworkDetail");
				formMapper.saveCustom(cModel);
			}
		}
		if ("0".equals(homeworkType)) {
			map.put("status", 3);
		} else {
			map.put("status", 1);
		}
		cModel.setSqlId("khTeacher/homework/homeworkDistribute/UpdateHomeworkStatus");
		formMapper.saveCustom(cModel);
		if (!"".equals(id)) {

			// 修改下添加除草稿外的作业详情

			queryPage.setQueryParam("khTeacher/homework/homeworkDistribute/QryHomeworkDetail");
			List<Map<String, Object>> homeworkDetailList = formMapper.query(queryPage);
			String questionIds = homeworkDetailList.size() > 0 ? homeworkDetailList.get(0).get("questionId").toString()
					: "";
			cModel.setSqlId("khTeacher/homework/homeworkDistribute/InsertHomeworkDetail");
			for (String questionIdScores : ids) {
				String[] questionIdScore = questionIdScores.split("-");
				String questionId = questionIdScore[0];
				if (!questionIds.contains(questionId)) {
					String score = "0";
					if (questionIdScore.length > 1) {
						if (!StringUtils.isEmpty(questionIdScore[1])) {
							score = questionIdScore[1];
						}

					}
					map.put("source", source);
					map.put("questionId", questionId);
					map.put("score", score);
					formMapper.saveCustom(cModel);
				}
			}
		} else {
			cModel.setSqlId("khTeacher/homework/homeworkDistribute/InsertHomeworkDetail");
			for (String questionIdScores : ids) {
				String[] questionIdScore = questionIdScores.split("-");
				String questionId = questionIdScore[0];
				String score = "0";
				if (questionIdScore.length > 1) {
					if (!StringUtils.isEmpty(questionIdScore[1])) {
						score = questionIdScore[1];
					}

				}
				map.put("source", source);
				map.put("questionId", questionId);
				map.put("score", score);
				formMapper.saveCustom(cModel);
			}
		}

	}

}
