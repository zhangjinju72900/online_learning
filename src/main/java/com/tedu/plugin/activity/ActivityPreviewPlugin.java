package com.tedu.plugin.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;
import com.tedu.common.util.FileUtil;
import com.tedu.common.util.QrCodeCreateUtil;
import com.tedu.oss.service.OssUploadService;

/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("activityPreviewPlugin")
public class ActivityPreviewPlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	
	@Value("${orcode.path}")
	private String orCodePath;
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		
		log.info(formModel.getData());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String id = map.get("id")==null?"":map.get("id").toString();
		String previewFileId = map.get("previewFileId")==null?"":map.get("previewFileId").toString();
		try {
			Map<String, Object> map2 = new HashMap<String, Object>();
			if(StringUtils.isBlank(previewFileId)){
				String fileName = getFileName();  
				
				FileUtil.fileExists(new File(orCodePath));
				
				File f = new File(orCodePath+fileName+"."+QrCodeCreateUtil.fileType);
				QrCodeCreateUtil.createQrCode(new FileOutputStream(f),QrCodeCreateUtil.activityPreviewUrl+id,900,"JPEG");
				
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId("khAdmin/activityManage/SaveFileIndex");
				Map<String, Object> logData = new HashMap<>();
				logData.put("uuid", fileName);
				logData.put("filename", fileName);
				logData.put("filetype", QrCodeCreateUtil.fileType);
				
				logData.put("length", f.length());
				logData.put("url", orCodePath);
				logData.put("path", orCodePath);
				cModel.setData(logData);
				formMapper.saveCustom(cModel);
				map2.put("id", cModel.getPrimaryFieldValue());
			} else {
				map2.put("id", previewFileId);
			}
			
			responseObj.setData(map2);
			
		} catch (WriterException | IOException e) {
			e.printStackTrace();
			log.error("生成活动预览二维码图片失败！", e);
		}
		  
		  
	}

	private String getFileName() {
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sb = new StringBuffer();
		sb.append(fileUUID);
//		sb.append(DateUtils.getDateToStr("yyyyMMddHHmmss", new Date()));
		return sb.toString();
	}
}

