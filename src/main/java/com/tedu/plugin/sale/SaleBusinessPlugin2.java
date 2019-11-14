package com.tedu.plugin.sale;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

@Service("saleBusinessEdit")
public class SaleBusinessPlugin2 implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	boolean customer = false;
	boolean phone = false;
	
	String customerId = null;
	String id = null;

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		/*
		 * // 成功率的校验
		 * log.info(formModel.getData().get("ctlEstimateTime").toString());
		 * String successRate =
		 * formModel.getData().get("ctlSuccessRate").toString(); if
		 * (StringUtils.isNotEmpty(successRate)) { // 判断是否为0到100的数 Pattern
		 * pattern =
		 * Pattern.compile("^(((\\d|[1-9]\\d)(\\.\\d{1,2})?)|100|100.0|100.00)$"
		 * ); Matcher num = pattern.matcher(successRate); Boolean flag =
		 * num.matches(); if (!flag) { throw new
		 * ValidException(ErrorCode.INVALIDATE_FORM_DATA, "输入提示:默认用%",
		 * "请输入0到100的整数或者小数"); } }
		 */
		/*boolean contactName = false;
		String customerName = formModel.getData().get("ctlCustomerName").toString();
		log.info("customerName" + customerName);
		if (customerName.equals("") || customerName == null) {
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "数据业务校验失败",
					"客户名称获取失败！customerName:" + customerName);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		QueryPage qp = new QueryPage();
		map.put("eq_name", customerName);
		qp.setParamsByMap(map);
		qp.setQueryParam("customer/QryCustomer");
		List<Map<String, Object>> qlists = formService.queryBySqlId(qp);
		// System.out.println("进入插件方法，判断客户是否存在");
		log.info("QryCustomer:" + qlists);
		if (!qlists.isEmpty() || qlists.size() != 0) {
			System.out.println("客户已存在");
			customer = true;
			customerId = qlists.get(0).get("id").toString();
			formModel.getData().put("eq_customerId", customerId);
			Map<String, Object> mapContact = new HashMap<String, Object>();
			QueryPage qpContact = new QueryPage();
			mapContact.put("eq_tel", formModel.getData().get("ctlInitPhone"));
			mapContact.put("eq_CustomerId", formModel.getData().get("eq_customerId"));
			qpContact.setParamsByMap(mapContact);
			qpContact.setQueryParam("ListIssuePriority1");
			List<Map<String, Object>> list = formService.queryBySqlId(qpContact);
			if (!list.isEmpty() || list.size() != 0) {
				log.info("客户不存在，插入联系人");
				phone = true;
				Map<String, Object> mapContact2 = new HashMap<String, Object>();
				QueryPage qpContact2 = new QueryPage();
				mapContact2.put("eq_tel", formModel.getData().get("ctlInitPhone"));
				mapContact2.put("eq_CustomerId", formModel.getData().get("eq_customerId"));
				mapContact2.put("eq_name", formModel.getData().get("ctlInitName"));
				qpContact2.setParamsByMap(mapContact2);
				qpContact2.setQueryParam("ListIssuePriority1");
				List<Map<String, Object>> list2 = formService.queryBySqlId(qpContact2);
				if (!list2.isEmpty() || list2.size() != 0) {
					contactName = true;
				}
			}
		}
		if (customer == false)

		{
			System.out.println("客户不存在");
			// 客户不存在
			Map<String, Object> data = new HashMap<>();
			CustomFormModel cModel = new CustomFormModel();
			data.put("customerName", customerName);
			data.put("createTime", new Date());
			data.put("createBy", SessionUtils.getUserInfo().getEmpId());
			data.put("updateTime", new Date());
			data.put("updateBy", SessionUtils.getUserInfo().getEmpId());
			cModel.setData(data);
			log.info("insertCustomer:"+data);
			cModel.setSqlId("customer/insertCustomer");
			formMapper.saveCustom(cModel);
			map = new HashMap<String, Object>();
			QueryPage qp1 = new QueryPage();
			map.put("eq_name", customerName);
			qp1.setParamsByMap(map);
			qp1.setQueryParam("customer/QryCustomer1");
			List<Map<String, Object>> list = formService.queryBySqlId(qp1);
			Map<String, Object> map2 = list.get(0);
			id = map2.get("id").toString();
			formModel.getData().put("eq_customerId", id);
		}
		if (phone == false && customer == false) {
			System.out.println("客户不存在,然后存入联系方式");
			Map<String, Object> data = new HashMap<>();
			CustomFormModel cModel = new CustomFormModel();
			data.put("customerId", id);
			data.put("tel", formModel.getData().get("ctlInitPhone"));
			data.put("name", formModel.getData().get("ctlInitName").toString());
			cModel.setData(data);
			log.info(data);
			cModel.setSqlId("customer/insertContact");
			formMapper.saveCustom(cModel);
		}
		if (phone == false && customer == true) {
			System.out.println("客户存在,然后存入联系方式");
			Map<String, Object> data = new HashMap<>();
			CustomFormModel cModel = new CustomFormModel();
			data.put("customerId", customerId);
			data.put("tel", formModel.getData().get("ctlInitPhone"));
			data.put("name", formModel.getData().get("ctlInitName").toString());
			cModel.setData(data);
			log.info(data);
			cModel.setSqlId("customer/insertContact");
			formMapper.saveCustom(cModel);
			customer = false;
		}
		if (phone == true && customer == true && contactName == false) {
			System.out.println("客户存在,然后存入联系方式");
			Map<String, Object> data = new HashMap<>();
			CustomFormModel cModel = new CustomFormModel();
			data.put("customerId", customerId);
			data.put("tel", formModel.getData().get("ctlInitPhone"));
			data.put("name", formModel.getData().get("ctlInitName").toString());
			cModel.setData(data);
			log.info(data);
			cModel.setSqlId("customer/updateContact");
			formMapper.saveCustom(cModel);
			phone = false;
		}*/
		/*
		 * String proname = formModel.getData().get("ctlProName").toString();
		 * QueryPage qp2 = new QueryPage();
		 * qp2.setQueryParam("customer/QryProname"); List<Map<String,Object>>
		 * qlists1 = formMapper.query(qp2); for (Map<String, Object> map :
		 * qlists1) { if (proname.equals(map.get("proname"))) { throw new
		 * ValidException(ErrorCode.INVALIDATE_FORM_DATA, "数据业务校验失败",
		 * "项目名称已存在"); } }
		 */
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

	}
}
