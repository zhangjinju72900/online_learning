package com.tedu.base.ftl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

@Controller
@RequestMapping("/sales")
public class SalesController {
	@Autowired
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	@RequestMapping(value = "/us")
	public String workplace(Model model) {
		return "pc/contact-us";
		
	}
	@RequestMapping("/saveBusiness")
	@ResponseBody
	public ResponseJSON addSales(@RequestBody  Map<String, Object> params) {
		String customerId = null;
		String customerName=params.get("customerName").toString();
		String phone=params.get("initPhone").toString();
		String initName=params.get("initName").toString();
		/*Map<String, Object> map = new HashMap<String, Object>();
		QueryPage qp = new QueryPage();
		map.put("eq_name", customerName);
		qp.setParamsByMap(map);
		qp.setQueryParam("customer/QryCustomer");
		List<Map<String, Object>> qlists = formService.queryBySqlId(qp);*/
		List<Map<String,Object>> list = getParams("customer/QryCustomer1", "eq_name", customerName);
		CustomFormModel customerModel = new CustomFormModel();
		Map<String, Object> data = new HashMap<>();
		if (!list.isEmpty() || list.size() != 0) {
			customerId = list.get(0).get("id").toString();
			params.put("customerId",customerId);
			list = getMoreParams("ftlSql/QryContacts","eq_tel","eq_customer_id",phone,customerId);
			if (!list.isEmpty() || list.size() != 0) {
				System.out.println("联系方式已存在");
			}else{
				data.put("customerId",customerId);
				data.put("tel",phone);
				data.put("name",initName);
				customerModel.setData(data);
				customerModel.setSqlId("ftlSql/insertContact");
				formService.saveCustom(customerModel);
			}
		}else{			
			data.put("customerName",customerName);
			customerModel.setData(data);
			customerModel.setSqlId("ftlSql/InsertCustomer");
			formService.saveCustom(customerModel);
			list = getParams("customer/QryCustomer1", "eq_name", customerName);
			Map<String, Object> map2 = list.get(0);
			customerId = map2.get("id").toString();
			params.put("customerId",customerId);
			CustomFormModel customerModel1 = new CustomFormModel();
			Map<String, Object> data1 = new HashMap<>();
			data1.put("customerId",customerId);
			data1.put("tel",phone);
			data1.put("name",initName);
			customerModel1.setData(data1);
			customerModel1.setSqlId("ftlSql/insertContact");
			formService.saveCustom(customerModel1);
		}
		customerModel = new CustomFormModel("", "",params);
		customerModel.setSqlId("ftlSql/InsertContactUs");
	    formService.saveCustom(customerModel);
		ResponseJSON result = new ResponseJSON();
		result.setStatus(200);
		return result;
	}
	@RequestMapping("/find")
	@ResponseBody
	public ResponseJSON findSales(@RequestBody  Map<String, Object> params) {
		String phone=params.get("phone").toString();
		List<Map<String,Object>> list = getParams("sale/findByPhone", "eq_init_contacts_tel", phone);
		ResponseJSON result = new ResponseJSON();
		if(list.isEmpty()||list.size()==0){
			result.setStatus(500);
		}else{
			result.setData(list);
			result.setStatus(200);
		}
		return result;
	}
	public List<Map<String, Object>> sql(String url) {
		QueryPage query = new QueryPage();
		query.setQueryParam(url);
		List<Map<String, Object>> list = formMapper.query(query);
		return list;
	}

	public List<Map<String, Object>> getParams(String url, String eq, String area) {
		QueryPage queryPage = new QueryPage();
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put(eq, area);
		queryPage.setParamsByMap(mapParam);
		queryPage.setQueryParam(url);
		// 查询人员并替换新旧表单相关人员信息(姓名)
		List<Map<String, Object>> list = formService.queryBySqlId(queryPage);
		return list;
	}
	public List<Map<String, Object>> getMoreParams(String url, String eq,String eq1, String area,String area1) {
		QueryPage queryPage = new QueryPage();
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put(eq, area);
		mapParam.put(eq1, area1);
		queryPage.setParamsByMap(mapParam);
		queryPage.setQueryParam(url);
		// 查询人员并替换新旧表单相关人员信息(姓名)
		List<Map<String, Object>> list = formService.queryBySqlId(queryPage);
		return list;
	}
}
