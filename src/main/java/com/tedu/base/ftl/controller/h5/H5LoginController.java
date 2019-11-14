package com.tedu.base.ftl.controller.h5;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.common.utils.SessionUtils;

@Controller
@RequestMapping ("/login")
public class H5LoginController {
	/**
	 * 
	 * @author zhangbinjie
	 *
	 */
	@Value("${base.app}")
    private String app;

    @Value("${base.ver}")
    private String ver;
    
    private String cid;
	@RequestMapping(value = "h5")
	public String workplace(Model model,HttpServletRequest request
			) { 
		//empId = SessionUtils.getUserInfo().getEmpId();
		//System.out.println(empId);
		//request.setAttribute("empId",empId);
		model.addAttribute("cid", AviatorEvaluator.execute("Guid()").toString());
        model.addAttribute("app", app);
        model.addAttribute("ver", ver);
		
		return "h5/h5login";
	}	
}
