package com.tedu.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tedu.common.util.AuthCode;

/**
 * Scorm/PPTH5 获取打开url组件
 * @author qun
 *
 */

@Component
public class ScormOrPPTH5UrlComponent {
	
	@Value("${Rdurl}")
	private String Rdurl;
	
	@Value("${base.website}")
	private String website;

	public String getScormUrl(String filePath, String courseId, String sectionId, String labelId, String userId,
			String customerResourcesId) {
		
		return Rdurl+"run.do?token=" + AuthCode.encodeEncrypt(filePath.substring(0, filePath.lastIndexOf("."))) + "&url=" + AuthCode.encodeEncrypt(website) + "&courseId=" + AuthCode.encodeEncrypt(courseId) + 
				"&sectionId=" + AuthCode.encodeEncrypt(sectionId) + "&labelId=" + AuthCode.encodeEncrypt(labelId) + "&userId=" + AuthCode.encodeEncrypt(userId) + "&resourceId=" + AuthCode.encodeEncrypt(customerResourcesId);
	}

	public String getPPTUrl(String filePath) {
		
		return Rdurl+"pptRun.do?token=" + AuthCode.encodeEncrypt(filePath.substring(0, filePath.lastIndexOf("."))) + "&url=" + AuthCode.encodeEncrypt(website);
	}

	
}
