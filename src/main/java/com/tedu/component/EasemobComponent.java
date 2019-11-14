package com.tedu.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.business.easemob.service.EasemobRecordService;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.common.constant.EasemobSendObjectEnum;
import com.tedu.common.constant.EasemobSendTypeEnum;

import net.sf.json.JSONObject;

/**
 * @ClassName: EasemobComponent
 * @Description:TODO环信相关组件
 * @author: qun
 * @date: 2018年8月20日 下午6:07:16
 *
 */
@Component
public class EasemobComponent {

	@Resource
	private CustomUserService customUserServiceImpl;

	@Resource
	private EasemobRecordService easemobRecordServiceImpl;

	public String addr = "https://a1.easemob.com/";

	@Value("${easemob.client_id}")
	private String CLIENT_ID;

	@Value("${easemob.client_secret}")
	private String CLIENT_SECRET;

	@Value("${easemob.org_name}")
	private String ORG_NAME;

	@Value("${easemob.app_name}")
	private String APP_NAME;
	@Resource
	private FormMapper formMapper;
	@Resource
	private FormService formService;
	/**
	 * @Title: sendNotice @Description: TODO发送消息 @author: qun @param: @param
	 * noticeId @param: @param roleIdAr @param: @param regionIdAr @param: @param
	 * school @param: @param title @param: @param context @return: void @date:
	 * 2018年8月21日 上午11:08:58 @throws
	 */
	public void sendNotice(String noticeId, String[] roleIdAr, String[] regionIdAr, List<Map<String, Object>> school,
			String title, String context) {

		StringBuffer roles = new StringBuffer();
		if (roleIdAr != null) {
			for (int i = 0; i < roleIdAr.length; i++) {
				if (StringUtils.isNotBlank(roleIdAr[i])) {
					roles.append("'").append(roleIdAr[i].trim()).append("'");
					if (i != roleIdAr.length - 1) {
						roles.append(",");
					}
				}
			}
		}

		StringBuffer regions = new StringBuffer();
		if (regionIdAr != null) {
			for (int i = 0; i < regionIdAr.length; i++) {
				if (StringUtils.isNotBlank(regionIdAr[i])) {
					regions.append("'").append(regionIdAr[i].trim()).append("'");
					if (i != regionIdAr.length - 1) {
						regions.append(",");
					}
				}
			}
		}

		StringBuffer schools = new StringBuffer();
		if (school != null) {
			for (int i = 0; i < school.size(); i++) {
				String schId = school.get(i).get("schoolId") == null ? "" : school.get(i).get("schoolId").toString();
				if (StringUtils.isNotBlank(schId)) {
					schools.append("'").append(school.get(i).get("schoolId").toString().trim()).append("'");
					if (i != school.size() - 1) {
						schools.append(",");
					}
				}
			}
		}

		List<String> uList = null;
		if (StringUtils.isNotBlank(schools.toString())) {
			uList = customUserServiceImpl.selectUserByRolesAndSch(roles.toString(), schools.toString());
		} else {
			uList = customUserServiceImpl.selectUserByRolesAndRegions(roles.toString(), regions.toString());
		}

		if (uList != null && uList.size() > 0) {
			String token = getToken();
			List<List<String>> subList = Lists.partition(uList, 20);
			for (int i = 0; i < subList.size(); i++) {
				String[] array = subList.get(i).toArray(new String[subList.get(i).size()]);
				String result = sendMessage(token, EasemobSendObjectEnum.USERS.getCode(), array,
						EasemobSendTypeEnum.TXT.getCode(), context, SessionUtils.getUserInfo().getValidateCode());

				easemobRecordServiceImpl.executeResult(result, EasemobSendObjectEnum.USERS.getCode(),
						EasemobSendTypeEnum.TXT.getCode(), title, context, array);
			}
			CustomFormModel cModel = new CustomFormModel();
			for (int j = 0; j < uList.size(); j++) {
				Map messMap = new HashMap();
				long userId = SessionUtils.getUserInfo().getUserId();
				//根据环信id查询用户Id
				QueryPage qp = new QueryPage();
				qp.getData().put("receiverId", uList.get(j));
				qp.setQueryParam("khAdmin/noticeManage/QryUserByEase");
				List<Map<String, Object>> userList =  formService.queryBySqlId(qp);
				if(userList.size()>0) {
					messMap.put("userId", userId);
					messMap.put("receiverId", userList.get(0).get("id").toString());
					messMap.put("baseId", noticeId);
					cModel.setData(messMap);
					cModel.setSqlId("khAdmin/noticeManage/insertMessageRecord");
					formMapper.saveCustom(cModel);
				}
			}
		}

	}

