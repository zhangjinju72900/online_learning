package com.tedu.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;

import com.tedu.plugin.resource.vo.ResourcesFile;

public class FileUtil {
	
	public static void fileExists(File file){
		//如果文件夹不存在则创建    
		if  (!file.exists()  && !file .isDirectory()){       
		    file .mkdirs();    
		} 
	}
	
	/** 
     * 解压缩zip包 
     * @param zipFilePath zip文件的全路径 
     * @param unzipFilePath 解压后的文件保存的路径 
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含 
     */ 
	public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName, String coding) throws Exception {
		if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
			throw new Exception("文件夹不能为空！");
		}
		List<ResourcesFile> reList = null;
		
		ZipFile zipFile = null;
		boolean firstIsDic = true;
		zipFile = new ZipFile(zipFilePath, Charset.forName(coding));
		Enumeration<?> entryEnum = zipFile.entries();
		if (null != entryEnum) {
			ZipEntry zipEntry = null;
			while (entryEnum.hasMoreElements()) {
				zipEntry = (ZipEntry) entryEnum.nextElement();
				if (zipEntry.getSize() > 0) {
					
					int lastSlash = zipEntry.getName().lastIndexOf("/")==-1?0:zipEntry.getName().lastIndexOf("/");
					int lastPoint = zipEntry.getName().lastIndexOf(".")==-1?zipEntry.getName().length()-1:zipEntry.getName().lastIndexOf(".");
					
					String key = zipEntry.getName().substring(0, lastSlash == 0 ? 0 : lastSlash + 1);
					
					OutputStream os = null;
			        InputStream is = null;
			        
			        if(firstIsDic){//压缩包第一层就是文件时，先创建目录
//			        	File file = new File(path + File.separator + zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(".")));
			        	File file = new File(unzipFilePath + File.separator + key);
						file.mkdirs();
			        }
					
					File targetFile = new File(unzipFilePath
                            + File.separator + zipEntry.getName());
					if(!targetFile.exists()){
						targetFile.createNewFile();
					}
                    os = new BufferedOutputStream(new FileOutputStream(targetFile));
                    is = zipFile.getInputStream(zipEntry);
                    byte[] buffer = new byte[4096];
                    int readLen = 0;
                    while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                        os.write(buffer, 0, readLen);
                        os.flush();
                    }
                    is.close();
                    os.close();
					
				}
				if (zipEntry.isDirectory()) {
					
					if(firstIsDic){
						firstIsDic = false;
					}

					String pathTemp = unzipFilePath + File.separator 
                            + zipEntry.getName();
					
					File file = new File(pathTemp);
					file.mkdirs();
					
					String key = zipEntry.getName().substring(0,
							zipEntry.getName().substring(0, zipEntry.getName().length() - 1).lastIndexOf("/") + 1);
					String key1 = zipEntry.getName().replace(key, "");

				}
			}
		}	
	}
	
	
	public static void main(String[] args) throws Exception {
		fileExists(new File("D:/qrcode/1/2/3"));
		
			FileUtil.unzip("D:\\files\\public\\20181031\\zip\\d8\\d8c2fb53996f4ece929bd07c8b2f3e59.zip", "D:\\files\\public\\20181031\\zip\\d8\\d8c2fb53996f4ece929bd07c8b2f3e59", true, "GBK");
	}

}
