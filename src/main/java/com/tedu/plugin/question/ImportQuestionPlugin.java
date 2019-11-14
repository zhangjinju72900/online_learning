package com.tedu.plugin.question;

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

@Service("ImportQuestionPlugin")
public class ImportQuestionPlugin implements ILogicPlugin {
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
		String questionClassifyId = map.get("questionClassifyId") != null ? map.get("questionClassifyId").toString(): "";
		try {
		QueryPage qp = new QueryPage();
		qp.setParamsByMap(map);
		qp.getData().put("fileId", fileId);
		qp.setQueryParam("khAdmin/question/QryFileById");
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
				ResultMap.put("questionClassifyId", questionClassifyId);
				// 获得获得第i行第0列的 问题描述
				Cell cell = row.getCell(0);
				String value = "";
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if(value.length()>1000) {
						throw new Exception("第"+(i+1)+"行第1列《问题描述》长度不能超过1000！");
					}
					ResultMap.put("content", value);
				} else {
					throw new Exception("第"+(i+1)+"行第1列《问题描述》为空或者格式不正确！");
				}
				// 获得获得第i行第1列的 题型
				cell = row.getCell(1);
				value = cell != null ? cell.getStringCellValue() + "" : "";
				value=value.trim();
				if (value != null && !"".equals(value)) {
					if ("单选题".equals(value)) {
						value = "0";
					} else if ("多选题".equals(value)) {
						value = "1";
					} else {
						value = "2";
					}
					ResultMap.put("questionType", value);
				} else {
					throw new Exception("第"+(i+1)+"行第2列《题型》为空或者格式不正确！");
				}
				String questionType = value;
				// 判断题
				if ("2".equals(questionType)) {
					co = "对错";
					// 获得获得第i行第2列的 正确答案
					cell = row.getCell(2);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value) && value.length() == 1&& co.contains(value)) {
						ResultMap.put("correctFlag", value);
					} else {
						throw new Exception("第"+(i+1)+"行第3列《正确答案》为空或者格式不正确！");
					}
					// 获得获得第i行第1列的难度
					cell = row.getCell(3);
					value = cell != null ? cell.getStringCellValue() + "" : "1";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if ("简单".equals(value)) {
							value = "0";
						} else if ("一般难度".equals(value)) {
							value = "1";
						}
						else if ("比较难".equals(value)) {
							value = "2";
						}else {
							value = "3";
						}
						ResultMap.put("difficultyLevel", value);
					} else {
						throw new Exception("第"+(i+1)+"行第4列《难度》为空或者格式不正确！");
					}
					// 获得获得第i行第4列的 答案解析
					cell = row.getCell(4);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第5列《 答案解析》长度不能超过1000！");
						}
						ResultMap.put("answerThought", value);
					} else {
						ResultMap.put("answerThought", "");
					}
					optionList.add("正确");
					optionList.add("错误");
					ResultMap.put("option", optionList);

				} else {
					co = "";
					
					// 获得获得第i行第1列的难度
					cell = row.getCell(3);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if ("简单".equals(value)) {
							value = "0";
						} else if ("一般难度".equals(value)) {
							value = "1";
						}
						else if ("比较难".equals(value)) {
							value = "2";
						}else {
							value = "3";
						}
						ResultMap.put("difficultyLevel", value);
					} else {
						throw new Exception("第"+(i+1)+"行第4列《难度》为空或者格式不正确！");
					}
					// 获得获得第i行第2列的 正确答案
					cell = row.getCell(2);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					char[] c = null ;
					String correct = value;
					if (value != null && !"".equals(value)) {
						if("0".equals(questionType)&&value.length()>1) {
							throw new Exception("第"+(i+1)+"行第3列《正确答案》单选类型能有一个正确答案");
						}else if("1".equals(questionType)&&value.length()<2) {
							throw new Exception("第"+(i+1)+"行第3列《正确答案》多选类型能最少含有两个正确答案");
						}
						c = value.toCharArray();
						ResultMap.put("correctFlag", value);
					} else {
						throw new Exception("第"+(i+1)+"行第3列《正确答案》为空或者格式不正确！");
					}
					// 获得获得第i行第4列的 答案解析
					cell = row.getCell(4);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第5列《 答案解析》长度不能超过1000！");
						}
						ResultMap.put("answerThought", value);
					} else {
						ResultMap.put("answerThought", "");
					}
					// 获得获得第i行第5列的 选项A
					cell = row.getCell(5);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第6列《 选项A》长度不能超过1000！");
						}
						co +="A";
						optionList.add(value);
					} else {
						throw new Exception("第"+(i+1)+"行第6列《选项》为空或者格式不正确！");
					}
					// 获得获得第i行第6列的 选项B
					cell = row.getCell(6);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第7列《 选项》长度不能超过1000！");
						}
						co +="B";
						optionList.add(value);
					} else {
						throw new Exception("第"+(i+1)+"行第7列《选项B》为空或者格式不正确！");
					}
					// 获得获得第i行第7列的 选项C
					cell = row.getCell(7);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第8列《 选项》长度不能超过1000！");
						}
						co +="C";
						optionList.add(value);
					}else {
						for (char v : c) {
							if (!co.contains(v + "")) {
								throw new Exception("第"+(i+1)+"行第3列《正确答案》格式不符合要求！");
							}
						}
						if(optionList.size()<correct.length()) {
							throw new Exception("第"+(i+1)+"行第3列《正确答案》与选项数据量不匹配");
						}
						ResultMap.put("option", optionList);
						resultList.add(ResultMap);
						continue;
					}
					// 获得获得第i行第8列的 选项D
					cell = row.getCell(8);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第9列《 选项》长度不能超过1000！");
						}
						co +="D";
						optionList.add(value);
					}else {
						for (char v : c) {
							if (!co.contains(v + "")) {
								throw new Exception("第"+(i+1)+"行第3列《正确答案》格式不符合要求！");
							}
						}
						if(optionList.size()<correct.length()) {
							throw new Exception("第"+(i+1)+"行第3列《正确答案》与选项数据量不匹配");
						}
						ResultMap.put("option", optionList);
						resultList.add(ResultMap);
						continue;
					}
					// 获得获得第i行第9列的 选项E
					cell = row.getCell(9);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第10列《 选项》长度不能超过1000！");
						}
						co +="E";
						optionList.add(value);
					}else {
						for (char v : c) {
							if (!co.contains(v + "")) {
								throw new Exception("第"+(i+1)+"行第3列《正确答案》格式不符合要求！");
							}
						}
						if(optionList.size()<correct.length()) {
							throw new Exception("第"+(i+1)+"行第3列《正确答案》与选项数据量不匹配");
						}
						ResultMap.put("option", optionList);
						resultList.add(ResultMap);
						continue;
					}
					// 获得获得第i行第11列的 选项F
					cell = row.getCell(10);
					value = value = cell != null ? cell.getStringCellValue() + "" : "";
					value=value.trim();
					if (value != null && !"".equals(value)) {
						if(value.length()>1000) {
							throw new Exception("第"+(i+1)+"行第11列《 选项》长度不能超过1000！");
						}
						co +="F";
						optionList.add(value);
					}else {
						for (char v : c) {
							if (!co.contains(v + "")) {
								throw new Exception("第"+(i+1)+"行第3列《正确答案》格式不符合要求！");
							}
						}
						if(optionList.size()<correct.length()) {
							throw new Exception("第"+(i+1)+"行第3列《正确答案》与选项数据量不匹配");
						}
						ResultMap.put("option", optionList);
						resultList.add(ResultMap);
						continue;
					}
					
					ResultMap.put("option", optionList);
					
				}
				resultList.add(ResultMap);
			}

			for (Map<String, Object> maps : resultList) {
				String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
				maps.put("uuid", uuid);
				CustomFormModel cModel = new CustomFormModel();
				cModel.setData(maps);
				// 删除
				cModel.setSqlId("khAdmin/question/InsertQuestion");
				formMapper.saveCustom(cModel);
				String questionType = maps.get("questionType").toString();
				String correctFlag = maps.get("correctFlag").toString();
				qp.getData().put("uuid", uuid);
				qp.setQueryParam(questionSql);
				qp.setDataByMap(maps);
				List<Map<String, Object>> questions = formMapper.query(qp);

				if (questions.size() > 0) {
					int a = 65;
					String select = "";
					String questionId = questions.get(0).get("questionId").toString();
					List<String> optionList = (List<String>) maps.get("option");
					for (int i = 0; i < optionList.size(); i++) {
						Map<String, Object> optionMap = new HashMap<String, Object>();
						optionMap.put("questionId", questionId);
						String option = optionList.get(i);
						if (!"2".equals(questionType)) {
							char s = (char) (a + i);
							optionMap.put("title", s);
							optionMap.put("acontent", option);
							// (#{data.questionId},
							// #{data.title},#{data.acontent},#{data.correctFlag},now(),#{data.createBy},#{data.createBy})

							if (correctFlag.contains(s + "")) {
								optionMap.put("correctFlag", 0);
							} else {
								optionMap.put("correctFlag", 1);
							}
						} else {
							optionMap.put("title", option);
							optionMap.put("acontent", option);
							if ("对".equals(correctFlag)) {
								if ("正确".equals(option)) {
									optionMap.put("correctFlag", 0);
								} else {
									optionMap.put("correctFlag", 1);
								}

							} else {
								if ("错误".equals(option)) {
									optionMap.put("correctFlag", 0);
								} else {
									optionMap.put("correctFlag", 1);
								}
							}
						}

						optionMap.put("createBy", SessionUtils.getUserInfo().getUserId());
						cModel.setData(optionMap);
						cModel.setSqlId("khAdmin/question/InsertQuestionAnser");
						formMapper.saveCustom(cModel);

					}

				}

			}

		  }
		}catch (Exception e) {
			responseObj.setMsg("导入失败：请检查导入文件"+e.getMessage());
		}
	}

}
