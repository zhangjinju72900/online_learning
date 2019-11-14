package com.tedu.plugin.order;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
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
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.task.SpringUtils;
import com.tedu.base.weixin.model.WXPayConfigImpl;
import com.tedu.common.error.ExErrorCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("ConfirmPayment")
public class ConfirmPayment implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	@Value("${wx.pay.order}")
	String wxOrderUrl;
	@Value("${wx.pay.callAddress}")
	String wxPayCallIP;
	@Value("${wx.pay.useSandBox:@null}")
	boolean useSandBox;
	@Value("${app.address}")
	String appAdress;

	private String amountMaxStr = "10000";
	private String amountMinStr = "0.01";

	FormService formService = SpringUtils.getBean("formService");

//	    private WXPayConfig wxPayConfig = new WXPayConfigImpl();
//	    private WXPay wxpay = new WXPay(wxPayConfig, WXPayConstants.SignType.MD5, useSandBox);

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		String orderId = "";
		if (formModel.getData().get("id") != null) {
			orderId = formModel.getData().get("id").toString();
		}
		// 查询是否付过款
		QueryPage queryPage3 = new QueryPage();
		queryPage3.setQueryParam("zhongdeorder/QryOrderByCode");
		queryPage3.getData().put("id", orderId);
		List<Map<String, Object>> list3 = formMapper.query(queryPage3);
		if (list3 != null && !list3.isEmpty()) {
			if (!"0".equals(list3.get(0).get("orderStatus").toString())) {
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "已经付过款");
			} else {
				return null;
			}
		} else {
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "订单异常");
		}
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o,
			FormEngineResponse formEngineResponse) {
		// String feTypeX = (String) formModel.getData().get("feTypeX");

		WXPayConfig wxPayConfig = new WXPayConfigImpl();

		WXPay wxpay = new WXPay(wxPayConfig, WXPayConstants.SignType.MD5, useSandBox);

		FormLogger.info("微信支付下单开始");
		HashMap<String, String> data = new HashMap<String, String>();
		String orderId = "";

		if (formEngineRequest.getData().get("id") != null) {
			orderId = formModel.getData().get("id").toString();
			FormLogger.info("orderId:" + formModel.getData().get("id").toString());
		}
		QueryPage queryPage3 = new QueryPage();
		queryPage3.setQueryParam("zhongdeorder/QryOrderByCode");
		queryPage3.getData().put("id", orderId);
		List<Map<String, Object>> list3 = formMapper.query(queryPage3);
		double amount =(Double.parseDouble(list3.get(0).get("amount").toString())+Double.parseDouble(list3.get(0).get("logistic_amount").toString()));
		// (Double.parseDouble(list3.get(0).get("amount").toString())+Double.parseDouble(list3.get(0).get("logistic_amount").toString()));

		if (amount > 0) {
			if (formEngineRequest.getData().get("code") != null) {

				data.put("out_trade_no", formModel.getData().get("code").toString());
				FormLogger.info("out_trade_no:" + formModel.getData().get("code").toString());

			} else {
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "支付单编号为空");

			}

			if (formEngineRequest.getData().get("code") != null) {

				data.put("spbill_create_ip", formEngineRequest.getClientIp().split(",")[0]);
				FormLogger.info("spbill_create_ip:" + formEngineRequest.getClientIp());

			} else {
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "客户端IP为空");

			}
			String tradeType = "MWEB";
			if (tradeType.equals("NATIVE")) {
				if (formEngineRequest.getData().get("classId") != null) {
					data.put("product_id", formModel.getData().get("classId").toString());
					FormLogger.info("product_id:" + formModel.getData().get("classId").toString());
				} else {
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "扫码支付-商品ID为空");

				}

			} else if (tradeType.equals("JSAPI")) {
				if (formEngineRequest.getData().get("openid") != null) {
					data.put("openid", formEngineRequest.getData().get("openid").toString());
					FormLogger.info("openid:" + formModel.getData().get("openid").toString());
				} else {
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "公众号支付-openid为空");

				}

			} else if (tradeType.equals("MWEB")) {
//	            if (formEngineRequest.getData().get("openid") != null) {
//	                data.put("openid", formEngineRequest.getData().get("openid").toString());
//	                FormLogger.info("openid:" + formModel.getData().get("openid").toString());
				data.put("scene_info",
						"{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \""+appAdress+"\",\"wap_name\": \"中德诺浩APP\"}}");
				data.put("trade_type", "MWEB");
//	            } else {
//	                throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "H5支付-openid为空");
				//
//	            }
			}
			data.put("body", "中德诺浩-商品购买");
			data.put("notify_url", wxPayCallIP);
			try {

				Map<String, String> r = new HashMap<String, String>();
				if (useSandBox) {
					data.put("total_fee", "301");
					FormLogger.info("沙箱支付下单开始");
					String key = GetSandBoxSignKey(wxpay.fillRequestData(data), wxpay);
					data.put("appid", wxPayConfig.getAppID());
					FormLogger.info("appid:" + wxPayConfig.getAppID());

					data.put("mch_id", wxPayConfig.getMchID());
					FormLogger.info("mch_id:" + wxPayConfig.getMchID());

					data.put("nonce_str", WXPayUtil.generateNonceStr());
					FormLogger.info("nonce_str:" + WXPayUtil.generateNonceStr());

					data.put("sign", WXPayUtil.generateSignature(data, key));
					FormLogger.info("沙箱生成签名");
					FormLogger.info("sign:" + WXPayUtil.generateSignature(data, key));

					String respXml = wxpay.requestWithoutCert(wxOrderUrl, data, 10000, 10000);
					FormLogger.info("沙箱请求的数据：" + WXPayUtil.mapToXml(data));
					FormLogger.info("沙箱返回的数据：" + respXml);

					r = WXPayUtil.xmlToMap(respXml);
				} else {
					if (amount > 0) {
						FormLogger.info("支付金额：" + amount);
						BigDecimal bigDecimal = new BigDecimal(amount);
						BigDecimal amountMax = new BigDecimal(amountMaxStr);
						FormLogger.info("最大金额：" + amountMaxStr);

						BigDecimal amountMin = new BigDecimal(amountMinStr);
						FormLogger.info("最小金额：" + amountMinStr);

						if (bigDecimal.doubleValue() == 0) {
							throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "支付金额不能为0");
						} else {
							String total_fee = bigDecimal.multiply(new BigDecimal("100")).intValue() + "";
							if (amountMax.subtract(bigDecimal).doubleValue() >= 0
									&& bigDecimal.subtract(amountMin).doubleValue() >= 0) {
								data.put("total_fee", total_fee);
								r = wxpay.unifiedOrder(data);
							} else {
								throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "支付金额不在最小支付金额和最大支付金额之间");
							}

						}
					} else {
						throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "支付金额为空");
					}
				}
				FormLogger.info("请求的数据：" + WXPayUtil.mapToXml(wxpay.fillRequestData(data)));
				FormLogger.info("微信支付下单返回的数据：" + WXPayUtil.mapToXml(r));
				if (tradeType.equals("JSAPI")) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("appId", wxPayConfig.getAppID());
					LocalDateTime localTime = LocalDateTime.now();
					map.put("timeStamp", localTime.toInstant(ZoneOffset.of("+8")).getEpochSecond() + "");
					map.put("nonceStr", WXPayUtil.generateNonceStr());
					map.put("package", "prepay_id=" + r.get("prepay_id"));
					map.put("signType", "MD5");
					String sign = WXPayUtil.generateSignature(map, wxPayConfig.getKey());
					map.put("paySign", sign);
					formEngineResponse.setCode("0");
					map.put("paymentNo", data.get("out_trade_no"));
					formEngineResponse.setData(map);
					FormLogger.info("公众号二次签名" + WXPayUtil.mapToXml(map));
				} else if (tradeType.equals("MWEB")) {
					formEngineResponse.setCode("0");
					Map<String, String> map = new HashMap<String, String>();
					map.put("mweb_url", r.get("mweb_url"));
					map.put("paymentNo", data.get("out_trade_no"));
					map.put("payType", "0");
					formEngineResponse.setData(map);
				} else {
					formEngineResponse.setCode("0");
					r.put("paymentNo", data.get("out_trade_no"));
					formEngineResponse.setData(r);
				}
			} catch (Exception e) {
				FormLogger.error(e.getMessage(), e);
			}
		} else {
			FormLogger.info("纯积分支付开始");
			// 更新状态插入
			Map<String, Object> maps = new HashMap<>();
			Map<String, Object> resultMap = new HashMap<>();
			maps.put("id", orderId);
			maps.put("status", 1 + "");
			FormLogger.info("更新订单状态完成");
			execute("zhongdeorder/UpdateOrderByCode",maps);
			int integral = Integer.parseInt(list3.get(0).get("integral").toString());
			int goodsId = Integer.parseInt(list3.get(0).get("goodsId").toString());
			if (integral > 0) {
				maps.put("goodsCount", 1);
				maps.put("integeral", "-" + integral);
				maps.put("goodsId", goodsId);
				maps.put("remark", "商品兑换-" + list3.get(0).get("goodsName")+"   订单编号（纯积分购买）："+formModel.getData().get("code"));
				maps.put("userId", list3.get(0).get("userId"));
				FormLogger.info(maps.toString());
				execute("zhongdeorder/UpdateMyIntegeralAndGoods", maps);
				execute("zhongdeorder/InsertIntegral", maps);
				FormLogger.info("更新商品数量及积分记录完成");
				// 用户积分减除
				// 获取个人现有积分和总积分
				QueryPage qp = new QueryPage();
				qp.setParamsByMap(maps);
				qp.getData().put("userId",list3.get(0).get("userId"));
				qp.setQueryParam("zhongdeorder/QryUserIntegral");
				List<Map<String, Object>> userIntegralList = formService.queryBySqlId(qp);
				int personIntegral = Integer.parseInt(userIntegralList.get(0).get("integral").toString());
				maps.put("personIntegral", personIntegral - integral);
				execute("zhongdeorder/UpdateUserIntegral", maps);
				resultMap.put("payType", "1");
				resultMap.put("orderId", orderId);
				resultMap.put("Msg", "积分扣除成功！");
				formEngineResponse.setData(resultMap);
				formEngineResponse.setCode("0");
				formEngineResponse.setMsg("积分扣除成功！");
			}
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
		return result.replace(".", "").substring(0, 18);
	}

	private String execute(String sqlId, Map<String, Object> data) {
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId(sqlId);
		cModel.setData(data);
		formMapper.saveCustom(cModel);
		return cModel.getPrimaryFieldValue();
	}

	public String GetSandBoxSignKey(Map<String, String> signParam, WXPay wxpay) throws Exception {
		FormLogger.info("获取沙箱key开始");
		String url = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
		String respXml = wxpay.requestWithoutCert(url, signParam, 100000, 10000);
		Map<String, String> resMap = WXPayUtil.xmlToMap(respXml);
		FormLogger.info("获取沙箱key结束:" + respXml);
		return resMap.get("sandbox_signkey").toString();
	}

	public Map<String, String> doOrderQuery(String out_trade_no, WXPayConfig wxPayConfig) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("out_trade_no", out_trade_no);
		Map<String, String> r = new HashMap<>();
		WXPay wxpay = new WXPay(wxPayConfig, WXPayConstants.SignType.MD5, useSandBox);
		try {
			r = wxpay.orderQuery(data);
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

}