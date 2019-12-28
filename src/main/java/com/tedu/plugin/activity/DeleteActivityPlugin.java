package com.tedu.plugin.activity;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 新建活动保存
 * 
 * @author jiaofengqin
 *
 */
@Service("deleteActivityPlugin")
public class DeleteActivityPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		Map<String, Object> map = requestObj.getData();
		String[] ids = map.get("id").toString().split(",");//获取删除活动的id
		for (String id : ids) {
			//遍历id，将活动对应的数据删除掉//活动角色表和活动地区表
			//删除活动角色表
			CustomFormModel activityRole = new CustomFormModel();
			activityRole.setData(map);
			activityRole.setSqlId("khAdmin/activityManage/DeleteActivityRole");
			formMapper.saveCustom(activityRole);

			//删除活动地区表
			CustomFormModel activityRegion = new CustomFormModel();
			activityRegion.setData(map);
			activityRegion.setSqlId("khAdmin/activityManage/DeleteActivityRegion");
			formMapper.saveCustom(activityRegion);
		}
	}
}
