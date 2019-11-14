package com.tedu.base.ftl.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.service.FormService;
import com.tedu.plugin.workflow.FormSDK;
/**
 * 
 * @author chenjun
 *
 */
@Controller
@RequestMapping ("/content")
public class contentController {
	@Resource
	FormService formService;
	@Resource
	FormSDK formSDK;
	@RequestMapping(value = "/index")
	public String workplace(Model model) {
		
		return "pc/content";
	}
	
	@RequestMapping(value = "/scorm")
	public String scorm(Model model) {
		
		return "pc/scorm";
	}
	
	public List<Map<String, Object>> getParams(String url, String eq, String area) {
		QueryPage queryPage = new QueryPage();
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put(eq, area);
		queryPage.setParamsByMap(mapParam);
		queryPage.setQueryParam(url);
		// 查询人员并替换新旧表单相关人员信息(姓名)
		List<Map<String, Object>> list = formService.queryBySqlId(queryPage);
		return list;
	}
}
