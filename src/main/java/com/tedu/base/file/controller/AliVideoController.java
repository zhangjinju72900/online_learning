package com.tedu.base.file.controller;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.service.FormService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @date 2019-08-20
 */
@Controller
public class AliVideoController {


    @Resource
    FormService formService;

    /**
     *
     * @Description: 打开文件
     * @param: @param request
     * @param: @return
     * @return: ResponseJSON
     */
    @RequestMapping("/viewMp4")
    public String viewMp4(HttpServletRequest request, HttpServletResponse response, Model model) {

        String id = request.getParameter("id");
        model.addAttribute("id",id);
        return "viewMp4";
    }


    /**
     *
     * @Description: 打开文件
     * @param: @param request
     * @param: @return
     * @return: ResponseJSON
     */
    @RequestMapping("/getVideo")
    public void getVideo(HttpServletRequest request, HttpServletResponse response) {
        // InputStream in = null ;

        try {
            String path = "";
            String id = request.getParameter("id");
            //查询文件路径
            QueryPage qp = new QueryPage();
            qp.getData().put("id",id);
            qp.setQueryParam("QryMp4Path");
            List<Map<String,Object>> list = formService.queryBySqlId(qp);
            if(list.size()>0){
                path = list.get(0).get("path").toString();
            }
            String range = request.getHeader("range");
            File inFile = new File(path);
            int length = (int) inFile.length();

            //response.setContentLength(length);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length",length+"");
            //response.setHeader( "Content-Disposition", "attachment;filename=" + URLEncoder.encode(key, "UTF-8"));
            response.setHeader(  "Access-Control-Allow-Origin","*");
            // 设置在下载框默认显示的文件名
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            // 读出文件到response
            // 这里是先需要把要把文件内容先读到缓冲区
            // 再把缓冲区的内容写到response的输出流供用户下载
            InputStream inputStream = new FileInputStream(inFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            OutputStream outputStream = response.getOutputStream();
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            // 人走带门
            bufferedInputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
