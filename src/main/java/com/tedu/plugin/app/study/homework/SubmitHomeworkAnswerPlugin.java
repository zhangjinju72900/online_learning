package com.tedu.plugin.app.study.homework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.tedu.common.constant.HomeworkTypeEnum;
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
@Service("submitHomeworkAnswerPlugin")
public class SubmitHomeworkAnswerPlugin implements ILogicPlugin {
	
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
		
		String userId = map.get("userId")==null?"":map.get("userId").toString();
		String homeworkAnswerId = map.get("homeworkAnswerId")==null?"":map.get("homeworkAnswerId").toString();//作业回答ID
		String confirmFlag = map.get("confirmFlag")==null?"":map.get("confirmFlag").toString();//有未回答项是否确定提交，0-检查并返回未回答ID，1-忽略直接提交
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("homeworkAnswerId", homeworkAnswerId);
		
		//验证homeworkAnswerId 与 userId准确性
		List<Map<String, Object>> homeworkAnswerList = queryDataBySqlIdAndParams("khApp/study/homework/QryHomeworkAnswerById", paramMap);
		
		if(homeworkAnswerList == null || homeworkAnswerList.size() == 0){
			log.error("提交作业请求数据有误homeworkAnswerId/userId不对应：homeworkAnswerId:"+homeworkAnswerId+"userId:"+userId);
			responseObj.setCode(ExErrorCode.SUBMIT_HOMEWORK_DATA_ERROR.getCode());
			responseObj.setMsg(ExErrorCode.SUBMIT_HOMEWORK_DATA_ERROR.getMsg());
			return;
		}
		Map<String, Object> homeworkAnswerlMap = homeworkAnswerList.get(0);
		String homeworkType = homeworkAnswerlMap.get("homeworkType")==null?"":homeworkAnswerlMap.get("homeworkType").toString();//试题类型
		String homeworkId = homeworkAnswerlMap.get("homeworkId")==null?"":homeworkAnswerlMap.get("homeworkId").toString();//作业ID
		
		paramMap.put("homeworkId", homeworkId);
		//查询作业已提交人数
		List<Map<String, Object>> homeworkAnswerNotSubmitCountList = queryDataBySqlIdAndParams("khApp/study/homework/QryHomeworkAnswerNotSubmitCountByHomeworkId", paramMap);
		
		//查询作业总人数
		List<Map<String, Object>> homeworkAnswerCountList = queryDataBySqlIdAndParams("khApp/study/homework/QryHomeworkAnswerCountByHomeworkId", paramMap);
		
		BigDecimal finishSpeed = BigDecimal.ZERO;
		int homeworkAnswerNotSubmitCount = 0;
		if(homeworkAnswerNotSubmitCountList != null && homeworkAnswerNotSubmitCountList.size() > 0 && homeworkAnswerCountList != null && homeworkAnswerCountList.size() > 0){
			homeworkAnswerNotSubmitCount = Integer.parseInt(homeworkAnswerNotSubmitCountList.get(0).get("homeworkAnswerNotSubmitCount").toString()) ; //获取未提交人数;
			//查询作业总人数
			int homeworkAnswerCount = Integer.parseInt(homeworkAnswerCountList.get(0).get("homeworkAnswerCount").toString());
			//第一个人那么就是超过99.99%的人员
			if(homeworkAnswerCount-homeworkAnswerNotSubmitCount==0) {
				finishSpeed = new BigDecimal(99.99);
			}
			//如果是最后一个那么久是0%
			else if(homeworkAnswerNotSubmitCount==1) {
				finishSpeed = new BigDecimal(0.00);
			}
			//其他情况下计算公式为 ：(未提交人数-1)/总人数
			else {
				homeworkAnswerNotSubmitCount = homeworkAnswerNotSubmitCount- 1;
				finishSpeed = new BigDecimal(homeworkAnswerNotSubmitCount).divide(new BigDecimal(homeworkAnswerCount), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
		}
		
		paramMap.put("finishSpeed", finishSpeed);
		if(homeworkAnswerNotSubmitCount!=0) {
			if(!homeworkType.equals(HomeworkTypeEnum.SUBJECTIVE.getCode())){
				//客观题 t_homework 算平均分、t_homework_answer得分、更改t_homework_answer状态 完成速度百分比
				
				
				execute("khApp/study/homework/UpdHomeworkAnswerRealScoreById", paramMap);
				
			}else{
				//主观题
				//更改t_homework_answer状态 完成速度百分比
				execute("khApp/study/homework/UpdHomeworkAnswerFinishById", paramMap);
				
			}
		}
		if(!homeworkType.equals(HomeworkTypeEnum.SUBJECTIVE.getCode())){
			execute("khApp/study/homework/UpdHomeworkAvgScoreById", paramMap);
			paramMap.put("status", HomeWorkAnswerStatusEnum.HAVE_BEEN_CORRECT.getCode());
		}else {
			paramMap.put("status", HomeWorkAnswerStatusEnum.HAVE_BEEN_SUBMIT.getCode());
		}
		//更改 t_homework_detail_answer状态
		execute("khApp/study/homework/UpdHomeworkAnswerDetailFinishById", paramMap);
		
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

