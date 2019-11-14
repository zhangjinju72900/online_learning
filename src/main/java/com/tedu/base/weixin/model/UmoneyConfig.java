package com.tedu.base.weixin.model;

import com.tedu.base.common.utils.PropertiesUtil;

public class UmoneyConfig {

	private String apiUrl;
	
    private String corpid;
	
    private String signkey;
    
    private String publickey;
    
    private String apiSoi;
    
    private String apiGos;
    
    private String apiGoi;
    
    public UmoneyConfig() {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        propertiesUtil.loadProperties("engine/conf","base");
        
        apiUrl=propertiesUtil.getProperties("umoney.api.url");
        corpid=propertiesUtil.getProperties("umoney.corpid");
        signkey=propertiesUtil.getProperties("umoney.signkey");
        publickey=propertiesUtil.getProperties("umoney.publickey");
        apiSoi=propertiesUtil.getProperties("umoney.api.soi");
        apiGos=propertiesUtil.getProperties("umoney.api.gos");
        apiGoi=propertiesUtil.getProperties("umoney.api.goi");
    }

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getSignkey() {
		return signkey;
	}

	public void setSignkey(String signkey) {
		this.signkey = signkey;
	}

	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	public String getApiSoi() {
		return apiSoi;
	}

	public void setApiSoi(String apiSoi) {
		this.apiSoi = apiSoi;
	}

	public String getApiGos() {
		return apiGos;
	}

	public void setApiGos(String apiGos) {
		this.apiGos = apiGos;
	}

	public String getApiGoi() {
		return apiGoi;
	}

	public void setApiGoi(String apiGoi) {
		this.apiGoi = apiGoi;
	}
    
    
}
