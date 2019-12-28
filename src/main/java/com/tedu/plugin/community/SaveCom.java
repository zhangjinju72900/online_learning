package com.tedu.plugin.community;


import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("saveCom")
public class SaveCom implements ILogicReviser {
    @Resource
    FormMapper formMapper = SpringUtils.getBean("simpleDao");

    @Override
    public FormModel beforeLogic(FormEngineRequest formEngineRequest) {
        return null;
    }

    @Override
    public void afterLogic(FormEngineRequest requestObj, FormEngineResponse responseObj) {
        Map<String,Object> map = requestObj.getData();


        /*//查出社区下一个需要的id
        QueryPage page = new QueryPage();
        page.setDataByMap(map);
        page.setQueryParam("khAdmin/resMan/QryMaxComId");
        List<Map<String,Object>> list = formMapper.query(page);
*/

        /* Map<String,Object> map1 = new HashMap<>();
            int id=Integer.parseInt(list.get(0).get("id").toString());
                map1.put("id",id);
                //将社区插入 到表中*/
                CustomFormModel model = new CustomFormModel();
                model.setData(map);
                model.setSqlId("khAdmin/resMan/InsertCom");
                formMapper.saveCustom(model);

    }
}
