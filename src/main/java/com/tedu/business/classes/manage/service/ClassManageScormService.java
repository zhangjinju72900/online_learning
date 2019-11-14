package com.tedu.business.classes.manage.service;

import java.util.List;
import java.util.Map;

public interface ClassManageScormService {

	List<Map<String, Object>> selectCourseListByClassId(String classId);

	List<Map<String, Object>> selectResourceColumnByCourseId(String classId);

	List<Map<String, Object>> selectScormResourceData(String courseId, String classId);

}
