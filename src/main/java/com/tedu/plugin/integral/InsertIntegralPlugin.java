package com.tedu.plugin.integral;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
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
@Service("insertIntegralPlugin")
public class InsertIntegralPlugin implements ILogicPlugin {
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
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  if(map == null || map.get("id") == null || map.get("Integral") == null || isNotNumber(map.get("Integral").toString()) || map.get("remark") == null){
			  responseObj.setCode("999999");
			  responseObj.setMsg("更改失败");
			  return;
		  }
		  String userId = map.get("id").toString();
		  String integral = map.get("Integral").toString();
		  String remark = map.get("remark").toString();
		  IntegralChangeModel model = new IntegralChangeModel(userId, null, integral, remark);
		  integralChangeComponent.setIntegralChange(IntegralEnum.MAINTAIN, model);
		  
		  //获取主表的id
		  /*String integralId=formModel.getPrimaryFieldValue().toString();
		  //获取integral并解析
		  Integer integral = map.get("integral")==null?0:Integer.parseInt(map.get("integral").toString());
		  Integer integral2 = map.get("Entegral")==null?0:Integer.parseInt(map.get("Entegral").toString());
		  updateIntegral(integral,integral2, integralId);*/

		  
		
	}
	
	
	private static boolean isNotNumber(String str) {
		
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return !pattern.matcher(str).matches(); 
		
	}
	
	
	private void updateIntegral(Integer integral, Integer integral2,String userId) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/integralManage/updateIntegral");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("integral", integral);
		map.put("userId", userId);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
	}

}

