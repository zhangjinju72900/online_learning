package com.tedu.business.easemob.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.business.easemob.service.EasemobRecordService;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.common.error.ExErrorCode;
import com.tedu.component.EasemobComponent;

@Controller
public class EasemobController {
	
	@Resource
	private EasemobComponent easemobComponent;
	
	@Resource
	private CustomUserService customUserServiceImpl;
	
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@RequestMapping("/registerEasemob")
	@ResponseBody
	public FormEngineResponse registerEasemob(HttpServletRequest request){
		String result = "注册完成";
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			List<Map<String, Object>> list = customUserServiceImpl.selectUnregisterUser();
			if(list != null){
				for (Map<String, Object> map : list) {
					if(map.get("card_num") != null && StringUtils.isNotBlank(map.get("card_num").toString())){
						easemobComponent.register(easemobComponent.getToken(), map.get("card_num").toString(), map.get("card_num").toString());
						customUserServiceImpl.updateEasemobUser(map.get("id").toString(), map.get("card_num").toString());
					}	
				}
			}
			response.setMsg(result);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode(ExErrorCode.REGISTER_EASEMOB_FAIL.getCode());
			response.setMsg(ExErrorCode.REGISTER_EASEMOB_FAIL.getMsg());
		}	
		return response;
	}
}
