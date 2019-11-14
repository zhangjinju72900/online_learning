package com.tedu.plugin.activity;

import com.sdicons.json.validator.impl.predicates.Null;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.LogUtil;
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
import com.tedu.component.BackEndVideoExecute;
import com.tedu.oss.service.OssUploadService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新建活动保存
 * 
 * @author quancong
 *
 */
@Service("saveActivityManagePlugin")
public class SaveActivityManagePlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Resource
	private OssUploadService ossUploadServiceImpl;
	
	@Resource
	private BackEndVideoExecute backEndVideoExecute;

	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;

	@Value("${orcode.path}")
	private String orCodePath;

	private String sqlTemplate = "khAdmin/activityManage/SaveActivityRegion";
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// 预览已经新建保存，保存时改为编辑保存

		Map<String, Object> map = (Map<String, Object>) formModel.getData();
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
			String startTime = map.get("eq_startTime")+"";
			String endTime  = map.get("eq_endTime")+"";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date st;
			Date et;
			 try {
				st=sdf.parse(startTime);
				et=sdf.parse(endTime);
				if(st.after(et)){ 
				 throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "活动时间错误", "活动结束时间不能早于开始时间,请修改");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		  
		 
		  
		  
		String previewFileId = map.get("previewFileId") == null ? "" : map.get("previewFileId").toString();
		if (StringUtils.isNotBlank(previewFileId)) {
			formModel.setEditMode(Mode.Update.value);
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String mode = map.get("Mode") == null ? "" : map.get("Mode").toString();
		String activityId = formModel.getPrimaryFieldValue().toString();

		if ("Edit".equals(mode)||"Add".equals(mode)) {
			// 编辑
			String deleteSql = "khAdmin/activityManage/DeleteActivityRegion";
			CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId(deleteSql);
			cModel.setData(formModel.getData());
			Map<String, Object> logData = formModel.getData();

			logData.put("activityId", activityId);
			LogUtil.info(cModel.getData().toString());
			formMapper.saveCustom(cModel);
		}

		String regions = map.get("eq_region") == null ? "" : map.get("eq_region").toString();
		String school = map.get("eq_school") == null ? "" : map.get("eq_school").toString();
		String classId = map.get("classId") == null ? "" : map.get("classId").toString();
		String[] regionArray = regions.split(",");
		if (StringUtils.isNotBlank(regions)&&regionArray.length==1) {
			if (classId.startsWith("[") && classId.endsWith("]")) {
				classId = classId.substring(1, classId.length() - 1);
			}
			// t_activity_join_region
			String[] classIdArray = classId.split(",");
			
			if(!"".equals(classId)) {
				for (String id : classIdArray) {
					if (StringUtils.isNotBlank(id)) {
						CustomFormModel cModel = new CustomFormModel();
						cModel.setSqlId(sqlTemplate);
						cModel.setData(formModel.getData());
						Map<String, Object> logData = formModel.getData();

						logData.put("id", activityId);
						logData.put("regionId", regions);
						logData.put("classId", id);
						logData.put("schoolId", school);
						LogUtil.info(cModel.getData().toString());
						formMapper.saveCustom(cModel);
					}
				}
			}else if("".equals(classId)&&!"".equals(school)) {
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				Map<String, Object> logData = formModel.getData();

				logData.put("id", activityId);
				logData.put("regionId", regions);
				logData.put("classId",null);
				logData.put("schoolId", school);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);
			}else {
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				Map<String, Object> logData = formModel.getData();

				logData.put("id", activityId);
				logData.put("regionId", regions);
				logData.put("classId", null);
				logData.put("schoolId", null);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);
			}
		}else {
			for (String region : regionArray) {
				if (StringUtils.isNotBlank(region)) {
					CustomFormModel cModel = new CustomFormModel();
					cModel.setSqlId(sqlTemplate);
					cModel.setData(formModel.getData());
					Map<String, Object> logData = formModel.getData();

					logData.put("id", activityId);
					logData.put("regionId", region.replace("[", "").replace("]", ""));
					logData.put("classId", null);
					logData.put("schoolId", null);
					LogUtil.info(cModel.getData().toString());
					formMapper.saveCustom(cModel);
				}
			}
		}

		if ("Edit".equals(mode)||"Add".equals(mode)) {
			// 编辑
			String deleteSql = "khAdmin/activityManage/DeleteActivityRole";
			CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId(deleteSql);
			cModel.setData(formModel.getData());
			Map<String, Object> logData = formModel.getData();

			logData.put("activityId", activityId);
			LogUtil.info(cModel.getData().toString());
			formMapper.saveCustom(cModel);
		}

		// t_activity_join_role
		String roles = map.get("eq_role") == null ? "" : map.get("eq_role").toString();
		if (roles.startsWith("[") && roles.endsWith("]")) {
			roles = roles.substring(1, roles.length() - 1);
		}
		if (StringUtils.isNotBlank(roles)) {
			String[] roleArray = roles.split(",");
			for (String id : roleArray) {
				if (StringUtils.isNotBlank(roles)) {
					CustomFormModel cModel = new CustomFormModel();
					cModel.setSqlId("khAdmin/activityManage/SaveActivityRole");
					cModel.setData(formModel.getData());
					Map<String, Object> logData = formModel.getData();

					logData.put("id", activityId);
					logData.put("roleId", id);
					LogUtil.info(cModel.getData().toString());
					formMapper.saveCustom(cModel);
				}

			}
		}
		/*Map<String, Object> map1 = requestObj.getData();
		String fileId = map1.get("fileId") == null ? "" : map1.get("fileId").toString();
		log.info("fileId" + fileId);
		String fileName = map.get("fileId") == null ? "" : map.get("fileId").toString();
		String fileType = "";
		if (fileName.length() > 0) {
			fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		}*/

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", activityId);
		responseObj.setData(map2);

		if ("Add".equals(mode) && requestObj != null && requestObj.getData() != null
				&& "0".equals(requestObj.getData().get("eq_activityType"))) {// 线下活动生成二维码

			try {
				String uuid = getFileName();
//				String qrFileName = "join-" + uuid;

				FileUtil.fileExists(new File(orCodePath));

				File f = new File(orCodePath + uuid + "." + QrCodeCreateUtil.fileType);
				QrCodeCreateUtil.createQrCode(new FileOutputStream(f), QrCodeCreateUtil.activityJoinUrl + activityId,
						1200, "JPEG");

				CustomFormModel cModel = new CustomFormModel();
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
				
				String qrFileId = cModel.getPrimaryFieldValue();
				
				cModel = new CustomFormModel();
				cModel.setSqlId("khAdmin/activityManage/UpdateQrIdById");
				logData = new HashMap<>();
				logData.put("qrFileId", qrFileId);
				logData.put("id", activityId);
				cModel.setData(logData);
				formMapper.saveCustom(cModel);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("生成线下活动二维码失败", e);
			}
		}
		
		try {
			String content = map.get("content") == null ? "" : map.get("content").toString();
			backEndVideoExecute.infoOrActivityExecute("activity", content, activityId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("处理后台资讯视频转图片失败", e);
		}
		
	}

	private String getFileName() {
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sb = new StringBuffer();
		sb.append(fileUUID);
		return sb.toString();
	}
}
