package com.tedu.plugin.activity;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.constant.SendMsgTypeEnum;
import com.tedu.common.error.ExErrorCode;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;
import com.tedu.component.SendMsgComponent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("ActivityApplyPlugin")
public class ActivityApplyPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	@Resource
	private IntegralChangeComponent integralChangeComponent;
	
	@Resource
	private SendMsgComponent sendMsgComponent;

	private String sqlTemplate = "khApp/discover/activity/insertActivityApply";
	private String sqlUpdate = "khApp/discover/activity/ActivityApply";
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		
		return null;
		
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		
		Map<String, Object> activityMap = (Map<String, Object>) formModel.getData();
		long userId=activityMap.get("createBy")==null?0:Long.parseLong(activityMap.get("createBy").toString());
		String activityId=(String) activityMap.get("id");
		QueryPage queryPage=new QueryPage();
		Map requestMap=new HashMap<String,Object>();
		requestMap.put("userId", userId);
		activityMap.put("id", activityId);
		//将当前用户的角色解析封装到数组方便后面判断
		//查询当前活动的参与角色
		requestMap.put("activityId", activityId);
		queryPage.setDataByMap(requestMap);
		queryPage.setQueryParam("khApp/discover/activity/QryActivityRoles");
		List<Map<String,Object>> activityRoleList=formMapper.query(queryPage);
		
		//查询当前用户所在地区
		queryPage.setQueryParam("khApp/discover/activity/QryUserRoles");
		List<Map<String,Object>> userRoles=formMapper.query(queryPage);
		
		//查询当前活动的参与地区
		queryPage.setQueryParam("khApp/discover/activity/QryActivityRegions");
		List<Map<String,Object>> activityRegionList=formMapper.query(queryPage);
		//查询当前用户所在地区
		queryPage.setQueryParam("khApp/discover/activity/QryUserRegions");
		List<Map<String,Object>> UserRegionList=formMapper.query(queryPage);
		//查询当用户可用总积分
		queryPage.setQueryParam("khApp/discover/activity/QryUserIntegral");
		List<Map<String,Object>> UserIntegralList=formMapper.query(queryPage);
		//查询参加活动需扣除的积分
		queryPage.setQueryParam("khApp/discover/activity/QryActivityIntegral");
		List<Map<String,Object>> ActivityIntegralList=formMapper.query(queryPage);
		//判断用户角色和所在地区是否满足活动条件
		boolean isRightRole=checkRole(activityRoleList,userRoles);
		boolean isRightRegion=checkRegion(activityRegionList,UserRegionList);
		boolean isRightIntegral=checkIntegral(UserIntegralList,ActivityIntegralList);
		if(isRightRole==false || isRightRegion==false){
			responseObj.setCode(ExErrorCode.JOIN_ACTIVITY_NO_PERMIS.getCode());
			responseObj.setMsg(ExErrorCode.JOIN_ACTIVITY_NO_PERMIS.getMsg());
			return ;
		}
		if(isRightIntegral==false){
			responseObj.setCode(ExErrorCode.JOIN_ACTIVITY_LACK_INTEGRAL.getCode());
			responseObj.setMsg(ExErrorCode.JOIN_ACTIVITY_LACK_INTEGRAL.getMsg());
			return ;
		}
		
		

		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();

		CustomFormModel cModel = new CustomFormModel();
		
		cModel.setSqlId(sqlUpdate);
		cModel.setData(formModel.getData());
		int inter =  formMapper.saveCustom(cModel);
		
		
		if (inter == 1) {
			cModel.setSqlId(sqlTemplate);

			cModel.setData(formModel.getData());

			Map<String, Object> logData = formModel.getData();

			LogUtil.info(cModel.getData().toString());

			formMapper.saveCustom(cModel);
			integralChangeComponent.setIntegralChange(IntegralEnum.APPLY_ACTIVITY,
					new IntegralChangeModel(cModel.getPrimaryFieldValue()));
			
			//给参加活动者发送消息
			map.put("baseId", cModel.getPrimaryFieldValue());
			sendMsgComponent.sendMsg(SendMsgTypeEnum.ACTIVITY_NOTICE, map);
		}
	}
	
	//判断当前用户的角色是否符合活动
	public static boolean checkRole(List<Map<String,Object>> activityRoleList, List<Map<String,Object>> userRoles){
		if(activityRoleList == null || activityRoleList.size() == 0){
			return true;
		}
		for(Map<String,Object> roleMap:activityRoleList){
			for(Map<String,Object> userRoleName:userRoles ){
				if(userRoleName.get("roleId").toString().equals(roleMap.get("roleId").toString())){
					return true;
				}
			}
		}
		return false;
	}
	//判断当前用户所在地区是否符合活动
	public static boolean checkRegion(List<Map<String,Object>> activityRegionList,List<Map<String,Object>> UserRegionList){
		if(activityRegionList == null || activityRegionList.size() == 0){
			return true;
		}
		String userClass = UserRegionList.get(0).get("classId")!=null?UserRegionList.get(0).get("classId").toString():"";
		String userSchool = UserRegionList.get(0).get("schoolId")!=null?UserRegionList.get(0).get("schoolId").toString():"";
		String userRegionId = UserRegionList.get(0).get("regionId")!=null?UserRegionList.get(0).get("regionId").toString():"";
		if("".equals(userClass)||"".equals(userSchool)||"".equals(userRegionId)) {
			return false;
		}
		boolean flag = false;
		
		StringBuffer regionBuffer = new StringBuffer();
		StringBuffer schoolBuffer = new StringBuffer();
		StringBuffer classBuffer = new StringBuffer();
		for(Map<String,Object> regionMap:activityRegionList){
			String regionId = regionMap.get("regionId")==null?"":regionMap.get("regionId").toString();
			String schoolId = regionMap.get("schoolId")!=null?regionMap.get("schoolId").toString():"";
			String classId = regionMap.get("classId")!=null?regionMap.get("classId").toString():"";
			if(!"".equals(regionId)) {
				regionBuffer.append(",").append(regionId);
			}
			if(!"".equals(schoolId)) {
				schoolBuffer.append(",").append(schoolId);
			}
			if(!"".equals(classId)) {
				classBuffer.append(",").append(classId);
			}
			
		}
		if(regionBuffer.length()>0&&regionBuffer.substring(1, regionBuffer.length()).split(",").length>1) {
			String [] reginArray = regionBuffer.substring(1, regionBuffer.length()).split(",");
			for(String r:reginArray) {
				if(r.equals(userRegionId)) {
					flag = true;
					break;
				}
			}
		}else if(regionBuffer.length()>0&&regionBuffer.toString().substring(1, regionBuffer.length()).split(",").length==1) {
			if(regionBuffer.toString().substring(1, regionBuffer.length()).equals(userRegionId)) {
				if(schoolBuffer.length()==0) {
					flag = true;
					return flag;
				}else if(schoolBuffer.substring(1,schoolBuffer.length()).split(",").length>1) {
					String [] schoolArray =schoolBuffer.substring(1,schoolBuffer.length()).split(",");
					for(String r:schoolArray) {
						if(r.equals(userSchool)) {
							if(classBuffer.length()==0) {
								flag = true;
								return flag;
							}else {
								String [] classArray =classBuffer.substring(1,classBuffer.length()).split(",");
								for(String c:classArray) {
									if(c.equals(userClass)) {
										flag = true;
										return flag;
									}
								}
							}
						}
					}
				}
			}
			
		}
		return flag;
	}
	//判断当前用户积分是否足够
		public static boolean checkIntegral(List<Map<String,Object>> UserIntegralList,List<Map<String,Object>> ActivityIntegralList){
			if(ActivityIntegralList == null){
				return true;
			}
			Long userIntegral=Long.parseLong(UserIntegralList.get(0).get("integral").toString());
			Long activityIntegral=Long.parseLong(ActivityIntegralList.get(0).get("join_integral").toString());
			if(userIntegral>=activityIntegral){
				return true;
			}
			return false;
		}	
}
