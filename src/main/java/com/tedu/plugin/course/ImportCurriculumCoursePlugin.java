package com.tedu.plugin.course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tedu.base.engine.aspect.ILogicServicePlugin;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

/**
 * 导入课表时将courseName解析后得到courseId并联合主表id插入到t_curriculum_course中
 * 和className，gradeName解析后得到classId，gradeId联合主表id插入到t_curriculum_class表中
 * 
 * @author gonghaoxin
 *
 */
@Service("importCurriculumCoursePlugin")
public class ImportCurriculumCoursePlugin implements ILogicServicePlugin {

	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();

		String curriculumId = formModel.getPrimaryFieldValue();
		String schoolId = map.get("schoolId") == null ? "" : map.get("schoolId").toString();
		String proId = map.get("proId") == null ? "" : map.get("proId").toString();
		String courseNames1 = map.get("courseName1") == null ? "" : map.get("courseName1").toString();
		String courseNames2 = map.get("courseName2") == null ? "" : map.get("courseName2").toString();
		String courseNames3 = map.get("courseName3") == null ? "" : map.get("courseName3").toString();
		String courseNames4 = map.get("courseName4") == null ? "" : map.get("courseName4").toString();
		String courseNames5 = map.get("courseName5") == null ? "" : map.get("courseName5").toString();
		String executeTime = map.get("executeTime") == null ? "" : map.get("executeTime").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formDate = executeTime.replaceAll("/", "-");
		Date date = null;
		try {
			date = sdf.parse(formDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 用于插入t_curriculum_course表
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> logData = new HashMap<>();

		logData.put("executeDate", date);
		logData.put("id", curriculumId);
		cModel.setSqlId("khAdmin/schoolManage/curriculum/updateExecuteTimeById");
		cModel.setData(logData);
		formMapper.saveCustom(cModel);
		

		logData.put("curriculumId", curriculumId);
		cModel.setSqlId("ImportCurriculumCourse");

		List<Map<String, Object>> qlist = new ArrayList<Map<String, Object>>();
		// 当courseName为空时，不执行插入
		String[] names1 = courseNames1.startsWith("[") ? courseNames1.substring(1, courseNames1.length() - 1).split(",")
				: courseNames1.split(",");
		for (String courseName : names1) {
			if (StringUtils.isNotBlank(courseName)) {
				// 用于查询courseId
				Map<String, Object> map1 = new HashMap<String, Object>();
				QueryPage qp = new QueryPage();
				map1.put("eq_courseName", courseName);
				map1.put("eq_pId", proId);
				qp.setParamsByMap(map1);
				qp.setQueryParam("QryCurriculumCourseId");
				qlist = formService.queryBySqlId(qp);
				log.info(qlist);
				if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
					String courseId = qlist.get(0).get("id").toString();
					logData.put("gradeType", 1);
					logData.put("courseId", courseId);
					cModel.setData(logData);
					formMapper.saveCustom(cModel);
				}
			}
		}

		String[] names2 = courseNames2.startsWith("[") ? courseNames2.substring(1, courseNames2.length() - 1).split(",")
				: courseNames2.split(",");
		for (String courseName : names2) {
			if (StringUtils.isNotBlank(courseName)) {
				// 用于查询courseId
				Map<String, Object> map1 = new HashMap<String, Object>();
				QueryPage qp = new QueryPage();
				map1.put("eq_courseName", courseName);
				map1.put("eq_pId", proId);
				qp.setParamsByMap(map1);
				qp.setQueryParam("QryCurriculumCourseId");
				qlist = formService.queryBySqlId(qp);
				log.info(qlist);
				if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
					String courseId = qlist.get(0).get("id").toString();
					logData.put("gradeType", 2);
					logData.put("courseId", courseId);
					cModel.setData(logData);
					formMapper.saveCustom(cModel);
				}
			}
		}

		String[] names3 = courseNames3.startsWith("[") ? courseNames3.substring(1, courseNames3.length() - 1).split(",")
				: courseNames3.split(",");
		for (String courseName : names3) {
			if (StringUtils.isNotBlank(courseName)) {
				// 用于查询courseId
				Map<String, Object> map1 = new HashMap<String, Object>();
				QueryPage qp = new QueryPage();
				map1.put("eq_courseName", courseName);
				map1.put("eq_pId", proId);
				qp.setParamsByMap(map1);
				qp.setQueryParam("QryCurriculumCourseId");
				qlist = formService.queryBySqlId(qp);
				log.info(qlist);
				if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
					String courseId = qlist.get(0).get("id").toString();
					logData.put("gradeType", 3);
					logData.put("courseId", courseId);
					cModel.setData(logData);
					formMapper.saveCustom(cModel);
				}
			}
		}

		String[] names4 = courseNames4.startsWith("[") ? courseNames4.substring(1, courseNames4.length() - 1).split(",")
				: courseNames4.split(",");
		for (String courseName : names4) {
			if (StringUtils.isNotBlank(courseName)) {
				// 用于查询courseId
				Map<String, Object> map1 = new HashMap<String, Object>();
				QueryPage qp = new QueryPage();
				map1.put("eq_courseName", courseName);
				map1.put("eq_pId", proId);
				qp.setParamsByMap(map1);
				qp.setQueryParam("QryCurriculumCourseId");
				qlist = formService.queryBySqlId(qp);
				log.info(qlist);
				if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
					String courseId = qlist.get(0).get("id").toString();
					logData.put("gradeType", 4);
					logData.put("courseId", courseId);
					cModel.setData(logData);
					formMapper.saveCustom(cModel);
				}
			}
		}

		String[] names5 = courseNames5.startsWith("[") ? courseNames5.substring(1, courseNames5.length() - 1).split(",")
				: courseNames5.split(",");
		for (String courseName : names5) {
			if (StringUtils.isNotBlank(courseName)) {
				// 用于查询courseId
				Map<String, Object> map1 = new HashMap<String, Object>();
				QueryPage qp = new QueryPage();
				map1.put("eq_courseName", courseName);
				map1.put("eq_pId", proId);
				qp.setParamsByMap(map1);
				qp.setQueryParam("QryCurriculumCourseId");
				qlist = formService.queryBySqlId(qp);
				log.info(qlist);
				if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
					String courseId = qlist.get(0).get("id").toString();
					logData.put("gradeType", 5);
					logData.put("courseId", courseId);
					cModel.setData(logData);
					formMapper.saveCustom(cModel);
				}
			}
		}

	}

}
