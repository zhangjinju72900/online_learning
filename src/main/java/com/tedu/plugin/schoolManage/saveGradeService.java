package com.tedu.plugin.schoolManage;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.xml.ValidationException;
import org.springframework.stereotype.Service;

@Service("saveGradeService")
public class saveGradeService implements ILogicPlugin {
	@Resource
	FormService formService;
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		log.info(formModel.getData().toString());
		//获取院校id,年级名
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		String schoolId=map.get("schoolId")+"";		
		String grade=map.get("grade")+"";
		//获取班级name
		String className=map.get("name")+"";
		String curriculumId = map.get("curriculumId")+"";
		//获取班级id
		String id = map.get("id")+"";
		String mode = map.get("Mode") == null ? "" : map.get("Mode").toString();
		if("".equals(id)){
			map.put("id", 0);
		}else{
			map.put("id", Integer.parseInt(id));
		}
		//通过院校id查询班级名
		QueryPage queryPage=new QueryPage(); 
		queryPage.setQueryParam("khAdmin/schoolManage/class/QryClassBySchoolId");
		queryPage.setDataByMap(map);
		List<Map<String,Object>> list=formMapper.query(queryPage);
		String sum = list.get(0).get("sum")+"";
		//判断list集合的name是否与页面name重复
		if ("Edit".equals(mode)) {
			if("0".equals(sum)){
				
			}else{
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"班级重复了","同一所学校同一年级下不能创建相同名字的班级");
			}
		}
		if("Add".equals(mode)){
			if("0".equals(sum)){
				
			}else{
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"课表有误","当前学校下存在该课表");
			}
		}
		map.put("curriculumId", Integer.parseInt(curriculumId));
		//通过院校id查询班级名
		QueryPage qPage=new QueryPage(); 
		qPage.setQueryParam("khAdmin/schoolManage/class/Curriculum");
		qPage.setDataByMap(map);
		List<Map<String,Object>> l=formMapper.query(qPage);
		String s = l.get(0).get("sum")+"";
		System.out.println("s::::::::::"+s);
		if("0".equals(s)){
			throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"学校改变","请重新选择课表");
		}
		//通过班级id查院校名
		queryPage.setQueryParam("khAdmin/schoolManage/class/QryBySchoolId");
		queryPage.setDataByMap(map);
		List<Map<String,Object>> list1=formMapper.query(queryPage);
		String schoolId1 = list1.get(0).get("schoolId").toString();
		if ("Edit".equals(mode)&& !schoolId1.equals(schoolId)) {
			Map<String,Object> data = new HashMap<>();
			  String sqlId = "khAdmin/schoolManage/class/DelClassToTeacher";
			  //存放更新的字段
			  CustomFormModel cfModel = new CustomFormModel();
			  cfModel.setSqlId(sqlId);
			  
			  //放更新的id
			  data.put("id", Integer.parseInt(id));
			  cfModel.setData(data);
			  formMapper.saveCustom(cfModel);
		}
		
		return formModel;
	}
	
	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {

	}
}
