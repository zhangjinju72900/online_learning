package com.tedu.business.user.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.business.user.service.PasswordChangeService;
import com.tedu.common.error.ExErrorCode;

@Controller
public class PasswordChangeController {
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Resource
	private PasswordChangeService passwordChangeServiceImpl;
	
	
	/**
	 * @Title:   
	 * @Description: TODO app修改密码
	 * @author: quancong
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年11月5日 下午14:59     
	 * @throws
	 */
	@RequestMapping("/passwordChange")
	@ResponseBody
	public FormEngineResponse changePassword(@RequestBody FormEngineRequest requestObj,HttpServletRequest request){
		String result="";
		FormEngineResponse response = new FormEngineResponse(result);
		Map <String, Object> param = requestObj.getData();
		//获得页面的参数值
		String oldPassWord = param.get("oldPassword")==null?"":param.get("oldPassword").toString();
		String newPassword = param.get("newPassword")==null?"":param.get("newPassword").toString();
		String confirmPassword=param.get("confirmPassword")==null?"":param.get("confirmPassword").toString();
		/*String oldPassWord = request.getParameter("oldPassword")==null?"":request.getParameter("oldPassword").toString();
		String newPassword = request.getParameter("newPassword")==null?"":request.getParameter("newPassword").toString();
		String confirmPassword = request.getParameter("confirmPassword")==null?"":request.getParameter("confirmPassword").toString();*/
		if(!(StringUtils.isBlank(oldPassWord) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmPassword))){
			//判断两次输入的密码是否相同
			if(newPassword.equals(confirmPassword)){
				//判断旧密码是否正确
				if(passwordChangeServiceImpl.checkOldPassword(oldPassWord)){
					//保存新的密码
					passwordChangeServiceImpl.updateUserPassword(newPassword);
					response.setMsg("密码已成功修改!");
					log.info("密码已成功修改!");
				}else{
					response.setCode(ExErrorCode.PWD_OLDPWD_ERROR.getCode());
					response.setMsg(ExErrorCode.PWD_OLDPWD_ERROR.getMsg());
					log.info(ExErrorCode.PWD_OLDPWD_ERROR.getMsg());
				}
			}else{
				response.setCode(ExErrorCode.PWD_INCONFORMITY.getCode());
				response.setMsg(ExErrorCode.PWD_INCONFORMITY.getMsg());
				log.info(ExErrorCode.PWD_INCONFORMITY.getMsg());
				
			}
		}else{
			response.setMsg(ExErrorCode.PWD_EMPTY.getCode());
			log.info(ExErrorCode.PWD_EMPTY.getMsg());
		}
		
		return response;
	}
	
	

}
