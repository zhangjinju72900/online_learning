package com.tedu.base.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.CustomLoginService;
import com.tedu.base.auth.login.service.LoginService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.service.FormService;

/**
 * 登录后授权
 *
 * @author xijianguang
 */
public class LoginRealmForCustom extends AuthorizingRealm {

//    @Resource
//    private LoginService loginService;

    @Resource
    private FormService formService;
    
    @Resource
    private CustomLoginService customLoginService;
    
    /**
     * 授权
     *
     * @author xijianguang
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //用户名
        if (!principals.fromRealm(getName()).iterator().hasNext())
            return null;
        String username = (String) principals.fromRealm(getName()).iterator().next();
        //根据用户名来添加相应的权限和角色
        if (!StringUtils.isEmpty(username)) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//            List<Map<String, String>> listMap = loginService.getAuthorization(username, "");
            Set<String> per = new HashSet<String>();
//            for (Map<String, String> m : listMap) {
//                if (m != null && !StringUtils.isEmpty(m.get("name")))
//                    per.add(m.get("name"));
//            }
            info.addStringPermissions(per);
            return info;
        }
        return null;
    }


    /**
     * 验证登录j
     *
     * @author xijianguang
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        //令牌——基于用户名和密码的令牌
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        //令牌中可以取出用户名  
        String accountName = token.getUsername();
        //让shiro框架去验证账号密码
        List<UserModel> userModels = null;

        String errorReason = "";
        Session s = SessionUtils.getSession();
        
        boolean result = false;

        try {
        	userModels = customLoginService.getUserInfoByName(token.getUsername());
        } catch (Exception e) {
            errorReason = "数据库查询用户失败";
            e.printStackTrace();
            throw new ServiceException(ErrorCode.SQL_PREPARE_FAILED, "登录用户查询失败");
        }
        if (userModels == null) {
            //用户不存在
            throw new UnknownAccountException("账号异常");
        } else if (userModels != null && userModels.size() > 1) {
            //有重复用户名的异常数据，不让登录
            throw new AccountException("账号异常");
        } else if (userModels != null && userModels.size() == 1) {
            UserModel userModel = userModels.get(0);
            if (userModel.getRoleName() == null || userModel.getRoleName().equals("")) {
                //无授权角色不让登录
                throw new UnauthenticatedException("账号角色数据异常");
            } else {
            	userModel.setUserName(token.getUsername());
                String passWord = MD5Util.MD5Encode(new String(token.getPassword())).toUpperCase();
                if (!StringUtils.isEmpty(userModel)) {

                    if (userModel.getPassword() == null || userModel.getPassword().equals("")) {
                        //空密码不让登录
                        throw new DisabledAccountException(userModel.getUserId()+"_密码为空");
                    } else {

                        if (!s.getAttribute("validateCode").equals("")) {
                            if ((userModel != null && userModel.getValidate() == 1
                                    && !((String) s.getAttribute(ConstantUtil.RANDOMCODEKEY)).equalsIgnoreCase(s.getAttribute("validateCode").toString()))
                                    || (userModel != null && userModel.getValidate() == 1 && StringUtils.isEmpty(s.getAttribute("validateCode").toString()))) {
                                //验证码错误
                                throw new CredentialsException();
                            }
                        } else {

                            String userPwd = MD5Util.MD5Encode(userModel.getPassword()).toUpperCase();
                            String saltedPwd = "";
                            if (passWord.equals(userPwd) && !userModel.getPassword().contains(PasswordUtil.ALGORITHM_NAME_STR)) {
                                //首次登陆如果是明文的密码，允许登录并更新数据库密码
                                String salt = AviatorEvaluator.execute("Guid()").toString();
                                saltedPwd = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, salt, passWord);
                                UserModel user = new UserModel();
                                user.setUserId(userModel.getUserId());
                                user.setSalt(salt);
                                user.setPassword(saltedPwd);
                                customLoginService.updateCustomPwd(user);
                                
                                s.setAttribute(ConstantUtil.USER_INFO, setRole(userModel));
                                return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());
                            } else {
                                saltedPwd = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, userModel.getSalt(), passWord);
                                String status = "enable";
                                UserModel u = new UserModel();
                                u.setUserName(userModel.getUserName());
                                if (saltedPwd.equals(userModel.getPassword())) {
                                    if (!status.equals(userModel.getStatus())) {
                                        //用户禁用
                                        throw new DisabledAccountException("禁用用户_" + userModel.getUserId() + "_" + userModel.getEmpId());
                                    }
                                } else if (!saltedPwd.equals(userModel.getPassword())) {
                                    u.setWrongCount(userModel.getWrongCount() + 1);
                                    int random = (int) Math.random() + 3;
                                    if (userModel.getWrongCount() >= random && userModel.getValidate() == 0) {
                                        u.setValidate(1);
//                                        loginService.updateWrongCount(u);
                                        throw new ExcessiveAttemptsException();
                                    } else {
                                        u.setValidate(0);
//                                        loginService.updateWrongCount(u);
                                        throw new UnsupportedTokenException();
                                    }
                                }
                            }
                        }

                    }
                }
            }
            
            s.setAttribute(ConstantUtil.USER_INFO, setRole(userModel));
        } else {
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());
    }


	private UserModel setRole(UserModel userModel) {
		userModel.setAuthType("2");
		if(org.apache.commons.lang3.StringUtils.isNotBlank(userModel.getVersion())){
			for (String roleId : userModel.getVersion().split(",")) {
				if("1".equals(roleId)){
					userModel.setAuthType("1");//返回app代表管理员
					break;
				}
			}
		}
		return userModel;
	}

}