package com.tedu.business.user.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.business.user.service.BindingTelService;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.error.ExErrorCode;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;
import com.tedu.component.SendSmsComponent;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class BindingTelController {
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private SendSmsComponent sendSmsComponent;
	
	@Resource
	private BindingTelService bindingTelServiceImpl;
	
	@Resource
	private IntegralChangeComponent integralChangeComponent;
	
	/**
	 * @Title: queryObjectByKey   
	 * @Description: TODO 获取手机验证码
	 * @author: qun 
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年7月31日 下午2:19:06     
	 * @throws
	 */
	@RequestMapping("/getTelAuthCode")
	@ResponseBody
	public FormEngineResponse getTelAuthCode(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "发送成功";
		FormEngineResponse response = new FormEngineResponse("");
		try {
			Map <String, Object> param = requestObj.getData();
			
			if(validateParam(response, param, "getTelAuthCode")){
				return response;
			}
			
			String code = RandomStringUtils.random(4, "0123456789");
			UserModel model = SessionUtils.getUserInfo();
			
			String type = param.get("type")==null?"":param.get("type").toString();
			if("resetPwd".equals(type)){
				if (bindingTelServiceImpl.verifyAccountExists(param.get("telNum").toString())){
					SendSmsResponse res = sendSmsComponent.sendSms(param.get("telNum").toString(), model==null?"用户":model.getName(), code);
					bindingTelServiceImpl.insertSmsRecord(res, param.get("telNum").toString(), param.get("type").toString(), code);
					SessionUtils.getSession().setAttribute("authCode"+param.get("type").toString(), code);
					response.setMsg(result);
				}else {
					response.setCode(ExErrorCode.ACCOUNT_NOT_EXIT.getCode());
					response.setMsg(ExErrorCode.ACCOUNT_NOT_EXIT.getMsg());
					return response;
				}
			}else if("bindingTel".equals(type)){
				if (!bindingTelServiceImpl.verifyAccountExists(param.get("telNum").toString())){
					SendSmsResponse res = sendSmsComponent.sendSms(param.get("telNum").toString(), model==null?"用户":model.getName(), code);
					bindingTelServiceImpl.insertSmsRecord(res, param.get("telNum").toString(), param.get("type").toString(), code);
					SessionUtils.getSession().setAttribute("authCode"+param.get("type").toString(), code);
					response.setMsg(result);
				}else {
					response.setCode(ExErrorCode.ACCOUNT_ALREADY_EXIT.getCode());
					response.setMsg(ExErrorCode.ACCOUNT_ALREADY_EXIT.getMsg());
					return response;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode(ExErrorCode.SMS_CODE_FAIL.getCode());
			response.setMsg(ExErrorCode.SMS_CODE_FAIL.getMsg());
		}	
		return response;
	}
	
	
	/**
	 * @Title: bindingTelNum   
	 * @Description: TODO 绑定手机号
	 * @author: qun 
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年8月2日 下午7:14:46     
	 * @throws
	 */
	@RequestMapping("/bindingTelNum")
	@ResponseBody
	public FormEngineResponse bindingTelNum(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			
			Map <String, Object> param = requestObj.getData();
			
			if(validateParam(response, param, "bindingTelNum")){
				return response;
			}
			
			if(param.get("code").toString().equals(SessionUtils.getSession().getAttribute("authCode"+param.get("type").toString()))){
				//给用户绑定手机号
				bindingTelServiceImpl.bindingTelNum(param.get("telNum").toString(), param.get("userId")==null?"":param.get("userId").toString());
				SessionUtils.getSession().setAttribute("authCode"+param.get("type").toString(), "");
				response.setMsg("绑定成功");
				
				integralChangeComponent.setIntegralChange(IntegralEnum.BINDING_TEL, new IntegralChangeModel("0"));
			}else{
				response.setCode(ExErrorCode.SMS_CODE_ERROR.getCode());
				response.setMsg(ExErrorCode.SMS_CODE_ERROR.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode(ExErrorCode.BINDING_TEL_FAIL.getCode());
			response.setMsg(ExErrorCode.BINDING_TEL_FAIL.getMsg());
		}	
		return response;
	}
	
	
	@RequestMapping("/resetPassword")
	@ResponseBody
	public FormEngineResponse resetPassWord(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			
			Map <String, Object> param = requestObj.getData();
			
			if(validateParam(response, param, "resetPassword")){
				return response;
			}
			
			if(param.get("code").toString().equals(SessionUtils.getSession().getAttribute("authCode"+param.get("type").toString()))){
				//给用户绑定手机号
				bindingTelServiceImpl.resetPassWord(param.get("telNum").toString(), param.get("password").toString(), param.get("confirmPassword").toString());
				SessionUtils.getSession().setAttribute("authCode"+param.get("type").toString(), "");
				response.setMsg("重置成功");
			}else{
				response.setCode(ExErrorCode.SMS_CODE_ERROR.getCode());
				response.setMsg(ExErrorCode.SMS_CODE_ERROR.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode(ExErrorCode.RESTE_PWD_FAIL.getCode());
			response.setMsg(ExErrorCode.RESTE_PWD_FAIL.getMsg());
		}	
		return response;
	}
	
	
	private boolean validateParam(FormEngineResponse response, Map<String, Object> param, String string) {
		
		if(param.get("type")==null || StringUtils.isBlank(param.get("type").toString())){
			response.setCode("999990");
			response.setMsg("类型为空");
			return true;
		}
		if(param.get("telNum")==null || StringUtils.isBlank(param.get("telNum").toString())){
			response.setCode("999991");
			response.setMsg("手机号为空");
			return true;
		}
		
		if("bindingTelNum".equals(string)){
			if(param.get("code")==null || StringUtils.isBlank(param.get("code").toString())){
				response.setCode("999992");
				response.setMsg("验证码为空");
				return true;
			}
		}
		
		if("resetPassword".equals(string)){
			if(param.get("code")==null || StringUtils.isBlank(param.get("code").toString())){
				response.setCode(ExErrorCode.SMS_CODE_ERROR.getCode());
				response.setMsg(ExErrorCode.SMS_CODE_ERROR.getMsg());
				return true;
			}
			if(param.get("password")==null || StringUtils.isBlank(param.get("password").toString())){
				response.setCode(ExErrorCode.PWD_EMPTY.getCode());
				response.setMsg(ExErrorCode.PWD_EMPTY.getMsg());
				return true;
			}
			
			if(param.get("confirmPassword")==null || StringUtils.isBlank(param.get("confirmPassword").toString())){
				response.setCode(ExErrorCode.PWD_INCONFORMITY.getCode());
				response.setMsg(ExErrorCode.PWD_INCONFORMITY.getMsg());
				return true;
			}
			
			if(!param.get("password").toString().equals(param.get("confirmPassword").toString())){
				response.setCode(ExErrorCode.PWD_INCONFORMITY.getCode());
				response.setData(ExErrorCode.PWD_INCONFORMITY.getMsg());
				return true;			
			}
		}
		
		return false;
	}

}
