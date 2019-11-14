package com.tedu.plugin.check;


import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("TopCheckManagePlugin")
public class TopCheckManagePlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	
	@Resource
	private IntegralChangeComponent integralChangeComponent;
	
	private String sqlTemplate = "khAdmin/checkManage/TopCheckManage";
	private String sqlStatus = "khAdmin/checkManage/selectCheckStatusByIdl";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String[] idTypes=(map.get("id") == null ? "":map.get("id").toString()).split(",");
		
			  for (String string : idTypes) {
				  String[] ids=string.split("-");
				  String type = ids[0];
				  String id = ids[1];
				  log.info(ids.toString());
				  
				  int a = Integer.parseInt(id);
				  QueryPage  sqlQuery = new QueryPage();
				   sqlQuery.setQueryParam(sqlStatus);
				   sqlQuery.setQueryType("");
				   sqlQuery.setSingle(1);
				   sqlQuery.setSqlId(sqlStatus);
				   HashMap paramMap = new HashMap<>();
				  paramMap.put("type", type);
				   paramMap.put("id", a);
				   sqlQuery.setDataByMap(paramMap);
				   List<Map<String, Object>> t = formMapper.query(sqlQuery);
				   String ss="";
				  for(int i=0;i<t.size();i++){
					  Map<String, Object> s = t.get(i);
					  
					ss = s.toString();	
			  }
				if(ss.equals("{check_status=1}")) {
					CustomFormModel cModel = new CustomFormModel();
					cModel.setSqlId(sqlTemplate);
					cModel.setData(formModel.getData());
					Map<String, Object> logData = formModel.getData();
					
					logData.put("id", id);
					logData.put("type", type);
					
					LogUtil.info(cModel.getData().toString());
					formMapper.saveCustom(cModel);
					
					if("info".equals(type)){//资讯置顶
						integralChangeComponent.setIntegralChange(IntegralEnum.TOP_INFO, new IntegralChangeModel(id));	
					}
					if("info_review".equals(type)){//资讯评论被置顶
						integralChangeComponent.setIntegralChange(IntegralEnum.TOP_INFO_REVIEW, new IntegralChangeModel(id));	
					}
				}else{
					responseObj.setMsg("请确认所选资讯都审核通过");
				}
				  
					
			}
		 
		  
		
	}
}

