package com.tedu.plugin.course;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.tedu.base.common.utils.SessionUtils;
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

@Service("InsertCurriculumCoursePluginA")
public class InsertCurriculumCoursePluginA implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	@Resource
	FormService formService;
	private String questionSql = "khTeacher/homework/homeworkTemplate/QryQuestionByUUID";

	/**
	 * 导入时数据校验
	 */

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		FormEngineResponse response = new FormEngineResponse("");
		Map<String, Object> map = formModel.getData();
		String fileId = map.get("fileId") != null ? map.get("fileId").toString() : "";
		//String questionClassifyId = map.get("questionClassifyId") != null ? map.get("questionClassifyId").toString(): "";
		try {
			QueryPage qp = new QueryPage();
			qp.setParamsByMap(map);
			qp.getData().put("fileId", fileId);
			qp.setQueryParam("course/QryFileById");
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
	
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				// 获得所有数据
				String co = "";
				for (int i = 2; i <= totalRowNum; i++) {
					Map<String, Object> ResultMap = new HashMap<String, Object>();
					List<String> optionList = new ArrayList<String>();
					// 获得第i行对象
					Row row = sheet.getRow(i);
					// 课程分类
					//ResultMap.put("questionClassifyId", questionClassifyId);
					// 获得获得第i行第0列的 课表名称
					Cell cell = row.getCell(0);
					String value = "";
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第1列《课表名称》长度不能超过1000！");
						}
						ResultMap.put("content", value);
					} else {
						throw new Exception("第"+(i+1)+"行第1列《课表名称》为空或者格式不正确！");
					}
					// 获得获得第i行第1列的院校名称
					cell = row.getCell(1);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					QueryPage qp1 = new QueryPage();
					qp1.setParamsByMap(map);
					qp1.setQueryParam("course/QryRegionName");
					List<Map<String, Object>> regionList = formService.queryBySqlId(qp1);
					String regionName = "";
					String regionId = "";
					List <String>schoolname = new ArrayList<String>();
					List <String>schoolIdList = new ArrayList<String>();
					Map<String, Object> fileMap1 = new HashMap<String, Object>(); 
					for(int aa =0;aa<regionList.size();aa++){
						Map<String, Object> regionNameMap = regionList.get(aa);
						regionName=regionNameMap.get("name").toString();
						qp1.getData().put("value", regionName);
						qp1.setQueryParam("course/QryIdByRegionName");
						List<Map<String, Object>> regionIdList = formService.queryBySqlId(qp1);
						Map<String, Object> regionIdMap = regionIdList.get(0);
						regionId=regionIdMap.get("id").toString();
						qp1.getData().put("regionId", regionId);
						qp1.setQueryParam("course/QrySchool");
						
						List<Map<String, Object>> fileList1 = formService.queryBySqlId(qp1);
						
						for(int a1 = 0;a1<fileList1.size();a1++){
							fileMap1 = fileList1.get(a1);
							String schoolName = fileMap1.get("name").toString();
							String schoolId = fileMap1.get("id").toString();
							schoolname.add(schoolName);
							schoolIdList.add(schoolId);
						}
					}
						boolean school = false;
						String schoolId ="";
						if (schoolname.size() > 0) {
							for(int a1 = 0;a1<schoolname.size();a1++){
								String schoolName = schoolname.get(a1);
								if(schoolName.equals(value) && value != null){
									school = true;
									schoolId = schoolIdList.get(a1);
									break;
								}
							}
						}
						if (school) {
							ResultMap.put("schoolId", schoolId);
						} else {
							throw new Exception("第"+(i+1)+"行第2列《院校名称》为空或者不存在！");
						}
					
					// 获得获得第i行第2列的执行时间
					cell = row.getCell(2);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if(!value.equals("")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date date = sdf.parse(value);
						String date1 =sdf.format(new Date());
						Date date2 = sdf.parse(date1);
						if(date.getTime()>=date2.getTime()){
							ResultMap.put("date", date);
						}else{
							throw new Exception("第"+(i+1)+"行第3列《执行时间》小于现在时间！");
						}
					}else{
						throw new Exception("第"+(i+1)+"行第3列《执行时间》为空！");
					}
					// 获得获得第i行第3列的课程专业
					cell = row.getCell(3);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					QueryPage qp2 = new QueryPage();
					qp2.setParamsByMap(map);
					qp2.setQueryParam("course/QryProfessional");
					List<Map<String, Object>> fileList2 = formService.queryBySqlId(qp2);
					String professionalId = "";
					boolean professional = false;
					if (fileList2.size() > 0) {
						for(int a = 0;a<fileList2.size();a++){
							Map<String, Object> fileMap2 = fileList2.get(a);
							String professionalName = fileMap2.get("text").toString();
							if(professionalName.equals(value) && value != null){
								professional = true;
								professionalId = fileMap2.get("value").toString();
								break;
							}
						}
					}
					if (professional) {
						ResultMap.put("professionalId", professionalId);
					} else {
						throw new Exception("第"+(i+1)+"行第4列《课程专业》为空或者不存在！");
					}
					
					// 获得获得第i行第4列的一年级课程
					cell = row.getCell(4);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					String[] courseNameList = value.split(",");
					QueryPage qp3 = new QueryPage();
					qp3.setParamsByMap(map);
					qp3.getData().put("professionalId", professionalId);
					qp3.setQueryParam("course/QryCourse1");
					List<Map<String, Object>> fileList3 = formService.queryBySqlId(qp3);
					String courseId1 ="";
					boolean course = false;
					List <String>courseIdList = new ArrayList<String>(); 
					if (fileList3.size() > 0) {
						for(int c=0;c<courseNameList.length;c++){
							course = false;
							String courseName1 = courseNameList[c];
							for(int a = 0;a<fileList3.size();a++){
								Map<String, Object> fileMap3 = fileList3.get(a);
								String courseName = fileMap3.get("name").toString();
								if(courseName.equals(courseName1) && value != null){
									course = true;
									courseId1 = fileMap3.get("id").toString();
									courseIdList.add(courseId1);
									break;
								}
							}
						}
						
					}
					if (course) {
						ResultMap.put("courseIdList", courseIdList);
					} else {
						throw new Exception("第"+(i+1)+"行第5列《一年级课程》为空、格式错误或者不存在！");
					}
					
					// 获得获得第i行第5列的二年级课程
					cell = row.getCell(5);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					courseNameList = value.split(",");
					List <String>courseIdList2 = new ArrayList<String>(); 
					QueryPage qp4 = new QueryPage();
					qp4.setParamsByMap(map);
					qp4.getData().put("professionalId", professionalId);
					qp4.setQueryParam("course/QryCourse1");
					List<Map<String, Object>> fileList4 = formService.queryBySqlId(qp4);
					boolean course1 = false;
					String courseId2 ="";
					if (fileList4.size() > 0) {
						for(int c=0;c<courseNameList.length;c++){
							course1 = false;
							String courseName1 = courseNameList[c];
							for(int a = 0;a<fileList4.size();a++){
								Map<String, Object> fileMap4 = fileList4.get(a);
								String courseName = fileMap4.get("name").toString();
								if(value != null && !"".equals(value)){
									if(courseName.equals(courseName1) && value != null){
										course1 = true;
										courseId2 = fileMap4.get("id").toString();
										courseIdList2.add(courseId2);
										break;
									}
								}else{
									course1 = true;
									break;
								}
							}
						}
							
						
					}
					if (course1) {
						ResultMap.put("courseIdList2", courseIdList2);
					} else {
						throw new Exception("第"+(i+1)+"行第6列《二年级课程》不存在或者格式错误！");
					}
					
					// 获得获得第i行第6列的三年级课程
					cell = row.getCell(6);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					courseNameList = value.split(",");
					List <String>courseIdList3 = new ArrayList<String>(); 
					QueryPage qp5 = new QueryPage();
					qp5.setParamsByMap(map);
					qp5.getData().put("professionalId", professionalId);
					qp5.setQueryParam("course/QryCourse1");
					List<Map<String, Object>> fileList5 = formService.queryBySqlId(qp5);
					String courseId3 ="";
					boolean course2 = false;
					if (fileList5.size() > 0) {
						for(int c=0;c<courseNameList.length;c++){
							course2 = false;
							String courseName1 = courseNameList[c];
							for(int a = 0;a<fileList5.size();a++){
								Map<String, Object> fileMap5 = fileList5.get(a);
								String courseName = fileMap5.get("name").toString();
								if(value != null && !"".equals(value)){
									if(courseName.equals(courseName1) && value != null){
										course2 = true;
										courseId3 = fileMap5.get("id").toString();
										courseIdList3.add(courseId3);
										break;
									}
								}else{
									course2 = true;
									break;
								}
							}
						}
							
						
					}
					if (course2) {
						ResultMap.put("courseIdList3", courseIdList3);
					} else {
						throw new Exception("第"+(i+1)+"行第7列《三年级课程》不存在或者格式错误！");
					}
					
					// 获得获得第i行第7列的四年级课程
					cell = row.getCell(7);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					courseNameList = value.split(",");
					List <String>courseIdList4 = new ArrayList<String>(); 
					QueryPage qp6 = new QueryPage();
					qp6.setParamsByMap(map);
					qp6.getData().put("professionalId", professionalId);
					qp6.setQueryParam("course/QryCourse1");
					List<Map<String, Object>> fileList6 = formService.queryBySqlId(qp6);
					boolean course3 = false;
					String courseId4 ="";
					if (fileList6.size() > 0) {
						for(int c=0;c<courseNameList.length;c++){
							course3 = false;
							String courseName1 = courseNameList[c];
							for(int a = 0;a<fileList6.size();a++){
								Map<String, Object> fileMap6 = fileList6.get(a);
								String courseName = fileMap6.get("name").toString();
								if(value != null && !"".equals(value)){
									if(courseName.equals(courseName1) && value != null){
										course3 = true;
										courseId4 =fileMap6.get("id").toString();
										courseIdList4.add(courseId4);
										break;
									}
								}else{
									course3 = true;
									break;
								}
							}
						}
						
					}
					if (course3) {
						ResultMap.put("courseIdList4", courseIdList4);
					} else {
						throw new Exception("第"+(i+1)+"行第8列《四年级课程》不存在或者格式错误！");
					}
					
					// 获得获得第i行第8列的五年级课程
					cell = row.getCell(8);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					courseNameList = value.split(",");
					List <String>courseIdList5 = new ArrayList<String>(); 
					QueryPage qp7 = new QueryPage();
					qp7.setParamsByMap(map);
					qp7.getData().put("professionalId", professionalId);
					qp7.setQueryParam("course/QryCourse1");
					List<Map<String, Object>> fileList7 = formService.queryBySqlId(qp7);
					boolean course4 = false;
					String courseId5 ="";
					if (fileList7.size() > 0) {
						for(int c=0;c<courseNameList.length;c++){
							course4 = false;
							String courseName1 = courseNameList[c];
							for(int a = 0;a<fileList7.size();a++){
								Map<String, Object> fileMap7 = fileList7.get(a);
								String courseName = fileMap7.get("name").toString();
								if(value != null && !"".equals(value)){
									if(courseName.equals(courseName1) && value != null){
										course4 = true;
										courseId5 =fileMap7.get("id").toString();
										courseIdList5.add(courseId5);
										break;
									}
								}else{
									course4 = true;
									break;
								}
							}
						}
							
					}
					if (course4) {
						ResultMap.put("courseIdList5", courseIdList5);
					} else {
						throw new Exception("第"+(i+1)+"行第9列《五年级课程》不存在或者格式错误！");
					}
					
					resultList.add(ResultMap);
				}
				
				
				for (Map<String, Object> maps : resultList) {
					String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
					maps.put("uuid", uuid);
					CustomFormModel cModel = new CustomFormModel();
					cModel.setData(maps);
					// 删除
					cModel.setSqlId("course/InsertCurriculum");
					formMapper.saveCustom(cModel);
					
					qp.getData().put("uuid", uuid);
					qp.setQueryParam("course/QryCurriculumUuid");
					qp.setDataByMap(maps);
					List<Map<String, Object>> curriculumUuid= formMapper.query(qp);
					if (curriculumUuid.size() > 0) {
						String courseId = maps.get("courseIdList").toString();
						String[] courseId1=courseId.startsWith("[")?courseId.substring(1, courseId.length()-1).split(","):courseId.split(",");
						String curriculumId = curriculumUuid.get(0).get("curriculumId").toString();
						if(!(courseId.length()<=0)){
							for(String courseId11 : courseId1){
								maps.put("curriculumId", curriculumId);
								maps.put("courseId", courseId11);
								maps.put("gradeType", 1);
								CustomFormModel cMode2 = new CustomFormModel();
								cMode2.setData(maps);
								cMode2.setSqlId("course/InsertCurriculumCourse");
								formMapper.saveCustom(cMode2);
							}
							
						}
						
						courseId = maps.get("courseIdList2").toString();
						courseId1=courseId.startsWith("[")?courseId.substring(1, courseId.length()-1).split(","):courseId.split(",");
						if(!(courseId.length()<=0)){
							for(String courseId11 : courseId1){
								if(!(courseId11.length()<=0)){
									maps.put("curriculumId", curriculumId);
									maps.put("courseId", courseId11);
									maps.put("gradeType", 2);
									CustomFormModel cMode2 = new CustomFormModel();
									cMode2.setData(maps);
									cMode2.setSqlId("course/InsertCurriculumCourse");
									formMapper.saveCustom(cMode2);
								}
								
							}
							
						}
						
						courseId = maps.get("courseIdList3").toString();
						courseId1=courseId.startsWith("[")?courseId.substring(1, courseId.length()-1).split(","):courseId.split(",");
						if(!(courseId.length()<=0)){
							for(String courseId11 : courseId1){
								if(!(courseId11.length()<=0)){
									maps.put("curriculumId", curriculumId);
									maps.put("courseId", courseId11);
									maps.put("gradeType", 3);
									CustomFormModel cMode2 = new CustomFormModel();
									cMode2.setData(maps);
									cMode2.setSqlId("course/InsertCurriculumCourse");
									formMapper.saveCustom(cMode2);
								}
								
							}
							
						}
						
						courseId = maps.get("courseIdList4").toString();
						courseId1=courseId.startsWith("[")?courseId.substring(1, courseId.length()-1).split(","):courseId.split(",");
						if(!(courseId.length()<=0)){
							for(String courseId11 : courseId1){
								if(!(courseId11.length()<=0)){
									maps.put("curriculumId", curriculumId);
									maps.put("courseId", courseId11);
									maps.put("gradeType", 4);
									CustomFormModel cMode2 = new CustomFormModel();
									cMode2.setData(maps);
									cMode2.setSqlId("course/InsertCurriculumCourse");
									formMapper.saveCustom(cMode2);
								}
								
							}
							
						}
						
						courseId = maps.get("courseIdList5").toString();
						courseId1=courseId.startsWith("[")?courseId.substring(1, courseId.length()-1).split(","):courseId.split(",");
						if(!(courseId.length()<=0)){
							for(String courseId11 : courseId1){
								if(!(courseId11.length()<=0)){
									maps.put("curriculumId", curriculumId);
									maps.put("courseId", courseId11);
									maps.put("gradeType", 5);
									CustomFormModel cMode2 = new CustomFormModel();
									cMode2.setData(maps);
									cMode2.setSqlId("course/InsertCurriculumCourse");
									formMapper.saveCustom(cMode2);
								}
								
							}
							
						}
					}
					
				}
				
			}
		}catch (Exception e) {
			responseObj.setMsg("导入失败：请检查导入文件"+e.getMessage());
		}
	}

}
