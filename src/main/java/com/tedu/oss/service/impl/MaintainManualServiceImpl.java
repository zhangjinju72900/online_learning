package com.tedu.oss.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.plugin.resource.vo.ResourcesFile;

@Service("maintainManualServiceImpl")
public class MaintainManualServiceImpl implements MaintainManualService{

	@Autowired
	private FormMapper formMapper;
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public Long insertMaintainManual(ResourcesFile resourcesFile, String key, Long parentId, String fileId, String bucketName, int resourcesType, int source) {

		TableModel model = new TableModel() {

			@Override
			public String getTableName() {
				return "t_maintain_manual";
			}

			@Override
			public String[] getFields() {
				String[] str = { "name", "file_type", "file_path", "parent_id", "file_id", "file_name",
						"oss_key", "oss_url", "resources_type", "source", "data_flag", "valid_flag", "create_time",
						"create_by", "update_time", "update_by" };
				return str;
			}
		};
		Map<String, Object> map = new HashMap<String, Object>();

		String newFileName = resourcesFile.getName().replace("_ppt", "").replace("_ppt", "").replace(".zip", "");
		
		map.put("name", newFileName);
		map.put("fileType", resourcesFile.getType());
		map.put("filePath", resourcesFile.getPublicFullPath());

		map.put("parentId", parentId);
		map.put("fileId", fileId);
		map.put("fileName", newFileName);
		map.put("ossKey", key);
		map.put("ossUrl", bucketName);
		map.put("resourcesType", resourcesType);
		map.put("source", source);
		
		map.put("dataFlag", resourcesFile.isDirectory()?"0":1);
		
		map.put("validFlag", 0);
		map.put("createTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("createBy", SessionUtils.getUserInfo().getUserId());
		map.put("updateTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
		map.put("updateBy", SessionUtils.getUserInfo().getUserId());
		SimpleFormModel simpleModel = new SimpleFormModel(model, map);
		int s = formMapper.insert(simpleModel);
		if (s == 1) {
			return Long.parseLong(simpleModel.getPrimaryFieldValue().toString());
		} else {
			log.error("新增全路径为" + resourcesFile.getFullpath() + ";fileId为" + fileId + "的t_maintain_manual表失败！");
			return 0L;
		}
	}

	@Override
	public Map<String, Object> queryFileById(String mmId) {
		QueryPage sqlQuery = new QueryPage();
		sqlQuery.setQueryParam("khAdmin/maintainManual/QryManualFileDetail");
		sqlQuery.setQueryType("");
		sqlQuery.setSingle(1);
		sqlQuery.setSqlId("khAdmin/maintainManual/QryManualFileDetail");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", mmId);
		sqlQuery.setDataByMap(paramMap);
		List<Map<String, Object>> tables = formMapper.query(sqlQuery);// 获取所有表
		if (tables != null && tables.size() > 0) {
			return tables.get(0);
		}
		return null;
	}

}
