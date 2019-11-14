package com.tedu.plugin.check;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.InfoCheckTypeEnum;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("informationCheckPlugin")
public class InformationCheckFailPlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	private String sqlTemplate = "khAdmin/checkManage/UpdateCheckManageStatusFail";
	private String sqlTemplate2 = "khApp/discover/information/updateReview";
	private String sqlTemplate3 = "khApp/discover/information/updateActReview";
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  String[] ids=(map.get("id") == null ? "":map.get("id").toString()).split("-");
		  String type = ids[0];
		  String id = ids[1];
		  log.info(ids.toString());
		  
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				Map<String, Object> logData = formModel.getData();
				
				logData.put("id", id);
				logData.put("type", type);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);

		if (InfoCheckTypeEnum.INFO_REVIEW.getCode().equals(type)) {
			//更改评论数
			CustomFormModel cModel2 = new CustomFormModel();
			cModel2.setSqlId(sqlTemplate2);
			cModel2.setData(formModel.getData());
			Map<String, Object> logData2 = formModel.getData();
			LogUtil.info(cModel2.getData().toString());
			logData.put("id", id);
			formMapper.saveCustom(cModel2);
		}

		if (InfoCheckTypeEnum.LIVE_REVIEW.getCode().equals(type)) {
			CustomFormModel cModel3 = new CustomFormModel();
			cModel3.setSqlId(sqlTemplate3);
			cModel3.setData(formModel.getData());
			Map<String, Object> logData3 = formModel.getData();
			LogUtil.info(cModel3.getData().toString());
			logData.put("id", id);
			formMapper.saveCustom(cModel3);
		}
	}
}

