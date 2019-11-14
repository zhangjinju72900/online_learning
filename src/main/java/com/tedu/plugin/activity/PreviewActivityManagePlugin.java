package com.tedu.plugin.activity;

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
import com.tedu.component.BackEndVideoExecute;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 活动预览
 * 
 * @author quancong
 *
 */
@Service("previewActivityManagePlugin")
public class PreviewActivityManagePlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	FormMapper formMapper = SpringUtils.getBean("simpleDao");

	@Resource
	private BackEndVideoExecute backEndVideoExecute;

	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;

	private String sqlTemplate = "khAdmin/activityManage/SaveActivityRegion";

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {

		// 预览时发布状态 设为0
		formModel.getData().put("eq_releaseStatus", 0);
		formModel.getData().put("releaseStatus", 0);
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		/*
		 * String content =
		 * map.get("content")==null?"":map.get("content").toString();
		 * List<String> urls = new ArrayList<String>(); String reg =
		 * "<source[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>"; Matcher m =
		 * Pattern.compile(reg).matcher(content); while (m.find()) { String r =
		 * m.group(1); urls.add(r); try { String imgPath = "123"; content =
		 * content.replaceAll("<video[^>]*?>[\\s\\S]*?<source src=\""
		 * +r+"\"[\\s\\S]*?<\\/video>","<img src=\""+imgPath+"\" videosrc=\""
		 * +r+"\"/>"); } catch (Exception e) { e.printStackTrace(); } }
		 * formModel.getData().put("content",content);
		 */
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
		String schoolId = map.get("eq_school")+"";
		String classId = map.get("classId")+"";

		if ("Edit".equals(mode)) {
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
		if (StringUtils.isNotBlank(regions)) {
			if (regions.startsWith("[") && regions.endsWith("]")) {
				regions = regions.substring(1, regions.length() - 1);
			}
			// t_activity_join_region
			String[] regionArray = regions.split(",");
			for (String id : regionArray) {
				if (StringUtils.isNotBlank(id)) {
					CustomFormModel cModel = new CustomFormModel();
					cModel.setSqlId(sqlTemplate);
					cModel.setData(formModel.getData());
					Map<String, Object> logData = formModel.getData();

					logData.put("id", activityId);
					logData.put("regionId", id);
					if("".equals(schoolId)&&"".equals(classId)){
						logData.put("schoolId", null);
						logData.put("classId", null);
					}else if("".equals(classId)&&!"".equals(schoolId)){
						logData.put("schoolId", Integer.parseInt(schoolId));
						logData.put("classId", null);
					}else if(!"".equals(classId)&&!"".equals(schoolId)){
						logData.put("schoolId", Integer.parseInt(schoolId));
						logData.put("classId", Integer.parseInt(classId));
					}
					LogUtil.info(cModel.getData().toString());
					formMapper.saveCustom(cModel);
				}
			}
		}

		if ("Edit".equals(mode)) {
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
		Map<String, Object> map1 = requestObj.getData();
		String fileId = map1.get("fileId") == null ? "" : map1.get("fileId").toString();
		log.info("fileId" + fileId);
		String fileName = map.get("fileId") == null ? "" : map.get("fileId").toString();
		String fileType = "";
		if (fileName.length() > 0) {
			fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", activityId);
		responseObj.setData(map2);

		try {
			String content = map.get("content") == null ? "" : map.get("content").toString();
			backEndVideoExecute.infoOrActivityExecute("activity", content, activityId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("处理后台资讯视频转图片失败", e);
		}

	}
}
