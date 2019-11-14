package com.tedu.plugin.information;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.business.keyword.service.KeyWordService;
import com.tedu.common.error.ExErrorCode;
import com.tedu.component.FrontEndVideoExecute;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("informationViewCDN")
public class informationViewCDN implements ILogicPlugin {

	@Resource
	private FormMapper formMapper;

	@Resource
	private OssQueryService ossQueryServiceImpl2;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		try {

			Map<String, Object> map = (Map<String, Object>) responseObj.getData();
			String fileId = map.get("fileId")==null?"":map.get("fileId").toString();
			String fileOssUrl = map.get("fileOssUrl")==null?"":map.get("fileOssUrl").toString();
			String content = map.get("content")==null?"":map.get("content").toString();
			
			String uFileId = map.get("uFileId")==null?"":map.get("uFileId").toString();
			String ufileOssUrl = map.get("ufileOssUrl")==null?"":map.get("ufileOssUrl").toString();
			
			String fileCDNUrl = ossQueryServiceImpl2.queryCDNUrlByOssUrl(fileId, fileOssUrl);
			String newContent = ossQueryServiceImpl2.getNewContentByContent(content);
			String ufileCDNUrl = ossQueryServiceImpl2.queryCDNUrlByOssUrl(uFileId, ufileOssUrl);
			
			map.put("ufileCDNUrl", ufileCDNUrl);
			map.put("fileCDNUrl", fileCDNUrl);
			map.put("content", newContent);
			map.remove("fileOssUrl");
			map.remove("ufileOssUrl");
			
		} catch (Exception e) {
			responseObj.setCode(ExErrorCode.INFO_OR_ACTIVITY_NOT_FOUND.getCode());
			responseObj.setMsg(ExErrorCode.INFO_OR_ACTIVITY_NOT_FOUND.getMsg());
			e.printStackTrace();
			log.error("更改资讯中OSS-URL为CDN-URL失败", e);
		}

	}
	
}
