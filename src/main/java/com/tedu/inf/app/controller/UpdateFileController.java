package com.tedu.inf.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.common.constant.AppUpdateFileVersionEnum;
import com.tedu.common.error.ExErrorCode;
import com.tedu.inf.app.service.UpdateFileService;
import com.tedu.inf.app.vo.UpdateFileVo;

@Controller
public class UpdateFileController {
	
	@Resource
	private UpdateFileService updateFileServiceImpl;
	
	@RequestMapping("/updateAppFile")
	@ResponseBody
	public FormEngineResponse updateAppFile(@RequestBody FormEngineRequest requestObj, HttpServletRequest request) throws IOException{
		
		Map <String, Object> param = requestObj.getData();

		FormEngineResponse response = new FormEngineResponse("");
		
		Integer type = param.get("type")==null?0:Integer.parseInt(param.get("type").toString());
		
		Long versionCode = param.get("versionCode")==null?0:Long.parseLong(param.get("versionCode").toString());
		
		UpdateFileVo vo = new UpdateFileVo();
		
		if(AppUpdateFileVersionEnum.APP_START_PAGE.getCode() == type){
			vo = updateFileServiceImpl.selectAppStartPage(versionCode, vo);
		}else if(AppUpdateFileVersionEnum.TEACHING_GUIDE_PAGE.getCode() == type){
			vo = updateFileServiceImpl.selectTeachingGuidePage(versionCode, vo);
		}else{
			response.setCode(ExErrorCode.APP_UPDATE_FILE_PARAM_ERROR.getCode());
			response.setMsg(ExErrorCode.APP_UPDATE_FILE_PARAM_ERROR.getMsg());
		}
		response.setData(vo);
		return response;
		
	}
	
}
