package com.tedu.plugin.app.study.homework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
import com.tedu.common.constant.HomeWorkAnswerStatusEnum;
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
@Service("saveHomeworkAnswerDetailPlugin")
public class SaveHomeworkAnswerDetailPlugin implements ILogicPlugin {
	
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Value("${oss.bucket_name1}")
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
		
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		
		String homeworkDetailId = map.get("homeworkDetailId")==null?"":map.get("homeworkDetailId").toString();//作业明细ID
		String homeworkAnswerId = map.get("homeworkAnswerId")==null?"":map.get("homeworkAnswerId").toString();//作业回答ID
		String homeworkDetailAnswerId = map.get("homeworkDetailAnswerId")==null?"":map.get("homeworkDetailAnswerId").toString();//作业回答明细ID
		String id = map.get("id")==null||StringUtils.isBlank(map.get("id").toString())?"0":map.get("id").toString();//试题ID
		
		String content = map.get("content")==null?"":map.get("content").toString();//主观题内容或客观题选择的选项ID
		String fileIds = map.get("fileIds")==null?"":map.get("fileIds").toString();//主观题附件
		
		String userId = map.get("userId")==null?"":map.get("userId").toString();
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("homeworkDetailId", homeworkDetailId);
		paramMap.put("homeworkAnswerId", homeworkAnswerId);
		paramMap.put("id", id);//试题ID
		paramMap.put("userId", userId);
		List<Map<String, Object>> homeworkDetailList = queryDataBySqlIdAndParams("khApp/study/homework/QryHomeworkDetailById", paramMap);//回答附件
		
		if(homeworkDetailList == null || homeworkDetailList.size() == 0){
			log.error("保存试题请求数据有误homeworkDetailId/homeworkAnswerId/id/userId不对应：homeworkDetailId:"+homeworkDetailId+"homeworkAnswerId:"+homeworkAnswerId+"id:"+id+"userId:"+userId);
			responseObj.setCode(ExErrorCode.SAVE_HOMEWORK_DATA_ERROR.getCode());
			responseObj.setMsg(ExErrorCode.SAVE_HOMEWORK_DATA_ERROR.getMsg());
			return;
		}
		
		Map<String, Object> homeworkDetailMap = homeworkDetailList.get(0);
		String status = homeworkDetailMap.get("status")==null?"":homeworkDetailMap.get("status").toString();//作业回答状态
		if(status.equals(HomeWorkAnswerStatusEnum.HAVE_BEEN_SUBMIT.getCode()) || status.equals(HomeWorkAnswerStatusEnum.HAVE_BEEN_CORRECT.getCode())){
			log.error("该作业已提交或批改不能修改");
			responseObj.setCode(ExErrorCode.HOMEWORK_HAVE_BEAN_SUBMIT_ERROR.getCode());
			responseObj.setMsg(ExErrorCode.HOMEWORK_HAVE_BEAN_SUBMIT_ERROR.getMsg());
			return;
		}
		
		
		String homeworkId = homeworkDetailMap.get("homeworkId")==null?"":homeworkDetailMap.get("homeworkId").toString();//作业ID
		String questionType = homeworkDetailMap.get("questionType")==null?"":homeworkDetailMap.get("questionType").toString();//试题类型
		String objectiveScore = homeworkDetailMap.get("objectiveScore")==null?"":homeworkDetailMap.get("objectiveScore").toString();//客观题分值
		String teacherId = homeworkDetailMap.get("teacherId")==null?"":homeworkDetailMap.get("teacherId").toString();//教师ID
		String realScore = "0";
		paramMap.put("homeworkId", homeworkId);
		
		if(!questionType.equals(QuestionTypeEnum.SUBJECTIVE.getCode())){
			//非主观题算应得积分
			List<Map<String, Object>> rightOptionsList = queryDataBySqlIdAndParams("khApp/study/homework/QryQuestionRightOptionsByQuestionId", paramMap);//该题正确选项
			if(StringUtils.isNotBlank(content) && rightOptionsList != null && rightOptionsList.size() > 0){
				
				realScore = objectiveScore;
				
				List<String> studentOptionsList = Arrays.asList(content.trim().split(","));
				if(studentOptionsList.size() == rightOptionsList.size()){
					
					Map<String, String> studentOptionsMap = studentOptionsList.stream().collect(Collectors.toMap(m ->m, m ->m));
					
					for (Map<String, Object> m : rightOptionsList) {
						if(!studentOptionsMap.containsKey(m.get("id").toString())){
							realScore = "0";
							break;
						}
					}
					/*rightOptionsList.stream().forEach(m ->{
						if(!studentOptionsMap.containsKey(m.get("id").toString())){
							realScore = "0";
						}
					});*/
				}else {
					realScore = "0";
				}
			}
		}
		
		
		paramMap.put("objectiveScore", objectiveScore);
		paramMap.put("realScore", realScore);
		
