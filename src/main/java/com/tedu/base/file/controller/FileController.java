
package com.tedu.base.file.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import static java.nio.file.StandardOpenOption.READ;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyuncs.DefaultAcsClient;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.excel.ExcelOutputData;
import com.tedu.base.common.excel.ExcelUtil;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.redis.RedisService;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.ImageUtil;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.service.DocumentService;
import com.tedu.base.file.service.FileService;
import com.tedu.base.file.util.HttpClientUtil;
import com.tedu.base.file.util.UploadFile;
import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.file.util.io.file;
import com.tedu.base.file.service.MarkdownAsPDFService;
import com.tedu.base.file.util.operation.LocalOperation;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.util.AliVideoPlayUtils;
import com.tedu.base.util.AliVideoService;
import com.tedu.base.util.RedisUtil;
import com.tedu.common.constant.HtmlResourceModuleEnum;
import com.tedu.common.constant.ResourceTypeEnum;
import com.tedu.common.util.AuthCode;
import com.tedu.oss.service.MaintainManualService;
import com.tedu.oss.service.OssQueryService;
import com.tedu.oss.service.OssRecordService;
import com.tedu.oss.service.OssUploadService;

import net.sf.json.JSONObject;

/**
 * 文件操作接口，包含上传和下载
 * 
 *
 */
@Controller
public class FileController {
	 @Autowired//(自动注入redisTemplet)
	    private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private FileService fileService;
	@Value("${file.upload.path}")
	private String DISH;
	@Resource
	private FormTokenService formTokenService;
	@Resource
	private DocumentService documentService;
	@Autowired(required = false)
	private RedisService redisService;
	@Resource
	private MarkdownAsPDFService markdownAsPDFService;
	@Resource
	FormService formService;

	@Resource
	private FormMapper formMapper;
	@Resource
	private OssQueryService ossQueryServiceImpl;

	@Resource
	private OssUploadService ossUploadServiceImpl;

	@Resource
	private OssRecordService ossRecordServiceImpl;

	@Resource
	private MaintainManualService maintainManualServiceImpl;

	@Value("${oss.bucket_name2}")
	private String BUCKET_NAME2;

	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT2;

	@Resource
	private OSS ossPubClient;

	@Resource
	private OSS ossPriClient;
	@Resource
	private AliVideoPlayUtils aliVideoPlayUtils;

	@Resource
	private com.tedu.business.file.service.FileService fileServiceImpl;

	// 私有
	@Value("${oss.bucket_name1}")
	private String BUCKET_NAME1;

	@Value("${oss.access_id}")
	private String ossAccess_id;
	@Value("${oss.access_key}")
	private String ossAccess_key;
	@Value("${oss.oss_endpoint}")
	private String OSS_ENDPOINT1;

	@Value("${video.aliyun.assessKeyId}")
	private String videoKeyId;

	@Value("${video.aliyun.accessKeySecret}")
	private String videoKeySecret;

	@Resource
	private AliVideoService aliVideoService;
	  @Resource
	    RedisUtil redisUtil;
	/**
	 * 全局变量
	 */
	private final String PRIVATE = "private";
	private final String PUBLIC = "public";
	public final Logger log = Logger.getLogger(this.getClass()); // 日志记录器
	ResponseJSON responseJSON = null; // 返回json数据
	String hintMessage = ""; // 提示消息

	private static final long serialVersionUID = 1L;

	private static final int BUFFER_LENGTH = 1024 * 16;
	private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
	private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");

	@RequestMapping("play")
	public String play(String url, Model model) {

		model.addAttribute("url", url);
		model.addAttribute("type", url.indexOf(".mp4") > -1 ? "mp4" : "pdf");
		return FormConstants.VIEW_PLAY;
	}

	/**
	 *
	 * @Description: 管理后台上传文件接口,只有图片直接上传oss，其他的点保存后再传oss
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * @throws IOException
	 */
	@RequestMapping("/localUpload")
	public void localUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String, Object> fileData = new HashMap<>();
		// 上传结果信息
		responseJSON = new ResponseJSON();
		// 创建文件上传对象
		UploadFile upfiles = new UploadFile(request, "") {
			// 生成文件新名字
			@Override
			public String offerUploadFileNames() {
				return fileUUID;
			}
		};
		OSSClient ossClient;
		String uploadUrl = upfiles.getParameter("uploadUrl");
		String accessType = upfiles.getParameter("accessType");
		fileData.put("accessType", accessType);
		if ("private".equals(accessType)) {
			ossClient = new OSSClient(OSS_ENDPOINT1, ossAccess_id, ossAccess_key);
		} else {
			ossClient = new OSSClient(OSS_ENDPOINT2, ossAccess_id, ossAccess_key);
		}
		String module = upfiles.getParameter("module");
		// 只能上传单文件判断

