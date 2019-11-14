package com.tedu.plugin.student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.alibaba.druid.sql.visitor.functions.If;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.task.SpringUtils;
import com.tedu.business.user.service.CustomUserService;
import com.tedu.component.EasemobComponent;

@Service("ImportStudentPlugin")
public class ImportStudentPlugin implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	@Resource
	FormService formService;
	@Resource
	private EasemobComponent easemobComponent;
	
	@Resource
	private CustomUserService customUserServiceImpl;
	
	
	private String studentSql = "khAdmin/schoolManage/student/QryStudentByUUID";

	/**
	 * 导入时数据校验
	 */

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		FormEngineResponse response = new FormEngineResponse("");
		Map<String, Object> map = formModel.getData();
		String fileId = map.get("fileId") != null ? map.get("fileId").toString() : "";
		try {
		QueryPage qp = new QueryPage();
		qp.setParamsByMap(map);
		qp.getData().put("fileId", fileId);
		qp.setQueryParam("khAdmin/schoolManage/student/QryFileById");
		List<Map<String, Object>> fileList = formService.queryBySqlId(qp);
		if (fileList.size() > 0) {
			Map<String, Object> fileMap = fileList.get(0);
			String filePath = fileMap.get("path").toString();
			String fileName = fileMap.get("uuid").toString();
			String fileType = fileMap.get("fileType").toString();
			filePath = filePath + fileName + "." + fileType;
			File file = new File(filePath);

			FileInputStream fs = null;

			Workbook wookbook = null;
			try {
				// 2003版本的excel，用.xls结尾
				fs = new FileInputStream(file);

				wookbook = new HSSFWorkbook(fs);// 得到工作簿

			} catch (Exception ex) {
				// ex.printStackTrace();
				try {
					// 2007版本的excel，用.xlsx结尾
					wookbook = new XSSFWorkbook(fs);// 得到工作簿
				} catch (IOException e) {
					throw new Exception("模板版本不正确，请从新下载模板！");
				}
			}

			Sheet sheet = wookbook.getSheetAt(0); // 获取第一个工作表，一个excel可能有多个工作表
			// 得到一个工作表

			// 获得数据的总行数
			int totalRowNum = sheet.getLastRowNum();

			List<Map<String, Object>> StudentList = new ArrayList<Map<String, Object>>();
			// 获得所有数据
			String co = "";
			Map<String, Object> cardMap = new HashMap<String, Object>();
			for (int i = 2; i <= totalRowNum; i++) {
				Map<String, Object> ResultMap = new HashMap<String, Object>();
				// 获得第i行对象
				Row row = sheet.getRow(i);
				// 获得第i行第0列的 姓名
				Cell cell = row.getCell(0);
				String value = "";
				String value1 = "";
				String value2 = "";
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if(value.length()>50) {
						throw new Exception("第"+(i+1)+"行第1列《姓名》长度不能超过50！");
					}
					ResultMap.put("name", value);
				} else {
					throw new Exception("第"+(i+1)+"行第1列《姓名》为空或者格式不正确！");
				}
				// 获得获得第i行第1列的 性别
				cell = row.getCell(1);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if ("男".equals(value)) {
						value = "0";
					} else {
						value = "1";
					}
					ResultMap.put("sex", value);
				} else {
					throw new Exception("第"+(i+1)+"行第2列《性别》为空或者格式不正确！");
				}
				Map<String,Object> data=new HashMap<String,Object>();
				// 获得获得第i行第2列的 身份证号
				cell = row.getCell(2);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if(value.length()!=15&&value.length()!=18) {
						throw new Exception("第"+(i+1)+"行第3列《身份证》长度不正确！");
					}
					if(cardMap.get(value)!=null) {
						throw new Exception("第"+(i+1)+"行第3列《身份证》在该文档中存在！");
					}
					
					
					data.put("cardNum", value);
					//身份证不能重复
					QueryPage queryPage2 = new QueryPage();
					queryPage2.setDataByMap(data);
					queryPage2.setQueryParam("khAdmin/schoolManage/student/QrycardNumCount");
					List<Map<String, Object>> R = formMapper.query(queryPage2);
					String count = R.get(0).get("count")+"";
					if(!count.equals("0")){
						throw new Exception("第"+(i+1)+"行第3列《身份证》重复！");
					}
					ResultMap.put("cardNum", value);
					cardMap.put(value,value);
				} else {
					throw new Exception("第"+(i+1)+"行第3列《身份证》为空或者格式不正确！");
				}
				// 获得获得第i行第3列的 联系电话
				cell = row.getCell(3);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if(value.length()>50) {
						throw new Exception("第"+(i+1)+"行第4列《 联系电话》长度不能超过50！");
					}
					ResultMap.put("tel", value);
				} else {
					ResultMap.put("tel", "");
				}
				//获得获得第i行第4列的 学校
				cell = row.getCell(4);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				/*Map<String,Object> data=new HashMap<String,Object>();*/
				if (value != null && !"".equals(value)) {
					QueryPage queryPage = new QueryPage();
					data.put("schoolName", value);
					queryPage.setDataByMap(data);
					queryPage.setQueryParam("khAdmin/schoolManage/student/QrySchoolId");
					List<Map<String, Object>> l = formMapper.query(queryPage);
					String s="";
					String r="";
					if(l.size()==0){
						throw new Exception("第"+(i+1)+"行第5列《学校》不存在！");
					}else{
						 s = l.get(0).get("id")+"";
						 r  = l.get(0).get("regionName")+"";
					}
					
					/*if("".equals(s)) {
						throw new Exception("第"+(i+1)+"行第5列《学校》不存在！");
					}*/
					//获得权限该用户的大区权限
					QueryPage queryPage1 = new QueryPage();
					queryPage1.setDataByMap(data);
					queryPage1.setQueryParam("khAdmin/schoolManage/student/QryRegion");
					List<Map<String, Object>> R = formMapper.query(queryPage1);
					int a=0;
					for (int j = 0; j <R.size(); j++) {
						String Rname = R.get(j).get("name")+"";
						if(Rname.equals(r)){
							ResultMap.put("schoolId", s);
							data.put("schoolId", s);
							a+=1;
							break;
						}
					}
					if(a==0){
						throw new Exception("该用户没有第"+(i+1)+"行第5列《学校》所在大区权限！");
					}
				
				} else {
					throw new Exception("第"+(i+1)+"行第5列《学校》为空或者格式不正确！");
				}
				
				// 获得获得第i行第7列的年级
				cell = row.getCell(7);
				value1 = cell != null ? cell.getStringCellValue() + "" : "";
				value1=value1.trim();
				if (value1 != null && !"".equals(value1)) {
					data.put("grade", value1);
					// 获得获得第i行第5列的班级
					Cell cell2 = row.getCell(5);
					value2 = cell2 != null ? cell2.getStringCellValue() + "" : "";
					value2=value2.trim();
					if (value2 != null && !"".equals(value2)) {
						data.put("className", value2);
						QueryPage queryPage = new QueryPage();
						queryPage.setDataByMap(data);
						queryPage.setQueryParam("khAdmin/teacher/QryClassId");
						List<Map<String, Object>> list = formMapper.query(queryPage);
						if(list.size()!=0){
							String c = list.get(0).get("id")+"";
							ResultMap.put("grade", value1);
							ResultMap.put("classId", c);
						}else{
							throw new Exception("第"+(i+1)+"行第6列《班级》不存在或当前学校下没有该班级！");
						}
						
					} 
				} 
					
					// 获得获得第i行第6列的 个性签名
					cell = row.getCell(6);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>200) {
							throw new Exception("第"+(i+1)+"行第7列《 个性签名》长度不能超过200！");
						}
						ResultMap.put("userExplain", value);
					} else {
						ResultMap.put("userExplain", "");
					}
					
					StudentList.add(ResultMap);
			}	
			

			for (Map<String, Object> maps : StudentList) {
				String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
				maps.put("uuid", uuid);
				CustomFormModel cModel = new CustomFormModel();
				cModel.setData(maps);
				//默认密码
				String password = "123456";
				String salt = TokenUtils.genUUID().toUpperCase();
				// String salt = AviatorEvaluator.execute("Guid()").toString();
				password = MD5Util.MD5Encode(password).toUpperCase();
				String saltPassword = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR, salt, password);
			
				maps.put("password", saltPassword);
				
				cModel.setSqlId("khAdmin/schoolManage/student/insertStudent");
				formMapper.saveCustom(cModel);
				
				qp.getData().put("uuid", uuid);
				qp.setQueryParam(studentSql);
				qp.setDataByMap(maps);
				List<Map<String, Object>> userId = formMapper.query(qp);
				maps.put("userId", userId.get(0).get("userId").toString());
				cModel.setData(maps);
				cModel.setSqlId("khAdmin/schoolManage/student/insertStudentClass");
				formMapper.saveCustom(cModel);
				
				cModel.setSqlId("khAdmin/schoolManage/student/insertStudentRole");
				formMapper.saveCustom(cModel);
				
				easemobComponent.register(easemobComponent.getToken(),userId.get(0).get("userId").toString(),
						userId.get(0).get("userId").toString());
				customUserServiceImpl.updateEasemobUser(userId.get(0).get("userId").toString(), userId.get(0).get("userId").toString());
				
			}

		  }
		}catch (Exception e) {
			responseObj.setMsg("导入失败：请检查导入文件"+e.getMessage());
		}
	}

}
