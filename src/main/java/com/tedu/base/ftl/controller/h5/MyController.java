package com.tedu.base.ftl.controller.h5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tedu.base.auth.login.controller.LoginController;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
/**
 * 
 * @author zhizhizhi
 *
 */
@Controller
@RequestMapping ("/my")
public class MyController {
	
    @Resource
    private FormService formService;
	
	@RequestMapping(value = "")
	public String my(String name,Model model,HttpServletRequest request,
			Long empId) { 
		empId = SessionUtils.getUserInfo().getEmpId();
		Long id = SessionUtils.getUserInfo().getUserId();
		name = SessionUtils.getUserInfo().getUserName();
		String empName = SessionUtils.getUserInfo().getEmpName();
		String authType =SessionUtils.getUserInfo().getAuthType();
		String status = SessionUtils.getUserInfo().getStatus();
		System.out.println(empId);
		System.out.println(id);
		System.out.println(name);
		System.out.println(empName);
		System.out.println(authType);
		System.out.println(status);
		initResource();
		request.setAttribute("empId",empId);
		request.setAttribute("id",id);
		request.setAttribute("name",name);
		request.setAttribute("empName",empName);
		request.setAttribute("authType",authType);
		request.setAttribute("status",status);
		return "h5/my";
	}
	
	private void initResource(){
        //load accessible url
        QueryPage queryPage = new QueryPage();
        queryPage.setQueryParam("ACLU");//所有当前用户可访问的url资源：满足授权的和不需授权的url
        List<Map<String, Object>> listUrl = formService.queryBySqlId(queryPage);
//    	//不限定权限的资源
        SessionUtils.setAccessibleUrl(listUrl);
        //load accessible control list
        try {
            queryPage = new QueryPage();
            queryPage.setQueryParam("ACL");
            List<Map<String, Object>> controlList = formService.queryBySqlId(queryPage);
            if (controlList != null) {
                Map<String, String> userControlMap = new HashMap<>();
                controlList.forEach(e -> userControlMap.put(ObjectUtils.toString(e.get("url")), ObjectUtils.toString(e.get("id"))));//"ui.panel.controlName"
                SessionUtils.setACL(userControlMap);
                FormLogger.logBegin(String.format("装载用户可访问组件{%s}个", controlList.size()));
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.ACL_LOAD_FAILED, e.getMessage());
        }
	}
}
