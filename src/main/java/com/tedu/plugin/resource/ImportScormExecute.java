package com.tedu.plugin.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.common.util.FileUtil;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

/**
 * 
 * @ClassName: ImportExecute
 * @Description:TODO 导入资源处理类
 * @author: qun
 * @date: 2018年7月27日 下午1:11:12
 *
 */
@Service("importScormExecute")
public class ImportScormExecute implements ILogicPlugin {

	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Autowired
	private CustomResourcesService customResourcesServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		Map<String, Object> map = requestObj.getData();
		String fileId = map.get("ctlPhotoId") == null ? "" : map.get("ctlPhotoId").toString();
		String id = map.get("ctlId") == null ? "" : map.get("ctlId").toString();
		String coding = map.get("ctlCoding") == null ? "" : map.get("ctlCoding").toString();
		
		Map<String, String> result = ossUploadServiceImpl.getFilePathById(fileId);
		String path = result.get("path");
		String fullPath = result.get("fullpath");
		String fileName = result.get("fileName");
		String fileType = result.get("fileType");
		
		if (!"UTF-8".equals(coding) && !"GBK".equals(coding)) {
			coding = "GBK";// IOS-UTF-8;WINDOWS-GBK
		}

		try {
			FileUtil.unzip(fullPath, path, true, coding);
			customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, fileType, fullPath), "", Long.parseLong(id), fileId, 
					"", "1.0", ResourceTypeEnum.SCORM.getCode(), CustomerResourcesSourceEnum.TFILEINDEX.getCode());
		} catch (Exception e) {
			log.error("上传资源失败！", e);
			e.printStackTrace();
			responseObj.setCode("111111");
			responseObj.setMsg("资源存储失败！");
		}

	}

	

	
	
	public static void main(String[] args) throws Exception {
		FileUtil.unzip("D:\\files\\public\\20181031\\zip\\d8\\d8c2fb53996f4ece929bd07c8b2f3e59.zip", "D:\\files\\public\\20181031\\zip\\d8\\d8c2fb53996f4ece929bd07c8b2f3e59", true, "GBK");
	}

}
