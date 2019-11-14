package com.tedu.base.ftl.controller.h5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/state")
public class StateController {
	/**
	 * 
	 * @author zhizhizhi
	 *
	 */
	@RequestMapping(value = "")
	public String state(Model model) { 
		
		return "h5/state";
	}
}