		map = fileService.localUpload(upfiles, fileUUID, accessType, uploadUrl, module);
		hintMessage = (String) map.get("hintMessage");
		if (StringUtils.isEmpty(hintMessage)) {
			// 获取上传后文件信息对象
			FileModel fileModel = (FileModel) map.get("fileModel");
			if (!StringUtils.isEmpty(request.getParameter("x")) && !StringUtils.isEmpty(request.getParameter("y"))
					&& !StringUtils.isEmpty(request.getParameter("h"))
					&& !StringUtils.isEmpty(request.getParameter("w"))) {
				int x = Double.valueOf(request.getParameter("x")).intValue();
				int y = Double.valueOf(request.getParameter("y")).intValue();
				int h = Double.valueOf(request.getParameter("h")).intValue();
				int w = Double.valueOf(request.getParameter("w")).intValue();
				ImageUtil.cutImage(fileModel.getPath(), fileModel.getFileUUID(), fileModel.getFileType(),
						new java.awt.Rectangle(x, y, w, h));
			}
			// 上传成功则返回id
			hintMessage = "上传成功";
			responseJSON.setStatus(fileModel.getId() == null ? 0 : fileModel.getId());
			fileData.put("fileId", fileModel.getId() == null ? 0 : fileModel.getId());
			fileData.put("massage", "上传成功");
			fileData.put("url", fileModel.getUrl());
			Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileModel.getId() + "");
			System.out.println("fileMap" + fileMap);
			String fullPath = fileMap.get("fullpath");
			String fileName = fileMap.get("fileName");
			String fileType = fileMap.get("fileType");
			if (("mp4".equalsIgnoreCase(fileType) || "flv".equalsIgnoreCase(fileType))
					&& "private".equals(accessType)) {
				Vector<Thread> vectors = new Vector<Thread>();
				// 启用5个线程
				Thread childrenThread = new Thread(new Runnable() {
					public void run() {
						try {
							String vid;
							vid = aliVideoPlayUtils.testUploadLocalFile(fileName, fileName + "." + fileType, fullPath,
									videoKeyId, videoKeySecret);
							// 设置转码
							// aliVideoPlayUtils.submitTranscodeJobs(vid);
							fileData.put("fileId", vid == null ? 0 : vid);
							fileData.put("status", vid == null ? 0 : vid);
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("子线程执行！");

					}
				});
				vectors.add(childrenThread);
				childrenThread.start();
				// 主线程
				for (Thread thread : vectors) {
					try {
						thread.join();
						fileServiceImpl.updateFileOssUrl(fileModel.getId(),
								"private".equals(accessType) ? "http://" + BUCKET_NAME1 : "http://know-how001",
								fileData.get("fileId").toString());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 使用join来保证childrenThread的5个线程都执行完后，才执行主线程
				}

			} else {

				String key = ossUploadServiceImpl.getOssKey(fileType);
				Vector<Thread> vectors = new Vector<Thread>();
				// 启用5个线程
				Thread childrenThread = new Thread(new Runnable() {
					public void run() {
						try {
							Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileModel.getId() + "");
							System.out.println("fileMap" + fileMap);
							String fullPath = fileMap.get("fullpath");
							String fileName = fileMap.get("fileName");
							String fileType = fileMap.get("fileType");
							fileData.get("accessType");
							if ("private".equals(fileData.get("accessType"))) {
								ossUploadServiceImpl.uploadOss(key, fullPath, ossPriClient, BUCKET_NAME1);
							} else {
								ossUploadServiceImpl.uploadOss(key, fullPath, ossClient, BUCKET_NAME2);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("子线程执行！");

					}
				});
				vectors.add(childrenThread);
				childrenThread.start();
				// 主线程
				for (Thread thread : vectors) {
					try {
						thread.join();
						String url = "";
						if ("private".equals(fileData.get("accessType"))) {
							ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key,
									BUCKET_NAME1);
							url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME1, OSS_ENDPOINT1);
						} else {
							ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key,
									BUCKET_NAME2);
							url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);

						}
						fileData.put("url", url);
						fileData.put("error", 0);
						fileData.put("fileId", fileModel.getId() == null ? 0 : fileModel.getId());

						fileServiceImpl.updateFileOssUrl(fileModel.getId(), url, key);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 使用join来保证childrenThread的5个线程都执行完后，才执行主线程
				}
			}
			FormLogger.logFlow("header.Accept =" + request.getHeader("Accept"), FormLogger.LOG_TYPE_INFO);
			responseJSON.setMsg(hintMessage);
			responseJSON.setData(fileData);

			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(FormUtil.toJsonString(responseJSON));
		}

		// request.getHeader("Accept");

	}

	/**
	 *
	 * @Description: markdown上传
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("/mdupload")
	@ResponseBody
	public Map<String, Object> mdupload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String accessType = "markdown";
		// 上传结果信息
		responseJSON = new ResponseJSON();
		// 创建文件上传对象
		UploadFile upfiles = new UploadFile(request, "") {
			// 生成文件新名字
			@Override
			public String offerUploadFileNames() {
				return fileUUID;
			}
		};

		// 只能上传单文件判断
		map = fileService.localUpload(upfiles, fileUUID, accessType, "", "");
		hintMessage = (String) map.get("hintMessage");
		if (StringUtils.isEmpty(hintMessage)) {
			String projectUr = request.getRequestURL().toString();
			// 获取上传后文件信息对象
			FileModel fileModel = (FileModel) map.get("fileModel");
			// 1表示上传成功
			map.put("success", 1);
			map.put("message", projectUr.replace("/mdupload", "") + "/localDownload?methodType=markdown&fileId="
					+ fileModel.getId());
			map.put("url", projectUr.replace("/mdupload", "") + "/localDownload?methodType=markdown&fileId="
					+ fileModel.getId());
		} else {
			// 上传失败
			map.put("success", 0);
		}

		return map;
	}

	/**
	 *
	 * @Description: 云端上传
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	public ResponseJSON OSSUpload(HttpServletRequest request) {
		// 上传结果信息
		String hintMessage = "";
		responseJSON = new ResponseJSON();

		responseJSON.setStatus(1);
		responseJSON.setMsg(hintMessage);
		return responseJSON;
	}

	/**
	 *
	 * @Description: 本地下载
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("/localDownload")
	public void localDownload(HttpServletRequest request, HttpServletResponse response) {
		// 获取文件id
		String uniqueCode = request.getParameter("fileId");
		String preview = request.getParameter("preview");
		LocalOperation operationFile = new LocalOperation(response);

		if (StringUtils.isNotEmpty(uniqueCode)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(uniqueCode);
			// 如果是数字则查询文件信息
			if (isNum.matches()) {
				Integer fileId = Integer.parseInt(uniqueCode);
				FileModel file = fileService.getFileById(fileId);
				if (file != null) {
					// 文件路径拼接
					String path = "";
					path = file.getPath() + file.getFileUUID() + "." + file.getFileType(); // +".dat"

					// 如果是公有文件可以直接下载，私有文件验证session
					if (PUBLIC.equals(file.getAccessType())) {
						if (StringUtils.isNotEmpty(preview)) {
							operationFile.fileDownload(path, true, file.getTitle() + "." + file.getFileType());
						} else {
							operationFile.fileDownload(path, false, file.getTitle() + "." + file.getFileType());
						}
					} else if (SessionUtils.getSession().getId().toString() != null) {
						if (StringUtils.isNotEmpty(preview)) {
							operationFile.fileDownload(path, true, file.getTitle() + "." + file.getFileType());
						} else {
							operationFile.fileDownload(path, false, file.getTitle() + "." + file.getFileType());
						}
					}
				}
			} else {
				// 如果不是数字则是路径
				if (SessionUtils.getSession().getId().toString() != null && StringUtils.isNotEmpty(preview)) {
					operationFile.fileDownload(uniqueCode, true, "");
				} else if (SessionUtils.getSession().getId().toString() != null) {
					operationFile.fileDownload(uniqueCode, false, "");
				}
			}
		}
	}

	/**
	 *
	 * @Description: 预览文件
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */

	@RequestMapping("viewfile")
	@ResponseBody
	public FormEngineResponse viewFile(@RequestBody FormEngineRequest requestObj, HttpServletRequest request) {
		FormEngineResponse response = new FormEngineResponse("");
		String fileContent = "";
		String filePath = "";
		// 获取文件id
		String uniqueCode = ObjectUtils.toString(requestObj.getData().get("uniqueCode"));
		if (StringUtils.isNotEmpty(uniqueCode)) {
			try {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(uniqueCode);
				if (isNum.matches()) {
					Integer fileId = Integer.parseInt(uniqueCode);
					FileModel fileModel = fileService.getFileById(fileId);
					if (fileModel != null) {
						filePath = fileModel.getPath() + fileModel.getFileUUID() + "." + fileModel.getFileType(); // +".dat"
					}
				} else {
					filePath = uniqueCode;
				}
				String lowerFilePath = filePath.toLowerCase();
				if (lowerFilePath.endsWith("png") || lowerFilePath.endsWith("jpg") || lowerFilePath.endsWith("jpeg")
						|| lowerFilePath.endsWith("gif")) {
					fileContent = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(filePath)));// 返回Base64编码过的字节数组字符串
				} else {
					fileContent = FileUtils.readFileToString(new File(filePath), "UTF-8");
				}
			} catch (IOException e) {
				FormLogger.error("预览文件接口", String.format("读取文件失败，文件不存在或操作异常,%s", e.getMessage()),
						FormLogger.LOG_TYPE_ERROR);
			}
		}
		response.setData(fileContent);
		return response;
	}

	/**
	 *
	 * @Description: 打开文件
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("openfile")
	public void openFile(HttpServletRequest request, HttpServletResponse response) {
		String fileContent = "";
		String filePath = "";
		// 获取文件id
		String uniqueCode = request.getParameter("uniqueCode");
		String token = request.getParameter("token");
		if (StringUtils.isNotEmpty(uniqueCode)) {
			try {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(uniqueCode);
				if (isNum.matches()) {
					Integer fileId = Integer.parseInt(uniqueCode);
					FileModel fileModel = fileService.getFileById(fileId);
					if (fileModel != null) {
						filePath = fileModel.getPath() + fileModel.getFileUUID() + "." + fileModel.getFileType(); // +".dat"
					}
				} else {
					filePath = uniqueCode;
				}

				FileUtils file = new FileUtils();
				fileContent = file.readFileToString(new File(filePath), "UTF-8");
			} catch (IOException e) {
				FormLogger.error("打开文件接口", String.format("读取文件失败，文件不存在或操作异常"), FormLogger.LOG_TYPE_ERROR);
				throw new ServiceException(ErrorCode.FILE_EXCEPTION, "文件操作异常");
			}
		}
		try {
			response.getWriter().write(fileContent);
		} catch (IOException e) {
			FormLogger.error("打开文件接口", String.format("写入失败，文件不存在或操作异常"), FormLogger.LOG_TYPE_ERROR);
			throw new ServiceException(ErrorCode.FILE_EXCEPTION, "文件操作异常");
		}
	}

	/**
	 *
	 * @Description: 运行文件
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@Value("${Rdurl}")
	private String Rdurl;

	@Value("${base.website}")
	private String website;

	@RequestMapping("runCode")
	public String runCode(HttpServletRequest request, Model model, HttpServletResponse response,
			RedirectAttributes attr) {
		String id = request.getParameter("id");// 资源ID
		String resourcesType = request.getParameter("resourcesType");// 资源类型
		String module = request.getParameter("module");// 模块

		/*
		 * String Rdurl = "http://127.0.0.1:8080/"; website = "http://127.0.0.1:8081/";
		 */
		if (StringUtils.isNotEmpty(id)) {
			try {
				// String token = "D:/document/work/201807/scorm-demo/task one
				// theory";
				Map<String, Object> map = null;
				if (HtmlResourceModuleEnum.CUSTOMER_RESOURCES.getCode().equals(module)) {
					map = ossUploadServiceImpl.getCustomerFilePathById(id);
				} else if (HtmlResourceModuleEnum.MAINTAIN_MANUAL.getCode().equals(module)) {
					map = maintainManualServiceImpl.queryFileById(id);
				}
				String filePath = map.get("filePath").toString();
				String token = filePath.substring(0, filePath.lastIndexOf("."));
				String url = AuthCode.encrypt(website);

				attr.addAttribute("token", AuthCode.encrypt(token));
				attr.addAttribute("url", url);
				attr.addAttribute("resourceId", AuthCode.encrypt(id));
				if (ResourceTypeEnum.SCORM.getCode() == Integer.parseInt(resourcesType)) {
					String courseId = request.getParameter("courseId");// 课程ID
					String sectionId = request.getParameter("sectionId");// 小节ID
					String labelId = request.getParameter("labelId");// 标签ID
					attr.addAttribute("courseId", AuthCode.encrypt(courseId));
					attr.addAttribute("sectionId", AuthCode.encrypt(sectionId));
					attr.addAttribute("labelId", AuthCode.encrypt(labelId));
					attr.addAttribute("userId", AuthCode.encrypt(SessionUtils.getUserInfo().getUserId() + ""));
				}
			} catch (Exception e) {
				FormLogger.error("参数操作", String.format("获取文件路径异常:%s", e.getMessage()), FormLogger.LOG_TYPE_ERROR);
				throw new ServiceException(ErrorCode.REDIS_ERROR, "获取文件路径异常");
			}
		} else {
			FormLogger.logFlow(String.format("runCode: do nothing with redis, id=%s ", id), FormLogger.LOG_TYPE_INFO);
		}
		LogUtil.info("redirect:" + Rdurl);
		LogUtil.info("website:" + website);
		if (ResourceTypeEnum.PPT.getCode() == Integer.parseInt(resourcesType)) {
			return "redirect:" + Rdurl + "pptRun.do";
		} else {
			return "redirect:" + Rdurl + "run.do";
		}
	}

	@RequestMapping("runCOrCppCode")
	@ResponseBody
	public FormEngineResponse runCOrCppCode(@RequestBody FormEngineRequest requestObj, HttpServletRequest request,
			Model model, HttpServletResponse response) throws IOException {

		String token = UUID.randomUUID().toString().replaceAll("-", "");
		String uniqueCode = (String) requestObj.getData().get("uniqueCode");
		String parameter = (String) requestObj.getData().get("parameter");

		String rootCatalog = (String) requestObj.getData().get("rootCatalog");
		Rdurl = Rdurl.substring(0, Rdurl.lastIndexOf("/") + 1);
		String result = "";
		if (StringUtils.isNotEmpty(uniqueCode)) {
			HttpClientUtil client = new HttpClientUtil();
			Map<String, String> map = new HashMap<>();
			map.put("token", token);
			// map.put("value",
			// "/usr/local/etc/course/少儿编程体验课02/课堂练习/test3.cpp");
			map.put("value", uniqueCode);
			map.put("parameter", parameter);
			try {
				result = client.doPost(Rdurl + "runCOrCpp.do", map);
			} catch (Exception e) {
				e.printStackTrace();
				FormLogger.logFlow(String.format("runCOrCppCode: connect HtmlContainer exception",
						uniqueCode + ":" + parameter, rootCatalog), FormLogger.LOG_TYPE_ERROR);
			}
			System.out.println(result);
		} else {
			FormLogger.logFlow(String.format("runCOrCppCode: get uniqueCode fail, uniqueCode=%s ,parameter=%s",
					uniqueCode + ":" + parameter, rootCatalog), FormLogger.LOG_TYPE_INFO);
			result = "runCOrCppCode: get uniqueCode fail";
		}
		return new FormEngineResponse(result);
	}

	/**
	 *
	 * @Description: 模板下载
	 * @author: gaolu
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping(value = "/downLoadTemp")
	public String downLoadTemp(ExcelOutputData map, String titles, HttpServletRequest request,
			HttpServletResponse response) {
		String[][] title = new String[titles.split(",").length - 1][1];
		for (int i = 0; i < titles.split(",").length - 1; i++) {
			title[i][0] = titles.split(",")[i];
		}
		map.setFileName(StringUtils.isEmpty(request.getParameter("title")) ? "批量导入.xls"
				: request.getParameter("title") + ".xls");
		map.setExcelTitle(title);
		return "excelView";
	}

	/**
	 *
	 * @Description: 本地上传
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("/uploadExcelBat")
	@ResponseBody
	public FormEngineResponse uploadExcelBat(HttpServletRequest request, String token) throws IOException {
		UploadFile upFile = new UploadFile(request, "");
		if (upFile != null) {
			String fileType = upFile.filepostfix[0];
			if (fileType.length() > 0) {
				// 得到上传文件的流
				InputStream is = upFile.getFileInputStream()[0];
				ServerTokenData serverTokenData = formTokenService.geToken(token);

				List<Map<String, Object>> list;
				List<String[]> lstLine = new ArrayList<>();
				if ("csv".equalsIgnoreCase(fileType)) {
					lstLine = ExcelUtil.csvToList(serverTokenData, is);
				} else if ("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)) {
					lstLine = ExcelUtil.excelToList(serverTokenData, is);
				}
				list = ExcelUtil.genTableData(serverTokenData, lstLine);
				DataGrid dgData = new DataGrid(list);
				return new FormEngineResponse(dgData);
			}
		}
		return null;
	}

	/**
	 *
	 * @Description: 二进制流形式传递内容 后台生成.md文件并上传至服务器
	 * @author: hejk
	 * @date: 2017年11月22日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	@RequestMapping("/createMdBat")
	@ResponseBody
	public int createMdBat(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		String fileId = request.getParameter("fileId"); // 文件ID
		String objStr = request.getParameter("fileName"); // 文档名称

		// server's path
		FilePathUtil filepath = new FilePathUtil(new Date(), DISH);

		String accessType = "markdown";
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String path = filepath.getMdPath("md", "", "", "");
		// insert
		if (fileId == null || fileId == "") {

			String fileSave = path + fileUUID + ".md"; // .dat
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			// io write
			FileIOWrite(request, fileSave);
			FileModel fm = null;
			// get file's size
			file = new File(fileSave);
			fm = fileService.localUpload(objStr, file.length() + "", fileUUID, accessType, "", fm, path);
			if (fm != null) {
				// successed return file's ID
				return fm.getId();
			} else {
				return 0;
			}

		} else {
			// update
			/**
			 * 调用高璐的备份文件方法
			 */

			// 将文件内容更新到文件内
			FileModel fm = fileService.getFileById(Integer.parseInt(fileId));
			int fmId = fm.getId();
			path = fm.getPath();
			String filePath = filepath.getDownPath(fm, "markdown", ""); // 当前md文档存放路径

			File file = new File(filePath);
			// io wirte
			FileIOWrite(request, filePath);

			file = new File(filePath);

			fm = fileService.localUpload(objStr, file.length() + "", fm.getFileUUID(), accessType, "edit", fm, path);
			return fmId;
		}

	}

	/**
	 * IO write
	 * 
	 * @param request
	 * @param bytes
	 * @param fileSave
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void FileIOWrite(HttpServletRequest request, String fileSave) throws IOException {
		int index;
		byte[] bytes = new byte[1024];
		try (FileOutputStream downloadFile = new FileOutputStream(fileSave)) {
			while ((index = request.getInputStream().read(bytes)) != -1) {
				downloadFile.write(bytes, 0, index);
				downloadFile.flush();
			}
		} catch (Exception e) {
			FormLogger.error(e.getMessage(), e);
		} finally {
			request.getInputStream().close();
		}
	}

	/**
	 *
	 * @Description: 将file信息存入知识库目录表、文档信息表
	 * @author: hejk
	 * @date: 2017年11月29日 下午13:31:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * @throws Exception
	 */
	@RequestMapping("/saveDocCata")
	@ResponseBody
	public int saveDocCata(HttpServletRequest request, HttpServletResponse response) {
		int res = 0;
		String file_id = request.getParameter("file_id"); // t_file_index.id
		String cata_id = request.getParameter("cata_id"); // t_doc_cata.id
		String file_name = request.getParameter("file_name"); // t_file_index.name
		try {
			documentService.handleDocument(file_id, cata_id, file_name);
			res = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 将markdown文档另存为pdf
	 * 
	 * @author: zhangzhiming
	 * @date: 2018年6月27日 下午13:07:39
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/saveAsPDF")
	@ResponseBody
	public int saveAsPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String fileId = request.getParameter("file_id");
		String fileName = request.getParameter("file_name");
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		FilePathUtil filepath = new FilePathUtil(new Date(), DISH);
		if (fileId == null || fileId == "") {
			return -1;
		} else {
			// 获取markdown的路径
			FileModel fm = fileService.getFileById(Integer.parseInt(fileId));
			String filePath = filepath.getDownPath(fm, "markdown", ""); // 当前md文档存放路径

			// html存放的路径
			String outUrl = filepath.getHtmlPath() + fileName + ".html";
			File hfile = new File(filepath.getHtmlPath());
			if (!hfile.exists()) {
				hfile.mkdirs();
			}
			// pdf存放的路径
			String path = filepath.getPdfPath();
			String destPath = path + fileName + ".pdf";
			File pfile = new File(path);
			if (!pfile.exists()) {
				pfile.mkdirs();
			}
			markdownAsPDFService.mdToHtml(filePath, outUrl);// markdown转html
			markdownAsPDFService.convert(outUrl, destPath);// html转pdf

			String accessType = "pdf";
			FileModel fmpdf = null;
			File file = new File(destPath);
			// 保存Pdf的信息
			fmpdf = fileService.localUpload(fileName, file.length() + "", fileUUID, accessType, "", fmpdf, path);
			System.out.println("fmpdf--------" + fmpdf.getId());
			return fmpdf.getId();
		}
	}

	/**
	 *
	 * 功能就是图片上传，同markdown相同，只是返回的json串指定参数.
	 * 
	 * @Description: KindEditor 图片上传
	 * @author: hejk
	 * @date: 2018年1月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * 
	 */
	@RequestMapping("/localKEUpload")
	@ResponseBody
	public Map localKEUpload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String accessType = "public";
		// 上传结果信息
		responseJSON = new ResponseJSON();
		// 创建文件上传对象
		UploadFile upfiles = new UploadFile(request, "") {
			// 生成文件新名字
			@Override
			public String offerUploadFileNames() {
				return fileUUID;
			}
		};

		// 只能上传单文件判断
		map = fileService.localUpload(upfiles, fileUUID, accessType, "", "");
		hintMessage = (String) map.get("hintMessage");
		if (StringUtils.isEmpty(hintMessage)) {
			// 获取上传后文件信息对象
			FileModel fileModel = (FileModel) map.get("fileModel");
			// 上传成功则返回id
			hintMessage = "上传成功";
			responseJSON.setStatus(fileModel.getId() == null ? 0 : fileModel.getId());
			/*
			 * String projectUr = request.getRequestURL().toString(); map.put("error",0);
			 * map.put("url",projectUr.replace("/localKEUpload","")+
			 * "/localDownload?methodType=markdown444444&fileId="+fileModel. getId());
			 */

			Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileModel.getId() + "");
			String fullPath = fileMap.get("fullpath");
			String key = ossUploadServiceImpl.getOssKey(fileModel.getFileType());
			ossUploadServiceImpl.uploadOss(key, fullPath, ossPubClient, BUCKET_NAME2);

			String fileName = fileMap.get("fileName");
			String fileType = fileMap.get("fileType");
			ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key, BUCKET_NAME2);

			String url = ossQueryServiceImpl.queryObjectByKey(key, ossPubClient, BUCKET_NAME2);
			map.put("url", url);
			map.put("error", 0);
			map.put("fileId", fileModel.getId());
		}
		responseJSON.setMsg(hintMessage);
		return map;
	}

	/**
	 *
	 * 功能就是图片上传，同markdown相同，只是返回的json串指定参数.
	 * 
	 * @Description: KindEditor 图片上传
	 * @author: hejk
	 * @date: 2018年1月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * 
	 */
	@RequestMapping("/localUEUpload")
	@ResponseBody
	public void localUEUpload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String accessType = "public";
		// 上传结果信息
		responseJSON = new ResponseJSON();
		// 创建文件上传对象
		UploadFile upfiles = new UploadFile(request, "") {
			// 生成文件新名字
			@Override
			public String offerUploadFileNames() {
				return fileUUID;
			}
		};
		String result = "FAIL";
		String url = "";
		// 只能上传单文件判断
		map = fileService.localUpload(upfiles, fileUUID, accessType, "", "");
		hintMessage = (String) map.get("hintMessage");
		if (StringUtils.isEmpty(hintMessage)) {
			// 获取上传后文件信息对象
			FileModel fileModel = (FileModel) map.get("fileModel");
			// 上传成功则返回id
			hintMessage = "上传成功";
			responseJSON.setStatus(fileModel.getId() == null ? 0 : fileModel.getId());
			/*
			 * String projectUr = request.getRequestURL().toString(); map.put("error",0);
			 * map.put("url",projectUr.replace("/localKEUpload","")+
			 * "/localDownload?methodType=markdown444444&fileId="+fileModel. getId());
			 */

			Map<String, String> fileMap = ossUploadServiceImpl.getFilePathById(fileModel.getId() + "");
			String fullPath = fileMap.get("fullpath");
			String key = ossUploadServiceImpl.getOssKey(fileModel.getFileType());
			ossUploadServiceImpl.uploadOss(key, fullPath, ossPubClient, BUCKET_NAME2);

			String fileName = fileMap.get("fileName");
			String fileType = fileMap.get("fileType");
			ossRecordServiceImpl.insertOssRecord(fileName, fileType, fileModel.getId().toString(), key, BUCKET_NAME2);

			url = ossQueryServiceImpl.queryPublicUrlByKey(key, BUCKET_NAME2, OSS_ENDPOINT2);
			result = "SUCCESS";

			fileServiceImpl.updateFileOssUrl(fileModel.getId(), url, key);

		}
		resultUEMessage("SUCCESS", true, url, response);
	}

	/**
	 * 返回UEditor的信息
	 * 
	 * @param message 错误的消息提示
	 * @param status  是否成功
	 * @param url     图片的URL
	 */
	private void resultUEMessage(String message, boolean status, String url, HttpServletResponse response) {
		JSONObject jsobject = new JSONObject();
		if (status) {
			jsobject.put("state", message);
			jsobject.put("url", url);
			jsobject.put("original", "");
		} else {
			jsobject.put("state", message);
			jsobject.put("url", "");
			jsobject.put("title", "");
			jsobject.put("original", "");
		}
		response.setContentType("application/json;charset=utf-8");
		try {
			String outputStr = "";
			if (jsobject != null) {
				outputStr = jsobject.toString();
			}
			// ServletOutputStream对UTF-8的支持不好，此处用Writer
			PrintWriter out = response.getWriter();
			out.write(outputStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @Description: 打开文件
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("downloadPdf")
	public void downloadPdf(HttpServletRequest request, HttpServletResponse response) {
		// InputStream in = null ;
		// 获取文件id
		String key = request.getParameter("key");
		String url = request.getParameter("url");
		if (key == null || "".equals(key)) {
			key = url.substring(url.indexOf("com/"), url.indexOf("?", 0)).split("/")[1];
		}
		OSSClient ossClient = null;
		String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
		File f = new File(DISH + uuid + key.substring(key.lastIndexOf("."), key.length()));
		if (StringUtils.isNotEmpty(key)) {
			try {

				// in = ossQueryServiceImpl2.queryDownPdfByKey(key,
				// ossPriClient, BUCKET_NAME1);
				if (url.contains(BUCKET_NAME2)) {
					ossClient = new OSSClient(OSS_ENDPOINT2, ossAccess_id, ossAccess_key);
					ossClient.getObject(new GetObjectRequest(BUCKET_NAME2, key), f);
				} else {
					ossClient = new OSSClient(OSS_ENDPOINT1, ossAccess_id, ossAccess_key);
					ossClient.getObject(new GetObjectRequest(BUCKET_NAME1, key), f);

				}
				FileInputStream in1 = new FileInputStream(f);

				/*
				 * BufferedReader reader = new BufferedReader(new InputStreamReader(in)); try {
				 * while (true) { String line = reader.readLine(); if (line == null){ break; }
				 * 
				 * in1.read(reader.readLine().getBytes()); }
				 * 
				 * } catch (Exception e) { e.printStackTrace(); }finally { reader.close(); }
				 */

				OutputStream out = response.getOutputStream();
				response.setContentType("application/pdf");
				byte[] b = new byte[512];
				while (in1.read(b) != -1) {
					out.write(b);
				}
				out.flush();
				out.close();
				in1.close();
			} catch (IOException e) {
				e.printStackTrace();
				FormLogger.error("打开文件接口", String.format("读取文件失败，文件不存在或操作异常"), FormLogger.LOG_TYPE_ERROR);
				throw new ServiceException(ErrorCode.FILE_EXCEPTION, "文件操作异常");

			} finally {

				ossClient.shutdown();
				f.delete();
			}
		}
	}

	/**
	 *
	 * @Description: 打开文件
	 * @author: bj
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("templeteDownload")
	public void templeteDownload(HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream out = null;
		try {
			String fileName = request.getParameter("fileName");
			String exportName = request.getParameter("exportName");
			String path = request.getSession().getServletContext().getRealPath("/");
			path = path + File.separator + "template" + File.separator + fileName;
			File file = new File(path);

			if (file.exists()) {
				fis = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				// 清空response
				response.reset();
				// 设置response的Header
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(
						"undefined".equals(exportName) || exportName == null ? "问题导入模板.xls" : exportName + ".xls",
						"UTF-8"));
				out = new BufferedOutputStream(response.getOutputStream());
				out.write(buffer);
				out.flush();
			}
		} catch (Exception e) {
			try {
				if (fis != null) {
					fis.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @Description: 打开文件
	 * @author: bj
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("fileToStream")
	public void fileToStream(HttpServletRequest request, HttpServletResponse response) {
		// InputStream in = null ;
		SessionUtils.getUserInfo();
		// 获取文件id
		String key = request.getParameter("key");
		String ossUrl = request.getParameter("ossUrl");
		String userId = request.getParameter("userId");
		String videoFilename = "";
		String videoPath = "";
		String type = key.substring(key.lastIndexOf(".") + 1, key.length());
		String token = request.getParameter("token");
		OSSClient ossClient = null;
		OSSObject ossObject = null;
		String gtoken = aliVideoService.getToken(token,"");
		if(gtoken!=null&&!"".equals(gtoken)) {
			try {
				redisUtil.update(token, "");
				String bucket = "";
				if (ossUrl.contains(BUCKET_NAME2)) {
					bucket = BUCKET_NAME2;
					ossClient = new OSSClient(OSS_ENDPOINT2, ossAccess_id, ossAccess_key);
					ossObject = ossClient.getObject(BUCKET_NAME2, key);
					// ossClient.getObject(new GetObjectRequest(BUCKET_NAME2, key), file);
				} else {
					bucket = BUCKET_NAME1;
					ossClient = new OSSClient(OSS_ENDPOINT1, ossAccess_id, ossAccess_key);
					ossObject = ossClient.getObject(BUCKET_NAME1, key);
				}
				String range = request.getHeader("range");
				String url = ossQueryServiceImpl.queryObjectByKey(key,ossClient,bucket);
				int length =(int) ossObject.getObjectMetadata().getContentLength();
			
				//response.setContentLength(length);
				response.setHeader("Accept-Ranges", "bytes");
				//response.setHeader("Content-Length",length+"");
				//response.setHeader( "Content-Disposition", "attachment;filename=" + URLEncoder.encode(key, "UTF-8"));
				response.setHeader(  "Access-Control-Allow-Origin","*");
				// 设置在下载框默认显示的文件名
				response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
				// 读出文件到response
				// 这里是先需要把要把文件内容先读到缓冲区
				// 再把缓冲区的内容写到response的输出流供用户下载
				InputStream inputStream =  ossObject.getObjectContent();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				OutputStream outputStream = response.getOutputStream();
				byte buffer[] = new byte[1024];
				int len = 0; 
				while ((len = bufferedInputStream.read(buffer)) > 0) {
				    outputStream.write(buffer, 0, len);
				}
				// 人走带门
				bufferedInputStream.close();
				outputStream.flush();
				outputStream.close();
				// 关闭OSSClient。
				ossClient.shutdown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				response.getWriter().write("No right to visit");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	public void getOssFile(File file, String key, String url) {

		OSSClient ossClient = null;
		if (url.contains(BUCKET_NAME2)) {
			ossClient = new OSSClient(OSS_ENDPOINT2, ossAccess_id, ossAccess_key);
			ossClient.getObject(new GetObjectRequest(BUCKET_NAME2, key), file);
		} else {
			ossClient = new OSSClient(OSS_ENDPOINT1, ossAccess_id, ossAccess_key);
			ossClient.getObject(new GetObjectRequest(BUCKET_NAME1, key), file);
		}

	}

	/**
	 *
	 * @Description: 打开文件
	 * @author: bj
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("getToken")
	public FormEngineResponse getToken(@RequestBody FormEngineRequest requestObj, HttpServletRequest request,
			HttpServletResponse response) {
		String userId = requestObj.getData().get("userId").toString();
		String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
		String token = TokenUtils.generateToken(uuid, userId);
		FormEngineResponse formEngineResponse = new FormEngineResponse("");
	    token = aliVideoService.getToken("token"+token,userId);
		formEngineResponse.setData(token);
		formEngineResponse.setCode("0");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			JSONObject jb = JSONObject.fromObject(formEngineResponse);
			pw.write(jb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 *
	 * @Description: 打开文件
	 * @author: bj
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("getVideoPlayAuth")
	public void getVideoPlayAuth(@RequestBody FormEngineRequest requestObj, HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter pw = null;
		String playAuth = "";
		try {
			pw = response.getWriter();
			String aliVid = requestObj.getData().get("vid").toString();
			DefaultAcsClient client = aliVideoPlayUtils.initVodClient(videoKeyId, videoKeySecret);
			playAuth = aliVideoPlayUtils.getVideoPlayAuth(client, aliVid).getPlayAuth();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FormEngineResponse formEngineResponse = new FormEngineResponse("");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", playAuth);
		map.put("code", "0");

		/*
		 * formEngineResponse.setData(map); formEngineResponse.setCode("0");
		 */
		JSONObject jsonObject = JSONObject.fromObject(map);
		pw.write(jsonObject.toString());
	}

	/**
	 *
	 * @Description: 获取视频点播播放地址
	 * @author: bj
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("getPlayUrl")
	public void getPlayUrl(@RequestBody FormEngineRequest requestObj, HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter pw = null;
		String url = "";
		try {
			pw = response.getWriter();
			String aliVid = requestObj.getData().get("vid").toString();

			url = aliVideoPlayUtils.getPlaySource(aliVid);
			String token = aliVideoService.getToken(aliVid);
			url = url + "?MtsHlsUriToken=" + token;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FormEngineResponse formEngineResponse = new FormEngineResponse("");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", url);
		map.put("code", "0");

		/*
		 * formEngineResponse.setData(map); formEngineResponse.setCode("0");
		 */
		JSONObject jsonObject = JSONObject.fromObject(map);
		pw.write(jsonObject.toString());
	}
}
