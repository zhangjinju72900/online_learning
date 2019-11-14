package com.tedu.plugin.teacher.attend;

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

@Service("InsertAttendClassPlugin")
public class InsertAttendClassPlugin implements ILogicPlugin {
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

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		CustomFormModel cModel = new CustomFormModel();
		QueryPage sqlQuery = new QueryPage();

		sqlQuery.setQueryParam("khTeacher/attendClass/QrySchool");
		sqlQuery.setDataByMap(map);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);

		map.put("schoolId", tables.get(0).get("schoolId"));
		
		sqlQuery.setQueryParam("khTeacher/attendClass/QryStudent");
		sqlQuery.setDataByMap(map);
		tables = formMapper.query(sqlQuery);

		
		map.put("studentCount",tables==null?0:tables.size());
		cModel.setSqlId("khTeacher/attendClass/InsertAttendClass");
		cModel.setData(map);
		formMapper.saveCustom(cModel);
		
		
		String attendClassId = cModel.getPrimaryFieldValue();
		map.put("id", cModel.getPrimaryFieldValue());
		
		/*sqlQuery.setQueryParam("khTeacher/attendClass/QryRecordId");
		sqlQuery.setDataByMap(map);
		tables = formMapper.query(sqlQuery);

		map.put("id", tables.get(0).get("id"));*/

		
		map.put("attendClassRecordId", cModel.getPrimaryFieldValue());
		for (Map<String,Object> table:tables) {
			map.put("studentId",table.get("studentId"));
			cModel.setSqlId("khTeacher/attendClass/InsertAttendClassSign");
			cModel.setData(map);
			formMapper.saveCustom(cModel);
		}
		
		String uuid = getFileName();
		FileUtil.fileExists(new File(orCodePath));
		
		String fullPath = orCodePath + uuid + "." + QrCodeCreateUtil.fileType;
		String ossKey = uuid + "." + QrCodeCreateUtil.fileType;

		File f = new File(fullPath);
		try {
			QrCodeCreateUtil.createQrCode(new FileOutputStream(f), QrCodeCreateUtil.attendClass + attendClassId,
					1200, "JPEG");
			
			cModel = new CustomFormModel();
			cModel.setSqlId("khAdmin/activityManage/SaveFileIndex");
			Map<String, Object> logData = new HashMap<>();
			logData.put("uuid", uuid);
			logData.put("filename", uuid);
			logData.put("filetype", QrCodeCreateUtil.fileType);

			logData.put("length", f.length());
			logData.put("url", orCodePath);
			logData.put("path", orCodePath);
			cModel.setData(logData);
			formMapper.saveCustom(cModel);
			String fileId = cModel.getPrimaryFieldValue();
			
			ossUploadServiceImpl.uploadOss(ossKey, fullPath, ossPubClient, BUCKET_NAME2);
			ossRecordServiceImpl.insertOssRecord(uuid, QrCodeCreateUtil.fileType, cModel.getPrimaryFieldValue(), ossKey, BUCKET_NAME2);
			String url = ossQueryServiceImpl.queryPublicUrlByKey(ossKey, BUCKET_NAME2, OSS_ENDPOINT2);
			
			cModel = new CustomFormModel();
			cModel.setSqlId("khTeacher/attendClass/UpdateAttendClass");
			map.put("fileId", fileId);
			map.put("ossKey", ossKey);
			map.put("ossUrl", url);
			cModel.setData(map);
			formMapper.saveCustom(cModel);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		formModel.setData(map);
		formEngineResponse.setData(map);
	}
	
	
	private String getFileName() {
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sb = new StringBuffer();
		sb.append(fileUUID);
		return sb.toString();
	}

}
