package com.tedu.business.attendance.manage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AbsenteeismCurveDao {

	List<Map<String, Object>> getCurveData(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("classId")String classId, @Param("userId")String userId);

}
