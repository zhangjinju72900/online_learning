package com.tedu.business.user.service;

public interface JoinActivityService {

	void updateJoinCount(String activityId);
	boolean checkRepeatActivityJoin(String joinBy, String activityId);
	String insertActivityJoin(String joinBy, String activityId);
	void changeIntegral(String activityJoinId);

}
