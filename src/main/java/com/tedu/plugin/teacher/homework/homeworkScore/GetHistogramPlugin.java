package com.tedu.plugin.teacher.homework.homeworkScore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.oss.service.OssQueryService;
/**
 * 返回柱状图所需参数
 * @author quancong
 *
 */
@Service("getHistogramPlugin")
public class GetHistogramPlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		 log.info(formModel.getData());
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		DataGrid dataList= (DataGrid) responseObj.getData();
  		List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
  		rows = (List<Map<String, Object>>) dataList.getRows();
  		List rowsNew = new ArrayList<Map<String,Object>>();
  		Map<String,Object> map = new HashMap<String,Object>();
  		if(rows!=null){
  			String[] x = new String[rows.size()];
  			Double[] y= new Double[rows.size()];
  			for(int i=0;i<rows.size();i++){
  	  			x[i] = rows.get(i).get("questionId") == null ? "" :rows.get(i).get("questionId").toString();
  	  			y[i] = rows.get(i).get("wrongRate") == null? 0 : Double.parseDouble(rows.get(i).get("wrongRate").toString());
  	  		}
  			map.put("xAxisData", x);
			map.put("seriesData", y);
			rowsNew.add(map);
  		}
  		else{
  			map.put("xAxisData", null);
  			map.put("seriesData", null);
			rowsNew.add(map);
  		}
  		dataList.setRows(rowsNew);
  		responseObj.setData(dataList);

	}

}
