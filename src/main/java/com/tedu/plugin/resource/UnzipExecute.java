package com.tedu.plugin.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
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
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.common.util.FileUtil;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssQueryService;
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
@Service("unzipExecute")
public class UnzipExecute implements ILogicPlugin {

	@Resource
	private OssUploadService ossUploadServiceImpl2;
	
	@Resource
	private FormMapper formMapper;

	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;

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
		String id = map.get("ctlId") == null ? "" : map.get("ctlId").toString();
		
		Map<String, Object> result = ossUploadServiceImpl2.getCustomerFilePathById(id);
		
		String filePath = result.get("filePath").toString();
		String token = filePath.substring(0, filePath.lastIndexOf("."));
		String coding = map.get("ctlCoding")==null?"":map.get("ctlCoding").toString();
		
		try {
			FileUtil.unzip(filePath, token, true, "1".equals(coding)?"UTF-8":"GBK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
