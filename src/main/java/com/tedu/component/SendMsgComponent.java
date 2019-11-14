package com.tedu.component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.common.constant.SendMsgTypeEnum;

/**
 * @ClassName:  SendMsgComponent   
 * @Description:TODO 消息组件
 * @author: qun
 * @date:   2018年11月19日 下午2:19:23   
 *
 */

@Component
public class SendMsgComponent {
	
	@Resource
	private FormMapper formMapper;	
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	public void sendMsg(SendMsgTypeEnum in, Map<String, Object> map){
		
		switch (in) {
		case INFO_LIKE:
			sendInfoLikeMsg(map);
			break;
		case INFO_REVIEW:
			sendInfoReviewMsg(map);		
			break;
		case INFO_REVIEW_LIKE:
			sendInfoReviewLike(map);
			break;
		case INFO_REVIEW_REVIEW:
			sendInfoReviewReview(map);
			break;
		case ACTIVITY_NOTICE:
			sendActivityApply(map);
			break;
		}
		
	}

	private void sendInfoReviewReview(Map<String, Object> map1) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("base_id", map1.get("baseId"));
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/checkManage/insertReviewReviewMessage");
		
		cModel.setData(map);

		int r2 = formMapper.saveCustom(cModel);
		
	}

	private void sendInfoReviewLike(Map<String, Object> map1) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("createBy", map1.get("createBy"));
		map.put("id", map1.get("id"));
		map.put("base_id", map1.get("baseId"));
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/discover/information/insertReviewLikeMessage");
		
		cModel.setData(map);

		int r2 = formMapper.saveCustom(cModel);
		
	}

	private void sendActivityApply(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("base_id", map.get("baseId"));
		param.put("createBy", map.get("createBy"));

		CustomFormModel cModel = new CustomFormModel();

		cModel.setSqlId("khApp/discover/activity/insertActivityMessage");
		
		cModel.setData(param);

		formMapper.saveCustom(cModel);
	}

	private void sendInfoReviewMsg(Map<String, Object> map) {
		
		CustomFormModel cModel2 = new CustomFormModel();

		cModel2.setSqlId("khAdmin/checkManage/insertReviewMessage");

		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("receiver_id", "");
		param.put("sender_id", "");
		param.put("send_type", 1);
		param.put("base_id", map.get("baseId"));
		param.put("remark", "");
		
		cModel2.setData(param);

		formMapper.saveCustom(cModel2);
	}

	private void sendInfoLikeMsg(Map<String, Object> map1) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("createBy", map1.get("createBy"));
		map.put("info_id", map1.get("id"));
		map.put("receiver_id", "");
		map.put("sender_id", "");
		map.put("send_type", 0);
		map.put("base_id", map1.get("baseId"));
		map.put("remark", "");
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/discover/information/insertLikeMessage");
		
		cModel.setData(map);

		int r2 = formMapper.saveCustom(cModel);
	}
	
}
