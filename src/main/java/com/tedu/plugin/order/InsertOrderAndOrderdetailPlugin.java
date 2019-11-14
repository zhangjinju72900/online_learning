package com.tedu.plugin.order;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

@Service("insertOrderAndOrderdetail")
public class InsertOrderAndOrderdetailPlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Resource
	FormService formService;
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();



		// 插入t_orader_detail中
		// 通过商品id查询商品名称
		QueryPage queryPage = new QueryPage();
		queryPage.setQueryParam("QryGoods");
		queryPage.getData().put("goodId", requestObj.getData().get("id"));
		List<Map<String, Object>> list = formMapper.query(queryPage);
		if (list.isEmpty()) {
			throw new ValidException(ErrorCode.DATA_NOT_FOUND, "数据不存在", "该商品不存在");
		}
		

		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		Map<String, Object> data = formModel.getData();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data != null) {
			//商品Id
			String  goodsId = data.get("id").toString(); 
			//商品支付方式Id
			String  payDetailId = data.get("payDetailId").toString();
			//收货地址Id
			String  addressId = data.get("addressId").toString(); 
			//收货地址Id
			String  addressPrice = data.get("addressPrice").toString(); 
			//备注
			String  remark = data.get("remark")!=null?data.get("remark").toString():"";
			//获取个人现有积分
			//String  personIntegral = data.get("personIntegral").toString(); 
			
			
			map.put("goodsId", goodsId);
			map.put("payDetailId", payDetailId);
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(map);
			qp.getData().put("goodsId", goodsId);
			qp.getData().put("payDetailId", payDetailId);
			qp.getData().put("userId", SessionUtils.getUserInfo().getUserId());
			qp.setQueryParam("zhongdeorder/QryGoodsById");
			List<Map<String, Object>> goodsList = formService.queryBySqlId(qp);
			
			//获取个人现有积分和总积分
			qp.setQueryParam("zhongdeorder/QryUserIntegral");
			List<Map<String, Object>> userIntegralList = formService.queryBySqlId(qp);
			//获取个人当前积分
			int personIntegral = Integer.parseInt(userIntegralList.get(0).get("integral").toString());
			
			if(goodsList.size()!=1) {
				responseObj.setCode("1");
				responseObj.setMsg("商品单价设置有误！");
				return;
			}else {
				Map<String, Object> goodsMap = goodsList.get(0);
				
				//更新更商品数量-1 减除本人现有积分
				String integral =goodsMap.get("integral").toString();
				if(personIntegral<Integer.parseInt(integral)) {
					responseObj.setCode("1");
					responseObj.setMsg("积分不足无法购买！");
					return;
				}
				String Amount = goodsMap.get("amount").toString();
			/*	int goodsCount = Integer.parseInt(goodsMap.get("goodsCount").toString());
				map.put("goodsCount", goodsCount);
				map.put("integeral", "-"+integral);
				map.put("remark", "商品兑换");
				execute("zhongdeorder/UpdateMyIntegeralAndGoods", map);*/
				//生成订单及订单号
				//生成订单单号
				String dateTime = DateUtils.getDateToStr("yyyyMMddhhmmss", new Date());
				String code = dateTime+getNumberFromString(UUID.randomUUID().toString().replace("-", "").substring(0,32));
				map.put("code", code);
				map.put("Integral", integral);
				map.put("realIntegral", integral);
				map.put("Amount", Amount);
				map.put("realAmount", Amount);
				//金额支付方式;0:无,1:微信,2:支付宝
				map.put("payMethod", 1);
				//收货地址
				map.put("consignAddressId", addressId);
				//配送金额
				map.put("logisticAmount",addressPrice);
				//物流
				map.put("logistic", "");
				//物流单号
				map.put("logisticCode", "");
				map.put("orderStatus", "0");
				map.put("remark", remark);
				
				execute("zhongdeorder/InsertOrder", map);
				//获取订单
				
				qp.getData().put("code", code);
				qp.setQueryParam("zhongdeorder/QryOrderByCode");
				List<Map<String, Object>> detailList = formService.queryBySqlId(qp);
				String orderId = detailList.get(0).get("id").toString();
				map.put("orderId", orderId);
				map.put("Quantity", 1);
				execute("zhongdeorder/InsertOrderdetail", map);
				resultMap.put("orderId", orderId);
				
			
				
			}
			responseObj.setData(resultMap);
			
			
/*
			QueryPage sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("zhongdeorder/QryDefaultAddress");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId("zhongdeorder/QryDefaultAddress");
			Map<String, Object> paramMap = new HashMap<>();
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> tables = formMapper.query(sqlQuery);
			if (tables != null && tables.size() > 0) {
				map.putAll(tables.get(0));
			}

			sqlQuery = new QueryPage();
			sqlQuery.setQueryParam("zhongdeorder/ConfirmPaymentById");
			sqlQuery.setQueryType("");
			sqlQuery.setSingle(1);
			sqlQuery.setSqlId("zhongdeorder/ConfirmPaymentById");
			paramMap = new HashMap<>();
			paramMap.put("id", data.get("id"));
			sqlQuery.setDataByMap(paramMap);
			List<Map<String, Object>> t = formMapper.query(sqlQuery);
			if (t != null && t.size() > 0) {
				map.putAll(t.get(0));

				if (tables == null || tables.size() == 0) {
					map.put("address", "");
					map.put("addressDetail", "");
					map.put("name", "");
					map.put("tel", "");
				}
			}

			if (map.get("goodId") != null) {
				sqlQuery = new QueryPage();
				sqlQuery.setQueryParam("zhongdeorder/QryGoodPayDeatil");
				sqlQuery.setQueryType("");
				sqlQuery.setSingle(1);
				sqlQuery.setSqlId("zhongdeorder/QryGoodPayDeatil");
				paramMap = new HashMap<>();
				paramMap.put("goodId", map.get("goodId"));
				sqlQuery.setDataByMap(paramMap);
				List<Map<String, Object>> ads = formMapper.query(sqlQuery);
				map.put("payDetail", ads);
			}*/
			
		}
	}
	public static String getNumberFromString(String code) {
		String result = "";
		String[] str = code.split("");
		String temp = "";
		int index = str.length - 1;
		byte start = (byte) str[0].charAt(0);
		byte end = (byte) str[index].charAt(0);
		if (str.length > 3 && str.length < 8) {
			index = str.length >> 2;
		} else if (str.length >= 8) {
			index = str.length >> 3;
		}
		byte div = (byte) (str[index].charAt(0));
		for (String s : str) {
			char c = s.charAt(0);
			byte b = (byte) c;
			temp = temp + b;
		}
		if ((temp.length() <= 17 && temp.contains("-")) || (temp.length() <= 16 && !temp.contains("-"))) {
			result = temp.replace("-", "");
		} else {
			BigDecimal bigTemp = new BigDecimal(temp.replace("-", ""));
			BigDecimal bigResult = new BigDecimal(0);
			bigResult = bigTemp.divide(new BigDecimal(str.length), 15, BigDecimal.ROUND_HALF_DOWN)
					.divide(new BigDecimal(index), 15, BigDecimal.ROUND_HALF_UP)
					.divide(new BigDecimal(div), 5, BigDecimal.ROUND_HALF_UP)
					.divide(new BigDecimal(start), 15, BigDecimal.ROUND_HALF_UP)
					.divide(new BigDecimal(end), 5, BigDecimal.ROUND_HALF_UP);
			String strtemp = bigResult.toString();
			String[] strTemp = strtemp.split("\\.");
			if (strtemp.length() == 17 && strtemp.contains(".")) {
				result = strtemp.substring(0, 16);
			} else if (strtemp.length() > 17 && strtemp.contains(".")) {
				result = strtemp.substring(strtemp.length() - 17, strtemp.length());
			} else {
				result = strtemp.substring(0, strtemp.length());
			}
		}
		return result.replace(".", "").substring(0,12);
	}
	private String execute(String sqlId, Map<String, Object> data){
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
	}

}
