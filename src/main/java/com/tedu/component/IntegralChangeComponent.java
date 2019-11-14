package com.tedu.component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.common.constant.ActivityTypeEnum;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
/**
 * @ClassName:  IntegralChangeComponent   
 * @Description:TODO 积分变动组件   
 * @author: qun
 * @date:   2018年11月19日 下午2:18:50   
 *
 */
@Component
public class IntegralChangeComponent {
	
	@Resource
	private FormMapper formMapper;	
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	public void setIntegralChange(IntegralEnum in, IntegralChangeModel model){
		switch(in){
			case SIGN_IN://签到
//				generateSignIn(model);
				break;
			case INFO_FORWARD://分享资讯
				generateInfoForward(model);
				break;
			case TOP_INFO_REVIEW://优质资讯评论
				generateTopReview(model);
				break;
			case TOP_INFO://置顶资讯
				generateTopInfo(model);
				break;	
			case RECOMMEND_INFO://推荐资讯
				generateRecommendInfo(model);
				break;
			case APPLY_ACTIVITY://报名活动相关
				generateApplyActivity(model);
				break;
			case JOIN_ACTIVITY://参加活动相关
				generateJoinActivity(model);
				break;
			case ACTIVITY_PIC://参加线上活动晒图并审核通过
				generateActivityPic(model);
				break;	
			case BINDING_TEL://绑定手机
				generateBindingTel(model);
				break;
			case PERFECT_INFO://完善个人资料
				generatePerfectInfo(model);
				break;
			case MAINTAIN://积分维护
				generateMaintain(model);	
		}
	}
	
	//积分维护
	private void generateMaintain(IntegralChangeModel model) {
		if(model.getIntegral().indexOf("-")>-1){
			String integral = model.getIntegral().replace("-", "");
			addCustomerUserIntegralSub(Integer.parseInt(integral), model.getUserId());
			insertCustomerUserRecord(IntegralEnum.MAINTAIN, model, Integer.parseInt(integral)*-1);
		}else{
			addCustomerUserIntegralAdd(Integer.parseInt(model.getIntegral()), model.getUserId());
			insertCustomerUserRecord(IntegralEnum.MAINTAIN, model, Integer.parseInt(model.getIntegral()));
		}
		
	}

	//完善资料
	private void generatePerfectInfo(IntegralChangeModel model) {
		if(isFirstBindingTel(IntegralEnum.PERFECT_INFO.getCode())){//判断是否第一次完善
			model.setUserId(SessionUtils.getUserInfo().getUserId()+"");
			addCustomerUserIntegralAdd(IntegralEnum.PERFECT_INFO.getIntegral(), model.getUserId());//增加user积分数
			insertCustomerUserRecord(IntegralEnum.PERFECT_INFO, model, null);
		}	
	}

	//绑定手机
	private void generateBindingTel(IntegralChangeModel model) {
		if(isFirstBindingTel(IntegralEnum.BINDING_TEL.getCode())){//判断是否第一次绑定
			model.setUserId(SessionUtils.getUserInfo().getUserId()+"");
			addCustomerUserIntegralAdd(IntegralEnum.BINDING_TEL.getIntegral(), model.getUserId());//增加user积分数
			insertCustomerUserRecord(IntegralEnum.BINDING_TEL, model, null);
		}
	}

	//参加活动
	private void generateJoinActivity(IntegralChangeModel model) {
		Map<String, Object> map = queryActivityByJoin(model);//按参加活动关联查询活动明细
		if(map != null){
			String activityType = map.get("activity_type")==null?"":map.get("activity_type").toString();
			String joinBy = map.get("join_by")==null?"":map.get("join_by").toString();
			model.setUserId(joinBy);
			//线下活动：报名减积分（-joinIntegral），参加活动加积分（+integral）
			//直播：报名参加减积分数（-joinIntegral），未报名参加减积分数（-integral）
			Integer joinIntegral = map.get("join_integral")==null?0:Integer.parseInt(map.get("join_integral").toString());
			Integer integral = map.get("integral")==null?0:Integer.parseInt(map.get("integral").toString());
			if(ActivityTypeEnum.OFFLINE.getCode().equals(activityType)){//线下活动
				addCustomerUserIntegralAdd(integral, model.getUserId());
				insertCustomerUserRecord(IntegralEnum.JOIN_ACTIVITY, model, integral);
			}else if(ActivityTypeEnum.LIVE.getCode().equals(activityType)){//直播
				String activityId = map.get("activity_id")==null?"":map.get("activity_id").toString();
				if(isApplyActivity(model, activityId)){//查询是否报名
					addCustomerUserIntegralSub(joinIntegral, model.getUserId());
					insertCustomerUserRecord(IntegralEnum.JOIN_ACTIVITY, model, joinIntegral*-1);
				}else{
					addCustomerUserIntegralSub(integral, model.getUserId());
					insertCustomerUserRecord(IntegralEnum.JOIN_ACTIVITY, model, integral*-1);
				}
			}
		}
	}
	
