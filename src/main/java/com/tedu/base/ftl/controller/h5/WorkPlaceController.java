package com.tedu.base.ftl.controller.h5;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.service.FormService;
import com.tedu.plugin.workflow.FormSDK;

/**
 * 
 * @author zhizhizhi
 *
 */
@Controller
@RequestMapping ("/workplace")
public class WorkPlaceController {


	@RequestMapping(value = "")
	public String workplace(String name,Model model,HttpServletRequest request, @RequestParam Map<String, String> params,
			Long empId) { 
		empId = SessionUtils.getUserInfo().getEmpId();
		
		
		request.setAttribute("empId",empId);
		return "h5/workPlace";
	}

	
}
