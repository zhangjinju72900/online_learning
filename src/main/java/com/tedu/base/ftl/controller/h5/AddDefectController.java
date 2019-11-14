package com.tedu.base.ftl.controller.h5;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tedu.base.common.utils.SessionUtils;

@Controller
@RequestMapping ("/add")
public class AddDefectController {
	/**
	 * 
	 * @author zhangbinjie
	 *
	 */
	@RequestMapping(value = "defect")
	public String workplace(Model model,HttpServletRequest request,
			Long empId) { 
		empId = SessionUtils.getUserInfo().getEmpId();
		request.setAttribute("empId",empId);
		return "h5/addDefect";
	}	
}
