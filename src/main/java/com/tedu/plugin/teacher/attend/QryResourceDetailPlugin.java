package com.tedu.plugin.teacher.attend;

import com.aliyun.oss.OSS;
import com.google.zxing.WriterException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.common.util.FileUtil;
import com.tedu.common.util.QrCodeCreateUtil;
import com.tedu.component.ScormOrPPTH5UrlComponent;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @ClassName:  QryResourceDetailPlugin   
 * @Description:TODO上课页面，查看标签下的资源下拉列表
 * @author: qun
 * @date:   2019年1月15日 下午2:56:07   
 *
 */
@Service("qryResourceDetailPlugin")
public class QryResourceDetailPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private OSS ossPriClient;
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private ScormOrPPTH5UrlComponent scormOrPPTH5UrlComponent;

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {

		Map<String, Object> map = formEngineRequest.getData();
		
		DataGrid dataList= (DataGrid) formEngineResponse.getData();

		if(dataList != null && dataList.getRows() != null){
			List<Map<String, Object>> rows = (List<Map<String, Object>>) dataList.getRows();
			rows.stream().forEach(m ->{
				if(FileTypeEnum.OTHERS.getCode().equals(m.get("resourcesType").toString()) && StringUtils.isNotBlank(m.get("ossKey").toString())){
					
					String url = ossQueryServiceImpl.queryObjectByKey(m.get("ossKey").toString(), ossPriClient, m.get("bucketName").toString());
					m.put("ossUrl", url.substring(7,19));
				}else if(FileTypeEnum.SCORM.getCode().equals(m.get("resourcesType").toString())){
					
					String url = scormOrPPTH5UrlComponent.getScormUrl(m.get("filePath").toString(), map.get("courseId")==null?"":map.get("courseId").toString(), 
							m.get("sectionId")==null?"":m.get("sectionId").toString(), map.get("id")==null?"":map.get("id").toString(), SessionUtils.getUserInfo().getUserId()+"", 
									m.get("customerResourcesId")==null?"":m.get("customerResourcesId").toString());
					m.put("ossUrl", url.substring(7,19));
				}else if(FileTypeEnum.PPT.getCode().equals(m.get("resourcesType").toString())){
					
					String url = scormOrPPTH5UrlComponent.getPPTUrl(m.get("filePath").toString());
					m.put("ossUrl", url.substring(7,19));
				}
				m.remove("filePath");
			});
		}
		formEngineResponse.setData(dataList);
	}
	
	

}
