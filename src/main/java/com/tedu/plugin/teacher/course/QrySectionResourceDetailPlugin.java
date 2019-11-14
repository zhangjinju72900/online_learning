package com.tedu.plugin.teacher.course;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.component.ScormOrPPTH5UrlComponent;
import com.tedu.oss.service.OssQueryService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;


/**
 * @ClassName:  QrySectionResourceDetailPlugin   
 * @Description:TODO 小节-标签-资源详情
 * @author: qun
 * @date:   2019年1月15日 下午2:45:31   
 *
 */
@Service("qrySectionResourceDetailPlugin")
public class QrySectionResourceDetailPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	

	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private ScormOrPPTH5UrlComponent scormOrPPTH5UrlComponent;
	@Value("${oss.access_id}")
	private String ossAccess_id;
	@Value("${oss.access_key}")
	private String ossAccess_key;
	@Value("${oss.bucket_name2}")
	private String ossBucket_name;
	@Value("${oss.oss_endpoint2}")
	private String oss_endpoint;
	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		log.info(formModel.getData());
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {

		DataGrid dataList= (DataGrid) formEngineResponse.getData();
		
		Map<String, Object> map = formEngineRequest.getData();
		OSSClient ossClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
		if(dataList != null && dataList.getRows() != null){
			List<Map<String, Object>> rows = (List<Map<String, Object>>) dataList.getRows();
			rows.stream().forEach(m ->{
				if(m.get("children") != null){
					List<Map<String, Object>> children = (List<Map<String, Object>>)m.get("children");
					if(children != null){
						children.stream().forEach(c ->{
							String resourcesType = c.get("resourcesType") == null ? "0" : c.get("resourcesType").toString();
							String url = "";
							if(resourcesType.equals(FileTypeEnum.OTHERS.getCode()) 
									&& c.get("ossKey") != null && StringUtils.isNotBlank(c.get("ossKey").toString()) 
									&& c.get("bucketName") != null && StringUtils.isNotBlank(c.get("bucketName").toString())){
								
								url = ossQueryServiceImpl.queryObjectByKey(c.get("ossKey").toString(), ossClient, c.get("bucketName").toString());
							}else if(resourcesType.equals(FileTypeEnum.SCORM.getCode())){
								
								String labelId = map.get("pid")==null?"":map.get("pid").toString();
								labelId = labelId.replace("cou-label-", "");
								
								url = scormOrPPTH5UrlComponent.getScormUrl(c.get("filePath").toString(),  map.get("courseId")==null?"0":map.get("courseId").toString(), c.get("sectionId")==null?"0":c.get("sectionId").toString(), 
										c.get("labelId")==null?"0":c.get("labelId").toString(), 
										SessionUtils.getUserInfo().getUserId()+"", c.get("id")==null?"0":c.get("id").toString());
							
							}else if(resourcesType.equals(FileTypeEnum.PPT.getCode())){
							
								url = scormOrPPTH5UrlComponent.getPPTUrl(c.get("filePath").toString());
							}
							c.put("ossUrl", url.substring(0,url.indexOf(".")));	
							c.remove("filePath");
						});
					}
				}	
			});
		}
		formEngineResponse.setData(dataList);
	}
	
	

}
