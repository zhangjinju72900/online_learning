package com.tedu.business.user.service;

public interface SaveCourseResoursesScoreService {

	boolean saveCourseResourcesScore(String customerResourcesId,
			String courseId,String sectionId,String labelId,
			String customerUserId,String score, String passScore, String percentScore, String passPercent);

}
