package com.tedu.base.ftl.controller.h5;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping ("/defect")
public class DefectDetailController {
	/**
	 * 
	 * @author zhangbinjie
	 *
	 */
	@RequestMapping(value = "/detail-{issueId:[\\d]+}")
	public String info(String name,Model model,HttpServletRequest request, @RequestParam Map<String, String> params,
			@PathVariable String issueId) {
		params.put("issueId", issueId);
		
		return "h5/defectdetail";
	}	
}
