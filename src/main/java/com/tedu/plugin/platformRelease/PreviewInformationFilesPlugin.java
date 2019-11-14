package com.tedu.plugin.platformRelease;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.base.task.SpringUtils;
import com.tedu.component.BackEndVideoExecute;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 新增或编辑资讯保存后对其中的视频文件或图片文件执行的一些逻辑
 * 
 * @author quancong
 *
 */
@Service("previewInformationFilesPlugin")
public class PreviewInformationFilesPlugin implements ILogicPlugin {

	@Resource
	private BackEndVideoExecute backEndVideoExecute;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		//预览时发布状态 设为0
		formModel.getData().put("eq_releaseStatus", 0);
		formModel.getData().put("releaseStatus", 0);
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		/*String content = map.get("content")==null?"":map.get("content").toString();
		List<String> urls = new ArrayList<String>();
		String reg = "<source[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>";
		Matcher m = Pattern.compile(reg).matcher(content);
		while (m.find()) {
			String r = m.group(1);
			urls.add(r);
			try {
				String imgPath = "123";
				content = content.replaceAll("<video[^>]*?>[\\s\\S]*?<source src=\""+r+"\"[\\s\\S]*?<\\/video>","<img src=\""+imgPath+"\" videosrc=\""+r+"\"/>");
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
		String infoId = formModel.getPrimaryFieldValue().toString();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", infoId);
		responseObj.setData(map2);
		
		try {
			Map<String,Object> map=(Map<String, Object>) formModel.getData();
			String content = map.get("content")==null?"":map.get("content").toString();
			backEndVideoExecute.infoOrActivityExecute("info", content, infoId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("处理后台资讯视频转图片失败", e);
		}

	}

}
