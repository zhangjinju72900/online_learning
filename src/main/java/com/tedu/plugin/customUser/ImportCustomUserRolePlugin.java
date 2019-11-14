package com.tedu.plugin.customUser;

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
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
/**
 * 导入用户时将角色解析后得到对应的角色id并联合主表id插入到t_customer_user_role中
 * @author quancong
 *
 */
@Service("importCustomUserRolePlugin")
public class ImportCustomUserRolePlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String id = map.get("id") == null ? "" : map.get("id").toString();
		String tel = map.get("tel") == null ? "" : map.get("tel").toString();
		String cardNum = map.get("cardNum") == null ? "" : map.get("cardNum").toString();
		
		QueryPage qp = new QueryPage();
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("tel", StringUtils.isBlank(tel)?"--":tel);
		requestMap.put("cardNum", StringUtils.isBlank(cardNum)?"--":cardNum);
		requestMap.put("id", id);
		qp.setQueryParam("khAdmin/userManage/QryUserByCardNumOrTelAndId");
		qp.setSqlId("khAdmin/userManage/QryUserByCardNumOrTelAndId");
		qp.setDataByMap(requestMap);
		List qlist = formService.queryBySqlId(qp);
		if(qlist != null && qlist.size() > 0){
			throw new ValidException(ErrorCode.MAIL_EXCEPTION, "身份证号或手机号重复", tel+"/"+cardNum);
		}
		
		return null;
		}
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		  log.info(formModel.getData());
		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
		  //获取主表的id
		  String userId=formModel.getPrimaryFieldValue().toString();
		  //获取roleid并解析
		  String roleNames = map.get("roleName")==null?"":map.get("roleName").toString();
		  String[] names=roleNames.startsWith("[")?roleNames.substring(1, roleNames.length()-1).split(","):roleNames.split(",");
		  //用于插入用户角色表
		  CustomFormModel cModel = new CustomFormModel();
		  cModel.setSqlId("insertCustomUserRole");
		  cModel.setData(formModel.getData());
		  Map<String, Object> logData = formModel.getData();
		  
		  
		  List<Map<String, Object>> qlist=new ArrayList<Map<String, Object>>();
		  //当roleName为空时，不执行插入
			for (String roleName : names) {
				if(!(roleName.length()<=0)){
					//用于查询roleId
					Map<String, Object> map1 = new HashMap<String, Object>();
					QueryPage qp = new QueryPage();
					map1.put("eq_roleName", roleName);
					qp.setParamsByMap(map1);
					qp.setQueryParam("QryCustomUserRoleId");
					qlist = formService.queryBySqlId(qp);
					log.info(qlist);
					if ((!qlist.isEmpty()) && (qlist.size() != 0)) {
						String roleId=qlist.get(0).get("id").toString();
						logData.put("userId", userId);
						logData.put("roleId", roleId);
						formMapper.saveCustom(cModel);
					}
				}
				
			}
		

	}

}
