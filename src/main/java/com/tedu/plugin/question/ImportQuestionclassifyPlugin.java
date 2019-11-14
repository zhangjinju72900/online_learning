package com.tedu.plugin.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tedu.base.engine.aspect.ILogicServicePlugin;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;


@Service("importQuestionclassifyPlugin")
public class ImportQuestionclassifyPlugin implements ILogicServicePlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		
		String questionId = formModel.getPrimaryFieldValue().toString();
		String title = map.get("atitle") == null ? "" : map.get("atitle").toString();
		String acontent = map.get("acontent") == null ? "" : map.get("acontent").toString();
		String correctFlag = map.get("acorrectFlag") == null ? "" : map.get("acorrectFlag").toString();
		
		System.out.println(correctFlag);
		
		CustomFormModel cModel = new CustomFormModel();
		String[] titles = title.split(",");
		String[] contents = acontent.split(",");
		System.out.println(titles.length);
		for(int i = 0;i < titles.length;i++) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("qid", questionId);
			map1.put("title",titles[i]);
			map1.put("content", contents[i]);
			if(correctFlag.equals(titles[i])) {
				map1.put("correctFlag", 0);
			}
			else {
				map1.put("correctFlag", 1);
			}
			cModel.setSqlId("insertQuestion");
			cModel.setData(map1);
			formMapper.saveCustom(cModel);
		}
		
		
	
		
		
	
	}

}
