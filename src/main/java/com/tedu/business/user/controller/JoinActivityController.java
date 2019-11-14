package com.tedu.business.user.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.business.user.service.BindingTelService;
import com.tedu.business.user.service.JoinActivityService;
import com.tedu.common.error.ExErrorCode;
import com.tedu.component.SendSmsComponent;

@Controller
public class JoinActivityController {
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	
	@Resource
	private JoinActivityService joinActivityServiceimpl;
	
	/**
	 * @Title:   
	 * @Description: TODO 扫描二维码确认参加活动
	 * @author: quancong
	 * @param: @param request
	 * @param: @return      
	 * @return: FormEngineResponse 
	 * @date:   2018年8月8日 下午2:59     
	 * @throws
	 */
	@RequestMapping("/joinActivity")
	@ResponseBody
	public FormEngineResponse getTelAuthCode(@RequestBody FormEngineRequest requestObj, HttpServletRequest request){
		String result = "";
		boolean isRepeat=false;
		FormEngineResponse response = new FormEngineResponse(result);
		try {
			Map <String, Object> param = requestObj.getData();
			String joinBy = param.get("joinBy")==null?"":param.get("joinBy").toString();
			String activityId = param.get("activityId")==null?"":param.get("activityId").toString(); 
			
			if(StringUtils.isBlank(joinBy) || StringUtils.isBlank(activityId)){
				response.setCode(ExErrorCode.JOIN_OR_ACTIVITY_EMPTY.getCode());
				response.setMsg(ExErrorCode.JOIN_OR_ACTIVITY_EMPTY.getMsg());
				return response;
			}
			//判断用户是否已参加该活动，避免重复参加
			isRepeat=joinActivityServiceimpl.checkRepeatActivityJoin(joinBy,activityId);
			if(isRepeat){
				response.setMsg("用户已参加该活动,不能重复参加");
				log.info("用户已参加该活动,不能重复参加");
			}else{
				//将用户信息和活动信息存入t_activity_join表中并得到返回的id值
				String activityJoinId=joinActivityServiceimpl.insertActivityJoin(joinBy, activityId);
				//更新t_activity中参加人数的值
				joinActivityServiceimpl.updateJoinCount(activityId);
				//更新用户参加活动后的积分值
				if(!StringUtils.isBlank(activityJoinId)){
					joinActivityServiceimpl.changeIntegral(activityJoinId);
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			response.setCode(ExErrorCode.JOIN_ACTIVITY_FAIL.getCode());
			response.setMsg(ExErrorCode.JOIN_ACTIVITY_FAIL.getMsg());
		}	
		return response;
	}
	
	

}
