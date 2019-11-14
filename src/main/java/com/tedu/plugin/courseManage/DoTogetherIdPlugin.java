package com.tedu.plugin.courseManage;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
@Service("DoTogetherIdPlugin")
public class DoTogetherIdPlugin implements ILogicPlugin{
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String courseId=String.valueOf(map.get("courseId"));
		String treeId=String.valueOf(map.get("treeId"));
		String pId=String.valueOf(map.get("pid"));
		String ids="";
		if(pId==null||pId==""){
			ids=courseId+","+treeId+","+"0";
		}else{
			ids=courseId+","+treeId+","+pId;
		}
		formModel.getData().put("id", ids);
		responseObj.setData(formModel.getData());
	}

}
