package com.tedu.base.export.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.export.util.DocUtil;
import com.tedu.base.export.util.DocUtil1;

import freemarker.template.Configuration;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/lesson")
public class ExportController {
	@Resource
	private FormService formService;
	@Resource
	private FormMapper formMapper;
	@Value("${file.upload.path}")
	private String rootPath;
	// private static final Logger logger =
	// LoggerFactory.getLogger(ExportController.class);

	@RequestMapping("/exportLesson")
	public FormEngineResponse getLessonPlanById(@RequestBody FormEngineRequest requestObj,FormModel formModel,Model model, FormEngineRequest formEngineRequest, HttpServletResponse response,HttpServletRequest request) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8"); // 注意这里要设置编码
		PrintWriter pw = response.getWriter();
		String [] ids = null;
		if(request.getParameter("lessonId")!=null) {
			
			ids = request.getParameter("lessonId").split(",");
		}else {
			ids = requestObj.getData().get("lessonId").toString().split(",");
			
		}
		// 获取uuid作为文件名称
		String templateName = "export.ftl";

		String separator = File.separator;
		
		String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
		String date = DateUtils.getDateToStr("YYYYMMdd", new Date());
		String outPath = rootPath + separator + "private" + separator + "report" + separator + date + separator+uuid+separator;
		String outName = uuid;
		List<Map<String, Object>> dataResultList = new ArrayList<Map<String,Object>>();
		for(String id:ids) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(dataMap);
			qp.getData().put("id", id);
			qp.setQueryParam("khTeacher/lessonPlan/QryLessonById");
			List<Map<String, Object>> dataList = formService.queryBySqlId(qp);
			dataMap = dataList.get(0);
			qp.setQueryParam("khTeacher/lessonPlan/QryLessonStepById");
			List<Map<String, Object>> groupList = formService.queryBySqlId(qp);

			dataMap.put("groupList", groupList);
			dataResultList.add(dataMap);
			
		
			

		}
		
		configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/template");// 就是
		// /Webapp/tempalte目录
		FormEngineResponse formEngineResponse = new FormEngineResponse("");
		String resultPath = DocUtil.createDoc(dataResultList, templateName, configuration, outPath,rootPath);
		formEngineResponse.setCode("0");
		formEngineResponse.setMsg("0");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("filePath", resultPath);
		formEngineResponse.setData(resultMap);
		JSONObject jb = JSONObject.fromObject(formEngineResponse);
		pw.write(jb.toString());
		return null;
	}
	@RequestMapping("/exportLesson1")
	public void getLessonPlanById1(FormModel formModel,Model model, FormEngineRequest formEngineRequest, HttpServletResponse response,HttpServletRequest request) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8"); // 注意这里要设置编码
		PrintWriter pw = response.getWriter();
		String [] ids = null;
			
			ids = request.getParameter("lessonId").split(",");
		// 获取uuid作为文件名称
		String templateName = "export.ftl";

		String separator = File.separator;
		
		String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
		String date = DateUtils.getDateToStr("YYYYMMdd", new Date());
		String outPath = rootPath + separator + "private" + separator + "report" + separator + date + separator+uuid+separator;
		String outName = uuid;
		List<Map<String, Object>> dataResultList = new ArrayList<Map<String,Object>>();
		for(String id:ids) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(dataMap);
			qp.getData().put("id", id);
			qp.setQueryParam("khTeacher/lessonPlan/QryLessonById");
			List<Map<String, Object>> dataList = formService.queryBySqlId(qp);
			dataMap = dataList.get(0);
			qp.setQueryParam("khTeacher/lessonPlan/QryLessonStepById");
			List<Map<String, Object>> groupList = formService.queryBySqlId(qp);

			dataMap.put("groupList", groupList);
			dataResultList.add(dataMap);
			
		
			

		}
		
		configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/template");// 就是
		// /Webapp/tempalte目录
		String resultPath = DocUtil.createDoc(dataResultList, templateName, configuration, outPath,rootPath);
		pw.write(resultPath);
	}
	
	
	
	
	@RequestMapping("/exportInformation1")
	public void getInformationById(FormModel formModel,Model model, FormEngineRequest formEngineRequest, HttpServletResponse response,HttpServletRequest request) throws Exception {
		String zipName="";
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8"); // 注意这里要设置编码
		PrintWriter pw = response.getWriter();
		String [] ids = null;
			
		ids = request.getParameter("inforid").split(",");
		// 获取uuid作为文件名称
		String templateName = "index.html";
		String separator = File.separator;
		String zipPath =" ";
		String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
		String date = DateUtils.getDateToStr("YYYYMMdd", new Date());
		String outPath = rootPath + separator + "private" + separator + "report" + separator + date + separator+uuid+separator;
		String outName = uuid;
		List<Map<String, Object>> dataResultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<ids.length;i++) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(dataMap);
			String id=ids[i];
			qp.getData().put("id", id);
			//通过id找到相应的信息
			qp.setQueryParam("khAdmin/checkManage/QryCheckManageDetailById");
			List<Map<String, Object>> dataList = formService.queryBySqlId(qp);
			dataMap = dataList.get(0);
			//格式化日期
			Date reviewTime = (Date) dataMap.get("reviewTime");
			String formate="yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(formate);
			String datedtr=sdf.format(reviewTime);
			zipName=dataMap.get("school").toString();
			dataMap.put("reviewTime",datedtr);
			/*String contents[] = dataMap.get("content").toString().split("<");
			String content = contents[0];
			dataMap.put("content", content);*/
			dataResultList.add(dataMap);
		}
	 	zipPath= DocUtil1.download(request, response, outPath, zipName, dataResultList, templateName);
		pw.write(zipPath);
	}
	
	


	@RequestMapping("/downLoad")
	public void downLoad(Model model, FormEngineRequest formEngineRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		OutputStream toClient = null;
		InputStream fis = null;
		String dirPath = "";
		try {
			String zipPath = request.getParameter("path");
			zipPath = URLDecoder.decode(zipPath, "UTF-8");
			File outFile = new File(zipPath);
			String zipName = outFile.getName();
			dirPath = zipPath.replace(zipName, "");
			fis = new BufferedInputStream(new FileInputStream(outFile));
			
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(zipName, "UTF-8"));
			toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (fis != null) {
					fis.close();
				}
				if (toClient != null) {
					toClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			DocUtil.deleteDir(new File(dirPath));
		}
	}
}
