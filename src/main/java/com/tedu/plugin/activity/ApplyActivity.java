package com.tedu.plugin.activity;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将用户报名的数据添加到数据库中
 * 
 * @author jiaofengqin
 *
 */
@Service("ApplyActivity")
public class ApplyActivity implements ILogicReviser {

	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Override
	public FormModel beforeLogic(FormEngineRequest formEngineRequest) {
		return null;
	}
	@Override
	public void afterLogic(FormEngineRequest requestObj, FormEngineResponse responseObj) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> map = requestObj.getData();
		//主页面获取到的数据
		String activity = map.get("eq_activity").toString();
		String applyBy = map.get("applyBy").toString();
		String schoolName = map.get("schoolName").toString();
		String grade = map.get("grade").toString();
		String className = map.get("className").toString();
		String tel = map.get("tel").toString();
		String mail = map.get("mail").toString();
		resultMap.put("activity", activity);
		resultMap.put("applyBy", applyBy);
		resultMap.put("schoolName", schoolName);
		resultMap.put("grade", grade);
		resultMap.put("className", className);
		resultMap.put("tel", tel);
		resultMap.put("mail", mail);

		QueryPage queryPage = new QueryPage();
		queryPage.setDataByMap(resultMap);
		queryPage.setQueryParam("khApp/discover/activity/QryActivityUser");
		List<Map<String, Object>> userLists = formMapper.query(queryPage);
		//首先判断该用户名是否存在，如果存在的话判断该用户名的角色是否满足报名条件
		if (userLists.size() > 0) {
			//说明该用户存在
			//查询该活动对应的角色有哪些
			QueryPage rolePage = new QueryPage();
			rolePage.setDataByMap(resultMap);
			rolePage.setQueryParam("khApp/discover/activity/QryActivityRole");
			List<Map<String, Object>> roleLists = formMapper.query(rolePage);
			//判断用户对应的角色是否满足报名条件
			String roleId = userLists.get(0).get("roleId").toString();
			String integral = userLists.get(0).get("integral").toString();
			boolean flag = false;
			for (Map<String, Object> roleList : roleLists) {
				String role = roleList.get("roleId").toString();
				if (roleId.equals(role)) {
					flag = true;
					//如果都满足的话，更改活动表中的报名人数加1
					CustomFormModel cfmm = new CustomFormModel();
					cfmm.setData(resultMap);
					cfmm.setSqlId("khApp/discover/activity/updateActivityCount");
					formMapper.saveCustom(cfmm);
					responseObj.setMsg("您已成功完成报名");

				}
			}
			if (!flag) {
				responseObj.setMsg("您的角色不满足条件，无法完成报名");
			}
		} else {
			responseObj.setMsg("您的用户名不存在，无法完成报名");
		}
	}
}


