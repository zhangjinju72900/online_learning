package com.tedu.plugin.teacher.classes.manage.info;


import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("getClassOtherInfoPlugin")
public class GetClassOtherInfoPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		Map<String, Object> map = (Map<String, Object>) formEngineResponse.getData();
		
		List<Map<String, Object>> alreaySignTables = queryBySqlId("khTeacher/classManage/classInfo/QryClassAlreaySignCount", map);//已签到数量
		
		List<Map<String, Object>> totalTables = queryBySqlId("khTeacher/classManage/classInfo/QryClassUserTotalCount", map);
		
		int signCount = alreaySignTables == null ? 0 : alreaySignTables.size();
		
		int totalCount = totalTables == null ? 0 : totalTables.size();
		
		BigDecimal classRate = totalCount == 0 ? BigDecimal.ZERO : new BigDecimal(signCount).divide(new BigDecimal(totalCount), 4, BigDecimal.ROUND_HALF_UP);
		
		map.put("classRate", classRate.multiply(new BigDecimal(100)));

		List<Map<String, Object>> preMonthList = queryBySqlId("khTeacher/classManage/classInfo/QryPreMonthRate", map);
		
		List<Map<String, Object>> preWeekList = queryBySqlId("khTeacher/classManage/classInfo/QryPreWeekRate", map);
		
		String classId = map.get("classId").toString();
		
		int monthlyRanking = 1;
		boolean monthFlag = false;
		
		if(preMonthList != null && preMonthList.size() > 0){
			for(int i = 0; i< preMonthList.size(); i++){
				if(classId.equals(preMonthList.get(i).get("classId").toString())){
					monthFlag = true;
					monthlyRanking = i + 1;
					break;
				}
			}
			if(!monthFlag){
				monthlyRanking = preMonthList.size() + 1;
			}
		}
		
		map.put("monthlyRanking", monthlyRanking);
		
		
		int weeklyRanking = 1;
		boolean weekFlag = false;
		
		if(preWeekList != null && preWeekList.size() > 0){
			for(int i = 0; i< preWeekList.size(); i++){
				if(classId.equals(preWeekList.get(i).get("classId").toString())){
					weekFlag = true;
					weeklyRanking = i + 1;
					break;
				}
			}
			if(!weekFlag){
				weeklyRanking = preWeekList.size() + 1;
			}
		}
		
		map.put("weeklyRanking", weeklyRanking);
		

		formEngineResponse.setData(map);
	}

	private List<Map<String, Object>> queryBySqlId(String string, Map<String, Object> map) {
		
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam(string);
		sqlQuery.setDataByMap(map);
		return formMapper.query(sqlQuery);
	}

}
