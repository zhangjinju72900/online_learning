package com.tedu.business.task;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.base.task.SpringUtils;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CalculateSignInRate {

	@Resource
	FormMapper formMapper;
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 定时获取TRANS表数据存储到 HISTORY_TRANS表
	 *
	 * @param time 为空取当天，不为空格式 yyyy-MM-dd
	 * @throws ServletException
	 */

	public void getTransData() throws Exception {
		String time = "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		time = sdf.format(new Date());
		log.info("开始执行计算班级出勤率定时任务:"+time);
		QueryPage sqlQuery2 = new QueryPage();
		sqlQuery2.setQueryParam("khTeacher/task/attendClass/QryAttendClassRecord");
		sqlQuery2.setQueryType("");
		sqlQuery2.setSingle(1);
		sqlQuery2.setSqlId("khTeacher/task/attendClass/QryAttendClassRecord");
		Map<String, Object> paramMap = new HashMap<>();
		sqlQuery2.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery2);	
		if(tables != null){
			for (Map<String, Object> map : tables) {
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId("khTeacher/task/attendClass/InsertClassSignRate");
				
				BigDecimal rate = (map.get("studentCount")== null || new BigDecimal(map.get("studentCount").toString()).compareTo(BigDecimal.ZERO) == 0 
						|| map.get("realStudentCount") == null || new BigDecimal(map.get("realStudentCount").toString()).compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : 
							new BigDecimal(map.get("realStudentCount").toString()).divide(
								new BigDecimal(map.get("studentCount").toString()), 2, BigDecimal.ROUND_HALF_UP);
				
				map.put("rate", rate);
				cModel.setData(map);
				formMapper.saveCustom(cModel);
			}
		}
		log.info("结束执行计算班级出勤率定时任务:"+time);
	}

}
