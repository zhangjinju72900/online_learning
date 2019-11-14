package com.tedu.plugin.information;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.business.keyword.service.KeyWordService;
import com.tedu.common.error.ExErrorCode;
import com.tedu.common.util.PicUtil;
import com.tedu.component.FrontEndVideoExecute;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("saveInfoPlugin")
public class SaveInfoPlugin implements ILogicPlugin {
	@Resource
	FormService formService; 
	
	@Resource
	com.tedu.component.EasemobComponent easemobComponent;
	
	@Resource
	private FormMapper formMapper;

	@Resource
	private OssQueryService ossQueryServiceImpl2;

	@Resource
	private OssUploadService ossUploadServiceImpl2;

	@Resource
	private KeyWordService keyWordServiceImpl;
	
	@Resource
	private FrontEndVideoExecute frontEndVideoExecute;

	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;

	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String, Object> o = requestObj.getData();
		if (o.get("longitude") == null || StringUtils.isBlank(o.get("longitude").toString())) {
			o.put("longitude", 0);
		}
		if (o.get("latitude") == null || StringUtils.isBlank(o.get("latitude").toString())) {
			o.put("latitude", 0);
		}
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		log.info(formModel.getData());
		try {

			Map<String, Object> map = (Map<String, Object>) formModel.getData();

			String createBy = map.get("createBy") == null ? "" : map.get("createBy").toString();
			String title = map.get("title") == null ? "" : map.get("title").toString();
			String activityId = map.get("activityId") == null ? "0" : map.get("activityId").toString();
			String source = map.get("source") == null ? "" : map.get("source").toString();
			String fileIds = map.get("fileIds") == null ? "" : map.get("fileIds").toString();
			String releaseAddress = map.get("releaseAddress") == null ? "" : map.get("releaseAddress").toString();
			String longitude = map.get("longitude") == null ? "0" : map.get("longitude").toString();
			String latitude = map.get("latitude") == null ? "0" : map.get("latitude").toString();

			if (keyWordServiceImpl.checkTitle(title)) {
				responseObj.setCode(ExErrorCode.KEY_WORD.getCode());
				responseObj.setMsg(ExErrorCode.KEY_WORD.getMsg());
				return;
			}

			CustomFormModel cModel = new CustomFormModel();
			cModel.setSqlId("khApp/discover/information/insertInfo");
			Map<String, Object> data = new HashMap<>();
			data.put("createBy", createBy);
			data.put("title", title);
			data.put("activityId", activityId);
			data.put("source", source);
			data.put("releaseAddress", releaseAddress);
			data.put("longitude", longitude);
			data.put("latitude", latitude);
			cModel.setData(data);
			formMapper.saveCustom(cModel);

			String pk = cModel.getPrimaryFieldValue();

			if (StringUtils.isNotBlank(fileIds)) {

				String[] fileIdArr = fileIds.split(",");

				StringBuffer sb = new StringBuffer();
				sb.append(title);
				
				for (int i = 0; i < fileIdArr.length; i++) {
					if(i==0){
						sb.append("<br>");
					}
					
					String fileId = fileIdArr[i];
					
					Map<String, String> fileMap = ossUploadServiceImpl2.getFilePathById(fileId);

					if (fileMap != null && "mp4".equals(fileMap.get("fileType"))) {
						String url = getUrlByFileId(fileId);
						
						sb.append("").append("<img src=\"").append(fileMap.get("description")).append("\"").append(" videosrc=\"").append(url).append("\" />")
						.append("");
						
						frontEndVideoExecute.infoVideoPicExecute(pk, fileMap.get("fullpath"));
					}

					if (fileMap != null && "jpg,png,bmp,jpeg".contains(fileMap.get("fileType"))) {

						String url = getUrlByFileId(fileId);

						sb.append("<img src=").append("\"").append(url).append(PicUtil.OSS_PIC_EXECUTE_SUFFIX).append("\" alt=\"\" style=\"\">");

						insertInfoPic(pk, fileId);
						
					}
				}

				if (StringUtils.isNotBlank(sb.toString())) {
					cModel = new CustomFormModel();
					
					
					cModel.setSqlId("khApp/discover/information/updateInfo");
					data = new HashMap<>();
						
					data.put("fileId", 0);

					data.put("id", pk);
					data.put("content", sb.toString());
					cModel.setData(data);
					formMapper.saveCustom(cModel);
				}
				//获取含有资讯审核菜单权限的用户
				QueryPage qp = new QueryPage();
				qp.setParamsByMap(map);
				qp.setQueryParam("QryResourceUser");
				List<Map<String, Object>> userList = formService.queryBySqlId(qp);
				if(userList.size()>0) {
					//获取该资讯信息
						//新增notice
						Map<String,Object> noticeMap = new HashMap<String, Object>();
						noticeMap.put("title", SessionUtils.getUserInfo().getNickName()+"发布了动态");
						noticeMap.put("context", SessionUtils.getUserInfo().getNickName()+"发布了动态,请在后台管理→资讯管理→审核管理中审核");
						noticeMap.put("updateBy",SessionUtils.getUserInfo().getUserId());
						cModel.setSqlId("InsertNoticeInformation");
						cModel.setData(noticeMap);
						formMapper.saveCustom(cModel);
						//获取该消息通知
						qp.setQueryParam("QryNoticeByMaxId");
						List<Map<String, Object>> noticeList = formService.queryBySqlId(qp);
						if(noticeList.size()>0) {
							String noticeId = noticeList.get(0).get("id").toString();
							noticeMap.put("baseId", noticeId);
							//插入明细表
							cModel.setSqlId("khAdmin/noticeManage/insertMessageRecord");
							String array = "";
							for(int i=0;i<userList.size();i++) {
								Map<String,Object> userMap = userList.get(i);
								if(userMap!=null&&userMap.get("id")!=null){
									String userId = userMap.get("id").toString();
									String easeUserName = userMap.get("easeUserName").toString();
									array = array + easeUserName +",";
									noticeMap.put("receiverId", userId);
									noticeMap.put("userId", 2);
									cModel.setData(noticeMap);
									formMapper.saveCustom(cModel);
									//发送通知
								     title = SessionUtils.getUserInfo().getNickName()+"发布了动";
								    String context = SessionUtils.getUserInfo().getNickName()+"发布了动态,请在后台管理→资讯管理→审核管理中审核";
								    if(userList.size()<20&&i==(userList.size()-1)) {
								    	easemobComponent.sendNotice(noticeId, array.split(","), title, context);
								    	array = "";
								    }else if(userList.size()%20==0||i==(userList.size()-1)){
								    	easemobComponent.sendNotice(noticeId, array.split(","), title, context);
								    	array = "";
								    }
								}
								
							}
							
							
				}
			}
				}
		} catch (Exception e) {
			responseObj.setCode(ExErrorCode.SAVE_INFO_FAIL.getCode());
			responseObj.setMsg(ExErrorCode.SAVE_INFO_FAIL.getMsg());
			e.printStackTrace();
			log.error("发布动态/晒图失败", e);
		}

	}
	
	
	
	

	private void insertInfoPic(String pk, String fileId) {

		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/discover/information/insertPic");
		Map<String, Object> data = new HashMap<>();
		data.put("informationId", pk);
		data.put("picId", fileId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		
	}

	private String getUrlByFileId(String fileId) {

		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khApp/discover/information/QryOssRecord");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khApp/discover/information/QryOssRecord");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("fileId", fileId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		if (tables != null && tables.size() > 0) {
			return getUrlByKey(tables.get(0).get("oss_key").toString());
		}
		return null;
	}

	private String getUrlByKey(String key) {
		return ossQueryServiceImpl2.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
	}
	
}
