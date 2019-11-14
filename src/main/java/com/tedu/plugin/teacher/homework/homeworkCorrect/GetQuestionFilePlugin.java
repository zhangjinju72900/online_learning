package com.tedu.plugin.teacher.homework.homeworkCorrect;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.oss.service.OssQueryService;
/**
 * 作业回答详情查询返回附件url
 * @author quancong
 *
 */
@Service("getQuestionFilePlugin")
public class GetQuestionFilePlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;
	
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 log.info(formModel.getData());
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		DataGrid dataList= (DataGrid) responseObj.getData();
  		List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
  		rows = (List<Map<String, Object>>) dataList.getRows();
  		
  		if(rows != null){
  			
  			for(Map<String, Object> row : rows){
  	  			
  				String questionOssKeys = row.get("questionOssKeys") == null ? "" : row.get("questionOssKeys").toString();
  	  			
  				String ansOssUrl = row.get("ansOssUrls") == null ? "" : row.get("ansOssUrls").toString();
  				
  				String ansOssKey = row.get("ansOssKeys") == null ? "" : row.get("ansOssKeys").toString();
  	  			
  	  			if (StringUtils.isNotBlank(questionOssKeys)) {
  	  				// 附件的ossKey
  	  				String[] keys =questionOssKeys.split(",");
  	  				
  	  				String[] questionFileArray = new String[keys.length];
  	  				
  	  				for (int i = 0; i < keys.length; i++) {
  	  					String url = ossQueryServiceImpl.queryObjectByKey(keys[i], ossPriClient, BUCKET_NAME1);
  	  					
  	  					questionFileArray[i] = url; 
  	  				}
  	  				row.put("questionFileArray", questionFileArray);
  	  				row.put("questionOssKeys", keys);
  	  			}else{
  	  				row.put("questionFileArray", new String[]{});
  	  				row.put("questionOssKeys", new String[]{});
  	  			}
  	  			
  	  			
  	  			if (StringUtils.isNotBlank(ansOssUrl)) {
	  				String[] ansOssUrls = ansOssUrl.split(",");
	  				
	  				String[] ansOssKeys = ansOssKey.split(",");
	  				
	  				row.put("ansOssUrls", ansOssUrls);
	  				row.put("ansOssKeys", ansOssKeys);
	  			}else{
	  				row.put("ansOssUrls", new String[]{});
	  				row.put("ansOssKeys", new String[]{});
	  			}
  	  		}
  	  		responseObj.setData(dataList);
  		}
	}

}
