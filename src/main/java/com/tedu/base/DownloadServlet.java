package com.tedu.base;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author xuan
 */
public class DownloadServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String fileName = req.getParameter("fileName");
        String exportname  = req.getParameter("exportName");


        // 设置响应数据的 MIME 类型
        resp.setContentType("application/x-msdownload");



        // 判断浏览器是否是 IE
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.contains("MSIE")) {
            // IE
            // 设置文件的名称
            resp.setHeader("Content-Disposition", "attachment; fileName="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else {
            // 非IE
            resp.setHeader("Content-Disposition", "attachment; fileName="
                    + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        }

        // 获取文件所在的路径
        String path = req.getServletContext().getRealPath("" +
                "/template");

        // 获取指定的文件对象
        File f = new File(path, fileName);

        ServletOutputStream out = resp.getOutputStream();

        // 将文件复制到输出流中，响应给浏览器
        Files.copy(Paths.get(f.getAbsolutePath()), out);
    }
}
