package com.tedu.common.constant;

public enum CustomerResourcesSourceEnum {
	TFILEINDEX(0, "t_file_index表"),
	T_FTP_UPLOAD_RECORD(1, "t_ftp_upload_record表");
	
	private int code;
	private String desc;
	
	CustomerResourcesSourceEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
