package com.tedu.plugin.school;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

@Service("insertSchoolPlugin")
public class InsertSchoolPlugin implements ILogicPlugin {
	

	@Resource
	private FormService formService;
	
	
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String id = map.get("id") == null ? "" : map.get("id").toString();
		String title = map.get("title") == null ? "" : map.get("title").toString();
		
		QueryPage qp = new QueryPage();
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("title", StringUtils.isBlank(title)?"":title);
		if(StringUtils.isBlank(id) || "0".equals(id)){
			qp.setQueryParam("khAdmin/schoolManage/school/QrySchoolByName");
			qp.setSqlId("khAdmin/schoolManage/school/QrySchoolByName");
		}else{
			requestMap.put("id", id);
			qp.setQueryParam("khAdmin/schoolManage/school/QrySchoolByNameAndId");
			qp.setSqlId("khAdmin/schoolManage/school/QrySchoolByNameAndId");
		}
		qp.setDataByMap(requestMap);
		List qlist = formService.queryBySqlId(qp);
		if(qlist != null && qlist.size() > 0){
			throw new ValidException(ErrorCode.MAIL_EXCEPTION, "学校名称重复", title);
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
	}

}
