package com.tedu.plugin.courseManage;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
/**
 * 对小节的id做处理，添加资源
 * @author zhangzhiming
 *
 */
@Service("doSectionIdPlugin")
public class doSectionIdPlugin implements ILogicPlugin{
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData());
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) requestObj.getData();
		String secId=String.valueOf(map.get("courseorsectionId"));
		String id="";
		if(!(secId.length()<=0)&&secId.contains("sec-")){
			id=secId.substring(secId.indexOf("-")+1);
		}else if(!(secId.length()<=0)&&secId.contains("cou-")){
			id=secId.substring(secId.indexOf("-")+1);
			formModel.getData().put("ctlType", "course");
		}
		formModel.getData().put("id", id);
		responseObj.setData(formModel.getData());
	}

}
