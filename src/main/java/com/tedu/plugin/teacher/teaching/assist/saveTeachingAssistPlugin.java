package com.tedu.plugin.teacher.teaching.assist;

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
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.file.util.io.file;
import com.tedu.base.task.SpringUtils;
/**
 * 获取教辅资料文件的oss_key和bucket_name
 * @author quancong
 *
 */
@Service("saveTeachingAssistPlugin")
public class saveTeachingAssistPlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		 return  null;
	}
		
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,FormEngineResponse responseObj) {
		 Map<String,Object> map=(Map<String, Object>) requestObj.getData();
		 String updateSql = "khTeacher/teachingAssist/UpdateTeachingAssist";
		 String insertSql = "khTeacher/teachingAssist/InsertTeachingAssist";
		 CustomFormModel cModel = new CustomFormModel();
		 String fileId = map.get("fileId")!=null?map.get("fileId").toString():"";
		 String mode = map.get("Mode")!=null?map.get("Mode").toString():"";
		 map.put("fileId", fileId);
		 String ossKey ="";
		 String bucketName = "";
		//根据fileId查询oss_key和oss_url(bucket_name)
		  QueryPage queryPage=new QueryPage(); 
		  queryPage.setQueryParam("khTeacher/question/QryFileOssKey");
		  queryPage.setDataByMap(map);
		  List<Map<String,Object>> list=formMapper.query(queryPage);
		  if(list.size()>0){
			  ossKey =list.get(0).get("ossKey")+"";
			  bucketName = list.get(0).get("bucketName")+"";
			  String fileType = list.get(0).get("fileType")+"";
			  if("mp4".equalsIgnoreCase(fileType)||"flv".equalsIgnoreCase(fileType)){
				  map.put("fileId", list.get(0).get("id"));
				  map.put("ossKey", fileId);
			  }else{
				  map.put("ossKey", ossKey);
			  }
			  map.put("bucketName", bucketName);
		  }
		 if("Edit".equals(mode)) {
			 cModel.setSqlId(updateSql);
		 }else {
			 cModel.setSqlId(insertSql);
		 }
		 cModel.setData(map);
		 
		 formMapper.saveCustom(cModel);
		
	}

}
