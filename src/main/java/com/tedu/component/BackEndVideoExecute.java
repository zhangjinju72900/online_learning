package com.tedu.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSS;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.common.util.VideoUtil;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;
@Component
public class BackEndVideoExecute {
	
	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private FormMapper formMapper;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;
	
	@Resource
	private OSS ossPubClient;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;
	
	@Value("${orcode.path}")
	private String ORCODE_PATH;
	
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * @Title: InfoOrActivityExecute   
	 * @Description: TODO后台发布的资讯/活动针对视频的H5标签异步处理方法、内容中包含外链手机端打开的处理
	 * @author: qun 
	 * @param: @param type
	 * @param: @param content
	 * @param: @param id      
	 * @return: void 
	 * @date:   2018年12月11日 下午2:48:04     
	 * @throws
	 */
	public void infoOrActivityExecute(String type, String content, String id){
		fixedThreadPool.execute(new AsynchronyExecute(id, content, type));
	}
	
	class AsynchronyExecute implements Runnable{
			
		private String id;
		private String content;
		private String type;
		
		public AsynchronyExecute(String id, String content, String type){
			this.id = id;
			this.content = content;
			this.type = type;
		}
		
		@Override
		public void run() {
			boolean flag = false;
			List<String> urls = new ArrayList<String>();
			String reg = "<source[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>";
			Matcher m = Pattern.compile(reg).matcher(content);
			while (m.find()) {
				
				flag = true;
				
				String r = m.group(1);
				urls.add(r);
				try {
					
					String key = ossUploadServiceImpl.getOssKey("jpg");
					String imgPath = VideoUtil.fetchFrame(r, ORCODE_PATH + key);
					ossUploadServiceImpl.uploadOss(key, imgPath, ossPubClient, BUCKET_NAME2);
					String url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
					
					//手机端打开视频针对video的处理
					content = content.replaceAll("<video[^>]*?>[\\s\\S]*?<source src=\""+r+"\"[\\s\\S]*?<\\/video>","<img src=\""+url+"\" videosrc=\""+r+"\"/>");
					
					
				} catch (Exception e) {
					e.printStackTrace();
					log.error("截取后端上传资讯截图并覆盖原内容失败", e);
				}
			}
			//手机端打开外链的处理
			if(content.contains("target=\"_self\"")){
				content = content.replace("target=\"_self\"", "target=\"_blank\" onclick=\"return openBrowser(this)\"");
				flag = true;
			}
			if(flag){
				CustomFormModel cModel = new CustomFormModel();
				if("activity".equals(type)){
					cModel.setSqlId("khAdmin/activityManage/UpdateContentById");
				}else {
					cModel.setSqlId("khAdmin/platformRelease/UpdateContentById");
				}
				cModel.put("id", id);
				cModel.put("content", content);
				formMapper.saveCustom(cModel);
			}
		}

	}

}
