package com.tedu.business.attendance.manage.service;

import java.util.Map;

public interface AbsenteeismCurveService {

	void getCurveData(String startDate, String endDate, String classId, String userId, Map<String, Object> result);

}
