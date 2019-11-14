package com.tedu.business.attendance.manage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.SessionUtils;
import com.tedu.business.attendance.manage.dao.AbsenteeismCurveDao;
import com.tedu.business.attendance.manage.service.AbsenteeismCurveService;

@Service("absenteeismCurveServiceImpl")
public class AbsenteeismCurveServiceImpl implements AbsenteeismCurveService{

	@Resource
	private AbsenteeismCurveDao absenteeismCurveDao;	
	
	@Override
	public void getCurveData(String startDate, String endDate, String classId, String userId, Map<String, Object> result) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
//		Long userId = SessionUtils.getUserInfo().getUserId();
		List<Map<String, Object>> qlists = absenteeismCurveDao.getCurveData(startDate, endDate, classId, userId);
		
		if(qlists != null){
			String[] x = new String[qlists.size()];
			Double[] y = new Double[qlists.size()];
			for (int i=0; i<qlists.size(); i++) {
				x[i] = qlists.get(i).get("startTime")==null?"":qlists.get(i).get("startTime").toString();
				y[i] = qlists.get(i).get("signRate")==null?0:Double.parseDouble(qlists.get(i).get("signRate").toString());
			}
			result.put("xAxisData", x);
			result.put("seriesData", y);
		}else{
			result.put("xAxisData", null);
			result.put("seriesData", null);
		}
		
	}

}
