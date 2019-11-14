package com.tedu.base.export.util;

/**     
 * @文件名称: DocUtil.java   
 * @描述: TODO  
 * @作者：  wuwh
 * @时间：2018年4月10日 上午10:36:08  
 * @版本：V1.0     
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import com.tedu.base.common.demo.FreeMarkerUtil;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.file.util.io.file;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @类功能说明：
 * 1.该代码在web项目中调用使用，需要在项目中的webroot目录下新建一个template文件夹，然后将预定义word的模板转成word.
 * xml文件放入template文件下即可 2.将要输出的数据放到map集合中，作为参数传入即可 依赖jar包:
 * @作者： bj
 * 
 * @创建时间：2018年4月10日 上午10:36:08 @版本：V1.0
 */
public class DocUtil1 {
/**
 * rootPath controller中 可以获取  @Value("${file.upload.path}")private String rootPath;
 * 
 * zipName 下载是的zip名称 不需要加.zip
 * 
 * List<Map<String,List<Map<String, Object>>>> lists中Map<String, Object> 存放的是模板中直接用到的数据然后将该map放到list里，
 * 之后再将该list放到一个Map中，此Map的键是对应的模板名称如：multiple.ftl，最后将该map添加到list里
 * @throws Exception 
 * 
 * 
 * 
 * */
	public static String download(HttpServletRequest request, HttpServletResponse response, String rootPath,
			String zipName, List<Map<String, Object>> dataResultList ,String templateName)
					throws Exception {
		String studentName=""; 
		String orgName = "";
        String outPath = "";
        String outName = "";
        String zipPath="";
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8"); // 注意这里要设置编码

		// 模板文件word.xml是放在WebRoot/template目录下的
		configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/template");
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String separator = File.separator;
		String dateDir = DateUtils.getDateToStr("YYYYMMdd", new Date());
		String dirPath =rootPath + separator + "private" + separator + "report" + separator + dateDir + separator+uuid+separator;
		for (int j = 0; j < dataResultList.size(); j++) {
				outPath = dirPath + zipName + separator ;
				int count = 0;
				for(int k=0;k<dataResultList.size();k++){
					Map<String, Object> orgMap = dataResultList.get(k);
					count=0;
					studentName = dataResultList.get(k).get("reviewName").toString();
					for(int k1=1;k1<dataResultList.size();k1++){
					String sN = dataResultList.get(k1).get("reviewName").toString();
					if(studentName.equals(sN)){
						count++;
						}
					}
					if(count>0){
						outName = zipName+studentName+"("+k+")";
						createDoc(orgMap, templateName, configuration, outPath, outName,request);
					}else{
						outName = zipName+studentName;
						createDoc(orgMap, templateName, configuration, outPath, outName,request);
					}
				}
			}
		zipPath = "审核信息" +".zip";
		File outFile = new File(zipPath);
		OutputStream oStream = new FileOutputStream(outFile);
		ZipUtils.toZip(dirPath + zipName, oStream, true);
		oStream.close();
		return zipPath;
	}	

	public static void createDoc(Map<String, Object> dataMap,String templateName,Configuration configuration,String outPath,String outName,HttpServletRequest request) throws Exception{
		Template t = null;
		OutputStreamWriter outputStreamWriter = null;
		FileOutputStream fileOutputStream = null;
		File outFile = null;
		Writer out = null;
		try{
			String path = request.getSession().getServletContext().getRealPath("/");
			path = path + File.separator + "template" + File.separator+"ue";
			File [] file= new File(path).listFiles();
		 	for(File src:file){
		 		copyDir( src, new File(outPath) ,request);		 
		 	}	
			
			// word.xml是要生成Word文件的模板文件
			t = configuration.getTemplate(templateName, "utf-8"); // 文件名// 还有这里要设置编码
			File file1 = new File(outPath);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			outFile = new File(outPath + outName + ".html");
			fileOutputStream = new FileOutputStream(outFile);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
			out = new BufferedWriter(outputStreamWriter); // 还有这里要设置编码
			
		
			t.process(dataMap, out);

			out.flush();
			out.close();
			outputStreamWriter.close();
			fileOutputStream.close();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
				try {
					if (outputStreamWriter != null) {
						outputStreamWriter.close();
					}
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	public static void copyDir(File src, File dest, HttpServletRequest request) throws IOException {
		if (src.isDirectory()) {
			File temp = new File(dest, src.getName());
			if (!temp.exists()) {
				temp.mkdirs();
			}
			for (File file : src.listFiles()) {
				copyDir(file, temp, request);
			}
		} else {
			copyFile(src, new File(dest, src.getName()));
		}
	}

	public static void copyFile(File src, File dest) throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;

		try {
			inputStream = new FileInputStream(src);
			outputStream = new FileOutputStream(dest);
			byte[] b = new byte[512];
			int len = 0;
			while (-1 != (len = inputStream.read(b))) {
				outputStream.write(b, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 打包成zip包
	 */
	public static File generateZip(String outPath, File[] files) throws Exception {
		ZipOutputStream out = null;
		File zipFile = new File(outPath);
		OutputStream tempOut = new FileOutputStream(zipFile);
		try {
			byte[] buffer = new byte[1024];
			// 生成的ZIP文件名为Demo.zip
			out = new ZipOutputStream(tempOut);
			// 需要同时下载的两个文件result.txt ，source.txt
			for (File file : files) {
				FileInputStream fis = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				out.closeEntry();
				fis.close();
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (out != null) {
				out.close();
			}
			if (tempOut != null) {
				tempOut.close();
			}
		}

		return zipFile;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	
}
