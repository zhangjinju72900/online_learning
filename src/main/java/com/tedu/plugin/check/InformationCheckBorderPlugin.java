package com.tedu.plugin.check;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("informationCheckBorderPlugin")
public class InformationCheckBorderPlugin implements ILogicPlugin {

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		Map<String, Object> map =  (Map<String, Object>) responseObj.getData();
		if(map != null && map.get("content") != null && StringUtils.isNotBlank(map.get("content").toString())){
			String content = map.get("content").toString();
			if(content.contains("<img")){
				content = content.replace("<img", "<img style=\"width:100%\"");
			}
			if(content.contains("<video")){
				content = content.replace("<video", "<video style=\"width:100%\"");
			}
			map.put("content", "<div style=\"border: 1px solid #7d7c7c;\">"+content+"</div>");
			
		}

	}
}
