package com.tedu.plugin.schoolManage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tedu.base.engine.aspect.ILogicServicePlugin;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

/**
 * 导入班级处理开课时间-年级
 * 
 * @author gonghaoxin
 *
 */

public class ImportClassPlugin implements ILogicServicePlugin {

	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String executeTime = map.get("startClassTime") == null ? "" : map.get("startClassTime").toString();
		
		if(StringUtils.isNotBlank(executeTime)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formDate = executeTime.replaceAll("/", "-");
			Date date = null;
			try {
				date = sdf.parse(formDate);
				
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				
				int year = c.get(Calendar.YEAR);
				map.put("startClassTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, date));
				map.put("grade", year);
			} catch (ParseException e) {
				log.error("导入班级转换开课时间失败", e);
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		log.info(formModel.getData());
	}

}
