package com.tedu.base.file.controller;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.util.AESUtil;
import com.tedu.base.util.AliVideoPlayUtils;
import com.tedu.base.util.AliVideoService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2019-08-20
 */
@Controller
public class AliVideoController {

    @Resource
    AliVideoPlayUtils aliVideoPlayUtils;

    @Resource
    AESUtil aesUtil;

    @Resource
    FormService formService;

	@Resource
	private AliVideoService aliVideoService;
    @RequestMapping("/decrypt")
    public void getDecryptKey(HttpServletRequest request, HttpServletResponse response) {
    	 FormLogger.info("进入解密----");
         String ciphertext = request.getParameter("CipherText");
         String mtsHlsUriToken = request.getParameter("MtsHlsUriToken");
         String vid = aesUtil.decrypt("aliToken", mtsHlsUriToken);
         String aliKey = "";
         FormLogger.info("text->" + ciphertext);
         FormLogger.info("mtsHlsUriToken->" + mtsHlsUriToken);
         FormLogger.info("vid->" + vid);

         QueryPage queryPage = new QueryPage();
         queryPage.setQueryParam("getAliKey");
         queryPage.getData().put("vid", vid);
         List<Map<String, Object>> dataList = (List<Map<String, Object>>) formService.queryBySqlId(queryPage);

         if (dataList != null && dataList.size() > 0) {
             aliKey = dataList.get(0).get("aliKey").toString();
         }

         byte[] decryptKey = null;
         if (aliKey.equals(ciphertext)) {
             try {
                 FormLogger.info("解密接口验证正确");
                 decryptKey = aliVideoPlayUtils.decryptKey(ciphertext);
             } catch (Exception e) {

             }
         }
         try {


             response.setHeader("Access-Control-Allow-Origin", "*");
             response.setStatus(200);
             //返回base64decode之后的密钥
             OutputStream responseBody = response.getOutputStream();
             responseBody.write(decryptKey);
             responseBody.close();
         } catch (Exception e) {
             FormLogger.info("解密报错" + e.getMessage());
         }
    }


    @RequestMapping("/encryptVideo")
    @ResponseBody
    public void encryptVideo(@RequestBody Map<String, String> params) {

        FormLogger.info("上传完成之后的回调");
        String vid = params.get("VideoId").toString();

        FormLogger.info("Vid" + vid);

        aliVideoPlayUtils.encryptVideo(vid);

    }
    

}