	//活动晒图
	private void generateActivityPic(IntegralChangeModel model) {
		Map<String, Object> map = queryActivityPicData(model);
		if(map != null){

			Integer count = map.get("count")==null?0:Integer.parseInt(map.get("count").toString());
			String activityType = map.get("activity_type")==null?"":map.get("activity_type").toString();
			
			if(ActivityTypeEnum.ONLINE.getCode().equals(activityType) && count < 2){//线上活动,且第一次晒图
				String releaseBy = map.get("release_by")==null?"":map.get("release_by").toString();
				model.setUserId(releaseBy);
				//线下活动：报名减积分（-joinIntegral），参加活动加积分（+integral）
				//直播：报名参加减积分数（-joinIntegral），未报名参加减积分数（-integral）
				Integer joinIntegral = map.get("join_integral")==null?0:Integer.parseInt(map.get("join_integral").toString());
				Integer integral = map.get("integral")==null?0:Integer.parseInt(map.get("integral").toString());
				if(!queryActivityUser(map,model)){
					integral=Integer.valueOf((int) Math.rint(integral.intValue()/2));
				}
				addCustomerUserIntegralAdd(integral, model.getUserId());
				insertCustomerUserRecord(IntegralEnum.JOIN_ACTIVITY, model, integral);
			}
		}
	}


	//报名活动
	private void generateApplyActivity(IntegralChangeModel model) {
		Map<String, Object> map = queryActivityApply(model);//按报名活动关联查询活动明细
		if(map != null){
			String activityType = map.get("activity_type")==null?"":map.get("activity_type").toString();
			String applyBy = map.get("apply_by")==null?"":map.get("apply_by").toString();
			model.setUserId(applyBy);
			//线下活动：报名减积分（-joinIntegral），参加活动加积分（+integral）
			//直播：报名参加减积分数（-joinIntegral），未报名参加减积分数（-integral）
			Integer joinIntegral = map.get("join_integral")==null?0:Integer.parseInt(map.get("join_integral").toString());
			Integer integral = map.get("integral")==null?0:Integer.parseInt(map.get("integral").toString());
			if(ActivityTypeEnum.OFFLINE.getCode().equals(activityType) || ActivityTypeEnum.ONLINE.getCode().equals(activityType)){//线下活动、线上
				addCustomerUserIntegralSub(joinIntegral, model.getUserId());
				insertCustomerUserRecord(IntegralEnum.APPLY_ACTIVITY, model, joinIntegral*-1);
			}
		}	
	}

	//推荐资讯
	private void generateRecommendInfo(IntegralChangeModel model) {
		Map<String, Object> map = queryInfoById(model);//按资讯ID查资讯详情
		if(map != null){
			String source = map.get("source")==null?"":map.get("source").toString();
			if(!"1".equals(source)){
				String releaseBy = map.get("release_by")==null?"":map.get("release_by").toString();
				model.setUserId(releaseBy);
				addCustomerUserIntegralAdd(IntegralEnum.RECOMMEND_INFO.getIntegral(), model.getUserId());//增加user积分数
				insertCustomerUserRecord(IntegralEnum.RECOMMEND_INFO, model, null);
			}
		}
	}

	//置顶资讯
	private void generateTopInfo(IntegralChangeModel model) {
		Map<String, Object> map = queryInfoById(model);//按资讯ID查资讯详情
		if(map != null){
			String source = map.get("source")==null?"":map.get("source").toString();
			if(!"1".equals(source)){
				String releaseBy = map.get("release_by")==null?"":map.get("release_by").toString();
				model.setUserId(releaseBy);
				addCustomerUserIntegralAdd(IntegralEnum.TOP_INFO.getIntegral(), model.getUserId());//增加user积分数
				insertCustomerUserRecord(IntegralEnum.TOP_INFO, model, null);
			}
		}	
	}

