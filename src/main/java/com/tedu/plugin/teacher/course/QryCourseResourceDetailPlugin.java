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
 * @Description:TODO 课程-标签-资源详情
 * @author: qun
 * @date:   2019年1月15日 下午2:45:31   
 *
 */
@Service("qryCourseResourceDetailPlugin")
public class QryCourseResourceDetailPlugin implements ILogicPlugin {
	@Resource
	FormLogService formLogService;
	@Resource
	private FormMapper formMapper;
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Value("${oss.access_id}")
	private String ossAccess_id;
	@Value("${oss.access_key}")
	private String ossAccess_key;
	@Value("${oss.bucket_name1}")
	private String ossBucket_name;
	@Value("${oss.oss_endpoint}")
	private String oss_endpoint;
	
	@Resource
	private OssQueryService ossQueryServiceImpl;
	
	@Resource
	private ScormOrPPTH5UrlComponent scormOrPPTH5UrlComponent;

	@Override
	public Object doBefore(FormEngineRequest formEngineRequest, FormModel formModel) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfter(FormEngineRequest formEngineRequest, FormModel formModel, Object o, FormEngineResponse formEngineResponse) {
		
		DataGrid dataList= (DataGrid) formEngineResponse.getData();
		
		Map<String, Object> map = formEngineRequest.getData();
		try {
		if(dataList != null && dataList.getRows() != null){
			List<Map<String, Object>> rows = (List<Map<String, Object>>) dataList.getRows();
			rows.stream().forEach(m ->{
				if(m.get("children") != null){
					List<Map<String, Object>> children = (List<Map<String, Object>>)m.get("children");
					if(children != null){
						children.stream().forEach(c ->{
							OSSClient ossPriClient = new OSSClient(oss_endpoint, ossAccess_id, ossAccess_key);
							String resourcesType = c.get("resourcesType") == null ? "0" : c.get("resourcesType").toString();
							String type = c.get("type") == null ? "0" : c.get("type").toString();
							String url = "";
							if("assist".equals(type)) {
								url =  c.get("bucketName") == null ? "0" : c.get("bucketName").toString();
							}else {
								
								if(resourcesType.equals(FileTypeEnum.OTHERS.getCode()) 
										
										&& c.get("ossKey") != null && StringUtils.isNotBlank(c.get("ossKey").toString()) 
										&& c.get("bucketName") != null && StringUtils.isNotBlank(c.get("bucketName").toString())){
									
									url = ossQueryServiceImpl.queryObjectByKey(c.get("ossKey").toString(), ossPriClient, c.get("bucketName").toString());
								}else if(resourcesType.equals(FileTypeEnum.SCORM.getCode())){
									
									String labelId = c.get("pid")==null?"":c.get("pid").toString();
									labelId = labelId.replace("cou-label-", "");
									
									url = scormOrPPTH5UrlComponent.getScormUrl(c.get("filePath").toString(),  map.get("courseId")==null?"0":map.get("courseId").toString(), "0", labelId, 
											SessionUtils.getUserInfo().getUserId()+"", c.get("id")==null?"0":c.get("id").toString());
								
								}else if(resourcesType.equals(FileTypeEnum.PPT.getCode())){
								
									url = scormOrPPTH5UrlComponent.getPPTUrl(c.get("filePath").toString());
								}
								
							}
							c.put("ossUrl", url.substring(7,19));
							c.remove("filePath");
							ossPriClient.shutdown();
						});
					}
				}	
			});
		}}catch (Exception e) {
			// TODO: handle exception
		}finally {
			
		}
		formEngineResponse.setData(dataList);
	}
	
	

}
