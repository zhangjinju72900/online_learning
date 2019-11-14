package com.tedu.plugin.goods;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("QryGoodsListPlugin")
public class QryGoodsListPlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map map = new HashMap();
		QueryPage qp = new QueryPage();
		qp.setQueryParam("khApp/mine/mall/QryGoods");// 查询项目名字
		List<Map<String, Object>> goodList = formService.queryBySqlId(qp);
		if(goodList.size()>0) {
			map.put("goodList", goodList);
			responseObj.setData(map);
		}
	}

	

}
