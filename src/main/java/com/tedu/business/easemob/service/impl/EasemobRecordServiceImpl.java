package com.tedu.business.easemob.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.business.easemob.service.EasemobRecordService;

import net.sf.json.JSONObject;

@Service("easemobRecordServiceImpl")
public class EasemobRecordServiceImpl implements EasemobRecordService{
	
	@Autowired
	private FormMapper formMapper;
	
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public void executeResult(String result, String code, String type, String title, String context, String[] array) {
		
		JSONObject ob = JSONObject.fromObject(result);
		JSONObject data = (JSONObject) ob.get("data");

		for (String receive : array) {
			saveRecord(receive, code, title, context, type, data.get(receive)==null?"":data.get(receive).toString());
		}
		
	}

	private void saveRecord(String receive, String code, String title, String context, String type, String result) {
		TableModel model = new TableModel() {

			@Override
			public String getTableName() {
				return "t_send_easemob_record";
			}

			@Override
			public String[] getFields() {
				String[] str = { "send_id", "receive_id", "target_type", "title", "context", "context_type",
						"send_time", "send_result", "remark", "create_time",
						"create_by", "update_time", "update_by" };
				return str;
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("sendId", SessionUtils.getUserInfo().getValidateCode());
		map.put("receiveId", receive);
		map.put("targetType", code);
		map.put("title", title);
		map.put("context", context);
		map.put("contextType", type);
		map.put("sendTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("sendResult", result);
		map.put("remark", "");
		map.put("createTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("createBy", SessionUtils.getUserInfo().getUserId());
		map.put("updateTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("updateBy", SessionUtils.getUserInfo().getUserId());
		SimpleFormModel simpleModel = new SimpleFormModel(model, map);
		int s = formMapper.insert(simpleModel);
		if (s != 1) {
			log.error("保存环信发送记录表失败！");
		}
	}

}
