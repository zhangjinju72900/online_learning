package com.tedu.base.ftl.controller.h5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/defect")
public class DefectListController {
	/**
	 * 
	 * @author zhangbinjie
	 *
	 */
	@RequestMapping(value = "list")
	public String workplace(Model model) { 
		
		return "h5/defectList";
	}	
}
