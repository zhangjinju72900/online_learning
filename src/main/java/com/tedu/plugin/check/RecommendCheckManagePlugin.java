package com.tedu.plugin.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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
import com.tedu.common.constant.IntegralEnum;
import com.tedu.common.model.IntegralChangeModel;
import com.tedu.component.IntegralChangeComponent;

/**
 * 批量删除资讯(根据传入的id)
 * 
 * @author quancong
 *
 */
@Service("RecommendCheckManagePlugin")
public class RecommendCheckManagePlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	@Resource
	com.tedu.component.EasemobComponent easemobComponent;
	@Resource
	private FormMapper formMapper;

	@Resource
	private IntegralChangeComponent integralChangeComponent;

	private String sqlTemplate = "khAdmin/checkManage/RecommendCheckManage";
	private String sqlStatus = "khAdmin/checkManage/selectCheckStatusByIdl";
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
		String[] idTypes = (map.get("id") == null ? "" : map.get("id").toString()).split(",");
		for (String string : idTypes) {
			String[] ids = string.split("-");
			String type = ids[0];
			String id = ids[1];
			log.info(ids.toString());

			int a = Integer.parseInt(id);
			QueryPage sqlQuery = new QueryPage();
			sqlQuery.setQueryParam(sqlStatus);
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId(sqlStatus);
			HashMap paramMap = new HashMap<>();
			paramMap.put("type", type);
			paramMap.put("id", a);
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> t = formMapper.query(sqlQuery);
			String ss = "";
			for (int i = 0; i < t.size(); i++) {
				Map<String, Object> s = t.get(i);

				ss = s.toString();
			}

			if (ss.equals("{check_status=1}")) {
				CustomFormModel cModel = new CustomFormModel();
				cModel.setSqlId(sqlTemplate);
				cModel.setData(formModel.getData());
				Map<String, Object> logData = formModel.getData();

				logData.put("id", id);
				logData.put("type", type);
				LogUtil.info(cModel.getData().toString());
				formMapper.saveCustom(cModel);

				if ("info".equals(type)) {// 资讯被推荐
					integralChangeComponent.setIntegralChange(IntegralEnum.RECOMMEND_INFO, new IntegralChangeModel(id));
				}

				QueryPage qp = new QueryPage();
				qp.setParamsByMap(map);
				qp.setQueryParam("QryResourceUser");
				List<Map<String, Object>> userList = formService.queryBySqlId(qp);
				// 获取该资讯信息
				qp.getData().put("id", id);
				qp.setQueryParam("QryInfmationById");
				List<Map<String, Object>> infmation = formService.queryBySqlId(qp);
				if (infmation.size() > 0) {
					String inTitle = infmation.get(0).get("title") != null ? infmation.get(0).get("title").toString()
							: "";
					String userName = infmation.get(0).get("userName") != null? infmation.get(0).get("userName").toString(): "";
					String createBy = infmation.get(0).get("createBy") != null? infmation.get(0).get("createBy").toString(): "";
					// 新增notice
					Map<String, Object> noticeMap = new HashMap<String, Object>();
					noticeMap.put("title", userName + "你的《" + inTitle + "》被推荐");
					noticeMap.put("context",
							SessionUtils.getUserInfo().getNickName() + "将" + userName + "《" + inTitle + "》设置为推荐");
					noticeMap.put("source", 0);
					noticeMap.put("updateBy", SessionUtils.getUserInfo().getUserId());
					cModel.setSqlId("InsertNoticeInformation");
					cModel.setData(noticeMap);
					formMapper.saveCustom(cModel);
					// 获取该消息通知
					qp.setQueryParam("QryNoticeByMaxId");
					List<Map<String, Object>> noticeList = formService.queryBySqlId(qp);
					if (noticeList.size() > 0) {
						String noticeId = noticeList.get(0).get("id").toString();
						noticeMap.put("baseId", noticeId);
						// 插入明细表
						cModel.setSqlId("khAdmin/noticeManage/insertMessageRecord");
						String array = "";
						Map<String, Object> userMap = new HashMap<String, Object>();
						String userId =createBy;
						String easeUserName = createBy;
						array = array + easeUserName + ",";
						noticeMap.put("receiverId", userId);
						noticeMap.put("userId", 2);
						cModel.setData(noticeMap);
						formMapper.saveCustom(cModel);
						// 发送通知
						String title = userName + "你的《" + inTitle + "》被推荐";
						String context = SessionUtils.getUserInfo().getNickName() + "将" + userName + "《" + inTitle + "》设置为推荐";
						easemobComponent.sendNotice(noticeId, createBy.split(","), title, context);
					}

				}

			} else {
				responseObj.setMsg("请确认所选资讯都审核通过");
			}

		}

	}
}