		List<Map<String, Object>> exitHomeworkAnswerDetailList = queryDataBySqlIdAndParams("khApp/study/homework/QryExistsHomeAnDetail", paramMap);
		if(!"0".equals(homeworkDetailAnswerId)||exitHomeworkAnswerDetailList.size()>0){//非0对之前回答做修改
			if(exitHomeworkAnswerDetailList.size()>0) {
				homeworkDetailAnswerId = exitHomeworkAnswerDetailList.get(0).get("id").toString();
			}
			paramMap.put("homeworkDetailAnswerId", homeworkDetailAnswerId);
			
			List<Map<String, Object>> homeworkAnswerDetailList = queryDataBySqlIdAndParams("khApp/study/homework/QryHomeworkAnswerDetailById", paramMap);//回答附件
			
			if(homeworkAnswerDetailList == null || homeworkAnswerDetailList.size() == 0){
				log.error("保存试题请求数据有误homeworkDetailAnswerId与其他数据不对应：homeworkDetailAnswerId:"+homeworkDetailAnswerId+"homeworkDetailId:"+homeworkDetailId+"homeworkAnswerId:"+homeworkAnswerId+"id:"+id+"userId:"+userId);
				responseObj.setCode(ExErrorCode.SAVE_HOMEWORK_DATA_ERROR.getCode());
				responseObj.setMsg(ExErrorCode.SAVE_HOMEWORK_DATA_ERROR.getMsg());
				return;
			}
			
			
			if(questionType.equals(QuestionTypeEnum.SUBJECTIVE.getCode())){//主观题
				paramMap.put("content", content);
				execute("khApp/study/homework/UpdHomeworkDetailSubjectiveById", paramMap);
				execute("khApp/study/homework/DelHomeworkDetailSubjectiveAnswerFileByDetailId", paramMap);//删除原有答案
			}else{
				
				execute("khApp/study/homework/UpdHomeworkDetailObjectiveById", paramMap);
				execute("khApp/study/homework/DelHomeworkDetailObjectiveAnswerByDetailId", paramMap);//删除原有答案
			}
		}else{
			
			paramMap.put("objectiveScore", objectiveScore);
			paramMap.put("realScore", realScore);
			paramMap.put("teacherId", teacherId);
			if(questionType.equals(QuestionTypeEnum.SUBJECTIVE.getCode())){//主观题
				paramMap.put("content", content);
			}else{
				paramMap.put("content", "");
			}
			
			//新增t_homework_detail_answer表
			homeworkDetailAnswerId = execute("khApp/study/homework/InsertHomeworkDetailAnswer", paramMap);
			paramMap.put("homeworkDetailAnswerId", homeworkDetailAnswerId);
		}
		
		//新增t_homework_detail_subjective_answer_file/t_homework_detail_objective_answer
		if(questionType.equals(QuestionTypeEnum.SUBJECTIVE.getCode())){//主观题
			if(StringUtils.isNotBlank(fileIds)){
				Arrays.asList(fileIds.split(",")).stream().forEach(l ->{
					if(StringUtils.isNotBlank(l.trim())){
						paramMap.put("fileId", l.trim());
						execute("khApp/study/homework/InsertHomeworkDetailSubjectiveAnswerFile", paramMap);
					}
				});
			}
		}else{
			//客观题且选项不为空
			if(StringUtils.isNotBlank(content)){
				Arrays.asList(content.split(",")).stream().forEach(l ->{
					if(StringUtils.isNotBlank(l.trim())){
						paramMap.put("optionId", l.trim());
						execute("khApp/study/homework/InsertHomeworkDetailObjectiveAnswer", paramMap);
					}
				});
			}
			
			//做错进入错题集
			if("0".equals(realScore)){
				execute("khApp/study/homework/InsertWrongQuestion", paramMap);
			}else {
				execute("khApp/study/homework/DeleteWrongQuestion", paramMap);
			}
		}
		responseObj.setData(map);
	}

	
	private String execute(String sqlId, Map<String, Object> data){
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
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

