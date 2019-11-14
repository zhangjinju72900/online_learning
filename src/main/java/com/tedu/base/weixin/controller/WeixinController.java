package com.tedu.base.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.JsonObject;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.jce.SHA1;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.task.SpringUtils;
import com.tedu.base.weixin.util.WeiXinParamesUtil;
import com.tedu.base.weixin.util.WeiXinUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
@Controller
@RequestMapping("/wx")
public class WeixinController {
    private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);
    
    @Resource
	FormService formService;
    
    FormMapper formMapper = SpringUtils.getBean("simpleDao");

    
    
    @RequestMapping("/getWXConfigSignature")
    public void getWXConfigSignature(@RequestBody FormEngineRequest requestObj,HttpServletResponse response,HttpServletRequest request) {
    	PrintWriter pw = null;
		try {
			Map <String, Object> param = requestObj.getData();
			String url = param.get("url").toString();
			pw = response.getWriter();
			String noncestr =WeiXinUtil.getRandomStr(12);//随机字符串
	    	String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
	    	String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.appId, WeiXinParamesUtil.appSecret)
					.getToken();
	    	String ticket = WeiXinUtil.JsapiTicket(accessToken);
	    
	    	//5、将参数排序并拼接字符串
	    	String str = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
	    	//第四部:对第三步的字符串进行SHA1加密，得到签名,并返回结果

	    	//6、将字符串进行sha1加密
	    	String signature = WeiXinUtil.getSha1(str);
	    	Map<String,String> map=new HashMap();
	    	map.put("timestamp",timestamp);
	    	map.put("appId",WeiXinParamesUtil.appId);
	    	map.put("accessToken",accessToken);
	    	map.put("ticket",ticket.toString());
	    	map.put("noncestr",noncestr);
	    	map.put("signature",signature);
	    	Map<String,Object> resultMap=new HashMap();
	    	resultMap.put("code", 0);
	    	resultMap.put("data", map);
	    	JSONObject jsonObject = JSONObject.fromObject(resultMap);
	    	System.out.println(jsonObject.toString());
	    	pw.write(jsonObject.toString());
		} catch (IOException e) {
			Map<String,String> map=new HashMap();
			map.put("code", "1");
			map.put("msg",e.getMessage());
			JSONObject jsonObject = JSONObject.fromObject(map);
			pw.write(jsonObject.toString());
			
		}
    	
    }
    private String execute(String sqlId, Map<String, Object> data){
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
	}
    @RequestMapping("/goodsPay")
    public String payForGoods(HttpServletRequest request, HttpServletResponse response) throws Exception {
         FormEngineResponse formEngineResponse = new FormEngineResponse("");
         InputStream inputStream = request.getInputStream();
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String text;
         String responseResultXML = "";
         while ((text = bufferedReader.readLine()) != null) {
             responseResultXML += text;

         }
     	 Map<String, Object> maps = new HashMap<>();
         Map <String, String> responseResult = WXPayUtil.xmlToMap(responseResultXML);
         FormLogger.info("接收到的报文：" + responseResultXML);


         String returnCode = "";
         returnCode = responseResult.get("return_code");
         String resultCode = responseResult.get("result_code");
         String paymentNo = responseResult.get("out_trade_no");
         String orderId   = responseResult.get("orderId");

         QueryPage queryPage1 = new QueryPage();
         queryPage1.setQueryParam("zhongdeorder/QryOrderByCode");
         queryPage1.getData().put("code", paymentNo);
         queryPage1.getData().put("id", orderId);
         List <Map <String, Object>> list1 = formService.queryBySqlId(queryPage1);
         if (list1 != null && list1.size() > 0) {
             orderId = list1.get(0).get("id").toString();
         }
         Map <String, Object> data = new HashMap <String, Object>();
         String code = "";
         if (returnCode.equals("SUCCESS") && "0".equals(list1.get(0).get("orderStatus").toString())) {
             if (resultCode.equals("SUCCESS") && "0".equals(list1.get(0).get("orderStatus").toString())) {
                 //更新状态插入
                 data.put("id", orderId);
                 data.put("status", 1);
                 CustomFormModel formModel = new CustomFormModel("", "", data);
                 formModel.setSqlId("zhongdeorder/UpdateOrderByCode");
                 formService.saveCustom(formModel);
                 int integral = Integer.parseInt(list1.get(0).get("integral").toString());
                 int goodsId = Integer.parseInt(list1.get(0).get("goodsId").toString());
                 if(integral>=0) {
                	 maps.put("goodsCount", 1);
                	 maps.put("integeral", "-"+integral);
                	 maps.put("goodsId", goodsId);
                	 maps.put("remark", "商品兑换-"+list1.get(0).get("goodsName")+"  订单编号（支付回调）："+paymentNo);
                	 maps.put("userId",list1.get(0).get("userId"));
                	 execute("zhongdeorder/UpdateMyIntegeralAndGoods", maps);
                	 execute("zhongdeorder/InsertIntegral", maps);
                		//用户积分减除
                	//获取个人现有积分和总积分
         			QueryPage qp = new QueryPage();
        			qp.setParamsByMap(maps);
        			qp.getData().put("userId",list1.get(0).get("userId"));
         			qp.setQueryParam("zhongdeorder/QryUserIntegral");
         			List<Map<String, Object>> userIntegralList = formService.queryBySqlId(qp);
         			int personIntegral = Integer.parseInt(userIntegralList.get(0).get("integral").toString());
     				maps.put("personIntegral", personIntegral-integral);
     				execute("zhongdeorder/UpdateUserIntegral", maps);
                 }
                 FormLogger.info("更新订单状态完毕");
                 code = "0";
             } else {
                 FormLogger.info("更新支付失败原因");
                 code = ErrorCode.DATA_NOT_FOUND.getCode();
             }
         }else {
        	 return "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                     + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
         }
        
         String resXml = "";
         if (returnCode.equals("SUCCESS")) {
             resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                     + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
         } else {
             resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                     + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
         }
         formEngineResponse.setCode(code);
         return resXml;
    	
    } 
   
    @RequestMapping ( value = "queryPayOrder" )
    @ResponseBody
    public FormEngineResponse queryPayOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FormEngineResponse formEngineResponse = new FormEngineResponse("");
        FormLogger.info("--------进入查询订单---------");
        String paymentNo = "";
        String code = "";
        String msg = "";
        if (request.getParameter("paymentNo") != null) {
            paymentNo = request.getParameter("paymentNo").toString();
            //查询微信
        }
        QueryPage queryPage1 = new QueryPage();
        queryPage1.setQueryParam("zhongdeorder/QryOrderByCode");
        queryPage1.getData().put("code", paymentNo);
        List <Map <String, Object>> list = formService.queryBySqlId(queryPage1);
        int integral = Integer.parseInt(list.get(0).get("integral").toString());
        int goodsId = Integer.parseInt(list.get(0).get("goodsId").toString());
        
        if (list != null && list.size() > 0) {
            if ("1".equals(list.get(0).get("orderStatus").toString())) {
                //更新状态插入
                code = "0";
                msg = "支付成功";
            } else {
                code = ErrorCode.DATA_NOT_FOUND.getCode();
                msg = "支付失败";
            }
        }

        formEngineResponse.setCode(code);
        formEngineResponse.setMsg(msg);
        return formEngineResponse;
    }

   
}