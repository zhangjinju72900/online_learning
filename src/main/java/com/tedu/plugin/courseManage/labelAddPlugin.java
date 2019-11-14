package com.tedu.plugin.courseManage;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
/**
 * 保存标签
 * @ClassName:  labelAddPlugin   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月29日 下午7:08:23   
 *
 */
@Service("labelAddPlugin")
public class labelAddPlugin implements ILogicPlugin{
	private String sqlTemplate1 = "zhongdeprofession/QrySaveSectionLabel";
	private String sqlTemplate2 = "zhongdeprofession/QrySaveCourseLabel";
	@Resource
	private FormMapper formMapper;
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
		String secId=String.valueOf(map.get("courseorsectionId"));
		CustomFormModel cModel = new CustomFormModel();
		String id=secId.split("-")[1];
		if(!(secId.length()<=0)&&secId.contains("sec-")){
			formModel.getData().put("courseorsectionId", id);
			cModel.setSqlId(sqlTemplate1);
		}else if(!(secId.length()<=0)){
			formModel.getData().put("courseorsectionId", id);
			cModel.setSqlId(sqlTemplate2);
		}

		cModel.setData(formModel.getData());
		formMapper.saveCustom(cModel);
		
	}

}
