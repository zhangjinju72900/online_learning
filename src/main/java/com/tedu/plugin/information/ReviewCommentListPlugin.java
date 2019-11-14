package com.tedu.plugin.information;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
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
import com.tedu.oss.service.OssQueryService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论的回复显示前5条
 * @author quancong
 *
 */
@Service("reviewCommentList")
public class ReviewCommentListPlugin implements ILogicPlugin {
	
	@Resource
	private OssQueryService ossQueryServiceImpl2;
	
	@Resource
	FormService formService;
	@Resource
	private FormMapper formMapper;
	private String sqlTemplate = "khApp/discover/information/QryReviewComment";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		  		DataGrid dataList= (DataGrid) responseObj.getData();
		  		List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
		  		rows = (List<Map<String, Object>>) dataList.getRows();
		  		
		  		long userId = SessionUtils.getUserInfo().getUserId();
		  		for(Map<String, Object> row:rows){
		  			
		  			String uFileId = row.get("uFileId")==null?"":row.get("uFileId").toString();
					String ufileOssUrl = row.get("ufileOssUrl")==null?"":row.get("ufileOssUrl").toString();
					String fileCDNUrl = ossQueryServiceImpl2.queryCDNUrlByOssUrl(uFileId, ufileOssUrl);
					row.put("ufileCDNUrl", fileCDNUrl);
					row.remove("ufileOssUrl");
		  			
		  			QueryPage qp = new QueryPage();
		  			List<Map<String, Object>> qlist=new ArrayList<Map<String, Object>>();
		  			qp.setQueryParam(sqlTemplate);
		  			
		  			Map<String, Object> paramMap = new HashMap<>();
		  			paramMap.put("userId", userId);
		  			paramMap.put("id", row.get("id"));
		  			qp.setDataByMap(paramMap);
		  			
		  			qlist = formService.queryBySqlId(qp);
		  			DataGrid reviewList = new DataGrid(qlist);
		  			reviewList.setTotal(qlist.size());
		  			FormEngineResponse reviewObject = new FormEngineResponse(reviewList);
		  			row.put("reviewData", reviewObject.getData());
		  		}
		  		dataList.setRows(rows);
				responseObj.setData(dataList);
	}
}