	/**
	 * @Title: getToken @Description: TODO获取token @author:
	 * qun @param: @return @return: String @date: 2018年8月20日 下午6:39:20 @throws
	 */
	public String getToken() {

		JSONObject paramJson = new JSONObject();
		paramJson.put("grant_type", "client_credentials");
		paramJson.put("client_id", CLIENT_ID);
		paramJson.put("client_secret", CLIENT_SECRET);
		Map<String, String> headers = null;
		String body = doPostForJson(headers, getUrl() + "/token", paramJson.toString());
		return JSONObject.fromObject(body).getString("access_token");
	}

	/**
	 * 
	 * @param noticeId
	 * @param classes
	 * @param title
	 * @param context
	 */
	public void sendNotice1(String noticeId, List<String> classes, String title, String context) {

		StringBuffer classess = new StringBuffer();
		if (classes != null) {
			for (int i = 0; i < classes.size(); i++) {
				String schId = classes.get(i);
				if (StringUtils.isNotBlank(schId)) {
					classess.append("'").append(schId).append("'");
					if (i != classes.size() - 1) {
						classess.append(",");
					}
				}
			}
		}

		List<String> uList = null;
		uList = customUserServiceImpl.selectUserByclasses(classess.toString());

		if (uList != null && uList.size() > 0) {
			String token = getToken();
			List<List<String>> subList = Lists.partition(uList, 20);
			for (int i = 0; i < subList.size(); i++) {
				String[] array = subList.get(i).toArray(new String[subList.get(i).size()]);
				String result = sendMessage(token, EasemobSendObjectEnum.USERS.getCode(), array,
						EasemobSendTypeEnum.TXT.getCode(), context, SessionUtils.getUserInfo().getValidateCode());

				easemobRecordServiceImpl.executeResult(result, EasemobSendObjectEnum.USERS.getCode(),
						EasemobSendTypeEnum.TXT.getCode(), title, context, array);
			}
		}
	}
	/**
	 * 
	 * @param noticeId
	 * @param classes
	 * @param title
	 * @param context
	 */
	public void sendNotice(String noticeId,String [] userId, String title, String context) {
			String token = getToken();
			String result = sendMessage(token, EasemobSendObjectEnum.USERS.getCode(), userId,
					EasemobSendTypeEnum.TXT.getCode(), context, SessionUtils.getUserInfo().getValidateCode());

			easemobRecordServiceImpl.executeResult(result, EasemobSendObjectEnum.USERS.getCode(),
					EasemobSendTypeEnum.TXT.getCode(), title, context, userId);
	}
	/**
	 * @Title: register授权注册 @Description: TODO @author: qun @param: @param
	 * token @param: @param username @param: @param
	 * password @param: @return @return: String @date: 2018年8月20日 下午6:46:19 @throws
	 */
	public String register(String token, String username, String password) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		JSONObject paramJson = new JSONObject();
		paramJson.put("username", username);
		paramJson.put("password", password);
		String body = doPostForJson(headers, getUrl() + "/users", paramJson.toString());
		return body;
	}

	/**
	 * @Title: sendMessage @Description: TODO发送消息 @author: qun @param: @param
	 * token @param: @param targetType @param: @param target @param: @param
	 * type @param: @param message @param: @param from @param: @return @return:
	 * String @date: 2018年8月20日 下午6:57:53 @throws
	 */
	public String sendMessage(String token, String targetType, String[] target, String type, String message,
			String from) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + token);
		JSONObject paramJson = new JSONObject();
		paramJson.put("target_type", targetType);
		paramJson.put("target", target);

		JSONObject msg = new JSONObject();
		msg.put("type", type);
		msg.put("msg", message);

		paramJson.put("msg", msg);
		paramJson.put("from", from);
		String body = doPostForJson(headers, getUrl() + "/messages", paramJson.toString());

		return body;
	}

	private String getUrl() {
		return addr + ORG_NAME + "/" + APP_NAME;
	}

	public static String doPostForJson(Map<String, String> headers, String url, String jsonParams) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(180 * 1000)
				.setConnectionRequestTimeout(180 * 1000).setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();

		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", "application/json"); //
		InputStream inputStream = null;
		ByteArrayOutputStream out = null;
		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				httpPost.addHeader(key, headers.get(key));
			}
		}
		try {
			httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
//            System.out.println("request parameters" + EntityUtils.toString(httpPost.getEntity()));
			HttpResponse response = httpClient.execute(httpPost);
//            System.out.println(" code:"+response.getStatusLine().getStatusCode());
//            System.out.println("doPostForInfobipUnsub response"+response.getStatusLine().toString());
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
			return null;
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
