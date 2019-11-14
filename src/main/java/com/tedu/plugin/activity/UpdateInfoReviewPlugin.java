package com.tedu.plugin.activity;

import com.jcraft.jsch.Session;
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
import com.tedu.business.keyword.service.KeyWordService;
import com.tedu.common.error.ExErrorCode;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除资讯(根据传入的id)
 * @author quancong
 *
 */
@Service("updateInfoReview")
public class UpdateInfoReviewPlugin implements ILogicPlugin {
	@Resource
    FormLogService formLogService;
	@Resource
	FormService formService;
	@Resource
	com.tedu.component.EasemobComponent easemobComponent;
	@Resource
	private FormMapper formMapper;

	@Resource
	private KeyWordService keyWordServiceImpl;
	
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
		
		
		Map<String,Object> map = (Map<String, Object>) formModel.getData();
		
		String reviewEdit = map.get("reviewEdit")==null?"":map.get("reviewEdit").toString();
		if(keyWordServiceImpl.checkTitle(reviewEdit)){
			 responseObj.setCode(ExErrorCode.KEY_WORD.getCode());
			 responseObj.setMsg(ExErrorCode.KEY_WORD.getMsg());
			 return;
		}
		
		CustomFormModel cModel = new CustomFormModel();
		
		cModel.setSqlId("khApp/discover/information/InfomationReview");

		cModel.setData(formModel.getData());

		LogUtil.info(cModel.getData().toString());

		formMapper.saveCustom(cModel);
		//获取含有资讯审核菜单权限的用户
		QueryPage qp = new QueryPage();
		qp.setParamsByMap(map);
		qp.setQueryParam("QryResourceUser");
		List<Map<String, Object>> userList = formService.queryBySqlId(qp);
		if(userList.size()>0) {
			//获取该资讯信息
			qp.getData().put("id", map.get("id"));
			qp.setQueryParam("QryInfmationById");
			List<Map<String, Object>> infmation = formService.queryBySqlId(qp);
			if(infmation.size()>0) {
				String inTitle = infmation.get(0).get("title")!=null?infmation.get(0).get("title").toString():"新资讯";
				String userName = infmation.get(0).get("userName")!=null?infmation.get(0).get("userName").toString():"";
				//新增notice
				Map<String,Object> noticeMap = new HashMap<String, Object>();
				noticeMap.put("title", SessionUtils.getUserInfo().getNickName()+"评论了"+userName+"《"+inTitle+"》");
				noticeMap.put("context", SessionUtils.getUserInfo().getNickName()+"评论了"+userName+"《"+inTitle+"》,请在后台管理→资讯管理→审核管理中审核");
				noticeMap.put("source",0);
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
						String userId = userMap.get("id").toString();
						String easeUserName = userMap.get("easeUserName").toString();
						array = array + easeUserName +",";
						noticeMap.put("receiverId", userId);
						noticeMap.put("userId", 2);
						cModel.setData(noticeMap);
						formMapper.saveCustom(cModel);
						//发送通知
					    String title = SessionUtils.getUserInfo().getNickName()+"评论了《"+inTitle+"》";
					    String context = SessionUtils.getUserInfo().getNickName()+"评论了《"+inTitle+"》";
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
		
		/*cModel = new CustomFormModel();
		
		cModel.setSqlId("khApp/discover/information/updateActReview");

		cModel.setData(formModel.getData());

		LogUtil.info(cModel.getData().toString());

		formMapper.saveCustom(cModel);*/
				

	}
}

