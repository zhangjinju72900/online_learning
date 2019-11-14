package com.tedu.oss.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.oss.service.OssRecordService;

@Service("ossRecordServiceImpl")
public class OssRecordServiceImpl implements OssRecordService{

	@Autowired
	private FormMapper formMapper;
	
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public Object insertOssRecord(String fileName, String fileType, String fileId, String key, String bUCKET_NAME1) {
		try {
			
			TableModel model = new TableModel() {
	
				@Override
				public String getTableName() {
					return "t_oss_record";
				}
	
				@Override
				public String[] getFields() {
					String[] str = { "name", "file_type", "file_id", "oss_key", "bucket_name", "create_time",
							"create_by", "update_time", "update_by" };
					return str;
				}
			};
			Map<String, Object> map = new HashMap<String, Object>();
	
			map.put("name", fileName);
			map.put("fileType", fileType);
			map.put("fileId", fileId);
			map.put("ossKey", key);
			map.put("bucketName", bUCKET_NAME1);
			map.put("resourcesType", 0);
			map.put("createTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
			map.put("createBy", SessionUtils.getUserInfo()==null?0:SessionUtils.getUserInfo().getUserId());
			map.put("updateTime", DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, new Date()));
			map.put("updateBy", SessionUtils.getUserInfo()==null?0:SessionUtils.getUserInfo().getUserId());
			SimpleFormModel simpleModel = new SimpleFormModel(model, map);
			int s = formMapper.insert(simpleModel);
			if (s != 1) {
				log.error("新增OSS上传记录失败，t_oss_record，file_id:" + fileId + ";ossKey为" + key);
				return 0;
			}
			return simpleModel.getPrimaryFieldValue();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("新增OSS上传记录失败", e);
		}
		return 0;
	}

}
