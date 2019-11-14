package com.tedu.plugin.customUser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

/**
 * 在保存客户账号信息后将对应的角色关系更新到t_customer_user_role表中
 * 
 * @author quancong
 *
 */
@Service("InsertEasemob")
public class InsertEasemob implements ILogicPlugin {
	@Value("${easemob.org_name}")
	private String orgName;
	@Value("${easemob.app_name}")
	private String appName;
	@Resource
	FormService formService;

	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Resource
	private EasemobComponent easemobComponent;

	@Resource
	private CustomUserService customUserServiceImpl;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		try {
			QueryPage qp = new QueryPage();
			qp.setQueryParam("QryAllUser");
			List<Map<String, Object>> userList = formService.queryBySqlId(qp);
			String url = "http://a1.easemob.com/"+orgName+"/"+appName+"/"+"users";
			
			if(userList.size()>0) {
				  //生成环信账号
				for(Map<String, Object> userMap:userList) {
					String easemodUserName = userMap.get("easUserName")!=null?userMap.get("easUserName").toString():"";
					//如果为空则更新环信用户名密码为用户Id
					String token = easemobComponent.getToken();
					Map <String,String> headerMap =  new HashMap<String, String>();
					Map<String, Object> map = new HashMap<String, Object>();
					String userId = userMap.get("id").toString();
					if("".equals(easemodUserName)) {
						  map.put("id", userId); 
						  CustomFormModel cModel = new CustomFormModel();
						  cModel.setSqlId("UpdateEasemodUser");
						  cModel.setData(map);
						  formMapper.saveCustom(cModel);
						  String checkUserExist = this.httpRequest(url+"/"+easemodUserName);
						  //不存在则注册账号
						  if(!checkUserExist.contains("count")) {
							     easemobComponent.register(token,userId,
							    		 userId);
								customUserServiceImpl.updateEasemobUser(userId, userId);
						  }
					}
					//将该用户注册到环信控制台
					else {
						 //检查环信用户是否存在
						 String  checkUserExist = this.httpRequest( url+"/"+userId);
						 //不存在则注册账号
						 if(!checkUserExist.contains("count")) {
							 easemobComponent.register(token,userId,
									 userId);
								customUserServiceImpl.updateEasemobUser(userId, userId);
						 }
					}
				}
				/*easemobComponent.register(easemobComponent.getToken(),userId,
						userId);
				customUserServiceImpl.updateEasemobUser(userId, userId);*/
			}
		  
			} catch (Exception e) {
				log.error("新建账号时生成环信账号失败", e);
				e.printStackTrace();
			}

	}
	 /** 
     * 2.发起http请求获取返回结果 
     *  
     * @param requestUrl 请求地址 
     * @return 
     */  
    public   String httpRequest(String requestUrl) {  
    	     log.info("httpRequest"+requestUrl);
        	CloseableHttpClient httpClient = HttpClients.createDefault();
        	HttpGet httpGet = new HttpGet(requestUrl);
        	RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(180 * 1000)
    				.setConnectionRequestTimeout(180 * 1000).setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();

        	httpGet.setConfig(requestConfig);
        	httpGet.setHeader("content-type", "application/json"); //
        	httpGet.addHeader("Authorization", "Bearer "+easemobComponent.getToken());
        	InputStream inputStream = null;
    		ByteArrayOutputStream out = null;
    		try {
    			HttpResponse response = httpClient.execute(httpGet);
    			inputStream = response.getEntity().getContent();
    			out = new ByteArrayOutputStream();
    			byte[] buffer = new byte[1024];
    			int len = 0;
    			while ((len = inputStream.read(buffer)) != -1) {
    				out.write(buffer, 0, len);
    			}
    			byte[] ss = out.toByteArray();
    			String result = null;
    			result = new String(ss, "utf-8");
    			return result;
    		} catch (Exception e) {
    			e.printStackTrace();
    			return "error";
    		} finally {
    			if (null != httpClient) {
    				try {
    					httpClient.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    			/*
    			 * if(inputStream != null){ try { inputStream.close(); } catch (IOException e) {
    			 * // TODO Auto-generated catch block e.printStackTrace(); } } if(out != null){
    			 * try { out.close(); } catch (IOException e) { // TODO Auto-generated catch
    			 * block e.printStackTrace(); } }
    			 */
    		}
    }  

}
