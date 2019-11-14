package com.tedu.plugin.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;

@Service("saleClueAdd")
public class SaleCluePlugin implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
/*	boolean customer = false;
	boolean phone = false;
	String customerId = null;
	String id = null;
*/
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData());
		//成功率的校验
		String successRate = formModel.getData().get("ctlSuccessRate").toString();
		if (StringUtils.isNotEmpty(successRate)){
			//判断是否为0到100的数
			Pattern pattern = Pattern.compile("^(((\\d|[1-9]\\d)(\\.\\d{1,2})?)|100|100.0|100.00)$");
			Matcher num = pattern.matcher(successRate);
			Boolean flag = num.matches();
			if (!flag) {
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "输入提示:默认用%", "请输入0到100的整数或者小数");
			}
		}
		
		
		//合同预估金额和销售预计收入的校验
		String amount = formModel.getData().get("ctlContractAmount").toString();
		log.info(amount);
			if (StringUtils.isNotEmpty(amount)&&Double.parseDouble(amount)<0) {
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "输入提示:", "合同预估金额请输入正数");
			}
		String income = formModel.getData().get("ctlSalesRevenue").toString();
		log.info(income);
			if (StringUtils.isNotEmpty(income)&&Double.parseDouble(income)<0) {
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "输入提示:", "销售预计收入请输入正数");
			}
		
		return null;

	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		}
}
