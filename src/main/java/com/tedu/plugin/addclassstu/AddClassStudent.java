package com.tedu.plugin.addclassstu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
/**
 * 可以实现学生,老师的多个一起插入
 *
 */
@Service("AddClassStudent")
public class AddClassStudent implements ILogicPlugin{
	
	@Resource
	private FormMapper formMapper;
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		//得到数据
		Map<String, Object> map1 = formModel.getData();
		//输出数据
		System.out.println("得到的数据"+map1.toString());
		//存放查出来的id
		List<String> ids = new ArrayList<String>();
		if(map1.get("userId").toString().contains(",")){ //若有多个id
			String[] is = map1.get("userId").toString().split(",");
			//获取页面所有id,存入集合中
			for(String id : is){
				ids.add(id);
			}
		}else{ //若只有一个
			ids.add(map1.get("userId").toString());
		}
		System.out.println("集合中元素"+ids);
		//遍历每一个进行插入
		for(String id: ids){
			//放置参数的map
			Map<String,Object> data = new HashMap<>();
			//放要插入的id和班级id
			data.put("id", Integer.parseInt(id));
			data.put("eq_classId", map1.get("eq_classId"));
			//创建编译对象,要插入的数据   自定义增删改查
			CustomFormModel cfModel = new CustomFormModel("","",data);
			cfModel.setSqlId("InsertUserClass");
			//执行sql
			formMapper.saveCustom(cfModel);
		}
	}
	
}
