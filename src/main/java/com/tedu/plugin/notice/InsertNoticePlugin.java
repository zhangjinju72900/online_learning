package com.tedu.plugin.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
/**
 * 在保存t_notice_user_role、t_notice_region
 * @author quancong
 *
 */

@Service("insertNoticePlugin")
public class InsertNoticePlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Resource
	com.tedu.component.EasemobComponent easemobComponent;
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		
		return null;
	
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  //获取主表的id
		  String noticeId=formModel.getPrimaryFieldValue().toString();
		  //获取roleid并解析
		  String roleIds = map.get("eq_role")==null?"":map.get("eq_role").toString();
		  String[] roleIdAr=roleIds.startsWith("[")?roleIds.substring(1, roleIds.length()-1).split(","):roleIds.split(",");
		  
		  String regionIds = map.get("eq_area")==null?"":map.get("eq_area").toString();
		  String[] regionIdAr=regionIds.startsWith("[")?regionIds.substring(1, regionIds.length()-1).split(","):regionIds.split(",");
		  
		  String mode = map.get("Mode")==null?"":map.get("Mode").toString();
		  //为编辑模式时先将原来的主表id对应的记录然后重新插入
		  Map<String, Object> logData = new HashMap<>();
		  
		  logData.put("noticeId", noticeId);
		  
		  if("Edit".equals(mode)){
			  String updateSql = "khAdmin/noticeManage/deleteNoticeRegionByNoticeId";
			  CustomFormModel cModel = new CustomFormModel();
			  
			  cModel.setSqlId(updateSql);
			  cModel.setData(logData);
			  
			  LogUtil.info(cModel.getData().toString());
			  formMapper.saveCustom(cModel);
			  
			  updateSql = "khAdmin/noticeManage/deleteNoticeRoleByNoticeId";
			  cModel.setSqlId(updateSql);
			  LogUtil.info(cModel.getData().toString());
			  formMapper.saveCustom(cModel);
			  //删除对应附件
			  updateSql = "khAdmin/noticeManage/deleteFileByNoticeId";
			  cModel.setSqlId(updateSql);
			  LogUtil.info(cModel.getData().toString());
			  formMapper.saveCustom(cModel);
		  }
		// 获取附件ID
			String [] fileIds = map.get("fileId") != null ? map.get("fileId").toString().split(",") :"".split(",");
			  CustomFormModel cModel = new CustomFormModel();
			for(String fileId:fileIds) {
				if (!"".equals(fileId)) {
					map.put("noticeId", noticeId);
					map.put("fileId", fileId);
					cModel.setData(map);
					cModel.setSqlId("khTeacher/systemNotice/InsertNoticeFile");
					formMapper.saveCustom(cModel);
				}
			}
		  //新增模式时将角色关系插入t_notice_user_role中
		  cModel.setSqlId("khAdmin/noticeManage/insertNoticeRole");
		  //当未选取roleId时，不执行插入
		  for (String roleId : roleIdAr) {
			  if(!(roleId.length()<=0)){
				  logData.put("roleId", roleId);
				  cModel.setData(logData);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }
			
		  }
		  
		  
		  //新增模式时将角色关系插入t_notice_region中
		  cModel = new CustomFormModel();
		  cModel.setSqlId("khAdmin/noticeManage/insertNoticeRegion");
		  //当未选取roleId时，不执行插入
		  for (String regionId : regionIdAr) {
			  if(StringUtils.isNotBlank(regionId)){
				  logData.put("regionId", regionId);
				  cModel.setData(logData);
				  LogUtil.info(cModel.getData().toString());
				  formMapper.saveCustom(cModel);
			  }
			
		  }
		  
		  
		  
		  String releaseStatus = map.get("releaseStatus")==null?"":map.get("releaseStatus").toString();
		  if("1".equals(releaseStatus)){
			  
			  String title = map.get("title")==null?"":map.get("title").toString();
			  String context = map.get("context")==null?"":map.get("context").toString();
			  List<Map<String, Object>> school = map.get("school")==null?null:(List<Map<String, Object>>)map.get("school");
			  easemobComponent.sendNotice(noticeId, roleIdAr, regionIdAr, school, title, context);
		  }
		

	}

}
