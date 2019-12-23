package com.tedu.base.file.controller;

import com.aliyun.oss.OSS;
import com.tedu.base.util.AliVideoPlayUtils;
import com.tedu.base.util.AliVideoService;
import com.tedu.oss.service.OssQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
	

	
	
	@RequestMapping("viewOffice")
	public String viewOffice(Model model,HttpServletRequest request){
		String key = request.getParameter("key");
		String url = ossQueryServiceImpl.queryObjectByKey(key, ossPriClient, BUCKET_NAME1);
		model.addAttribute("url", url);
		/*model.addAttribute("key", key);*/
		return "h5/viewOffice";
	}


}
