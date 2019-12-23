package com.tedu.base.auth.login.controller;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.CustomizedToken;
import com.tedu.base.auth.LoginType;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.CustomLoginService;
import com.tedu.base.auth.login.service.LoginService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.*;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.UserLog;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.msg.mail.SendMsgService;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录类
 *
 * @author xijianguang
 */
@Controller
public class LoginController {
	@Value("${base.image.logImg}")
	private String logImg;
	@Value("${base.image.loginImg}")
	private String loginImg;
	@Value("${base.image.sysName}")
	private String sysName;
	@Value("${base.image.loginLogo}")
	private String loginLogo;

	@Value("${base.notice}")
	private String notice;

	@Value("${base.title}")
	private String baseTitle;
	@Value("${base.copyright}")
	private String copyRight;

	@Value("${base.app}")
	private String app;

	@Value("${base.ver}")
	private String ver;

	@Value("${ui.dialog.size.small}")
	private String small;
	@Value("${ui.dialog.size.medium}")
	private String medium;
	@Value("${ui.dialog.size.large}")
	private String large;

	@Value("${base.image.favicon.png}")
	private String faviconPng;

	@Value("${base.image.favicon.ico}")
	private String faviconIco;

	@Value("${base.mainFresh}")
	private String mainFresh;

	private String cid;

	@Resource
	private LoginService loginService;
	@Resource
	private FormTokenService formTokenService;
	@Resource
	private FormService formService;
	@Resource
	private FormLogService formLogService;
	@Resource
	private SendMsgService sendMsgService;

	@Resource
	private CustomLoginService customLoginService;

	// 日志记录器
	public final Logger log = Logger.getLogger(this.getClass());

	/**
	 * 登出操作
	 */
	@RequestMapping("/logOutJson")
	@ResponseBody
	public FormEngineResponse logOutJson(HttpServletRequest request, Model model) {
		FormEngineResponse formEngineResponse = new FormEngineResponse("");
		try {
			if (SessionUtils.getUserInfo() != null) {
				this.forLogOut(model);
			}
			formEngineResponse.setCode("0");
			formEngineResponse.setMsg("登出成功");
		} catch (Exception e) {
			formEngineResponse.setCode("1");
			formEngineResponse.setMsg("登出失败");
		}
		return formEngineResponse;
	}

	@RequestMapping("/logOut")
	public String logOut(HttpServletRequest request, Model model) {
		return this.forLogOut(model);
	}

	private String forLogOut(Model model) {
		model.addAttribute("baseTitle", baseTitle);
		model.addAttribute("loginImg", loginImg);
		model.addAttribute("loginLogo", loginLogo);
		model.addAttribute("notice", notice);
		model.addAttribute("copyRight", copyRight);
		model.addAttribute("faviconPng", faviconPng);
		model.addAttribute("faviconIco", faviconIco);

		// 登出记录日志
		UserLog log = new UserLog();
		log.setUiName("login");// 必填
		log.setUiTitle("登录页");
		log.setAction("logout");
		log.setUserId(SessionUtils.getUserInfo().getUserId());
		log.setEmpId(SessionUtils.getUserInfo().getEmpId());
		log.setControlName("");
		log.setControlTitle("");
		log.setPanelName("");
		log.setPanelTitle("");
		log.setFlowId(TokenUtils.genUUID());// 必填
		log.setSessionId(SessionUtils.getSession().getId().toString());
		log.setCreateTime(new Date());
		log.setCreateBy(SessionUtils.getUserInfo().getEmpId());

		formLogService.save(log);

		// 清Session
		SessionUtils.removeAll();
		return "login";
	}

	/**
	 * 验证是否需要验证码
	 */
	@RequestMapping("/getValidateStatus")
	@ResponseBody
	public FormEngineResponse getValidateStatus(HttpServletRequest request) {
		FormEngineResponse formEngineResponse = new FormEngineResponse("");
		List<UserModel> userModels = null;
		try {
			userModels = loginService.getUserInfoByName(request.getParameter("userName"));
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.SQL_PREPARE_FAILED, "登录用户查询失败");
		}
		if (userModels == null) {
			formEngineResponse.setCode("0");
			formEngineResponse.setMsg("0");
		} else if (userModels.size() > 0) {
			formEngineResponse.setCode("0");
			UserModel userModel = userModels.get(0);
			formEngineResponse.setMsg(String.valueOf(userModel.getValidate()));
		}

