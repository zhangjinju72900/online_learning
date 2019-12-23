package com.tedu.plugin.resource;

import com.aliyun.oss.OSS;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 */
@Service("importExecute")
public class ImportExecute implements ILogicPlugin {

	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Autowired
	private CustomResourcesService customResourcesServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;

	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
	@Resource
	private OSS ossPriClient;
	@Resource
	private FormMapper formMapper;

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
		
		Map<String, String> result = ossUploadServiceImpl.getFilePathById(fileId);
		String path = result.get("path");
		String fullPath = result.get("fullpath");
		String fileName = result.get("fileName");
		String fileType = result.get("fileType");
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourcesManage/QryFileById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("fileId", fileId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		String key = tables.get(0).get("ossKey")+"";
		try {
			if("mp4".equalsIgnoreCase(fileType)||"flv".equalsIgnoreCase(fileType)){
				// 获取所有表
				customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, fileType, fullPath), tables.get(0).get("ossKey").toString(), Long.parseLong(id), fileId, 
						BUCKET_NAME1, "1.0", ResourceTypeEnum.OTHERS.getCode(), CustomerResourcesSourceEnum.TFILEINDEX.getCode());
			}else{
				/*String key = ossUploadServiceImpl.getOssKey(fileType);
				ossUploadServiceImpl.uploadOss(key, fullPath, ossPriClient, BUCKET_NAME1);
				
				ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileId, key, BUCKET_NAME1);*/
				
				customResourcesServiceImpl.insertResources(new ResourcesFile(fileName, path, fileType, fullPath), key, Long.parseLong(id), fileId, 
						BUCKET_NAME1, "1.0", ResourceTypeEnum.OTHERS.getCode(), CustomerResourcesSourceEnum.TFILEINDEX.getCode());
			}
			
		} catch (Exception e) {
			log.error("上传资源失败！", e);
			e.printStackTrace();
			responseObj.setCode("111111");
			responseObj.setMsg("资源存储失败！");
		}

	}

	

	/** 
     * 解压缩zip包 
     * @param zipFilePath zip文件的全路径 
     * @param unzipFilePath 解压后的文件保存的路径 
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含 
     */ 
	public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
		if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
			throw new Exception("文件夹不能为空！");
		}
		File zipFile = new File(zipFilePath);
		// 如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
		if (includeZipFileName) {
			String fileName = zipFile.getName();
			if (StringUtils.isNotEmpty(fileName)) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			unzipFilePath = unzipFilePath + File.separator + fileName;
		}
		// 创建解压缩文件保存的路径
		File unzipFileDir = new File(unzipFilePath);
		if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
			unzipFileDir.mkdirs();
		}

		// 开始解压
		ZipEntry entry = null;
		String entryFilePath = null, entryDirPath = null;
		File entryFile = null, entryDir = null;
		int index = 0, count = 0, bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipFile zip = new ZipFile(zipFile);
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		// 循环对压缩包里的每一个文件进行解压
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			// 构建压缩包中一个文件解压后保存的文件全路径
			entryFilePath = unzipFilePath + File.separator + entry.getName();
			// 构建解压后保存的文件夹路径
			index = entryFilePath.lastIndexOf(File.separator);
			if (index != -1) {
				entryDirPath = entryFilePath.substring(0, index);
			} else {
				entryDirPath = "";
			}
			entryDir = new File(entryDirPath);
			// 如果文件夹路径不存在，则创建文件夹
			if (!entryDir.exists() || !entryDir.isDirectory()) {
				entryDir.mkdirs();
			}

			// 创建解压文件
			entryFile = new File(entryFilePath);
			if (entryFile.exists()) {
				// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(entryFilePath);
				// 删除已存在的目标文件
				entryFile.delete();
			}

			// 写入文件
			bos = new BufferedOutputStream(new FileOutputStream(entryFile));
			bis = new BufferedInputStream(zip.getInputStream(entry));
			while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
				bos.write(buffer, 0, count);
			}
			bos.flush();
			bos.close();
		}
	}

}
