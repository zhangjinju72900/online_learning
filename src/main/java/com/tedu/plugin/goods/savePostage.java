package com.tedu.plugin.goods;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("savePostage")
public class savePostage implements ILogicPlugin {
	@Resource
	FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		/*价格和免邮价格不能为空*/
		//获取价格
		String price=map.get("price")+"";
		//获取免邮价格
		String freeshipping=map.get("freeshipping")+"";
		//获取省份
		String province1=map.get("province")+"";
		log.info(province1);
		/*if(price.equals("")&&freeshipping.equals("")&&!province1.equals("")){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","价格与免邮价格不能为空");
		}
		if(price.equals("")&&freeshipping.equals("")&&province1.equals("")){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","省份,价格和免邮价格均不能为空");
		}
		if(price.equals("")){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"请填写价格","价格不能为空");
		}
		
		if(freeshipping.equals("")){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"请填写免邮价格","免邮价格不能为空");
		}*/
		int priceInt=Integer.parseInt(price);
		int freeshippingInt=Integer.parseInt(freeshipping);
		if(freeshippingInt<0 && priceInt<0){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","价格与免邮价格不能为负数");
		}
		if(priceInt<0){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","价格不能为负数");
		}
		if(freeshippingInt<0){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","免邮价格不能为负数");
		}
		/*弹出省份已存在*/
		//1.获取当前要增加的省份
		String province=map.get("province")+"";
		//2.查询数据库中的所有省份名
		QueryPage queryPage=new QueryPage(); 
		//queryPage.setQueryParam("khAdmin/postage/QryCityList");		
		//查询数据库中所有省份code
		queryPage.setQueryParam("khAdmin/postage/QryProvinceCode");				
		queryPage.setDataByMap(map);
		//3.判断是否省份code是否重复
		List<Map<String,Object>> list=formMapper.query(queryPage);
		for(int i=0;i<list.size();i++){
			String provinceList=list.get(i).get("province")+"";
			if(province.equals(provinceList)){
				//获取页面省份code对应的provinceName
				QueryPage queryPage1=new QueryPage(); 
				/*Map requestMap=new HashMap<String,Object>();
				requestMap.put("province", provinceList);*/
				map.put("province", provinceList);
				queryPage1.setDataByMap(map);
				queryPage1.setQueryParam("khAdmin/postage/QryPageProvinceName");
				List<Map<String,Object>> list1=formMapper.query(queryPage1);
				log.info(list1.toString());
				
				String provinceName=list1.get(0).get("provinceName")+"";
				
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"省份",provinceName+"已存在");
			}
		}
		return null;
		
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		
		/*CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> data = formModel.getData();
		cModel.setData(data);
		log.info(data);
		cModel.setSqlId("khApp/mine/mall/SaveGoodsCollect");
		formMapper.saveCustom(cModel);*/
	}
}
