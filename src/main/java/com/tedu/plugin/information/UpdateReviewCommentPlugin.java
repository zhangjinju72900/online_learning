package com.tedu.plugin.information;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.common.constant.SendMsgTypeEnum;
import com.tedu.component.SendMsgComponent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回复评论
 * @author quancong
 *
 */
@Service("updateReviewComment")
public class UpdateReviewCommentPlugin implements ILogicPlugin {
	
	@Resource
	FormService formService;
	
	@Resource
	private FormMapper formMapper;
	
	@Resource
	private SendMsgComponent sendMsgComponent;
	
	private String sqlTemplate = "khApp/discover/information/insertReviewComment";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
  		log.info(formModel.getData());
  		Map<String,Object> map=(Map<String, Object>) formModel.getData();
  		String reviewId = (String) map.get("reviewId");
//  		String comment = (String) map.get("content");
  		String createBy = map.get("createBy")==null||StringUtils.isBlank(map.get("createBy").toString())?SessionUtils.getUserInfo().getUserId()+"":map.get("createBy").toString();
  		//通过当前评论id找到对应的information_id
  		Map<String, Object> map1 = new HashMap<String, Object>();
		QueryPage qp = new QueryPage();
		map1.put("id", reviewId);
		map1.put("createBy", createBy);
		qp.setDataByMap(map1);
		qp.setQueryParam("khApp/discover/information/QryInformationIdById");
		List<Map<String, Object>> qlist=new ArrayList<Map<String, Object>>();
		qlist = formService.queryBySqlId(qp);
		log.info(qlist);
		if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
			CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId(sqlTemplate);
			formModel.getData().put("createBy", createBy);
			cModel.setData(formModel.getData());
			Map<String, Object> logData = formModel.getData();
			logData.put("infoId", qlist.get(0).get("infoId"));

			LogUtil.info(cModel.getData().toString());
			
			formMapper.saveCustom(cModel);
		}
	}
}

