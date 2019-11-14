package com.tedu.plugin.platformRelease;

import com.aliyun.oss.OSS;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.util.VideoUtil;
import com.tedu.component.BackEndVideoExecute;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

@Service("editInformationFilesPlugin")
public class EditInformationFilesPlugin implements ILogicPlugin {
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private BackEndVideoExecute backEndVideoExecute;
	
	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;
	
	@Resource
	private OSS ossPubClient;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;
	
	@Value("${orcode.path}")
	private String ORCODE_PATH;

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		/*String content = map.get("content")==null?"":map.get("content").toString();
		List<String> urls = new ArrayList<String>();
		String reg = "<source[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>";
		Matcher m = Pattern.compile(reg).matcher(content);
		while (m.find()) {
			String r = m.group(1);
			urls.add(r);
			try {
//				String imgPath = "123";
				
				String key = ossUploadServiceImpl.getOssKey("jpg");
				String imgPath = VideoUtil.fetchFrame(r, "D:\\222.jpg");
				ossUploadServiceImpl.uploadOss(key, imgPath, ossPubClient, BUCKET_NAME2);
				String url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
				
				content = content.replaceAll("<video[^>]*?>[\\s\\S]*?<source src=\""+r+"\"[\\s\\S]*?<\\/video>","<img src=\""+url+"\" videosrc=\""+r+"\"/>");
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		formModel.getData().put("content",content);*/
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		
		try {
			Map<String,Object> map=(Map<String, Object>) formModel.getData();
			String content = map.get("content")==null?"":map.get("content").toString();
			String id = map.get("id")==null?"":map.get("id").toString();
			backEndVideoExecute.infoOrActivityExecute("info", content, id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("处理后台资讯视频转图片失败", e);
		}

	}
}
