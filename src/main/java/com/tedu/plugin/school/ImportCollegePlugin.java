package com.tedu.plugin.school;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

@Service("ImportCollegePlugin")
public class ImportCollegePlugin implements ILogicPlugin {
	@Resource
	private FormService formService;

	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {
		log.info(formModel.getData().toString());
		// FormEngineResponse response = new FormEngineResponse("");
		Map<String, Object> map = formModel.getData();
		String fileId = map.get("fileId") != null ? map.get("fileId").toString() : "";
		// String questionClassifyId = map.get("questionClassifyId") != null ?
		// map.get("questionClassifyId").toString(): "";
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
				// String co = "";
				for (int i = 2; i <= totalRowNum; i++) {
					Map<String, Object> ResultMap = new HashMap<String, Object>();
					// List<String> optionList = new ArrayList<String>();
					// 获得第i行对象
					Row row = sheet.getRow(i);
					// 获得获得第i行第0列的 院校名字
					Cell cell = row.getCell(0);
					String value = "";
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value = value.trim();

					String id = map.get("id") == null ? "" : map.get("id").toString();
					String title = value;
					// value = map.get("value") == null ? "" :
					// map.get("value").toString();
					Map<String, Object> requestMap = new HashMap<>();
					requestMap.put("title", StringUtils.isBlank(title) ? "" : title);
					if (StringUtils.isBlank(id) || "0".equals(id)) {
						qp.setQueryParam("khAdmin/schoolManage/school/QrySchoolByName");
						qp.setSqlId("khAdmin/schoolManage/school/QrySchoolByName");
					} else {
						requestMap.put("id", id);
						qp.setQueryParam("khAdmin/schoolManage/school/QrySchoolByNameAndId");
						qp.setSqlId("khAdmin/schoolManage/school/QrySchoolByNameAndId");
					}
					qp.setDataByMap(requestMap);
					List qlist = formService.queryBySqlId(qp);
					if (qlist != null && qlist.size() > 0) {
						throw new Exception("学校名称重复");
					}
					if (value != null && !"".equals(value)) {
						if (value.length() > 1000) {
							throw new Exception("第" + (i + 1) + "行第1列《院校名字》长度不能超过1000！");
						}
						ResultMap.put("collegeName", value);
					} else {
						throw new Exception("第" + (i + 1) + "行第1列《院校名字》为空或者格式不正确！");
					}

					// 获得获得第i行第1列的 院校类型
					cell = row.getCell(1);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value = value.trim();
					if (value != null && !"".equals(value)) {
						if ("中职".equals(value)) {
							value = "0";
						} else {
							value = "1";
						}
						ResultMap.put("collegeType", value);
					} else {
						throw new Exception("第" + (i + 1) + "行第2列《院校类型》为空或者格式不正确！");
					}

					// 获得第i行第2列的选择大区
					cell = row.getCell(2);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value = value.trim();
					if (value != null && !"".equals(value)) {
						qp.setParamsByMap(map);
						qp.getData().put("value", value);
						// 从数据库中获取region的id属性
						qp.setQueryParam("khAdmin/schoolManage/school/QryIdByRegionName");
						List<Map<String, Object>> regionIdList = formService.queryBySqlId(qp);
						// 从数据库中获取region的name属性
						qp.setQueryParam("khAdmin/schoolManage/school/QryRegionName");
						List<Map<String, Object>> regionList = formService.queryBySqlId(qp);

						boolean flag = false;
						if (regionList.size() > 0) {
							// 将获取到的regionId用变量承接
							Map<String, Object> regionIdMap = regionIdList.get(0);
							String regionId = regionIdMap.get("id").toString();

							for (int i1 = 0; i1 < regionList.size(); i1++) {
								// 将获取到的regionName用变量承接
								Map<String, Object> regionMap = regionList.get(i1);
								String regionName = regionMap.get("name").toString();
								if (regionName.equals(value)) {
									value = regionId;
									flag = true;
									break;
								}
							}
							if (flag == true) {
								ResultMap.put("regionType", value);
							} else {
								throw new Exception("您没有此权限!");
							}
						}

					} else {
						throw new Exception("第" + (i + 1) + "行第3列《选择大区》为空或者格式不正确！");
					}

					// 获得第i行第3列的选择省份
					cell = row.getCell(3);
					value = cell != null ? cell.getStringCellValue() + "" : "";
					value = value.trim();
					if (value != null && !"".equals(value)) {
						qp.setParamsByMap(map);
						qp.getData().put("value", value);
						// 从数据库中获取city的code属性
						qp.setQueryParam("khAdmin/schoolManage/school/QryCodeByCityName");
						List<Map<String, Object>> cityCodeList = formService.queryBySqlId(qp);
						// 从数据库中获取city的name属性
						qp.setQueryParam("khAdmin/schoolManage/school/QryCityName");
						List<Map<String, Object>> cityNameList = formService.queryBySqlId(qp);
						if (cityNameList.size() > 0) {
							// 将获取到的cityCode用变量承接
							Map<String, Object> cityCodeMap = cityCodeList.get(0);
							String cityCode = cityCodeMap.get("code").toString();

							for (int i2 = 0; i2 < cityNameList.size(); i2++) {
								// 将获取到的cityName用变量承接
								Map<String, Object> cityNameMap = cityNameList.get(i2);
								String cityName = cityNameMap.get("name").toString();
								if (cityName.equals(value)) {
									value = cityCode;
									break;
								}
							}
						}
						ResultMap.put("provinceType", value);
					} else {
						throw new Exception("第" + (i + 1) + "行第4列《选择省份》为空或者格式不正确！");
					}
					resultList.add(ResultMap);
				}

				for (Map<String, Object> maps : resultList) {
					String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
					maps.put("uuid", uuid);
					CustomFormModel cModel = new CustomFormModel();
					cModel.setData(maps);
					// 删除
					cModel.setSqlId("khAdmin/schoolManage/school/InsertSchool");
					formMapper.saveCustom(cModel);
				}

			}
		} catch (Exception e) {
			responseObj.setMsg("导入失败：" + e.getMessage());
		}
	}
}
