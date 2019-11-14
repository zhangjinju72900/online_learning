package com.tedu.plugin.teacher.homework.homeworkCorrect;


import java.util.ArrayList;
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
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.oss.service.OssQueryService;
/**
 * 返回试题的选项内容
 * @author quancong
 *
 */
@Service("getObjAnswerOptionsPlugin")
public class GetObjAnswerOptionsPlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	
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
		log.info(responseObj.getData());
  		Map<String,Object> questionMap = (Map<String, Object>) responseObj.getData();
  		List<Map<String, Object>> optionContent = queryDataBySqlIdAndParams("khApp/study/homework/QryQuestionOptions", questionMap);//题目选项
  		questionMap.put("optionContent", optionContent);
  		responseObj.setData(questionMap);
  		
	}
	
	private List<Map<String, Object>> queryDataBySqlIdAndParams(String sqlId, Map<String, Object> paramMap) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(sqlId);
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId(sqlId);
		sqlQuery.setDataByMap(paramMap);
		return formMapper.query(sqlQuery);
	}

}