		return formEngineResponse;
	}

	/**
	 * 登录操作
	 *
	 * @throws Exception
	 */
	@RequestMapping("/login")
	@ResponseBody
	public FormEngineResponse toLogin(@RequestBody FormEngineRequest requestObj, HttpServletRequest request)
			throws Exception {
		return login(requestObj, request, LoginType.EMPLOYEE.toString());
	}

	@RequestMapping("/loginCustom")
	@ResponseBody
	public FormEngineResponse toLoginCustom(@RequestBody FormEngineRequest requestObj, HttpServletRequest request)
			throws Exception {
		System.err.println("1" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		initResource();
		return login(requestObj, request, LoginType.CUSTOM.toString());
	}

	@RequestMapping("/loginTeacher")
	@ResponseBody
	public FormEngineResponse toLoginTeacher(@RequestBody FormEngineRequest requestObj, HttpServletRequest request)
			throws Exception {
		System.err.println("2" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		FormEngineResponse response = new FormEngineResponse("");
		initResource();
		Map<String, Object> param = requestObj.getData();
		Map qyMap = new HashMap();
		QueryPage qp = new QueryPage();
		boolean flag = false;
		if (param.get("userName") != null) {
			String userName = param.get("userName").toString();
			qp.setParamsByMap(qyMap);
			qp.getData().put("userName", userName);
			qp.setQueryParam("emp/QryRoleByUserName");
			
			List<Map<String, Object>> roleList = formService.queryBySqlId(qp);
			if (roleList.size() > 0) {
				String[] roleIds = roleList.get(0).get("roleId").toString().split(",");
				for (String id : roleIds) {
					// 11为教师角色 不包含则不验证直接反馈结果
					if ("11".equals(id)) {
						flag = true;
					}
				}

			}
			
		}
		if (!flag) {
			response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
			response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
			return response;
		}else {
			return login(requestObj, request, LoginType.TEACHER.toString());
		}
		
	}

	@RequestMapping("/loginAssistant")
	@ResponseBody
	public FormEngineResponse toLoginAssistant(@RequestBody FormEngineRequest requestObj, HttpServletRequest request)
			throws Exception {
		initResource();
		return login(requestObj, request, LoginType.EMPLOYEE.toString());
	}

	private FormEngineResponse login(FormEngineRequest requestObj, HttpServletRequest request, String type) {
		FormEngineResponse response = new FormEngineResponse("");
		Map<String, Object> param = requestObj.getData();

		System.out.println(param.toString());
		String openId = "";
		if (param.get("openid") != null) {
			openId = param.get("openid").toString();
		}

		System.err.println("3" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));

		if (param.get("userName") != null) {
			String password = param.get("password").toString();
			String userName = param.get("userName").toString();
			String validateCode = ObjectUtils.toString(param.get("validateCode"));
			// 判断是否为有教师角色

			UserLog log = new UserLog();
			log.setUiName("mainFrame");
			log.setUiTitle("主页");
			log.setAction("login" + type);
			String errorReason = "";

			Session s = SessionUtils.getSession();
			s.setAttribute("validateCode", validateCode);

			CustomizedToken token = new CustomizedToken(userName, password, type);
			Subject subject = SecurityUtils.getSubject();
			subject.hasRole("");

			System.err.println("4" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));

			try {
				Map<Object, Object> dataMap = new HashMap<Object, Object>();

				subject.login(token);

				System.err.println("5" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));

				SessionUtils.setAttrbute("ctx", request.getContextPath());
				s.setAttribute("ip", request.getLocalAddr());
				s.setAttribute("port", request.getLocalPort());
				response.setCode("0");
				response.setMsg("登录成功");
				UserModel user = SessionUtils.getUserInfo();
				log.setUserId(user.getUserId());
				log.setEmpId(user.getEmpId());
				log.setSessionId(s.getId().toString());
				log.setCreateBy(user.getEmpId());
				log.setExecResult("登录成功");
//	            dataMap.put("userId", userId + "");

				// 微信登录，绑定openId

				System.err.println("6" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));

				if (type.equals(LoginType.CUSTOM.toString()) && !openId.equals("")) {

					Map<String, Object> map = new HashMap<>();
					CustomFormModel formModel = new CustomFormModel();
					map.put("openId", openId);
					map.put("id", user.getUserId());
					formModel.setData(map);
					formModel.setSqlId("UpdateOpenId");
					formService.saveCustom(formModel);
					System.err.println("7" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
				}
				try {
					PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
					PropertyDescriptor[] descriptors = propertyUtilsBean
							.getPropertyDescriptors(SessionUtils.getUserInfo());
					for (int i = 0; i < descriptors.length; i++) {
						String name = descriptors[i].getName();
						if (!"class".equals(name) && !"password".equals(name) && !"roleName".equals(name)
								&& !"empId_foreign".equals(name)) {
							dataMap.put(name, propertyUtilsBean.getNestedProperty(SessionUtils.getUserInfo(), name));
						}
					}
					if(propertyUtilsBean.getNestedProperty(SessionUtils.getUserInfo(), "name")==null) {
						dataMap.put("name", propertyUtilsBean.getNestedProperty(SessionUtils.getUserInfo(), "nickName"));
					}
					System.err.println("8" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));

					if (StringUtils.isNotBlank(SessionUtils.getUserInfo().getValidateCode())
							&& StringUtils.isNotBlank(SessionUtils.getUserInfo().getCheckResult())) {
						dataMap.put("easemodUsername", SessionUtils.getUserInfo().getValidateCode());
						dataMap.put("easemodPassword", SessionUtils.getUserInfo().getCheckResult());
					} else {
						dataMap.put("easemodUsername", "");
						dataMap.put("easemodPassword", "");
					}
					dataMap.put("sessionId", SessionUtils.getSessionId());
					customLoginService.updateLoginData(SessionUtils.getUserInfo().getUserId());

					System.err.println("9" + DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.err.println(dataMap.toString());
				response.setData(dataMap);
			} catch (UnknownAccountException e) {
				response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
				response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
				errorReason = "账号不存在";
			} catch (UnauthenticatedException e) {
				response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
				response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
				errorReason = "用户名无角色";
			} catch (CredentialsException e) {
				response.setCode(ErrorCode.LOGIN_CODE_ERROR.getCode());
				response.setMsg(ErrorCode.LOGIN_CODE_ERROR.getMsg());
				errorReason = "验证码错误";
			} catch (DisabledAccountException e) {
				String[] megs = e.getMessage().split("_");
//	                Long empId = Long.parseLong(megs[2]);
				log.setUserId(Long.parseLong(megs[0]));
				log.setEmpId(Long.parseLong(megs[0]));
				log.setCreateBy(Long.parseLong(megs[1]));
				response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
				response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
				errorReason = "用户已被冻结";
			} catch (UnsupportedTokenException e) {
				response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
				response.setMsg("用户名或密码错误");
				// 密码错误
				errorReason = "密码错误";
				try {
					// 延迟一秒返回结果
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					throw new ServiceException(ErrorCode.UNKNOWN, "延迟登录异常");
				}

			} catch (ExcessiveAttemptsException e) {
				response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
				response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());

				// 密码错误,次数太多，出验证码
				errorReason = "密码错误";
				try {
					// 延迟一秒返回结果
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					throw new ServiceException(ErrorCode.UNKNOWN, "延迟登录异常");
				}

			} 
			catch (AuthenticationException e) {
				response.setCode(ErrorCode.UNKNOWN.getCode());
				response.setMsg("账号角色数据异常");
			}catch (Exception e) {
				response.setCode(ErrorCode.UNKNOWN.getCode());
				response.setMsg(e.getMessage());
				errorReason = e.getMessage();
				e.printStackTrace();
			}
			// 登录成功 记录用户日志
			log.setControlName("");
			log.setControlTitle("");
			log.setPanelName("");
			log.setPanelTitle("");
			log.setFlowId(TokenUtils.genUUID());
			log.setCreateTime(new Date());
			if (log.getExecResult() == null) {
				log.setExecResult("登录失败");
			}
			log.setErrorReason(errorReason);

			String ip = "";
			if (request.getHeader("x-forwarded-for") == null) {
				ip = request.getRemoteAddr();
			} else {
				ip = request.getHeader("x-forwarded-for");
			}

			log.setClientIp(ip);
			formLogService.save(log);

		} else {
			// 根据openId,登录

			response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
			response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());

		}
		return response;

	}

	/**
	 * 跳转首页
	 */
	@RequestMapping("")
	public String welcome(HttpServletRequest request, Model model) {
		return index(request, model, "");
	}

	@RequestMapping("/forCustom")
	public String welcomeforCustom(HttpServletRequest request, Model model) {
		return index(request, model, "forCustom");
	}

	private String index(HttpServletRequest request, Model model, String type) {

		ModelAndView modelAndView = new ModelAndView();
		UserModel userModel = (UserModel) SessionUtils.getAttrbute(ConstantUtil.USER_INFO);
		model.addAttribute("baseTitle", baseTitle);
		// TODO 菜单类型从数据字典中动态获得
		if (userModel != null) {
			List<Map<String, String>> allMenuList = new ArrayList<Map<String, String>>();
			if (SessionUtils.getUserInfo().isAdminRole()) {
				allMenuList = loginService.getAllAuthorization();
			} else {
				// 学生登录,userName=student
				if ("forCustom".equals(type)) {
//                    allMenuList = loginService.getAuthorization("student", "menu");
					allMenuList = customLoginService.getAuthorization("student", "menu");
				} else {
					allMenuList = customLoginService.getAuthorization(userModel.getUserName(), "menu");
				}
			}
	
			List<Map<String, String>> mainMenuList = allMenuList
					.stream().filter(stringStringMap -> !stringStringMap.isEmpty()
							&& !stringStringMap.get("type").isEmpty() && stringStringMap.get("type").equals("mainMenu"))
					.collect(Collectors.toList());
			List<Map<String, String>> quickMenuList = allMenuList.stream()
					.filter(stringStringMap -> !stringStringMap.isEmpty() && !stringStringMap.get("type").isEmpty()
							&& stringStringMap.get("type").equals("quickMenu") && stringStringMap.get("parent") != null)
					.collect(Collectors.toList());
			List<Map<String, String>> index = allMenuList.stream()
					.filter(stringStringMap -> !stringStringMap.isEmpty() && !stringStringMap.get("type").isEmpty()
							&& stringStringMap.get("type").equals("index") && stringStringMap.get("parent") != null)
					.collect(Collectors.toList());

			List<String> menuParentList = new ArrayList<String>();

			mainMenuList.stream().forEach(stringStringMap -> menuParentList.add(stringStringMap.get("parentName")));

			List<String> menuParentDistinct = menuParentList.stream().distinct().collect(Collectors.toList());

			// 倒序排列快捷菜单
			Collections.sort(quickMenuList, new Comparator<Map<String, String>>() {
				public int compare(Map<String, String> o1, Map<String, String> o2) {
					Integer seq1 = Integer.valueOf(String.valueOf(o1.get("seq")));
					Integer seq2 = Integer.valueOf(String.valueOf(o2.get("seq")));
					return seq2.compareTo(seq1);
				}
			});

			if (!index.isEmpty()) {
				Map<String, String> indexResource = index.get(0);
				model.addAttribute("indexTarget", indexResource.get("target"));

				model.addAttribute("indexName", indexResource.get("name"));
				model.addAttribute("indexUrl", indexResource.get("url"));
				model.addAttribute("indexMenuCode", indexResource.get("code"));
			} else {
				model.addAttribute("indexName", "");
				model.addAttribute("indexUrl", "");
				model.addAttribute("indexMenuCode", "");
			}
			model.addAttribute("menuList", mainMenuList);
			SessionUtils.setAttrbute("mainMenuList",mainMenuList);
			model.addAttribute("quickMenuList", quickMenuList);

			model.addAttribute("menuParentList", menuParentDistinct);

			model.addAttribute("windowSize", large);

			model.addAttribute("logImg", logImg);
			model.addAttribute("sysName", sysName);
			model.addAttribute("mainFresh", mainFresh);
			initResource();
			FormUtil.checkOutSavedRequest(request, model);
			// 登陆成功跳转主页
			return "mainFrame";
		} else {
			model.addAttribute("loginImg", loginImg);
			model.addAttribute("loginLogo", loginLogo);

			model.addAttribute("notice", notice);
			model.addAttribute("cid", AviatorEvaluator.execute("Guid()").toString());
			model.addAttribute("app", app);
			model.addAttribute("ver", ver);
			model.addAttribute("copyRight", copyRight);
			model.addAttribute("faviconPng", faviconPng);
			model.addAttribute("faviconIco", faviconIco);
			model.addAttribute("date", DateUtils.getDateToStr("yyyyMMdd", new Date()));
			// 交换秘钥HMAC
			if ("".equals(type))
				return "login";
			else
				return "loginForCustom";
		}

	}

	/**
	 * token拦截器需要将所有可访问资源初始在内存中 ShiroFilerChainManager中的查询不正确。 暂用此方式代替
	 */
	public void initResource() {
		// load accessible url
		QueryPage queryPage = new QueryPage();
		queryPage.setQueryParam("ACLU");// 所有当前用户可访问的url资源：满足授权的和不需授权的url
		List<Map<String, Object>> listUrl = formService.queryBySqlId(queryPage);
//    	//不限定权限的资源
		SessionUtils.setAccessibleUrl(listUrl);
		// load accessible control list
		try {
			queryPage = new QueryPage();
			queryPage.setQueryParam("ACL");
			List<Map<String, Object>> controlList = formService.queryBySqlId(queryPage);
			if (controlList != null) {
				Map<String, String> userControlMap = new HashMap<>();
				controlList.forEach(
						e -> userControlMap.put(ObjectUtils.toString(e.get("url")), ObjectUtils.toString(e.get("id"))));// "ui.panel.controlName"
				SessionUtils.setACL(userControlMap);
				FormLogger.logBegin(String.format("装载用户可访问组件{%s}个", controlList.size()));
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.ACL_LOAD_FAILED, e.getMessage());
		}
	}

	@RequestMapping("/loginAn")
	@ResponseBody
	public FormEngineResponse toLoginAn(@RequestBody FormEngineRequest requestObj, HttpServletRequest request)
			throws Exception {
		FormEngineResponse response = new FormEngineResponse("");
		if (SessionUtils.getUserInfo() == null) {
			Map<Object, Object> dataMap = new HashMap<Object, Object>();
			UserModel user = new UserModel();
			user.setUserId(-1);
			user.setUserName("admin");
			SessionUtils.setAttrbute(ConstantUtil.USER_INFO, user);
			initAnResource();
			dataMap.put("sessionId", SessionUtils.getSessionId());
			response.setData(dataMap);

		}
		return response;
//      return login(requestObj, request, LoginType.EMPLOYEE.toString());
	}

	public void initAnResource() {
		// load accessible url
		QueryPage queryPage = new QueryPage();
		queryPage.setQueryParam("ACLU");// 所有当前用户可访问的url资源：满足授权的和不需授权的url
		List<Map<String, Object>> listUrl = formService.queryBySqlId(queryPage);
//    	//不限定权限的资源
		SessionUtils.setAccessibleUrl(listUrl);
		// load accessible control list
		try {
			queryPage = new QueryPage();
			queryPage.setQueryParam("ACL");
			List<Map<String, Object>> controlList = formService.queryBySqlId(queryPage);
			if (controlList != null) {
				Map<String, String> userControlMap = new HashMap<>();
				controlList.forEach(
						e -> userControlMap.put(ObjectUtils.toString(e.get("url")), ObjectUtils.toString(e.get("id"))));// "ui.panel.controlName"
				SessionUtils.setACL(userControlMap);
				FormLogger.logBegin(String.format("装载用户可访问组件{%s}个", controlList.size()));
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.ACL_LOAD_FAILED, e.getMessage());
		}
	}

}
