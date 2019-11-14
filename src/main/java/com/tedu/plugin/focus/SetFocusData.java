package com.tedu.plugin.focus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.oss.service.CustomResourcesService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssUploadService;
import com.tedu.plugin.resource.vo.ResourcesFile;

/**
 * @ClassName:  SetFocusData   
 * @Description:TODO 关注列表设置单向/双向关注  
 * @author: qun
 * @date:   2018年9月17日 上午11:37:29   
 *
 */
@Service("setFocusData")
public class SetFocusData implements ILogicPlugin {

	@Resource
	private OssQueryService ossQueryServiceImpl2;
	
	@Resource
	private FormMapper formMapper;

	public final Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
			FormEngineResponse responseObj) {

		DataGrid gd = (DataGrid) responseObj.getData();
		List<Map<String, Object>> l = (List<Map<String, Object>>) gd.getRows();
		
		if(l != null && l.size() > 0){
		
			QueryPage qp2 = new QueryPage();
			qp2.setQueryParam("khApp/mine/focus/QryFocusUser"); 
			List<Map<String,Object>> qlists1 = formMapper.query(qp2);
			
			Map<String, String> userMap = new HashMap<>();
			if(qlists1 != null && qlists1.size() > 0){
				for (Map<String, Object> map : qlists1) {
					userMap.put(map.get("customerUserId")==null?"":map.get("customerUserId").toString(), "");
				}
			}
			
			for (Map<String, Object> map : l) {
				if(userMap.containsKey(map.get("focusOnId").toString())){
					map.put("focusType", 1);
				}
				
				String fileId = map.get("fileId")==null?"":map.get("fileId").toString();
				String fileOssUrl = map.get("fileOssUrl")==null?"":map.get("fileOssUrl").toString();
				String fileCDNUrl = ossQueryServiceImpl2.queryCDNUrlByOssUrl(fileId, fileOssUrl);
				map.put("ufileCDNUrl", fileCDNUrl);
				map.remove("fileOssUrl");
			}
		}
	}
}
