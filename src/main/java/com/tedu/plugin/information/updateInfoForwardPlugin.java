package com.tedu.plugin.information;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("updateInfoForward")
public class updateInfoForwardPlugin implements ILogicPlugin {
	@Resource
	IntegralChangeComponent integralChangeComponent;
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();

		  String id = formModel.getPrimaryFieldValue();
		  
		  integralChangeComponent.setIntegralChange(IntegralEnum.INFO_FORWARD, new IntegralChangeModel(id));

				

	}
}

