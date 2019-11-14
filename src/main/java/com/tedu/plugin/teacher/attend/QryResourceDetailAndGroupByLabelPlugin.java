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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @ClassName:  QryResourceDetailAndGroupByLabelPlugin   
 * @Description:TODO上课页面 资源列表   
 * @author: qun
 * @date:   2019年1月15日 下午2:57:41   
 *
 */
@Service("qryResourceDetailAndGroupByLabelPlugin")
public class QryResourceDetailAndGroupByLabelPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private OSS ossPriClient;
	
	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
	
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
			
			try {
				
				for (Map<String, Object> m : rows) {
					
					String[] resourcesIds = m.get("resourcesIds").toString().split(",");
					
					String[] sectionIds = m.get("sectionId").toString().split(",");
					
					String[] labelIds = m.get("labelId").toString().split(",");
					
					String[] courseResNames = m.get("courseResNames").toString().split(",");
					
					String[] resourcesTypes = m.get("resourcesType").toString().split(",");
					
					String[] fileTypes = m.get("fileType").toString().split(",");
					
					String[] filePaths = m.get("filePath").toString().split(",");
					
					String[] urls = new String[resourcesTypes.length];
					
					String[] ossKeys = m.get("ossKeys").toString().split(",");
					
					for(int i=0; i<resourcesTypes.length; i++){
						String url = "";
						if(resourcesTypes[i].equals(FileTypeEnum.SCORM.getCode())){
							
							url = scormOrPPTH5UrlComponent.getScormUrl(filePaths[i],  map.get("courseId")==null?"":map.get("courseId").toString(), sectionIds[i], labelIds[i], 
									SessionUtils.getUserInfo().getUserId()+"", resourcesIds[i]);
						}else if(resourcesTypes[i].equals(FileTypeEnum.PPT.getCode())){
							
							url = scormOrPPTH5UrlComponent.getPPTUrl(filePaths[i]);
						}else{
							
							url = ossQueryServiceImpl.queryObjectByKey(ossKeys[i], ossPriClient, BUCKET_NAME1);
						}
						urls[i] = url;
					}
					
					StringBuilder sb = new StringBuilder();
					
					Arrays.asList(m.get("ossKeys").toString().split(",")).stream().forEach(l ->{
						if(StringUtils.isNotBlank(l)){
							String url = ossQueryServiceImpl.queryObjectByKey(l, ossPriClient, BUCKET_NAME1);
							sb.append(url).append(",");
						}
					});
					
					m.put("ossKeys", m.get("ossKeys").toString().split(","));
					
//					String[] urls = sb.toString().split(",");
					
					m.put("resourcesIds", resourcesIds);
					
					m.put("courseResNames", courseResNames);
					
					m.put("urls", urls);
					
					m.put("fileTypes", fileTypes);
					
					m.put("resourcesTypes", resourcesTypes);
					
					m.remove("filePath");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
		}
	}
	
	

}
