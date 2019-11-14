package com.tedu.plugin.app.study.attend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import com.tedu.base.common.model.DataGrid;
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
 * 查看上课详情
 * @author quancong
 *
 */
@Service("appAttendClassDetailPlugin")
public class AppAttendClassDetailPlugin implements ILogicPlugin {
	
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String,Object> map= (Map<String, Object>) requestObj.getData();
		List<Map<String, Object>> tables = queryDataBySqlIdAndParams("khApp/study/attendClass/QryRoleIdByUserId", map);
		if(tables != null && tables.size() > 0){
			tables.stream().forEach(m ->{
				if("10".equals(m.get("roleId").toString())){
					map.put("roleId", "10");
				}else if("11".equals(m.get("roleId").toString())){
					map.put("roleId", "11");
				}
			});
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		Map<String,Object> map= (Map<String, Object>) requestObj.getData();
		List<Map<String, Object>> tables = queryDataBySqlIdAndParams("khApp/study/attendClass/QryAttendClassDetail", map);
		if(tables != null){
			tables.stream().forEach(l ->{
				l.put("showAttendClassDetail", "查看上课详情");
			});
		}
		DataGrid dgData = new DataGrid(tables==null?new ArrayList<Map<String, Object>>():tables);
		dgData.setTotal(tables==null?0:tables.size());
		responseObj.setData(dgData);
		
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

