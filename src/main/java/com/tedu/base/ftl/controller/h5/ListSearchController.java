package com.tedu.base.ftl.controller.h5;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tedu.base.common.utils.SessionUtils;

@Controller
@RequestMapping ("/list")
public class ListSearchController {
	/**
	 * 
	 * @author zhangbinjie
	 *
	 */
	@RequestMapping(value ="/search-{type}")
	public String workplace(Model model,HttpServletRequest request, @RequestParam Map<String, String> params
	,@PathVariable String type		) { 
		
		
		request.setAttribute("type",type);
		request.setAttribute("empId",SessionUtils.getUserInfo().getEmpId());
		return "h5/listSearch";
	}	
}
