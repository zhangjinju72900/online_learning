package com.tedu.oss.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ListPartsRequest;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PartListing;
import com.aliyun.oss.model.PartSummary;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.util.AliVideoPlayUtils;
import com.tedu.common.constant.CustomerResourcesSourceEnum;
import com.tedu.common.constant.FtpUploadResourceTypeEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.common.util.FileUtil;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

@Service("ossUploadServiceImpl")
public class OssUploadServiceImpl implements OssUploadService{
	  @Value("${video.aliyun.assessKeyId}")
	    private String videoKeyId;

	    @Value("${video.aliyun.accessKeySecret}")
	    private String videoKeySecret;
	    
	    @Resource
		private AliVideoPlayUtils aliVideoPlayUtils;
	@Autowired
	private FormMapper formMapper;
	
	@Autowired
	private CustomResourcesService customResourcesServiceImpl;
	
	@Resource
	private OssRecordService ossRecordServiceImpl;
	
	@Resource
	private MaintainManualService maintainManualServiceImpl;
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	public void uploadOss(String key, String fullPath, OSS ossClient, String BUCKET_NAME) {
		
		File file = new File(fullPath);
		
		if(file != null){
			
			List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());
			
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			
			final long partSize = 100 * 1024 * 1024L;   // 50MB

			//文件大于50M分片上传
			if(file.length() > partSize){
				//申请上传ID
				String uploadId = claimUploadId(key, ossClient, BUCKET_NAME);
				int partCount = (int) (file.length() / partSize);
	            if (file.length() % partSize != 0) {
	                partCount++;
	            }
	            
	            if (partCount > 10000) {
	            	log.error("总分片数不能大于10000！");
	                throw new RuntimeException("总分片数不能大于10000！");
	            } else {                
	                System.out.println("共分片： " + partCount);
	            }
	            
	            for (int i = 0; i < partCount; i++) {
	                long startPos = i * partSize;
	                long curPartSize = (i + 1 == partCount) ? (file.length() - startPos) : partSize;
	                executorService.execute(new PartUploader(file, startPos, curPartSize, i + 1, uploadId, key, partETags, ossClient, BUCKET_NAME));
	            }
	            
	            executorService.shutdown();
	            while (!executorService.isTerminated()) {
	                try {
	                    executorService.awaitTermination(5, TimeUnit.SECONDS);
	                } catch (InterruptedException e) {
	                	log.error("分片上传资源失败！", e);
	                    e.printStackTrace();
	                }
	            }
	            
	            if (partETags.size() != partCount) {
	            	log.error("上传多个部分失败，部分还没有上传成功！");
	                throw new IllegalStateException("上传多个部分失败，部分还没有上传成功！" + key);
	            } else {
	            	log.info("成功的上传并将多个部分合并到一个对象中！ " + key);
	            }
	            
	            //查看上传的所有分片
//	            listAllParts(uploadId, key);
	            
	            //分片排序
	            completeMultipartUpload(uploadId, partETags, key, ossClient, BUCKET_NAME);
	            
	            log.info("完成上传！ " + key);
	            
			}else{
				ossClient.putObject(new PutObjectRequest(BUCKET_NAME, key, file));
			}
		}
	}
	
	private class PartUploader implements Runnable {
        
        private File localFile;
        private long startPos;        
        private long partSize;
        private int partNumber;
        private String uploadId;
        private String key;
        private List<PartETag> partETags;
        private OSS ossClient;
        private String BUCKET_NAME;
        
        
        public PartUploader(File localFile, long startPos, long partSize, int partNumber, String uploadId, String key, List<PartETag> partETags, OSS ossClient, String BUCKET_NAME) {
            this.localFile = localFile;
            this.startPos = startPos;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
            this.key = key;
            this.partETags = partETags;
            this.ossClient = ossClient;
            this.BUCKET_NAME = BUCKET_NAME;
        }
        
        @Override
        public void run() {
            InputStream instream = null;
            try {
                instream = new FileInputStream(this.localFile);
                instream.skip(this.startPos);
                
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(BUCKET_NAME);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(this.uploadId);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(this.partSize);
                uploadPartRequest.setPartNumber(this.partNumber);
                
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                
                log.info("分片号：" + this.partNumber + " 完成");
                
                synchronized (partETags) {
                    partETags.add(uploadPartResult.getPartETag());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } 
    }
	
	
	private String claimUploadId(String key, OSS ossClient, String BUCKET_NAME) {
		
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(BUCKET_NAME, key);
        
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
        
        return result.getUploadId();
    }
	
	private void completeMultipartUpload(String uploadId, List<PartETag> partETags, String key, OSS ossClient, String BUCKET_NAME) {
       
		Collections.sort(partETags, new Comparator<PartETag>() {

            @Override
            public int compare(PartETag p1, PartETag p2) {
                return p1.getPartNumber() - p2.getPartNumber();
            }
        });
        
        CompleteMultipartUploadRequest completeMultipartUploadRequest = 
                new CompleteMultipartUploadRequest(BUCKET_NAME, key, uploadId, partETags);
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }
	
	
	private void listAllParts(String uploadId, String key, OSS ossClient, String BUCKET_NAME) {
        System.out.println("所有分片数据......");
        ListPartsRequest listPartsRequest = new ListPartsRequest(BUCKET_NAME, key, uploadId);
        PartListing partListing = ossClient.listParts(listPartsRequest);
        
        int partCount = partListing.getParts().size();
        for (int i = 0; i < partCount; i++) {
            PartSummary partSummary = partListing.getParts().get(i);
            System.out.println("分片号：" + partSummary.getPartNumber() + ", 上传结果信息：" + partSummary.getETag());
        }
    }

	public String getOssKey(String fileType) {
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(fileUUID);
			sb.append(DateUtils.getDateToStr("yyyyMMddhhmmss", new Date()));
			sb.append(SessionUtils.getUserInfo()==null?"":SessionUtils.getUserInfo().getUserId());
			sb.append(".").append(fileType);
		} catch (Exception e) {
			log.error("生成osskey 异常，当前osskey为："+sb.toString(), e);
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public void clearUpData(String fileId, String id, String coding, String path, String zipFilePath, OSS ossClient, String BUCKET_NAME, String resourceType) throws IOException{
		Map<String, List<ResourcesFile>> resourcesMap = new HashMap<>();
		List<ResourcesFile> reList = null;
		
		ZipFile zipFile = null;
		boolean firstIsDic = true;
		zipFile = new ZipFile(zipFilePath, Charset.forName(coding));
		Enumeration<?> entryEnum = zipFile.entries();
		if (null != entryEnum) {
			ZipEntry zipEntry = null;
			while (entryEnum.hasMoreElements()) {
				zipEntry = (ZipEntry) entryEnum.nextElement();
				if (zipEntry.getSize() > 0) {
					
					int lastSlash = zipEntry.getName().lastIndexOf("/")==-1?0:zipEntry.getName().lastIndexOf("/");
					int lastPoint = zipEntry.getName().lastIndexOf(".")==-1?zipEntry.getName().length()-1:zipEntry.getName().lastIndexOf(".");
					
					String key = zipEntry.getName().substring(0, lastSlash == 0 ? 0 : lastSlash + 1);
					if (resourcesMap.containsKey(key)) {
						System.out.println("name"+zipEntry.getName());
						reList = resourcesMap.get(key);
						ResourcesFile f = new ResourcesFile(
								zipEntry.getName().substring(lastSlash == 0 ? 0 : lastSlash + 1, lastPoint), key,
								zipEntry.getName().substring(lastPoint + 1),
								zipEntry.getName(), false, path + File.separator + zipEntry.getName());
						reList.add(f);
						/*if(f.getType().length() > 5){
							System.out.println(1111);
						}*/
						resourcesMap.put(key, reList);
					} else {
						reList = new ArrayList<>();
						ResourcesFile f = new ResourcesFile(
								zipEntry.getName().substring(lastSlash == 0 ? 0 : lastSlash + 1, lastPoint), key,
								zipEntry.getName().substring(lastPoint + 1),
								zipEntry.getName(), false, path + File.separator + zipEntry.getName());
						reList.add(f);
						
						/*if(f.getType().length() > 5){
							System.out.println(1111);
						}*/
						
						resourcesMap.put(key, reList);
					}
					
					OutputStream os = null;
			        InputStream is = null;
			        
			        if(firstIsDic){//压缩包第一层就是文件时，先创建目录
//			        	File file = new File(path + File.separator + zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(".")));
			        	File file = new File(path + File.separator + key);
						file.mkdirs();
			        }
					
					File targetFile = new File(path
                            + File.separator + zipEntry.getName());
					if(!targetFile.exists()){
						targetFile.createNewFile();
					}
                    os = new BufferedOutputStream(new FileOutputStream(targetFile));
                    is = zipFile.getInputStream(zipEntry);
                    byte[] buffer = new byte[4096];
                    int readLen = 0;
                    while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                        os.write(buffer, 0, readLen);
                        os.flush();
                    }
                    is.close();
                    os.close();
					
				}
				if (zipEntry.isDirectory()) {
					
					if(firstIsDic){
						firstIsDic = false;
					}

					String pathTemp = path + File.separator 
                            + zipEntry.getName();
					
					File file = new File(pathTemp);
					file.mkdirs();
					
					String key = zipEntry.getName().substring(0,
							zipEntry.getName().substring(0, zipEntry.getName().length() - 1).lastIndexOf("/") + 1);
					String key1 = zipEntry.getName().replace(key, "");
					if (resourcesMap.containsKey(key)) {
						reList = resourcesMap.get(key);
						ResourcesFile f = new ResourcesFile(key1.substring(0, key1.lastIndexOf("/")), key, "",
								zipEntry.getName(), true, pathTemp);
						reList.add(f);
						resourcesMap.put(key, reList);
					} else {
						reList = new ArrayList<>();

						ResourcesFile f = new ResourcesFile(key1.substring(0, key1.lastIndexOf("/")), key, "",
								zipEntry.getName(), true, pathTemp);
						reList.add(f);
						resourcesMap.put(key, reList);
					}

				}
			}

			
			Map<String, Long> im = new HashMap<>();
			for (Entry<String, List<ResourcesFile>> entry : resourcesMap.entrySet()) {
				if ("".equals(entry.getKey())) {
					im.put(entry.getKey(), Long.parseLong(id));
					List<ResourcesFile> l = entry.getValue();
					fuck(l, resourcesMap, im, fileId, ossClient, BUCKET_NAME, coding, resourceType);
				}
			}
		}
	}
	
	
	public void fuck(List<ResourcesFile> l, Map<String, List<ResourcesFile>> resourcesMap, Map<String, Long> im,
			String fileId, OSS ossClient, String BUCKET_NAME, String coding, String resourceType) {
		if (l != null) {
			for (ResourcesFile resourcesFile : l) {

				if (!resourcesFile.isDirectory()) {
					// 保存文件
					String key = "";

					log.info(resourcesFile.getName() + "---pid=" + im.get(resourcesFile.getPath()));

					try {
						
						if(resourcesFile.getName().toLowerCase().contains("_scorm") || resourcesFile.getName().toLowerCase().contains("_ppt")){//scorm 或者 ppt转H5的文件解压

							FileUtil.unzip(resourcesFile.getPublicFullPath(), resourcesFile.getPublicFullPath().substring(0, resourcesFile.getPublicFullPath().lastIndexOf(".")), true, coding);
						}else{
							if(resourcesFile.getType().toLowerCase().contains("mp4")||resourcesFile.getType().toLowerCase().contains("flv")) {
								 String vid = "";
			                	 vid = aliVideoPlayUtils.testUploadLocalFile(resourcesFile.getName(), resourcesFile.getName()+"."+resourcesFile.getType(), resourcesFile.getPublicFullPath(),videoKeyId,videoKeySecret);
			 					 //设置转码
			                	 //aliVideoPlayUtils.submitTranscodeJobs(vid);
			                	 key = vid;
			                	 ossRecordServiceImpl.insertOssRecord(resourcesFile.getName(), resourcesFile.getType(), fileId, vid, BUCKET_NAME);
							}else {
								key = getOssKey(resourcesFile.getType());// 生成key的方式
								
								uploadOss(key, resourcesFile.getPublicFullPath(), ossClient, BUCKET_NAME);
								
								ossRecordServiceImpl.insertOssRecord(resourcesFile.getName(), resourcesFile.getType(), fileId, key, BUCKET_NAME);
							}
							
						}

						int fileType = getFileType(resourcesFile.getName());
						
						if(resourceType.equals(FtpUploadResourceTypeEnum.CUSTOMER_USER_RESOURCE.getCode())){
							
							Long id = customResourcesServiceImpl.insertResources(resourcesFile, key, im.get(resourcesFile.getPath()), fileId, StringUtils.isBlank(key)?"":BUCKET_NAME, "1.0", 
									fileType, CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
						
						}else if(resourceType.equals(FtpUploadResourceTypeEnum.MAINTAIN_MANUAL.getCode())){
							
							Long id = maintainManualServiceImpl.insertMaintainManual(resourcesFile, key, im.get(resourcesFile.getPath()), fileId, StringUtils.isBlank(key)?"":BUCKET_NAME, fileType, 
									CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
							
						}	
						
					} catch (Exception e) {
						log.error("上传文件失败，key:" + key + ";fileId:" + fileId, e);
						e.printStackTrace();
					}

				}else {
					if(resourceType.equals(FtpUploadResourceTypeEnum.CUSTOMER_USER_RESOURCE.getCode())){
						// 保存目录，生成该目录的id,为下级目录的pid
						Long id = customResourcesServiceImpl.insertResources(resourcesFile, "", im.get(resourcesFile.getPath()), fileId, BUCKET_NAME, "1.0", 
								ResourceTypeEnum.OTHERS.getCode(), CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
						im.put(resourcesFile.getFullpath(), id);
					}
					else{
						/*Map<String,Object> map = new HashMap<String,Object>();
						CustomFormModel model = new CustomFormModel();
						model.setSqlId("InsertMaintain");
						map.put("name", resourcesFile.getName());
						map.put("parentId", im.get("parentId")!=null?im.get("parentId"):im.get(""));
						model.setData(map);
						formMapper.saveCustom(model);*/
						// 保存目录，生成该目录的id,为下级目录的pid
						Long id = maintainManualServiceImpl.insertMaintainManual(resourcesFile, "", im.get(resourcesFile.getPath()), fileId, "", 0, 
								CustomerResourcesSourceEnum.T_FTP_UPLOAD_RECORD.getCode());
						//im.put("parentId",Long.valueOf(id));
						im.put(resourcesFile.getFullpath(),Long.valueOf(id));
					}
				}

				fuck(resourcesMap.get(resourcesFile.getFullpath()), resourcesMap, im, fileId, ossClient, BUCKET_NAME, coding, resourceType);
			}
		}
	}
	

	private int getFileType(String name) {
		if(name.toLowerCase().endsWith("_scorm")){
			return ResourceTypeEnum.SCORM.getCode();
		}else if (name.toLowerCase().endsWith("_ppt")){
			return ResourceTypeEnum.PPT.getCode();
		}else {
			return ResourceTypeEnum.OTHERS.getCode();
		}
	}

	@Override
	public Map<String, String> getFilePathById(String fileId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourcesManage/QryFileById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/resourcesManage/QryFileById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("fileId", fileId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			Map<String, String> result = new HashMap<>();
			Map m = tables.get(0);
			String path = m.get("path") == null ? "" : m.get("path").toString();
			String uuid = m.get("uuid") == null ? "" : m.get("uuid").toString();
			String fileType = m.get("file_type") == null ? "" : m.get("file_type").toString();
			String filename = m.get("filename") == null ? "" : m.get("filename").toString();
			String description = m.get("description")==null?"":m.get("description").toString();
			result.put("uuid", uuid);
			result.put("fileName", filename);
			result.put("path", path+uuid.trim());
			result.put("fullpath", path.trim() + uuid.trim() + "." + fileType.trim());
			result.put("fileType", fileType);
			result.put("description", description);
			return result;
		}
		return null;
	}
	
	
	
	@Override
	public Map<String, Object> getCustomerFilePathById(String fileId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/resourcesManage/QryResourcesById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/resourcesManage/QryResourcesById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", fileId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			return tables.get(0);
			
		}
		return null;
	}


}
