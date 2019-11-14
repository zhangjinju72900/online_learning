package com.tedu.plugin.teacher.maintain.manual;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insertManualTreeData")
public class InsertManualTreeData implements ILogicPlugin {
    @Autowired(required=false)
    FormLogService formLogService;
    @Autowired(required=false)
    private FormMapper formMapper;

    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        return null;
    }

    @Override
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
        Map<String, Object> map = (Map<String, Object>) formModel.getData();
        CustomFormModel cModel = new CustomFormModel();
        System.out.println(map);
    	QueryPage q = new QueryPage();
        q.setDataByMap(map);
        q.setQueryParam("khAdmin/maintainManual/selectManualDetails");
        List<Map<String,Object>> list = formMapper.query(q);
        if(list != null && list.size() > 0){
        	map.put("path", list.get(0).get("path"));
        	map.put("fileName", list.get(0).get("fileName"));
        	map.put("type", list.get(0).get("type"));
        	map.put("ossKey", list.get(0).get("ossKey"));
        	map.put("ossUrl", list.get(0).get("ossUrl"));
        }else{
        	map.put("path", "");
        	map.put("fileName", "");
        	map.put("type", "");
        	map.put("ossKey", "");
        	map.put("ossUrl", "");
        	map.put("fileId", 0);
        }
        if(map.get("id")==""){
        	map.put("pid", map.get("parentId"));
        	cModel.setData(map);
        	cModel.setSqlId("khAdmin/maintainManual/insertManual");
        	formMapper.saveCustom(cModel);
        }else{
        	map.put("pid", map.get("parentId"));
        	cModel.setData(map);
        	cModel.setSqlId("khAdmin/maintainManual/updateManual");
        	formMapper.saveCustom(cModel);
        }
    }
}
