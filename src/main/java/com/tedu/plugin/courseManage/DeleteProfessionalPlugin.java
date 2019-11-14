package com.tedu.plugin.courseManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
/**
 * 删除专业
 *   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月30日 下午2:32:21   
 *
 */
@Service("deleteProfessional")
public class DeleteProfessionalPlugin implements ILogicPlugin{
	
	public final Logger log = Logger.getLogger(this.getClass());

	@Resource
	private FormMapper formMapper;
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		CustomFormModel cModel = new CustomFormModel();
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("zhongdeprofession/QryProfessAndCourse");
		sqlQuery.setDataByMap(formModel.getData());
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		try {
			
			if(tables != null && tables.size() > 0){
				responseObj.setMsg("专业包含子专业或包含课程,不可删除");
			}else{
				cModel.setSqlId("zhongdeprofession/DeleteProess");
				cModel.setData(formModel.getData());
				formMapper.saveCustom(cModel);
			}
			
		} catch (Exception e) {
			log.error("删除专业失败，请查看日志", e);
			responseObj.setMsg("删除专业失败，请查看日志");
			e.printStackTrace();
		}
		
	}
	
}
