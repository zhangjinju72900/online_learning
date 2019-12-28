package com.tedu.plugin.activity;

import com.tedu.base.common.utils.SessionUtils;
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
import java.util.Map;

/**
 * 将用户报名的数据添加到数据库中
 * 
 * @author jiaofengqin
 *
 */
@Service("ReviewActivity")
public class ReviewActivity implements ILogicReviser {

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
		String id=map.get("id").toString();
		String content = map.get("content").toString();
		resultMap.put("reviewEdit", content);
		resultMap.put("id", id);
		resultMap.put("empId", SessionUtils.getUserInfo().getEmpId());
		CustomFormModel cfmm = new CustomFormModel();
		cfmm.setData(resultMap);
		cfmm.setSqlId("khApp/discover/activity/ActivityReview");
		formMapper.saveCustom(cfmm);

		//最后将活动表中的评论数+1
		CustomFormModel cfmm1 = new CustomFormModel();
		cfmm1.setData(resultMap);
		cfmm1.setSqlId("khApp/discover/activity/ActReview");
		formMapper.saveCustom(cfmm1);
	}
}


