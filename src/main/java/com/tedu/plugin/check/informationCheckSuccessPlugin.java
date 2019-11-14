package com.tedu.plugin.check;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.common.constant.InfoCheckTypeEnum;
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.constant.SendMsgTypeEnum;
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
@Service("informationCheckSuccessPlugin")
public class informationCheckSuccessPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	com.tedu.component.EasemobComponent easemobComponent;
	@Resource
	private FormMapper formMapper;

	@Resource
	private SendMsgComponent sendMsg;

	@Resource
	FormService formService;
	@Resource
	private IntegralChangeComponent integralChangeComponent;

	private String sqlTemplate = "khAdmin/checkManage/UpdateCheckManageStatus";
	private String sqlTemplate2 = "khApp/discover/information/updateReview";
	private String sqlTemplate3 = "khApp/discover/information/updateActReview";
	private String sqlTemplate4 = "khApp/discover/information/updateReviewReview";

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String[] ids = (map.get("id") == null ? "" : map.get("id").toString()).split("-");
		String type = ids[0];
		String id = ids[1];

		log.info(ids.toString());

		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlTemplate);
		cModel.setData(formModel.getData());
		Map<String, Object> logData = formModel.getData();

		logData.put("id", id);
		logData.put("type", type);
		LogUtil.info(cModel.getData().toString());
		formMapper.saveCustom(cModel);

		if (InfoCheckTypeEnum.INFO_REVIEW.getCode().equals(type)) {
			// 更改评论数
			CustomFormModel cModel2 = new CustomFormModel();

			cModel2.setSqlId(sqlTemplate2);

			cModel2.setData(formModel.getData());

			LogUtil.info(cModel2.getData().toString());
			logData.put("id", id);

			formMapper.saveCustom(cModel2);

			// 给资讯发布者发送消息
			formModel.getData().put("baseId", id);
			sendMsg.sendMsg(SendMsgTypeEnum.INFO_REVIEW, formModel.getData());

		} else if (InfoCheckTypeEnum.LIVE_REVIEW.getCode().equals(type)) {
			CustomFormModel cModel3 = new CustomFormModel();
			cModel3.setSqlId(sqlTemplate3);

			cModel3.setData(formModel.getData());

			LogUtil.info(cModel3.getData().toString());
			logData.put("id", id);

			formMapper.saveCustom(cModel3);

		} else if (InfoCheckTypeEnum.INFO_REVIEW_REVIEW.getCode().equals(type)) {
			CustomFormModel cModel3 = new CustomFormModel();
			cModel3.setSqlId(sqlTemplate4);

			cModel3.setData(formModel.getData());

			formMapper.saveCustom(cModel3);

			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("baseId", id);
			sendMsg.sendMsg(SendMsgTypeEnum.INFO_REVIEW_REVIEW, map1);

		} else if (InfoCheckTypeEnum.AC_JOIN_PIC.getCode().equals(type)) {

			integralChangeComponent.setIntegralChange(IntegralEnum.ACTIVITY_PIC, new IntegralChangeModel(id));

		} else {
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(map);
			// qp.setQueryParam("QryResourceUser");

			// 获取该资讯信息
			qp.getData().put("id", id);
			qp.setQueryParam("QryInfmationById");
			List<Map<String, Object>> infmation = formService.queryBySqlId(qp);
			if (infmation.size() > 0) {
				String inTitle = infmation.get(0).get("title") != null ? infmation.get(0).get("title").toString() : "";
				String userName = infmation.get(0).get("userName") != null ? infmation.get(0).get("userName").toString()
						: "";
				String createBy = infmation.get(0).get("createBy") != null ? infmation.get(0).get("createBy").toString()
						: "";
				// 获取关注的人
				qp.getData().put("userId", createBy);
				qp.setQueryParam("QryFocusUser");
				List<Map<String, Object>> userList = formService.queryBySqlId(qp);
				if (userList.size() > 0) {

					// 新增notice
					Map<String, Object> noticeMap = new HashMap<String, Object>();
					noticeMap.put("title", userName + "发布了新动态");
					noticeMap.put("context", userName + "发布了，我们一去围观吧！");
					noticeMap.put("source", 0);
					noticeMap.put("updateBy", SessionUtils.getUserInfo().getUserId());
					cModel.setSqlId("InsertNoticeInformation");
					cModel.setData(noticeMap);
					formMapper.saveCustom(cModel);
					// 获取该消息通知
					qp.setQueryParam("QryNoticeByMaxId");
					List<Map<String, Object>> noticeList = formService.queryBySqlId(qp);
					for (int i = 0; i < userList.size(); i++) {
						Map<String, Object> userMap = userList.get(i);
						if (noticeList.size() > 0) {
							String noticeId = noticeList.get(0).get("id").toString();
							noticeMap.put("baseId", noticeId);
							// 插入明细表
							cModel.setSqlId("khAdmin/noticeManage/insertMessageRecord");
							String userId = userMap.get("id").toString();
							noticeMap.put("receiverId", userId);
							noticeMap.put("userId", 2);
							cModel.setData(noticeMap);
							formMapper.saveCustom(cModel);
							// 发送通知
							String title = userName + "发布了新动态";
							String context = userName + "发布了新动态，我们一去围观吧！";
							easemobComponent.sendNotice(noticeId, userMap.get("easName").toString().split(","), title,
									context);
						}
					}

				}

			}

		}
	}
}
