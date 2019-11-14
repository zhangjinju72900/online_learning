package com.tedu.plugin.resource.vo;

import java.io.Serializable;

public class ResourcesFile implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private boolean isDirectory;
	private String name;
	private String path;//包内当前文件的上级路径
	private String fullpath;//包内当前文件全路径
	private String publicFullPath;//当前文件在服务器完整路径
	private String type;
	public ResourcesFile() {
		super();
	}
	
	public ResourcesFile(String name, String path, String type, String publicFullPath) {
		super();
		this.name = name;
		this.path = path;
		this.type = type;
		this.publicFullPath = publicFullPath;
	}
	
	public ResourcesFile(String name, String path, String type, String fullpath, boolean isDirectory, String publicFullPath) {
		super();
		this.name = name;
		this.path = path;
		this.type = type;
		this.fullpath = fullpath;
		this.isDirectory = isDirectory;
		this.publicFullPath = publicFullPath;
	}
	
	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getFullpath() {
		return fullpath;
	}

	public void setFullpath(String fullpath) {
		this.fullpath = fullpath;
	}

	public String getPublicFullPath() {
		return publicFullPath;
	}

	public void setPublicFullPath(String publicFullPath) {
		this.publicFullPath = publicFullPath;
	}
	
}