	//置顶资讯评论
	private void generateTopReview(IntegralChangeModel model) {
		Map<String, Object> map = queryInfoReviewById(model);//按资讯ID查资讯详情
		if(map != null){
			String reviewBy = map.get("review_by")==null?"":map.get("review_by").toString();
			model.setUserId(reviewBy);
			addCustomerUserIntegralAdd(IntegralEnum.TOP_INFO_REVIEW.getIntegral(), model.getUserId());//增加user积分数
			insertCustomerUserRecord(IntegralEnum.TOP_INFO_REVIEW, model, null);
		}	
	}

	//转发
	private void generateInfoForward(IntegralChangeModel model) {
		Map<String, Object> map = queryInfoForwardById(model);//按分享ID查资讯详情、被推荐的资讯才有积分
		if(map != null){
			String forwardBy = map.get("forward_by")==null?"":map.get("forward_by").toString();
			model.setUserId(forwardBy);
			if(checkGenerateInfoForward(IntegralEnum.INFO_FORWARD.getCode(), model)){//id是否存在及每天两次校验
				addCustomerUserIntegralAdd(IntegralEnum.INFO_FORWARD.getIntegral(), model.getUserId());//增加user积分数
				insertCustomerUserRecord(IntegralEnum.INFO_FORWARD, model, null);
			}
		}
	}
	

