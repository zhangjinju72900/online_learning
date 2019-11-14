package com.tedu.plugin.video;

import com.aliyun.oss.OSS;
import com.google.zxing.WriterException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.util.AliVideoPlayUtils;
import com.tedu.common.util.FileUtil;
import com.tedu.common.util.QrCodeCreateUtil;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("SynOssToAliVideo")
public class SynOssToAliVideo implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Value("${orcode.path}")
	private String orCodePath;
	
	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;
	
	@Resource
	private OSS ossPubClient;
	
	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	@Resource
	private AliVideoPlayUtils aliVideoPlayUtils;

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		try {
			//aliVideoPlayUtil.ossVideoUploadToAliVideoPlay();
			//aliVideoPlayUtils.ossVideoUploadToAliVideoPlay();
			aliVideoPlayUtils.submit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String getFileName() {
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sb = new StringBuffer();
		sb.append(fileUUID);
		return sb.toString();
	}

}
