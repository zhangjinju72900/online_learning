package com.tedu.plugin.app.study.homework;

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
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("questionDetailPlugin")
public class QuestionDetailPlugin implements ILogicPlugin {
	
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;
	
	@Value("${oss.oss_endpoint}")
	private String OSS_ENDPOINT1;
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		//log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) requestObj.getData();
		String homeworkAnswerId = map.get("homeworkAnswerId")==null?"":map.get("homeworkAnswerId").toString();//作业回复ID
		String id = map.get("homeWorkDetailId")==null||StringUtils.isBlank(map.get("homeWorkDetailId").toString())?"0":map.get("homeWorkDetailId").toString();//试题ID
		String type = map.get("type")==null||StringUtils.isBlank(map.get("type").toString())?QuestionSwitchEnum.NEXT.getCode():map.get("type").toString();//next:下一题 previous:上一题 默认下一题
		
		String sqlId = "khApp/study/homework/QryMyHomeWorkDeatilNext";
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("homeworkAnswerId", homeworkAnswerId);
		paramMap.put("id", id);//试题ID
		
		List<Map<String, Object>> tables = queryDataBySqlIdAndParams(sqlId, paramMap);
		if(tables != null && tables.size() > 0){
			for(int i=0;i<tables.size();i++) {
			Map<String, Object> resultMap = tables.get(i);
			
			String questionType = resultMap.get("questionType")==null?"0":resultMap.get("questionType").toString();//试题类型（0-单选，1-多选，2-判断，3-主观）
			paramMap.put("id", resultMap.get("id")==null?"0":resultMap.get("id").toString());//试题ID
			
			if(QuestionTypeEnum.SUBJECTIVE.getCode().equals(questionType)){//主观题查题干附件及回答附件
				
				List<Map<String, Object>> questionFile = queryDataBySqlIdAndParams("khApp/study/homework/QryQuestionFile", paramMap);//题干附件
				if(questionFile != null){
					questionFile.stream().forEach(m ->{
						String url = ossQueryServiceImpl.queryObjectByKey(m.get("ossKey").toString(), ossPriClient, BUCKET_NAME1);						
						m.put("fileUrl", url);
					});
				}
				resultMap.put("questionFile", questionFile);
				if(resultMap.get("homeworkDetailAnswerId") != null){//学生对该题有回答
					String homeworkDetailAnswerId = resultMap.get("homeworkDetailAnswerId")==null?"0":resultMap.get("homeworkDetailAnswerId").toString();//针对该题的回答ID
					paramMap.put("homeworkDetailAnswerId", homeworkDetailAnswerId);
					List<Map<String, Object>> answerFile = queryDataBySqlIdAndParams("khApp/study/homework/QryAnswerFileByAnswerDtlId", paramMap);//回答附件
					
					resultMap.put("answerFile", answerFile);
				}
			}else{
				paramMap.put("id", resultMap.get("id")==null?"0":resultMap.get("id").toString());//试题ID
				List<Map<String, Object>> questionOptions = queryDataBySqlIdAndParams("khApp/study/homework/QryQuestionOptions", paramMap);//题干选项
				
				if(resultMap.get("homeworkDetailAnswerId") != null){//学生对该题有回答
					String homeworkDetailAnswerId = resultMap.get("homeworkDetailAnswerId")==null?"0":resultMap.get("homeworkDetailAnswerId").toString();//针对该题的回答ID
					paramMap.put("homeworkDetailAnswerId", homeworkDetailAnswerId);
					List<Map<String, Object>> answerOptions = queryDataBySqlIdAndParams("khApp/study/homework/QryAnswerOptionByAnswerDtlId", paramMap);//回答选项
					if(questionOptions != null && answerOptions != null){
						Map<String, String> stdSelectMap = answerOptions.stream().collect(Collectors.toMap(l ->l.get("questionAnswerOptionsId").toString(), l -> l.get("questionAnswerOptionsId").toString()));
						questionOptions.stream().forEach(m ->{
							if(stdSelectMap.containsKey(m.get("id").toString())){
								m.put("select", true);
							}else{
								m.put("select", false);
							}
						});
					}
				}
				resultMap.put("questionOptions", questionOptions);
			}
			
			/*int nextFlag = resultMap.get("nextFlag")==null?0:Integer.parseInt(resultMap.get("nextFlag").toString());//是否有下一题 0无 1有
			int previousFlag = resultMap.get("previousFlag")==null?0:Integer.parseInt(resultMap.get("previousFlag").toString());//是否有上一题 0无 1有
			if("0".equals(id)) {
				resultMap.put("questionCount",nextFlag+1);
			}
			if(QuestionSwitchEnum.NEXT.getCode().equals(type)){//判断是否有下一题
				
				if(nextFlag >= 1){
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
				if(previousFlag >= 1){
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
		Map<String,List> resMap = new HashMap();
		resMap.put("questionList", tables);
		responseObj.setData(resMap);
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

