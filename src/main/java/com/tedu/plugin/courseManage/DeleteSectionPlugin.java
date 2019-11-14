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
 * 删除小结
 * @ClassName:  DeleteSectionOrLabelPlugin   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月30日 下午2:32:21   
 *
 */
@Service("deleteSectionPlugin")
public class DeleteSectionPlugin implements ILogicPlugin{
	
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
		//{courseId=8, treeId=sec-8, pid=cou-8, jid=, id=null, Mode=Edit, session={userInfo=com.tedu.base.auth.login.model.UserModel@caca6d}}
		CustomFormModel cModel = new CustomFormModel();
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String treeId = map.get("treeId").toString();
		String pid = map.get("pid").toString();
		String courseId = map.get("courseId").toString();
		String sectionId="";
		try {
			sectionId=treeId.split("-")[1];
			QueryPage sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("zhongdeprofession/QrySectionLabelBySid");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId("zhongdeprofession/QrySectionLabelBySid");
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("courseId", courseId);
			paramMap.put("sectionId", sectionId);
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> tables = formMapper.query(sqlQuery);
			
			if(tables != null && tables.size() > 0){
				responseObj.setMsg("当前小节下标签包含资源，不可删除");
			}else{
				formModel.getData().put("courseId", courseId);
				formModel.getData().put("sectionId", sectionId);
				log.info(formModel.getData());
				cModel.setSqlId("zhongdeprofession/DeleteCourseSection");
				cModel.setData(formModel.getData());
				formMapper.saveCustom(cModel);
			}
			
		} catch (Exception e) {
			log.error("删除小节失败，请查看日志", e);
			responseObj.setMsg("删除小节失败，请查看日志");
			e.printStackTrace();
		}
		
	}
	
}
