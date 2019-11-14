package com.tedu.business.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.business.user.service.JoinActivityService;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;

@Service("joinActivityServiceImpl")
public class JoinActivityServiceImpl implements JoinActivityService{
	
	@Resource
	private FormMapper formMapper;	
	
	
	@Resource
	private IntegralChangeComponent integralChangeComponent;
	
	public final Logger log = Logger.getLogger(this.getClass());

	/**
	 * 更改t_activity表中的参加人数字段
	 */
	@Override
	public void updateJoinCount(String activityId) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/mine/activity/updateActivityCount");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activityId", activityId);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
	}
	
	/**
	 * 将参与人id和活动id存入t_activity_join表中
	 */
	@Override
	public String insertActivityJoin(String joinBy, String activityId) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/mine/activity/insertActivityJoin");
		Map<String, Object> map = new HashMap<String, Object>();
		UserModel model = SessionUtils.getUserInfo();
		long userId=model.getUserId();
		long empId=model.getEmpId();
		map.put("createBy", empId);
		map.put("joinBy", joinBy);
		map.put("activityId", activityId);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
		
	}

	/**
	 * 检查t_activity_join表中是否已存在该用户的信息
	 * 返回值为true时表示用户已参加活动，false表示未参加
	 */
	@Override
	public boolean checkRepeatActivityJoin(String joinBy, String activityId) {
		Map<String, Object> map = new HashMap<String, Object>();
		QueryPage qp = new QueryPage();
		map.put("joinBy", joinBy);
		map.put("activityId", activityId);
		qp.setDataByMap(map);
		qp.setQueryParam("khApp/mine/activity/QueryRepeatActivityJoin");
		List<Map<String, Object>> qlists = formMapper.query(qp);
		if (qlists.size() != 0) {
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * 更新参与用户的积分t_customer_user
	 */
	@Override
	public void changeIntegral(String activityJoinId) {
		IntegralChangeModel integralChangeModel=new IntegralChangeModel(null);
		integralChangeModel.setId(activityJoinId);
		integralChangeComponent.setIntegralChange(IntegralEnum.JOIN_ACTIVITY, integralChangeModel);
	}

}
