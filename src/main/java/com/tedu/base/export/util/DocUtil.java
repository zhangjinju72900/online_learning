package com.tedu.base.export.util;

/**     
 * @文件名称: DocUtil.java   
 * @描述: TODO  
 * @作者：  wuwh
 * @时间：2018年4月10日 上午10:36:08  
 * @版本：V1.0     
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.tedu.base.common.utils.DateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @类功能说明：
 * 1.该代码在web项目中调用使用，需要在项目中的webroot目录下新建一个template文件夹，然后将预定义word的模板转成word.
 * xml文件放入template文件下即可 2.将要输出的数据放到map集合中，作为参数传入即可 依赖jar包: @作者： bj
 * 
 * @创建时间：2018年4月10日 上午10:36:08 @版本：V1.0
 */
public class DocUtil {
	/**
	 * rootPath controller中 可以获取 @Value("${file.upload.path}")private String
	 * rootPath;
	 * 
	 * zipName 下载是的zip名称 不需要加.zip
	 * 
	 * List<Map<String,List<Map<String, Object>>>> lists中Map<String, Object>
	 * 存放的是模板中直接用到的数据然后将该map放到list里，
	 * 之后再将该list放到一个Map中，此Map的键是对应的模板名称如：multiple.ftl，最后将该map添加到list里
	 * 
	 * @throws Exception
	 * 
	 * 
	 * 
	 */

	public static String  createDoc(List<Map<String, Object>> dataList, String templateName, Configuration configuration,
			String outPath,String rootPath) throws Exception {

		Template t = null;
		OutputStreamWriter outputStreamWriter = null;
		FileOutputStream fileOutputStream = null;
		File outFile = null;
		Writer out = null;
		String filePath ="";
		String date = DateUtils.getDateToStr("YYYYMMdd", new Date());
		String separator = File.separator;
		String dirPath =rootPath + separator + "private" + separator + "export" + separator + date + separator + "zip" + separator + UUID.randomUUID().toString().replace("-", "").substring(0, 32) + separator ;
		try {
			// word.xml是要生成Word文件的模板文件
			t = configuration.getTemplate(templateName, "utf-8"); // 文件名
																	// 还有这里要设置编码
			File file = new File(outPath);

			if (!file.exists()) {
				file.mkdirs();
			}
			if(dataList.size()>1) {
				String zipName = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
				for (int i = 0; i < dataList.size(); i++) {
					Map<String, Object> dataMap = dataList.get(i);
					String outName = dataMap.get("name").toString();
					outFile = new File(outPath + outName + ".doc");

					fileOutputStream = new FileOutputStream(outFile);
					outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
					out = new BufferedWriter(outputStreamWriter); // 还有这里要设置编码

					t.process(dataMap, out);

					out.flush();
					out.close();
					outputStreamWriter.close();
					fileOutputStream.close();
				}
				if(!new File(dirPath).exists()) {
					new File(dirPath).mkdirs();
				}
				String zipPath = dirPath + zipName+".zip";
				File zipFile = new File(zipPath);
				OutputStream oStream = new FileOutputStream(zipFile);
				toZip(outPath, oStream, true);
				filePath = zipPath;
				oStream.close();
				
			}else if(dataList.size()==1){
				Map<String, Object> dataMap = dataList.get(0);
				String outName = dataMap.get("name").toString();
				outFile = new File(outPath + outName + ".doc");
				filePath = outPath + outName + ".doc";
				fileOutputStream = new FileOutputStream(outFile);
				outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
				out = new BufferedWriter(outputStreamWriter); // 还有这里要设置编码

				t.process(dataMap, out);

				out.flush();
				out.close();
				outputStreamWriter.close();
				fileOutputStream.close();
			}
			

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (outputStreamWriter != null) {
					outputStreamWriter.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				if(dataList.size()>1) {
					deleteDir(new File(outPath));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 
	 * 压缩成ZIP 方法1
	 * 
	 * @param srcDir           压缩文件夹路径
	 * 
	 * @param out              压缩文件输出流
	 * 
	 * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 * 
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * 
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 * 
	 */

	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)

			throws RuntimeException {

		long start = System.currentTimeMillis();

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);

			File sourceFile = new File(srcDir);

			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);

			long end = System.currentTimeMillis();

			System.out.println("压缩完成，耗时：" + (end - start) + " ms");

		} catch (Exception e) {

			throw new RuntimeException("zip error from ZipUtils", e);

		} finally {

			if (zos != null) {

				try {

					zos.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

		}

	}

	/**
	 * 
	 * 压缩成ZIP 方法2
	 * 
	 * @param srcFiles 需要压缩的文件列表
	 * 
	 * @param out      压缩文件输出流
	 * 
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 * 
	 */

	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {

		long start = System.currentTimeMillis();

		ZipOutputStream zos = null;

		try {

			zos = new ZipOutputStream(out);

			for (File srcFile : srcFiles) {

				byte[] buf = new byte[BUFFER_SIZE];

				zos.putNextEntry(new ZipEntry(srcFile.getName()));

				int len;

				FileInputStream in = new FileInputStream(srcFile);

				while ((len = in.read(buf)) != -1) {

					zos.write(buf, 0, len);

				}

				zos.closeEntry();

				in.close();

			}

			long end = System.currentTimeMillis();

			System.out.println("压缩完成，耗时：" + (end - start) + " ms");

		} catch (Exception e) {

			throw new RuntimeException("zip error from ZipUtils", e);

		} finally {

			if (zos != null) {

				try {

					zos.close();

				} catch (IOException e) {

					e.printStackTrace();

				}
			}

		}

	}

	/**
	 * 
	 * 递归压缩方法
	 * 
	 * @param sourceFile       源文件
	 * 
	 * @param zos              zip输出流
	 * 
	 * @param name             压缩后的名称
	 * 
	 * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 * 
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * 
	 * @throws Exception
	 */

	private static void compress(File sourceFile, ZipOutputStream zos, String name,

			boolean KeepDirStructure) throws Exception {

		byte[] buf = new byte[BUFFER_SIZE];

		if (sourceFile.isFile()) {

			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字

			zos.putNextEntry(new ZipEntry(name));

			// copy文件到zip输出流中

			int len;

			FileInputStream in = new FileInputStream(sourceFile);

			while ((len = in.read(buf)) != -1) {

				zos.write(buf, 0, len);

			}

			// Complete the entry

			zos.closeEntry();

			in.close();

		} else {

			File[] listFiles = sourceFile.listFiles();

			if (listFiles == null || listFiles.length == 0) {

				// 需要保留原来的文件结构时,需要对空文件夹进行处理

				if (KeepDirStructure) {

					// 空文件夹的处理

					zos.putNextEntry(new ZipEntry(name + "/"));

					// 没有文件，不需要文件的copy

					zos.closeEntry();

				}

			} else {

				for (File file : listFiles) {

					// 判断是否需要保留原来的文件结构

					if (KeepDirStructure) {

						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,

						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了

						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);

					} else {

						compress(file, zos, file.getName(), KeepDirStructure);

					}

				}

			}

		}

	}
}
