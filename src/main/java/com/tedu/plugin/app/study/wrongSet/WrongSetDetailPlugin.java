package com.tedu.plugin.app.study.wrongSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.google.zxing.WriterException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.constant.QuestionSwitchEnum;
import com.tedu.common.constant.QuestionTypeEnum;
import com.tedu.common.error.ExErrorCode;
import com.tedu.common.util.FileUtil;
import com.tedu.common.util.QrCodeCreateUtil;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;

/**
 * 错题集错题详情，上一题下一题
 * @author quancong
 *
 */
@Service("wrongSetDetailPlugin")
public class WrongSetDetailPlugin implements ILogicPlugin {
	
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String userId = map.get("userId")==null||StringUtils.isBlank(map.get("userId").toString())?"":map.get("userId").toString();//用户ID
		String courseId = map.get("courseId")==null?"":map.get("courseId").toString();//课程id
		String questionId = map.get("questionId")==null||StringUtils.isBlank(map.get("questionId").toString())?"0":map.get("questionId").toString();//试题ID
		String type = map.get("type")==null||StringUtils.isBlank(map.get("type").toString())?QuestionSwitchEnum.NEXT.getCode():map.get("type").toString();//next:下一题 previous:上一题 默认下一题
		String sqlId ="khApp/study/misques/QryWrongSetDeatilNext";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("courseId", courseId);//课程ID
		paramMap.put("questionId", questionId);//试题ID
		paramMap.put("userId", userId);//用户ID
		List<Map<String, Object>> tables = queryDataBySqlIdAndParams(sqlId, paramMap);
		if(tables != null && tables.size() > 0){
			for(int i=0;i<tables.size();i++) {
				
				Map<String, Object> resultMap = tables.get(i);
				String questionType = resultMap.get("questionType")==null?"0":resultMap.get("questionType").toString();//试题类型（0-单选，1-多选，2-判断，3-主观）
				paramMap.put("id", resultMap.get("questionId")==null?"0":resultMap.get("questionId").toString());//试题ID
				List<Map<String, Object>> questionOptions = queryDataBySqlIdAndParams("khApp/study/homework/QryQuestionOptions", paramMap);//题目选项
				resultMap.put("questionOptions", questionOptions);
				/*//判断是否有下一题和上一题
			int nextFlag = resultMap.get("nextFlag")==null?0:Integer.parseInt(resultMap.get("nextFlag").toString());//是否有下一题 0无 1有
			int previousFlag = resultMap.get("previousFlag")==null?0:Integer.parseInt(resultMap.get("previousFlag").toString());//是否有上一题 0无 1有
			if(QuestionSwitchEnum.NEXT.getCode().equals(type)){//判断是否有下一题
				if(nextFlag >= 2){
					resultMap.put("nextFlag", 1);
				}else{
					resultMap.put("nextFlag", 0);
				}
				if(previousFlag >= 1){
					resultMap.put("previousFlag", 1);
				}else{
					resultMap.put("previousFlag", 0);
				}
			}else{
				if(nextFlag >= 1){
					resultMap.put("nextFlag", 1);
				}else{
					resultMap.put("nextFlag", 0);
				}
				if(previousFlag >= 2){
					resultMap.put("previousFlag", 1);
				}else{
					resultMap.put("previousFlag", 0);
				}
			}*/
			}
			
		}else{
			if(QuestionSwitchEnum.NEXT.getCode().equals(type)){
				responseObj.setCode(ExErrorCode.NOT_FOUND_NEXT_QUESTION.getCode());
				responseObj.setMsg(ExErrorCode.NOT_FOUND_NEXT_QUESTION.getMsg());
			}else{
				responseObj.setCode(ExErrorCode.NOT_FOUND_PREVIOUS_QUESTION.getCode());
				responseObj.setMsg(ExErrorCode.NOT_FOUND_PREVIOUS_QUESTION.getMsg());
			}
		}
		Map<String, Object> wrongMap = new HashMap<>();
		wrongMap.put("wrongList", tables);
		responseObj.setData(wrongMap);
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

