package com.tedu.plugin.platformRelease;

import com.aliyun.oss.OSS;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.component.BackEndVideoExecute;
import com.tedu.oss.service.OssQueryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 新增或编辑资讯保存后对其中的视频文件或图片文件执行的一些逻辑
 * 
 * @author quancong
 *
 */
@Service("SaveCommunityFilesPlugin")
public class SaveCommunityFilesPlugin implements ILogicPlugin {

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
	
	@Resource
	private BackEndVideoExecute backEndVideoExecute;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		
		/*String content = map.get("content")==null?"":map.get("content").toString();
		List<String> urls = new ArrayList<String>();
		String reg = "<source[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>";
		Matcher m = Pattern.compile(reg).matcher(content);
		while (m.find()) {
			String r = m.group(1);
			urls.add(r);
			try {
				
				String key = ossUploadServiceImpl.getOssKey("jpg");
				String imgPath = VideoUtil.fetchFrame(r, ORCODE_PATH + key);
				ossUploadServiceImpl.uploadOss(key, imgPath, ossPubClient, BUCKET_NAME2);
				String url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
				
				content = content.replaceAll("<video[^>]*?>[\\s\\S]*?<source src=\""+r+"\"[\\s\\S]*?<\\/video>","<img src=\""+url+"\" videosrc=\""+r+"\"/>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		formModel.getData().put("content",content);*/

		String previewFileId = map.get("previewFileId")==null?"":map.get("previewFileId").toString();
		if(StringUtils.isNotBlank(previewFileId)){
			formModel.setEditMode(Mode.Update.value);
		}
		
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		String activityId = formModel.getPrimaryFieldValue().toString();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", activityId);
		responseObj.setData(map2);
		try {
			Map<String,Object> map=(Map<String, Object>) formModel.getData();
			String content = map.get("content")==null?"":map.get("content").toString();
			backEndVideoExecute.infoOrActivityExecute("info", content, activityId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("处理后台话题视频转图片失败", e);
		}
	}

}
