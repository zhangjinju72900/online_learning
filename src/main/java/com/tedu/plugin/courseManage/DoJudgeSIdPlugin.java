package com.tedu.plugin.courseManage;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

/**
 * 编辑小节、选择资源
 * @ClassName:  DoJudgeSIdPlugin   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月29日 下午6:56:45   
 *
 */

@Service("DoJudgeSIdPlugin")
public class DoJudgeSIdPlugin implements ILogicPlugin{
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
		String sid=map.get("sid")==null?"":map.get("sid").toString();//编辑小节的传参
		String treeId=map.get("treeId")==null?"":map.get("treeId").toString();//选择资源的传参
		String flag="";
		if(StringUtils.isNotBlank(sid) && !sid.contains("sec-")){//验证编辑小节
			flag="zhang";//当前选择的不是小节，不能编辑小节
		}else if(StringUtils.isNotBlank(treeId) && (!treeId.contains("cou-") && !treeId.contains("sec-"))){//验证加资源

			flag="zhang";//当前选择的是标签，可以加资源
		}
		formModel.getData().put("id", flag);
		responseObj.setData(formModel.getData());
	}

}
