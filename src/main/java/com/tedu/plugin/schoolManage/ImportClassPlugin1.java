package com.tedu.plugin.schoolManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.task.SpringUtils;
/**
 * 导入班级
 * 
 * @author wuyun
 *
 */
@Service("ImportClassPlugin")
public class ImportClassPlugin1 {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	@Resource
	FormService formService;
	private String schoolId = null;

	/**
	 * 导入时数据校验
	 */

	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		FormEngineResponse response = new FormEngineResponse("");
		Map<String, Object> map = formModel.getData();
		String fileId = map.get("fileId") != null ? map.get("fileId").toString() : "";
		try {
		QueryPage qp = new QueryPage();
		qp.setParamsByMap(map);
		qp.getData().put("fileId", fileId);
		qp.setQueryParam("khAdmin/schoolManage/class/QryFileById");
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
			for (int i = 2; i <= totalRowNum; i++) {
				Map<String, Object> ResultMap = new HashMap<String, Object>();
				// 获得第i行对象
				Row row = sheet.getRow(i);
				// 获得获得第i行第0列的 学校
				Cell cell = row.getCell(0);
				String value = "";
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				QueryPage qp1 = new QueryPage();
				qp1.setParamsByMap(map);
				qp1.setQueryParam("khAdmin/schoolManage/class/QryRegionName");
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
					qp1.setQueryParam("khAdmin/schoolManage/class/QryIdByRegionName");
					List<Map<String, Object>> regionIdList = formService.queryBySqlId(qp1);
					Map<String, Object> regionIdMap = regionIdList.get(0);
					regionId=regionIdMap.get("id").toString();
					qp1.getData().put("regionId", regionId);
					qp1.setQueryParam("khAdmin/schoolManage/class/QrySchool");
					
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
						throw new Exception("第"+(i+1)+"行第2列'院校名称'为空或者不存在！");
					}
				
				
				
				// 获得获得第i行第1列的 项目类型
				cell = row.getCell(1);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					ResultMap.put("classType", value);
				} else {
					throw new Exception("第"+(i+1)+"行第2列'项目类型'为空或者格式不正确！");
				}
				
				// 获得获得第i行第2列的 班级
				cell = row.getCell(2);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if(value.length()>1000) {
						throw new Exception("第"+(i+1)+"行第1列'班级'长度不能超过1000！");
					}
					ResultMap.put("className", value);
				} else {
					throw new Exception("第"+(i+1)+"行第1列'班级'为空或者格式不正确！");
				}
					// 获得获得第i行第3列的年级
				cell = row.getCell(3);
				value = cell != null ? cell.getStringCellValue()+ "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					ResultMap.put("grade", value);
				} else {
					throw new Exception("第"+(i+1)+"行第2列'年级'为空或者格式不正确！");
				}
					// 获得获得第i行第4列的 开课时间
				cell = row.getCell(4);
			     value = cell != null ? cell.getStringCellValue() + "" : "";
			     value=value.trim();
			     if(!value.equals("")){
			      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			      Date date = sdf.parse(value);
			      String date1 =sdf.format(new Date());
			      Date date2 = sdf.parse(date1);
			      if(date.getTime()>=date2.getTime()){
			       ResultMap.put("date", value);
			      }else{
			       throw new Exception("第"+(i+1)+"行第3列'执行时间'小于现在时间！");
			      }
			     }else{
			      throw new Exception("第"+(i+1)+"行第3列'执行时间'为空！");
			     }
					
					// 获得获得第i行第5列的 课表
					cell = row.getCell(5);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第6列'课表'长度不能超过1000！");
						}
						qp.setParamsByMap(map);
						qp.getData().put("name", value);
						qp.getData().put("schoolId", schoolId);
						// 从数据库中获取region的id属性
						qp.setQueryParam("khAdmin/schoolManage/class/QryIdBycurriculumName");
						List<Map<String, Object>> curriculumIdList = formService.queryBySqlId(qp);
						if (curriculumIdList.size() > 0) {
								// 将获取到的regionId用变量承接
								Map<String, Object> curriculumIdMap = curriculumIdList.get(0);
								String curriculumId = curriculumIdMap.get("id").toString();
								ResultMap.put("curriculumId", curriculumId);
							}else{
								throw new Exception("第"+(i+1)+"行第6列'课表'不存在或不唯一");
							}
					} else {
						 throw new Exception("第"+(i+1)+"行第3列《课表》为空！");
					}
				resultList.add(ResultMap);
			}

			for (Map<String, Object> maps : resultList) {
				CustomFormModel cModel = new CustomFormModel();
				cModel.setData(maps);
				// 删除
				cModel.setSqlId("khAdmin/schoolManage/class/InsertClass");
				formMapper.saveCustom(cModel);

					}

				}

		}catch (Exception e) {
			responseObj.setMsg("导入失败：请检查导入文件"+e.getMessage());
		}
	}

}
