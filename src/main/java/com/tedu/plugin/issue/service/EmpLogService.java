package com.tedu.plugin.issue.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
/**
 * 验证专业名是否重复
 * @version 1.0
 * @date 2019年4月4日11点27分
 * @author Dali
 *
 */
@Service("empLogService")
public class EmpLogService implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;		


	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		//获取页面数据
		Map<String,Object> map1 = formModel.getData();
		//获取专业名称
		String ctlProfessionName = map1.get("ctlProfessionName").toString();
		String ctlProfessionId = map1.get("ctlProfessionId").toString();
		QueryPage page1 = new QueryPage();
		//设置sql查询条件
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("ctlParent", map1.get("ctlParent"));
		page1.setQueryParam("zhongdeprofession/QryCheckName");
		page1.setDataByMap(map2);
		//数据库查询到数据
		List<Map<String,Object>> list = formMapper.query(page1);
		//判断是否重复
		if(list.size()>0){
			for(int i = 0;i<list.size();i++){
				if(ctlProfessionName.equals(list.get(i).get("name").toString()) && (ctlProfessionId.equals(list.get(i).get("id").toString()))){
					throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "专业名重复","专业名与该分支下的"+ctlProfessionName+"重复");
				}
			}
		}
		return formModel;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,FormEngineResponse responseObj) {
		  
	}
}
