package com.tedu.plugin.teacher.homework.homeworkDistribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
/**
 * 保存主观题附件文件
 * @author quancong
 *
 */
@Service("updateQuestionFileDAOPlugin")
public class UpdateQuestionFileDAOPlugin implements ILogicServicePlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 log.info(formModel.getData());
		 return  null;
	}
		
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		  log.info(formModel.getData());
		  //获取主表id
//		  String questionId = formModel.getPrimaryFieldValue();
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String[] ids=map.get("fileIds").toString().split(",");
		  String ossKey ="";
		  String bucketName = "";
		  CustomFormModel cModel = new CustomFormModel();
		  
		  cModel.setData(map);
		  cModel.setSqlId("khTeacher/question/DeleteQuestionFile");
		  formMapper.saveCustom(cModel);
		  
//		  QueryPage queryPage=new QueryPage(); 
//		  queryPage.setQueryParam("khTeacher/question/QryQuestionIdById");
//		  queryPage.setDataByMap(map);
//		  List<Map<String,Object>> list=formMapper.query(queryPage);
//		  if(list != null && list.size() > 0){
			  
			  map.put("questionId", map.get("id"));
		  
			  cModel.setSqlId("khTeacher/question/InsertQuestionFile");
			  for(String fileId:ids){
				  if(org.apache.commons.lang3.StringUtils.isNotBlank(fileId.trim())){
					  map.put("fileId", fileId);
					  //根据fileId查询oss_key和oss_url(bucket_name)
					  QueryPage queryPage=new QueryPage(); 
					  queryPage.setQueryParam("khTeacher/question/QryFileOssKey");
					  queryPage.setDataByMap(map);
					  List<Map<String,Object>> list=formMapper.query(queryPage);
					  if(list.size()>0){
						  ossKey =list.get(0).get("ossKey")+"";
						  bucketName = list.get(0).get("bucketName")+"";
					  }
					  map.put("ossKey", ossKey);
					  map.put("bucketName", bucketName);
					  formMapper.saveCustom(cModel);
				  }	  
			  }
//		  }
	  
	}

}
