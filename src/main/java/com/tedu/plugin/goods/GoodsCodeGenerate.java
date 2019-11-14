package com.tedu.plugin.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;

@Service("goodsCodeGenerate")
public class GoodsCodeGenerate implements ILogicPlugin{
	
	@Resource
	FormMapper formMapper;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		/*获取商品数量*/
		int quantity=Integer.parseInt(map.get("quantity")+"");
		if(quantity<0){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","商品数量不能为负数");
		}
		
		Map<String, Object> data = formModel.getData();
		  //Map<String,Object> map = new HashMap<>();
		  String name=(String) data.get("name");   
		  QueryPage sqlQuery1 = new QueryPage();
		  sqlQuery1.setQueryParam("khAdmin/goodsManage/selectproductName");
		  sqlQuery1.setQueryType("");
		  sqlQuery1.setSingle(1);
		  List<Map<String, Object>> tables = formMapper.query(sqlQuery1);
		  
		  boolean index=true;
		   for (Map<String, Object> map2 : tables) {
			   if(map2.get("name").equals(name)){
				   index=false;
				   break;
			   }	   
		   }
		   if(!index){
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","商品名称重复");
		   }
		return null;
		
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		  
	
		
		String id = formModel.getPrimaryFieldValue();
		
		CustomFormModel cModel = new CustomFormModel();
	    cModel.setSqlId("khAdmin/goodsManage/insertSerialNumber");
	    Map<String, Object> logData = new HashMap<>();
	    logData.put("serType", 0);
	    cModel.setData(logData);
	    LogUtil.info(cModel.getData().toString());
	    formMapper.saveCustom(cModel);
	    
	    QueryPage sqlQuery = new QueryPage();
	    sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/goodsManage/QrySerialNumber");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/goodsManage/QrySerialNumber");
		logData = new HashMap<>();
		logData.put("serType", 0);
		sqlQuery.setDataByMap(logData);
		List<Map<String, Object>> colls = formMapper.query(sqlQuery);
		
		if(colls != null && colls.size() > 0){
			cModel = new CustomFormModel();
		    cModel.setSqlId("khAdmin/goodsManage/updateCodeById");
		    logData = new HashMap<>();
		    logData.put("id", id);
		    logData.put("code", colls.get(0).get("code"));
		    cModel.setData(logData);
		    LogUtil.info(cModel.getData().toString());
		    formMapper.saveCustom(cModel);
		}
	
	}
}
