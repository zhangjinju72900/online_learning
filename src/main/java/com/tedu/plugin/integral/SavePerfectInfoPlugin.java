package com.tedu.plugin.integral;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;
/**
 * 在取编辑输入的积分并调整
 * @author cuiyue
 *
 */
@Service("savePerfectInfoPlugin")
public class SavePerfectInfoPlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	@Resource
	IntegralChangeComponent integralChangeComponent;
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		
		return null;
	}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		try {
			 Map<String,Object> map=(Map<String, Object>) requestObj.getData();
			if(map.get("fileId")!=null&&StringUtils.isNotBlank(map.get("fileId").toString())&& !"0".equals(map.get("fileId").toString())
					&& map.get("sex")!=null&&StringUtils.isNotBlank(map.get("sex").toString())
					&& map.get("nickname")!=null&&StringUtils.isNotBlank(map.get("nickname").toString())
					&& map.get("provinceName")!=null&&StringUtils.isNotBlank(map.get("provinceName").toString())){
				IntegralChangeModel model = new IntegralChangeModel(null);
				integralChangeComponent.setIntegralChange(IntegralEnum.PERFECT_INFO, model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("完善资料后赠送积分失败", e);
		}
		
	}
	
}

