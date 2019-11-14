package com.tedu.plugin.operate;

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
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.task.SpringUtils;
import com.tedu.oss.service.OssQueryService;
/**
 * 返回每个时间端的活跃次数
 * @author wuyun
 *
 */
@Service("getAllTimesPlugin")
public class GetAllTimesPlugin implements ILogicPlugin {
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
		//获取页面数据
		Map<String,Object> Querymap = requestObj.getData();
				
		QueryPage page1 = new QueryPage();
		//设置sql查询条件
		Map<String,Object> QueryUseMap = new HashMap<String,Object>();
		
		page1.setQueryParam("operatelog/QryAllTimes");
		page1.getData().put("customUserId",Querymap.get("customUserId"));
		page1.getData().put("createTimeStart",Querymap.get("createTimeStart"));
		page1.getData().put("createTimeEnd",Querymap.get("createTimeEnd"));
		page1.setDataByMap(QueryUseMap);
		//数据库查询到数据
		List<Map<String,Object>> list = formService.queryBySqlId(page1);
				
		DataGrid dataList= (DataGrid) responseObj.getData();
  		List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
  		rows = (List<Map<String, Object>>) dataList.getRows();
  		List rowsNew = new ArrayList<Map<String,Object>>();
  		Map<String,Object> map = new HashMap<String,Object>();
  		if(rows!=null){
  			int[] x = new int[24];
  			int[] y = new int[24];
  			//给y全部初始化为0
  			for(int i : y){
  				i = 0;
  			}
  			//给x赋值为0~23
  			for(int j = 0;j<24;j++){
  				x[j] = j;
  			}
  			for(Map<String,Object> Model:list){
  				int time = Integer.valueOf(Model.get("hours").toString());
  				y[time] = Integer.valueOf(Model.get("countTimes").toString());
  			}
  			map.put("xAxisData", x);
			map.put("seriesData", y);
			FormLogger.info(map.toString());
			rowsNew.add(map);
  		}
  		else{
  			map.put("xAxisData", null);
  			map.put("seriesData", null);
			rowsNew.add(map);
  		}
  		dataList.setTotal(rows.size());
  		dataList.setRows(rowsNew);
  		responseObj.setData(map);

	}

}
