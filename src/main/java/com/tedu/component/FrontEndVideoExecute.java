package com.tedu.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class FrontEndVideoExecute {
	
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
	 * @Description: TODO后台发布的资讯/活动针对视频的H5标签异步处理方法
	 * @author: qun 
	 * @param: @param type
	 * @param: @param content
	 * @param: @param id      
	 * @return: void 
	 * @date:   2018年12月11日 下午2:48:04     
	 * @throws
	 */
	public void infoVideoPicExecute(String id, String fullpath) {
		fixedThreadPool.execute(new AsynchronyExecute(id, fullpath));
	}
	
	class AsynchronyExecute implements Runnable{
			
		private String id;
		private String fullpath;
		
		public AsynchronyExecute(String id, String fullpath){
			this.id = id;
			this.fullpath = fullpath;
		}
		
		@Override
		public void run() {
			try {
				String key = ossUploadServiceImpl.getOssKey("jpg");
				key = key.substring(20);
				String imgPath = VideoUtil.fetchFrame(fullpath, ORCODE_PATH + key);
				/*ossUploadServiceImpl.uploadOss(key, imgPath, ossPubClient, BUCKET_NAME2);
				String url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);*/
				
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId("khAdmin/activityManage/SaveFileIndex");
				Map<String, Object> data = new HashMap<>();
				String name = key.substring(0, key.indexOf("."));
				String fileType = key.substring(key.indexOf(".")+1);
				System.out.println("name"+name);
				data.put("uuid", name);
				data.put("filename", name);
				data.put("filetype", fileType);
				data.put("length", 1);
				data.put("url", fullpath);
				data.put("path", ORCODE_PATH);
				data.put("url", fullpath);
				
				cModel.setData(data);
				formMapper.saveCustom(cModel);
				
				String picId = cModel.getPrimaryFieldValue();
				cModel = new CustomFormModel();
				cModel.setSqlId("khApp/discover/information/insertPic");
				data = new HashMap<>();
				data.put("informationId", id);
				data.put("picId", picId);
				cModel.setData(data);
				formMapper.saveCustom(cModel);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("app上传视频，截取其中图片存储失败！", e);
			}		
			
		}

	}


}
