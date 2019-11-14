package com.tedu.plugin.courseManage;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
/**
 * 对小节的id做处理，编辑小节
 * @author zhangzhiming
 *
 */
@Service("doSectionIdForEditPlugin")
public class DoSectionIdForEditPlugin implements ILogicPlugin{
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) requestObj.getData();
		String secId=String.valueOf(map.get("eq_id"));
		if(!(secId.length()<=0)&&secId.contains("sec-")){
			String id=secId.substring(secId.indexOf("-")+1);
			requestObj.getQuery().getData().put("eq_id", id);
			requestObj.getQuery().getParams().get(0).setFieldValue(id);
		}
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
	}

}
