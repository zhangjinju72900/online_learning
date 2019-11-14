package com.tedu.plugin.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

@Service("insertTeacherPlugin")
public class InsertTeacherPlugin implements ILogicPlugin {
	
	private String sqlTemplate = "khAdmin/schoolManage/student/insertRole";

	@Resource
	private FormService formService;
	
	@Resource
	private EasemobComponent easemobComponent;

	@Resource
	private CustomUserService customUserServiceImpl;
	
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		log.info(formModel.getData().toString());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		String id = map.get("id") == null ? "" : map.get("id").toString();
		String tel = map.get("tel") == null ? "" : map.get("tel").toString();
		String cardNum = map.get("cardNum") == null ? "" : map.get("cardNum").toString();
		
		QueryPage qp = new QueryPage();
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("tel", StringUtils.isBlank(tel)?"--":tel);
		requestMap.put("cardNum", StringUtils.isBlank(cardNum)?"--":cardNum);
		if(StringUtils.isBlank(id) || "0".equals(id)){ 
			qp.setQueryParam("khAdmin/userManage/QryUserByCardNumOrTel");
			qp.setSqlId("khAdmin/userManage/QryUserByCardNumOrTel");
		}else{
			requestMap.put("id", id);
			qp.setQueryParam("khAdmin/userManage/QryUserByCardNumOrTelAndId");
			qp.setSqlId("khAdmin/userManage/QryUserByCardNumOrTelAndId");
		}
		qp.setDataByMap(requestMap);
		List qlist = formService.queryBySqlId(qp);
		if(qlist != null && qlist.size() > 0){
			throw new ValidException(ErrorCode.MAIL_EXCEPTION, "身份证号或手机号重复", cardNum);
		}
	//改变学校重新选择班级
	     //1.获取页面班级		
	  List pClass = (List) map.get("pClass");
	  List list = new ArrayList();
	  for (int i = 0; i < pClass.size(); i++) {
	   Map<String,Object> classs = (Map<String, Object>) pClass.get(i);	   
	   String del = classs.get("delete")+"";	   
	   System.out.println("delete："+del);	   
	   if(!del.equals("1")){		  
	     Object cil = classs.get("className");
	     System.out.println("dedede"+cil);
		   list.add(cil);
	   }		  
	  }  
	  //2.通过院校id查询班级名
	     String schoolId=map.get("eq_schoolId")+"";    
	     QueryPage queryPage=new QueryPage(); 
	     queryPage.setQueryParam("khAdmin/schoolManage/teacher/QryClassBySchoolId");
	     queryPage.setDataByMap(map);
	     List<Map<String,Object>> list1=formMapper.query(queryPage);  	     
	     List name = new ArrayList();
	     for (int i = 0; i < list1.size(); i++) {
			   Map<String,Object> namee = (Map<String, Object>) list1.get(i);
			   Object a = namee.get("name");
			   name.add(a);
			  }  
	   //3.判断页面上的班级在该学校下是否存在，若存在则保存成功，不存在则提示重新选择班级 
	     if(name.containsAll(list)){	      
	     }else{
	      throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"请重新选择班级","班级在该学校中不存在"); 
	     }

		
		/*List pClass = (List) map.get("pClass");
		List list = new ArrayList();
		for (int i = 0; i < pClass.size(); i++) {
			Map<String,Object> classs = (Map<String, Object>) pClass.get(i);
			Object cil = classs.get("className");
			list.add(cil);
		}
		for(int i = 0; i < list.size(); i++){          
            for (int j = i + 1; j < list.size(); j++){
                if (list.get(i).equals(list.get(j))){
                	throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,"不能添加同一个班级",list.get(i)+"班级重复");
                }
            }
        }
		*/
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData());
		Map<String, Object> map = (Map<String, Object>) formModel.getData();
		// 获取主表的id
		String id = formModel.getPrimaryFieldValue().toString();
		// 获取roleid并解析
		/* String[] res = responseObj.getData().toString().split(","); */
		String schoolId = map.get("schoolId") == null ? "" : map.get("schoolId").toString();
		/* LogUtil.info("ides"+ides+"----"+"shcoolId---"+schoolId); */
		String mode = map.get("Mode") == null ? "" : map.get("Mode").toString();

		FormModel my = new FormModel("frmStudentNew", "pRole");
		my.getData().put("id", my.getPrimaryFieldValue());
		CustomFormModel cModel = new CustomFormModel();
		Map<String, Object> data = my.getData();
		if ("Add".equals(mode)) {
			cModel.setData(data);
			cModel.setSqlId(sqlTemplate);
			data.put("customerId", id);
			data.put("roleId", 11);
			try {
				formMapper.saveCustom(cModel);
			} catch (Exception e) {
				LogUtil.info(e.toString());

			}
		}
		
		if ("Add".equals(mode)) {//生成环信账号
			try {
				easemobComponent.register(easemobComponent.getToken(),id,
						map.get("cardNum").toString());
				customUserServiceImpl.updateEasemobUser(id,id);
			} catch (Exception e) {
				log.error("新建学生时生成环信账号失败", e);
				e.printStackTrace();
			}
		}

	}

}
