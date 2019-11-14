package com.tedu.plugin.check;

import com.tedu.base.common.model.DataGrid;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("informationCheckListPlugin")
public class InformationCheckListPlugin implements ILogicPlugin {
	
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		DataGrid dg = (DataGrid) responseObj.getData();
		if(dg != null && dg.getRows() != null){
			List<Map<String, Object>> list = (List<Map<String, Object>>) dg.getRows();
			list.stream().forEach(m ->{
				if(m.get("title") != null && m.get("title").toString().length() > 50){
					m.put("title", m.get("title").toString().substring(0, 48)+"...");
				}
			});
		}
	}
}
