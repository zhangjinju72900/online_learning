package com.tedu.plugin.teacher.maintain.manual;

import com.aliyun.oss.OSS;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.common.constant.FileTypeEnum;
import com.tedu.component.ScormOrPPTH5UrlComponent;
import com.tedu.oss.service.OssQueryService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service("insertManualSearchRecord")
public class InsertManualSearchRecord implements ILogicPlugin {
    @Autowired(required=false)
    FormLogService formLogService;
    @Autowired(required=false)
    private FormMapper formMapper;
    
    @Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;
    
    @Resource
	private OSS ossPriClient;
    
    @Resource
	private OssQueryService ossQueryServiceImpl;
    
    @Resource
	private ScormOrPPTH5UrlComponent scormOrPPTH5UrlComponent;

    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        return null;
    }

    @Override
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {

    	Map<String, Object> map = (Map<String, Object>) requestObj.getData();
    	
    	List<Map<String, Object>> list = null;
    	
    	
    	if(map != null && map.get("searchRecordId") != null && StringUtils.isNotBlank(map.get("searchRecordId").toString())){
    		
    		List<Map<String, Object>> searchRecord = queryBySqlId("khTeacher/maintainManual/QrySearchRecordById", map);
    		if(searchRecord != null && searchRecord.size() > 0){
    			map.put("id", searchRecord.get(0).get("maintainManualId").toString());
    			map.put("text", searchRecord.get(0).get("text").toString());
    		}
    		
    	}
    	
    	if(map != null && map.get("id") != null && StringUtils.isNotBlank(map.get("id").toString()) && !"0".equals(map.get("id").toString())){
    		
    		list = queryBySqlId("khTeacher/maintainManual/QryMaintainManual", map);
    	}else {
    		
    		list = queryBySqlId("khTeacher/maintainManual/QryMaintainManualByText", map);
    	}
    	
        
		if(list != null &&  list.size() > 0){
			list.stream().forEach(m ->{
				String url = "";
				if(FileTypeEnum.OTHERS.getCode().equals(m.get("resourcesType").toString()) && StringUtils.isNotBlank(m.get("ossKey").toString())){
					url = ossQueryServiceImpl.queryObjectByKey(m.get("ossKey").toString(), ossPriClient, BUCKET_NAME1);
				}else if(FileTypeEnum.PPT.getCode().equals(m.get("resourcesType").toString())){
					
					url = scormOrPPTH5UrlComponent.getPPTUrl(m.get("filePath").toString());
				}
				m.put("ossUrl", url.substring(7,19));
				m.remove("filePath");
			});
		}
		
		DataGrid dataList = new DataGrid(list);
		dataList.setTotal(list.size());
		
		responseObj.setData(dataList);
    	
    	
        if((map.get("text") != null && StringUtils.isNotBlank(map.get("text").toString())) || (map.get("id") != null && StringUtils.isNotBlank(map.get("id").toString()))){
        	if(map.get("id") == null || StringUtils.isBlank(map.get("id").toString())){
        		map.put("id", 0);
        	}
        	if(map.get("text") == null || StringUtils.isBlank(map.get("text").toString())){
        		map.put("text", "");
        	}
        	CustomFormModel cModel = new CustomFormModel();
        	cModel.setData(map);
        	cModel.setSqlId("khTeacher/maintainManual/InsureSearchRecord");
        	formMapper.saveCustom(cModel);
        }
        
        
    }
    
    
    
    private List<Map<String, Object>> queryBySqlId(String string, Map<String, Object> map) {
		
		QueryPage sqlQuery = new QueryPage();
		if(map.get("page")!=null){
			sqlQuery.setPage(Integer.parseInt(map.get("page").toString()));
			sqlQuery.setRows(Integer.parseInt(map.get("rows").toString()));
		}
		sqlQuery.setQueryParam(string);
		sqlQuery.setDataByMap(map);
		return formMapper.query(sqlQuery);
	}
}
