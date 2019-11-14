package com.tedu.plugin.platformRelease;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicServicePlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("deleteInformationsPlugin2")
public class DeleteInformationsPlugin2 implements ILogicServicePlugin {
	@Resource
    FormLogService formLogService;
	
	@Resource
	FormService formService;
	@Resource
	private FormMapper formMapper;
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult) {
		
//			  	log.info(formModel.getData());
			  	Map<String,Object> map=(Map<String, Object>) formModel.getData();
			  	String id = map.get("id")==null?"":map.get("id").toString();
			  	String userId = map.get("userId")==null?"":map.get("userId").toString();
			  	UserModel user = SessionUtils.getUserInfo();
			  	
			  	userId = StringUtils.isBlank(userId) ? user.getUserId()+"" : userId;
			  	
			  	String roleId = user.getVersion();
	  			boolean isStudent = false;
	  			if("10".equals(roleId)){
	  				isStudent = true;
	  			}
	  			
			  	//更新
	  			CustomFormModel cModel = new CustomFormModel();
	  			if(!isStudent){
					cModel.setSqlId("informations/LogicDeleteInformation");
					cModel.setData(formModel.getData());
					Map<String, Object> logData = formModel.getData();
					logData.put("id", id);
					LogUtil.info(cModel.getData().toString());
					formMapper.saveCustom(cModel);
	  			}else {
	  				cModel.setSqlId("informations/LogicDeleteInformation2");
					cModel.setData(formModel.getData());
					Map<String, Object> logData = formModel.getData();
					logData.put("id", id);
					logData.put("userId", userId);
					LogUtil.info(cModel.getData().toString());
					formMapper.saveCustom(cModel);
	  			}
				

	}
}