	/**
	 * @Title: isApplyActivity   
	 * @Description: TODO 查询用户是否报名该活动
	 * @author: qun 
	 * @param: @param model
	 * @param: @param activityId
	 * @param: @return      
	 * @return: boolean 
	 * @date:   2018年8月2日 下午2:16:31     
	 * @throws
	 */
	private boolean isApplyActivity(IntegralChangeModel model, String activityId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryActivityApplyByAcIdUid");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryActivityApplyByAcIdUid");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("activityId", activityId);
		paramMap.put("applyBy", model.getUserId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		if(tables == null || tables.size() < 1){
			return false;
		}
		return true;
	}
	
	
	/**
	 * @Title: isFirstBindingTel   
	 * @Description: TODO是否第一次绑定手机
	 * @author: qun 
	 * @param: @return      
	 * @return: boolean 
	 * @date:   2018年9月5日 下午2:01:49     
	 * @throws
	 */
	private boolean isFirstBindingTel(int changeType) {
		if(SessionUtils.getUserInfo() == null){
			return false;
		}
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryUserIntegralByUId");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryUserIntegralByUId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", SessionUtils.getUserInfo().getUserId());
		paramMap.put("changeType", changeType);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()==0?true:false;
	}

	
	
	/**
	 * @Title: queryActivityApply   
	 * @Description: TODO 根据活动报名明细ID查询活动数据
	 * @author: qun 
	 * @param: @param model
	 * @param: @return      
	 * @return: Map<String,Object> 
	 * @date:   2018年8月2日 下午2:16:57     
	 * @throws
	 */
	private Map<String, Object> queryActivityApply(IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryActivityApplyById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryActivityApplyById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", model.getId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()<1?null:tables.get(0);
	}
	
	/**
	 * @Title: queryActivityByJoin   
	 * @Description: TODO 根据参加明细查询活动数据
	 * @author: qun 
	 * @param: @param model
	 * @param: @return      
	 * @return: Map<String,Object> 
	 * @date:   2018年8月2日 下午2:17:40     
	 * @throws
	 */
	private Map<String, Object> queryActivityByJoin(IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryActivityJoinById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryActivityJoinById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", model.getId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()<1?null:tables.get(0);
	}
	
	/**
	 * @Title: queryActivityPicData   
	 * @Description: TODO根据资讯ID查询晒图及活动相关数据
	 * @author: qun 
	 * @param: @param model
	 * @param: @return      
	 * @return: Map<String,Object> 
	 * @date:   2018年11月26日 下午5:57:57     
	 * @throws
	 */
	private Map<String, Object> queryActivityPicData(IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryInfoActivityById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryInfoActivityById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", model.getId());
		paramMap.put("userId", model.getUserId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()!=1?null:tables.get(0);
	}
	
	/**
	 * @Title: queryInfoById   
	 * @Description: TODO 根据资讯ID查资讯
	 * @author: qun 
	 * @param: @param model
	 * @param: @return      
	 * @return: Map<String,Object> 
	 * @date:   2018年8月2日 下午5:32:51     
	 * @throws
	 */
	private Map<String, Object> queryInfoById(IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryInfoById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryInfoById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", model.getId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()<1?null:tables.get(0);
	}
	
	/**
	 * @Title: queryInfoReviewById   
	 * @Description: TODO 根绝ID查询 资讯表-评论明细
	 * @author: qun 
	 * @param: @param model
	 * @param: @return      
	 * @return: Map<String,Object> 
	 * @date:   2018年8月2日 下午5:33:26     
	 * @throws
	 */
	private Map<String, Object> queryInfoReviewById(IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryInfoReviewById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryInfoReviewById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", model.getId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()<1?null:tables.get(0);
	}
	
	/**
	 * @Title: queryInfoForwardById   
	 * @Description: TODO 根据id查询资讯转发明细
	 * @author: qun 
	 * @param: @param model
	 * @param: @return      
	 * @return: Map<String,Object> 
	 * @date:   2018年8月2日 下午5:40:48     
	 * @throws
	 */
	private Map<String, Object> queryInfoForwardById(IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryInfoForwardById");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryInfoForwardById");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", model.getId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables==null||tables.size()<1?null:tables.get(0);
	}


	/**
	 * @Title: checkGenerateInfoForward   
	 * @Description: TODO 根据userId、积分变动类型查询用户积分明细表
	 * @author: qun 
	 * @param: @param code
	 * @param: @param model
	 * @param: @return      
	 * @return: boolean 
	 * @date:   2018年8月2日 下午2:19:52     
	 * @throws
	 */
	private boolean checkGenerateInfoForward(Integer code, IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryIntegralRecordByUserId");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryIntegralRecordByUserId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", model.getUserId());
		paramMap.put("changeType", code);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		if(tables == null || tables.size() < 2){
			return true;
		}
		return false;
	}

	/**
	 * @Title: insertCustomerUserRecord   
	 * @Description: TODO 保存用户积分明细表数据 
	 * @author: qun 
	 * @param: @param in
	 * @param: @param m      
	 * @return: void 
	 * @date:   2018年8月2日 下午2:20:29     
	 * @throws
	 */
	private void insertCustomerUserRecord(IntegralEnum in, IntegralChangeModel m, Integer integral) {
		TableModel model = new TableModel() {

			@Override
			public String getTableName() {
				return "t_customer_user_integral_record";
			}

			@Override
			public String[] getFields() {
				String[] str = { "customer_user_id", "base_order_id", "base_order_type", "integral", "change_type", "change_time", "remark",
						"create_time", "create_by", "update_time", "update_by" };
				return str;
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("customerUserId", m.getUserId());
		map.put("baseOrderId", m.getId());
		map.put("baseOrderType", in.getCode());
		map.put("integral", integral==null?in.getIntegral():integral);
		map.put("changeType", in.getCode());
		map.put("changeTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("remark", m.getRemark()==null?"":m.getRemark());
		map.put("createTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("createBy", SessionUtils.getUserInfo().getUserId());
		map.put("updateTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("updateBy", SessionUtils.getUserInfo().getUserId());
		SimpleFormModel simpleModel = new SimpleFormModel(model, map);
		int s = formMapper.insert(simpleModel);
		if (s != 1) {
			log.error("新增用户积分明细表数据失败！baseOrderType:"+in.getCode()+";baseOrderId:"+m.getId());
		}
	}

	/**
	 * @Title: addCustomerUserIntegralAdd   
	 * @Description: TODO 增加用户表积分
	 * @author: qun 
	 * @param: @param integral
	 * @param: @param userId      
	 * @return: void 
	 * @date:   2018年8月2日 下午2:20:49     
	 * @throws
	 */
	private void addCustomerUserIntegralAdd(Integer integral, String userId) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khApp/mine/updateCustomerIntegral");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("integral", integral);
		map.put("userId", userId);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
	}
	
	/**
	 * @Title: addCustomerUserIntegralSub   
	 * @Description: TODO 减用户表积分
	 * @author: qun 
	 * @param: @param integral
	 * @param: @param userId      
	 * @return: void 
	 * @date:   2018年8月2日 下午2:21:15     
	 * @throws
	 */
	private void addCustomerUserIntegralSub(Integer integral, String userId) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("khAdmin/integralManage/updateSubCustomerIntegral");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("integral", integral);
		map.put("userId", userId);
		cModel.setData(map);
		formMapper.saveCustom(cModel);
	}
	/**
	 * 判断用户是否报名该活动
	 * @author 张智明
	 * @param map
	 * @param model
	 * @return
	 */
	private Boolean queryActivityUser(Map<String, Object> map,IntegralChangeModel model) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/integralManage/QryActivityUser");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/integralManage/QryActivityUser");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("activityId", map.get("activityId"));
		paramMap.put("userId", model.getUserId());
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);
		return tables.size()!=0?true:false;
	}

}
