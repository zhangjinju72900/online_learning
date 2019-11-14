package com.tedu.plugin.oss;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

@Service("OssDownLoadService")
public class OssDownLoadService implements ILogicPlugin {
	@Value("${oss.access_id}")
	private String ossAccess_id;
	@Value("${oss.access_key}")
	private String ossAccess_key;
	@Value("${oss.bucket_name2}")
	private String ossBucket_name;
	@Value("${oss.oss_endpoint2}")
	private String oss_endpoint;
	@Value("${file.upload.path}")
	private String rootPath;
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
/*		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
			// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
			String bucketName = ossBucket_name;
			String objectName =map.get("ossKey").toString();

			OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
			String fileType = objectName.substring(objectName.lastIndexOf("."),objectName.length());
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String separator = File.separator;
			String dateDir = DateUtils.getDateToStr("YYYYMMdd", new Date());
			String dirPath =rootPath + separator + "private" + separator + "ossdown" + separator + dateDir + separator + "report" + separator  ;
			File filePath = new File(dirPath);
			if(!filePath.exists()) {
				filePath.mkdirs();
			}
			File copyFile = new File(dirPath+uuid +fileType);
			// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
			ossClient.getObject(new GetObjectRequest(bucketName, objectName),copyFile);

			// 关闭OSSClient。
			ossClient.shutdown();
			formModel.getData().put("filePath",dirPath+uuid +fileType );*/
		return null;
	}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		Map<String,Object> map= requestObj.getData();
		// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
		String bucketName = ossBucket_name;
		String objectName =map.get("ossKey").toString();

		OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
		String fileType = objectName.substring(objectName.lastIndexOf("."),objectName.length());
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String separator = File.separator;
		String dateDir = DateUtils.getDateToStr("YYYYMMdd", new Date());
		String dirPath =rootPath  + "private" + separator + "ossdown" + separator + dateDir + separator + "report" + separator  ;
		File filePath = new File(dirPath);
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		File copyFile = new File(dirPath+uuid +fileType);
		// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
		ossClient.getObject(new GetObjectRequest(bucketName, objectName),copyFile);

		// 关闭OSSClient。
		ossClient.shutdown();
		map.put("filePath",dirPath+uuid +fileType );
		responseObj.setData(map);
		
	}

}
