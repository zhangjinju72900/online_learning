package com.tedu.plugin.app.study.wrongSet;

import com.fasterxml.jackson.databind.util.ObjectIdMap;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SumQuesNum")
public class SumQuesNum implements ILogicPlugin {
	@Autowired(required=false)
	FormLogService formLogService;
	@Autowired(required=false)
	private FormMapper formMapper;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		int sum = 0;
		
		DataGrid dg = (DataGrid)(responseObj.getData());
		List<Map<String,Object>> list = (List<Map<String,Object>>)(dg.getRows());
		Map<String,Object> data= new HashMap<String,Object>();
		for(Map<String, Object> map:list){	
			String number = map.get("number")+"";
			int qty = Integer.parseInt(number);
			sum = sum+qty;
		}

		String total = sum+"";
		data.put("courseName", "全部课程");
		data.put("number", total);
		list.add(data);
		dg.setRows(list);
		dg.setTotal(list.size());
		responseObj.setData(dg);
		
	}
}
