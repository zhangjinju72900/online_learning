package com.tedu.inf.app.vo;

import java.io.Serializable;
import java.util.List;

public class UpdateFileVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 是否需要更新 0-是，1-否
	 */
	private Integer updateFlag = 1; 
	
	private Long versionId = 1l; 
	
	private List<String> urlList;

	public Integer getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(Integer updateFlag) {
		this.updateFlag = updateFlag;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public UpdateFileVo() {
	}

	public UpdateFileVo(Integer updateFlag, List<String> urlList) {
		this.updateFlag = updateFlag;
		this.urlList = urlList;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
	
	
	
}
