package com.tedu.plugin.courseManage;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

/**
 * 添加标签
 * @ClassName:  DoJudgeIdPlugin   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月29日 下午6:57:10   
 *
 */

@Service("DoJudgeIdPlugin")
public class DoJudgeIdPlugin implements ILogicPlugin{
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
//		String courseId=String.valueOf(map.get("courseId"));
		String treeId=String.valueOf(map.get("slid"));
		if(treeId.contains("sec-")){
			formModel.getData().put("id", treeId);//当前选择的是小节，可以加标签
		}else if(treeId.contains("cou-")){
			formModel.getData().put("id", treeId);//当前选择的是课程，可以加标签
		}else{
			formModel.getData().put("id", "");//当前选择时标签，不能再加标签
		}
		responseObj.setData(formModel.getData());
	}

}
