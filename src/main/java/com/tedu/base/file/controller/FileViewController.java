package com.tedu.base.file.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aliyun.oss.OSS;
import com.tedu.base.util.AliVideoPlayUtils;
import com.tedu.base.util.AliVideoService;
import com.tedu.base.util.RedisUtil;
import com.tedu.oss.service.OssQueryService;

/**
 * 文件操作接口，包含上传和下载
 * @author gaolu
 *
 */
@Controller
public class FileViewController {
    
    @Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private OSS ossPriClient;
	@Resource
	private AliVideoService aliVideoService;
	@Resource
   private AliVideoPlayUtils AliVideoPlayUtils;
	
	//私有
	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Value("${base.website}")
	private String baseUrl;
	
	@RequestMapping("viewMp4")
	public String viewMp4(Model model,HttpServletRequest request){
		String key = request.getParameter("key");
		if(!key.contains(".")){
			String token = aliVideoService.getToken(key);
			String url  = AliVideoPlayUtils.getPlaySource(key);
			model.addAttribute("key", url+"?MtsHlsUriToken="+token);
		}else{
			String url = ossQueryServiceImpl.queryObjectByKey(key, ossPriClient, BUCKET_NAME1);
			model.addAttribute("url", url);
			
		}
		model.addAttribute("ctx", baseUrl);
		return "h5/viewMp4";
	}
	
	
	@RequestMapping("viewOffice")
	public String viewOffice(Model model,HttpServletRequest request){
		String key = request.getParameter("key");
		String url = ossQueryServiceImpl.queryObjectByKey(key, ossPriClient, BUCKET_NAME1);
		model.addAttribute("url", url);
		/*model.addAttribute("key", key);*/
		return "h5/viewOffice";
	}


}
