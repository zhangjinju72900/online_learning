package com.tedu.plugin.area;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("deleteAreaPlugin")
public class deleteAreaPlugin implements ILogicPlugin {

	public final Logger log = Logger.getLogger(this.getClass());

	@Resource
	private FormMapper formMapper;

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		log.info(formModel.getData());
		CustomFormModel cModel = new CustomFormModel();
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("area/DelQrySchool");
		sqlQuery.setDataByMap(formModel.getData());
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		try {

			if(tables != null && tables.size() > 0){
				formEngineResponse.setMsg("有学校绑定该大区,不可删除");
			}else{
				cModel.setSqlId("area/DelArea");
				cModel.setData(formModel.getData());
				formMapper.saveCustom(cModel);
			}

		} catch (Exception e) {
			log.error("删除大区失败，请查看日志", e);
			formEngineResponse.setMsg("删除大区失败，请查看日志");
			e.printStackTrace();
		}
	}
}
