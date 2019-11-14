package com.tedu.plugin.teacher.homework.homeworkTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.oss.service.OssQueryService;
/**
 * 判斷題目數量
 * @author quancong
 *
 */
@Service("dTemplatePlugin")
public class DTemplatePlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	@Resource
	private OssQueryService ossQueryServiceImpl;
	private String sql = "khTeacher/homework/homeworkTemplate/DelHomeworkTemplate1";
	private  String sql1 = "khTeacher/homework/homeworkTemplate/QryTemplateDetail1";
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		 Map<String, Object> map = (Map<String, Object>) formModel.getData();
		 String count = map.get("total").toString();
		 String id = map.get("id").toString();
		 int a = Integer.parseInt(id);
		 String  homeworkTemplateId ="";
		  QueryPage  sqlQuery = new QueryPage();
		   sqlQuery.setQueryParam(sql1);
		   sqlQuery.setQueryType("");
		   sqlQuery.setSingle(1);
		   sqlQuery.setSqlId(sql1);
		   HashMap paramMap = new HashMap<>();
		   paramMap.put("id", a);
		   sqlQuery.setDataByMap(paramMap);
		   List<Map<String, Object>> t = formMapper.query(sqlQuery);
		   if(t.size()>0){
			   homeworkTemplateId =t.get(0).get("homeworkTemplateId")+"";
			  }
		   map.put(" homeworkTemplateId", homeworkTemplateId);
		if(count.equals("1")){
			CustomFormModel cModel = new CustomFormModel();
			cModel.setData(map);
			cModel.setSqlId(sql);
			map.put("homeworkTemplateId", homeworkTemplateId);
			formMapper.saveCustom(cModel);
		}
		
  		
	}

}
