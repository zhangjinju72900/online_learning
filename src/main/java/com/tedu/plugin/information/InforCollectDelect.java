package com.tedu.plugin.information;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("infoCollectDelect")
public class InforCollectDelect implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();

		String type = map.get("type")==null?"":map.get("type").toString();
		String userId = map.get("createBy")==null?"":map.get("createBy").toString();
		String createBy = StringUtils.isBlank(userId)?SessionUtils.getUserInfo().getUserId()+"":userId;
		CustomFormModel cModel2 = new CustomFormModel();
		formModel.getData().put("createBy", createBy);
		if("good".equals(type)){
			cModel2.setSqlId("khApp/discover/information/deleteCollectGood");
		}else{
			cModel2.setSqlId("khApp/discover/information/deleteCollect");
		}
		cModel2.setData(formModel.getData());
		// Map<String, Object> logData2 = formModel.getData();
		LogUtil.info(cModel2.getData().toString());

		int i = formMapper.saveCustom(cModel2);
		if (i > 0) {
			cModel2 = new CustomFormModel();
			if("good".equals(type)){
				cModel2.setSqlId("khApp/discover/information/cancelGoodCollect");
				cModel2.setData(formModel.getData());
				formMapper.saveCustom(cModel2);
			}else{
				cModel2.setSqlId("khApp/discover/information/cancelinfoCollect");
				cModel2.setData(formModel.getData());
				formMapper.saveCustom(cModel2);
			}	
		}

	}
}