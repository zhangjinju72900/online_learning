package com.tedu.plugin.signIn;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("addIntegralRecord")
public class AddIntegralRecord implements ILogicPlugin {
	@Resource
	FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		String userId = requestObj.getData().get("userId").toString();
		
		int integral = 0;
		boolean flag = true;
		int[] num = new int[] { 0, 5, 5, 10, 10, 15, 15, 20 };
		QueryPage queryPage = new QueryPage();
		formModel.getData().put("createBy", userId);
		formModel.getData().put("userId", userId);
		formModel.getData().put("updateBy", userId);
		for (int i = 0; i <= 6; i++) {
			queryPage.setQueryParam("khApp/mine/QrySignInRecord");
			log.info("sdfdsfsdfsdfsdf");
			queryPage.getData().put("userId", formModel.getData().get("userId"));
			queryPage.getData().put("day", i);
			List<Map<String, Object>> list = formMapper.query(queryPage);
			if ((list.isEmpty() && i != 0) || (i == 0 && !list.isEmpty())) {
				integral = num[i];
				log.info(integral);
				flag = false;
				break;
			}
		}
		if (flag) {
			integral = num[7];
		}
		if (integral == 0) {
			throw new ValidException(ErrorCode.RESUBMIT_ERROR, "重复请求", "今天签到过了");
		}
		requestObj.getData().put("createBy", userId);
		requestObj.getData().put("integral", integral);
		return formModel;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> data = formModel.getData();
		
		String integral = requestObj.getData().get("integral")==null?"0":requestObj.getData().get("integral").toString();
		
		String id = formModel.getPrimaryFieldValue();
		
		//查询昨天有没有签到
		QueryPage queryPage = new QueryPage();
		queryPage.setQueryParam("khApp/mine/QryYesterdaySignData");
		queryPage.getData().put("userId", data.get("userId"));
		List<Map<String, Object>> list = formMapper.query(queryPage);
		
		log.info(data);
		cModel.setData(data);
		cModel.setSqlId("khApp/mine/SaveIntegralRecord");
		data.put("integral", integral);
		data.put("baseId", id);
		formMapper.saveCustom(cModel);
		cModel.setData(data);
		if(list != null && list.size() > 0){
			cModel.setSqlId("khApp/mine/updateCustomerIntegralAndSignDays");
			
		}else {
			cModel.setSqlId("khApp/mine/updateCustomerIntegral2");
			data.put("continuousSignDays", 1);
		}	
		
		formMapper.saveCustom(cModel);
		data.put("integral", integral);
		
		queryPage.setQueryParam("khApp/mine/QryContinuousSignById");
		queryPage.getData().put("userId", data.get("userId"));
		List<Map<String, Object>> list1 = formMapper.query(queryPage);
		data.put("continuousSignDays", list1.get(0).get("continuousSign"));
		
		responseObj.setData(data);
	}

}
