package com.tedu.base.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadURLStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadURLStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.kms.model.v20160120.DecryptRequest;
import com.aliyuncs.kms.model.v20160120.DecryptResponse;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyRequest;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.SubmitTranscodeJobsRequest;
import com.aliyuncs.vod.model.v20170321.SubmitTranscodeJobsResponse;
import com.google.gson.Gson;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.oss.service.OssQueryService;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author bj
 * 
 * @Description: TODO
 * @date 2019-03-21
 */
@Component("aliVideoPlayUtils")
public class AliVideoPlayUtils {
	@Value("${video.aliyun.assessKeyId}")
	private String videoKeyId;

	@Value("${video.aliyun.accessKeySecret}")
	private String videoKeySecret;

	@Resource
	FormService formService;

	@Resource
	private FormMapper formMapper;

	@Value("${video.code.templateId}")
	private String templateId;

	@Value("${video.domain.url}")
	private String domain;

	@Resource
	private OSS ossPubClient;

	@Resource
	private OSS ossPriClient;
	@Resource
	private OssQueryService ossQueryServiceImpl;

	//public String serviceKey = "4eeb734c-772b-4593-9d56-e98cce7b68e3";
	  public String serviceKey = "41c7b5a8-c8ea-4080-94cc-c94150694703";
	/**
	 * 初始化
	 *
	 * @param accessKeyId
	 * @param accessKeySecret
	 *
	 * @return
	 *
	 * @throws ClientException
	 */
	public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
		String regionId = "cn-shanghai"; // 点播服务接入区域
		DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		return client;
	}

	/**
	 * 获取播放地址函数
	 */
	public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client,String videoKeyId) throws Exception {
		GetPlayInfoRequest request = new GetPlayInfoRequest();
		request.setVideoId(videoKeyId);
		return client.getAcsResponse(request);
	}

	/**
	 * 获取播放凭证函数
	 */
	public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client, String aliVid) throws Exception {
		GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
		request.setVideoId(aliVid);
		return client.getAcsResponse(request);

	}

	/**
	 * 获取阿里授权URL
	 *
	 * @return
	 */
	public String getAuthURL() {
		return "";
	}

	/**
	 * oss上传至视频点播
	 */
	private String testUploadURLStream(String title, String fileName, String url) throws ClientException {
		UploadURLStreamRequest request = new UploadURLStreamRequest(videoKeyId, videoKeySecret, title, fileName, url);
		String videoId = "";
		UploadVideoImpl uploader = new UploadVideoImpl();
		request.setPrintProgress(false);
		UploadURLStreamResponse response = uploader.uploadURLStream(request);
		System.out.print("RequestId=" + response.getRequestId() + "\n"); // 请求视频点播服务的请求ID
		if (response.isSuccess()) {
			videoId = response.getVideoId();
			System.out.print("VideoId=" + response.getVideoId() + "\n");
		} else {
			// 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
			System.out.print("VideoId=" + response.getVideoId() + "\n");
			System.out.print("ErrorCode=" + response.getCode() + "\n");
			System.out.print("ErrorMessage=" + response.getMessage() + "\n");
		}

		return videoId;
	}

	/**
	 * 本地文件上传
	 * 
	 */
	public String testUploadLocalFile(String title, String fileName, String localFile, String videoKeyId,
			String videoKeySecret) throws ClientException {
		UploadVideoRequest request = new UploadVideoRequest(videoKeyId, videoKeySecret, title, localFile);
		/* 可指定分片上传时每个分片的大小，默认为1M字节 */
		request.setPartSize(1 * 1024 * 1024L);

		/*
		 * 是否开启断点续传,
		 * 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。 注意:
		 * 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启
		 */
		request.setEnableCheckpoint(false);

		UploadVideoImpl uploader = new UploadVideoImpl();
		UploadVideoResponse response = uploader.uploadVideo(request);
		System.out.print("RequestId=" + response.getRequestId() + "\n"); // 请求视频点播服务的请求ID
		if (response.isSuccess()) {
			System.out.print("VideoId=" + response.getVideoId() + "\n");
		} else {
			/*
			 * 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，
			 * 此时需要根据返回错误码分析具体错误原因
			 */
			System.out.print("VideoId=" + response.getVideoId() + "\n");
			System.out.print("ErrorCode=" + response.getCode() + "\n");
			System.out.print("ErrorMessage=" + response.getMessage() + "\n");
		}
		return response.getVideoId();
	}

	public static String decodeBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			Base64 decoder = new Base64();
			try {
				b = decoder.decode(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
			}
		}
		return result;
	}


	public static OSSClient initOssClient(JSONObject uploadAuth, JSONObject uploadAddress) {
		String endpoint = uploadAddress.getString("Endpoint");
		String accessKeyId = uploadAuth.getString("AccessKeyId");
		String accessKeySecret = uploadAuth.getString("AccessKeySecret");
		String securityToken = uploadAuth.getString("SecurityToken");
		return new OSSClient(endpoint, accessKeyId, accessKeySecret, securityToken);
	}

	public static void uploadLocalFile(OSSClient ossClient, JSONObject uploadAddress, String localFile) {
		String bucketName = uploadAddress.getString("Bucket");
		String objectName = uploadAddress.getString("FileName");
		File file = new File(localFile);
		ossClient.putObject(bucketName, objectName, file);
	}

	public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient vodClient) throws ClientException {
		RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
		request.setAcceptFormat(FormatType.JSON);
		request.setVideoId("VideoId");

		// 设置请求超时时间
		request.setSysReadTimeout(1000);
		request.setSysConnectTimeout(1000);

		return vodClient.getAcsResponse(request);
	}


	 public String getPlaySource(String vid) {

	        String playUrl = "";
	        try {
	            DefaultAcsClient client = initVodClient(videoKeyId, videoKeySecret);

	            GetPlayInfoResponse response = new GetPlayInfoResponse();
	            try {
	                response = getPlayInfo(client, vid);
	                List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
	                //播放地址
	                for (int i = 0; i < playInfoList.size(); i++) {
	                    GetPlayInfoResponse.PlayInfo playInfo = playInfoList.get(i);
	                    if (playInfo.getEncryptType().equals("HLSEncryption")) {
	                        playUrl = playInfo.getPlayURL();
	                    }


	                }
	                //Base信息
	                System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
	            } catch (Exception e) {
	                System.out.print("ErrorMessage = " + e.getLocalizedMessage());
	            }
	            System.out.print("RequestId = " + response.getRequestId() + "\n");
	        } catch (Exception e) {

	        }
	        return playUrl;
	    }

	public SubmitTranscodeJobsResponse submitTranscodeJobs(String vid)
			throws Exception {
		DefaultAcsClient client = initVodClient(videoKeyId, videoKeySecret);
//      String vid = "7eb1b14768994c3e8c5f087bfda20cb9";
		SubmitTranscodeJobsRequest request = new SubmitTranscodeJobsRequest();
		// 需要转码的视频ID
		request.setVideoId(vid);
		// 转码模板ID
		request.setTemplateGroupId(templateId);
		// 构建需要替换的水印参数(只有需要替换水印相关信息才需要构建)
		// JSONObject overrideParams = buildOverrideParams();
		// 覆盖参数，暂只支持水印部分参数替换(只有需要替换水印相关信息才需要传递)
		// request.setOverrideParams(overrideParams.toJSONString());
		JSONObject encryptConfig = buildEncryptConfig(client);
		String key = encryptConfig.getString("CipherText").toString();

		Map<String, Object> conditionMap = new HashMap<>(2);
		conditionMap.put("vid", vid);
		conditionMap.put("key", key);
		CustomFormModel model = new CustomFormModel("", "", conditionMap);
		model.setSqlId("InsertAliKey");
		formService.saveCustom(model);

		FormLogger.info("加密信息:" + encryptConfig);
		 //HLS标准加密配置(只有标准加密才需要传递)
        request.setEncryptConfig(encryptConfig.toJSONString());
		return client.getAcsResponse(request);
	}

	/**
	 * 构建HLS标准加密的配置信息
	 *
	 * @return
	 *
	 * @throws ClientException
	 */
	public JSONObject buildEncryptConfig(DefaultAcsClient client) throws ClientException {
		// 点播给用户在KMS(秘钥管理服务)中的Service Key，可在用户秘钥管理服务对应的区域看到描述为vod的service key
		// 随机生成一个加密的秘钥，返回的response包含明文秘钥以及密文秘钥，
		// 视频标准加密只需要传递密文秘钥即可
		GenerateDataKeyResponse response = generateDataKey(client);
		JSONObject encryptConfig = new JSONObject();
		// 解密接口地址，该参数需要将每次生成的密文秘钥与接口URL拼接生成，表示每个视频的解密的密文秘钥都不一样
		// 至于Ciphertext这个解密接口参数的名称，用户可自行制定，这里只作为参考参数名称
		encryptConfig.put("DecryptKeyUri", "http://www.knowhowedu.cn/decrypt?" + "CipherText=" + response.getCiphertextBlob());
		// 秘钥服务的类型，目前只支持KMS
		encryptConfig.put("KeyServiceType", "KMS");
		// 密文秘钥
		encryptConfig.put("CipherText", response.getCiphertextBlob());
		return encryptConfig;
	}

	public byte[] decryptKey(String cipherBlob) throws ClientException {
		DefaultAcsClient client = initVodClient(videoKeyId, videoKeySecret);
		DecryptRequest decReq = new DecryptRequest();
		decReq.setCiphertextBlob(cipherBlob);
		decReq.setAcceptFormat(FormatType.JSON);
		try {
			DecryptResponse response = client.getAcsResponse(decReq);
			String plaintext = response.getPlaintext();
			// 注意：需要base64 decode
			return Base64.decodeBase64(plaintext);
		} catch (ClientException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 生成加密需要的秘钥，response中包含密文秘钥和明文秘钥，用户只需要将密文秘钥传递给点播即可 注意：KeySpec
	 * 必须传递AES_128，且不能设置NumberOfBytes
	 *
	 * @param client     KMS-SDK客户端
	 * @param serviceKey 点播提供生成秘钥的service key，在用户的秘钥管理服务中可看到描述为vod的加密key
	 *
	 * @return
	 *
	 * @throws ClientException
	 */
	public GenerateDataKeyResponse generateDataKey(DefaultAcsClient client) throws ClientException {
		GenerateDataKeyRequest request = new GenerateDataKeyRequest();
		request.setKeyId(serviceKey);
		request.setKeySpec("AES_128");
		return client.getAcsResponse(request);
	}

	public void encryptVideo(String vid) {
		try {
			DefaultAcsClient client = initVodClient(videoKeyId, videoKeySecret);
			SubmitTranscodeJobsResponse response = new SubmitTranscodeJobsResponse();

			response = submitTranscodeJobs(vid);
			// 任务ID
			System.out.println("JobId = " + response.getTranscodeJobs().get(0).getJobId());
			System.out.println("RequestId = " + response.getRequestId());

		} catch (Exception e) {
			System.out.println("ErrorMessage = " + e.getLocalizedMessage());
		}
	}

	/**
	 * OSS视频上传至云点播
	 * 
	 * @throws ClientException
	 */
	public void ossVideoUploadToAliVideoPlay() throws IOException, ClientException {

		QueryPage queryPage = new QueryPage();
		queryPage.setQueryParam("video/QryOssUrl");
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) formService.queryBySqlId(queryPage);
		CustomFormModel cModel = new CustomFormModel();
		cModel.setSqlId("video/updateVid");
		OSSClient ossClient;
		FormLogger.info("OSS视频同步到视频点播服务开始执行...共" + dataList.size() + "个");
		for (int i = 0; i < dataList.size(); i++) {
			/*
			 * if(i>=67) { try { Thread.sleep(15000); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 * if(dataMap!=null&&!ossKey.contains(".")){ try {
			 * submitTranscodeJobs(client,ossKey); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } } }
			 */

			Map<String, Object> dataMap = dataList.get(i);
			if (dataMap != null) {
				String ossKey = dataMap.get("ossKey").toString();
				String bucketName = dataMap.get("bucketName").toString();
				String fileName = dataMap.get("fileName").toString();
				String fileType = "." + dataMap.get("fileType").toString();
				FormLogger.info("第" + (i + 1) + "个：《" + fileName + "》开始同步");
				if ("know-how001".equals(bucketName)) {
					ossClient = (OSSClient) ossPubClient;
				} else {
					ossClient = (OSSClient) ossPriClient;
				}

				String ossUrl = ossQueryServiceImpl.queryObjectByKey(ossKey, ossClient, bucketName);
				System.out.println("ossUrl:" + ossUrl);
				if (ossUrl != null && !"".equals(ossUrl)) {
					String vid = "";
					try {
						vid = testUploadURLStream(fileName, fileName + fileType, ossUrl);
						dataMap.put("vid", vid);
					} catch (ClientException e) {

						e.printStackTrace();
					}
					if (vid != null && !"".equals(vid)) {
						cModel.setData(dataMap);
						formMapper.saveCustom(cModel);
					} else {
						FormLogger.info("第" + (i + 1) + "个：《" + fileName + "》同步失败，资源ID：" + dataMap.get("id"));
					}
				}
				FormLogger.info("第" + (i + 1) + "个：《" + fileName + "》同步完成");
			}

		}

	}
	
	public void submit() {
		String ids = "4d300e53e7e74daabbbd3d5a4d39b2e5,"
				+"610960b8cc224500a831ca51584e2323,"
				+"239ab3a1f1ae4117bc402e7956e09bcf,"
				+"d3c06066d2764035b5b6e16491e52780,"
				+"2a4e73f158ef4b49a8d57a36c1b94e74,"
				+"80866412f2ff4ca8a8fd410a5782e6a6,"
				+"4578bcb39cac4b4f9b0426a40b54775f,"
				+"44a382ef7a9d4b659570319800fe2362,"
				+"445ce43980bf4622ae521089bb9dd461,"
				+"1af8107a61bb4776ba245b20e5c7ad13,"
				+"bbe75513ddee4038a40328333cd12929,"
				+"b75e1a40bcef49a790228449b77e62cf,"
				+"95ac23b7fcb84f89b91e8a858d7b087b,"
				+"ce7721ef5ac740ce8bc0e611f253ad23,"
				+"b13faf483694464b88e11f8ea078e9bd,"
				+"8a28c9b688cc489f9f4bd44d54ee1384,"
				+"203ad0ee67114bb19dd00bad6efb8df9,"
				+"ecde21d4b2af47f5a140b5e23719a865,"
				+"aa48b3becc59479f82ab0ae6ad4cc9ec,"
				+"a7859e33c4c24d88a53e171bbb2f344a,"
				+"8a9a8b84daed4338aaabad5b55f92851,"
				+"d24c20d3a4af43058897a15538797572,"
				+"b408b4dc58f2473899255e0193be393a,"
				+"adde9106507949ce8dcaa2838d23e6ee,"
				+"26e38793fdb849ee8a6d2bc9fc01bae1,"
				+"9530ce02cf6e4889aeb50e8565ea6322,"
				+"d280d6631e0a4596ad062d1b54ab0fb4,"
				+"9b48a6a0ad0244269665b8e93944a44a,"
				+"c29b70e81d3b4cbfa2cadb22adfa26ac,"
				+"3d12086d55ac45f09e6e9a14a7736a93,"
				+"44c45f6e56a34fe3b21774b8a7a0047f,"
				+"ce4b8b97e8a54ac6898bf83b35a51133,"
				+"03c8dad14a2e4fe0b53b4ef3aa2e6363,"
				+"fc2fff71228d4cb2b6ce12a9005d8cec,"
				+"a3dbf5ac23094b5f8a0cb8ff66acadc4,"
				+"e3228786ecd44ccbb680f9979107320a,"
				+"384824fbf07b4d5aa93bf1b9b7ca1a1c,"
				+"8b5b9cb135e74804b0b3497bdd4ad1fc,"
				+"de6a029b2eb14ec19e603f83f799b407,"
				+"25eea7c306e64075b3c2be20b26b3f39,"
				+"61ec111d814b48858e53a61aeb5ca965,"
				+"f48fb443bb7642f19246e859ed9a317f,"
				+"88a1e968795c40ecbca76e265e4d72b8,"
				+"9851f2c7a45e43c4bd93a51412f9fa71,"
				+"2b5b0f2b6c3f408e8101e31170795358,"
				+"601adaf419d340db862514eb0da068dc,"
				+"3279fd61d5504e73baee347298b908a1,"
				+"0e157632ce054220bb3a88a39e9095d4,"
				+"dfb316285f3e4d6bb6c54c419c425ad4,"
				+"100dc7e5aec54177becd23202c35e47c,"
				+"9124667820ca49269e48c5ef8301352a,"
				+"7efcde2701924016b5019db74e64b490,"
				+"679fa6bb29dd4e779ac8d537784dbf01,"
				+"33e4f1beaf944a1f821568ab751b48bc,"
				+"eeff51f6ae9d40e7b6588bbe209c0c8f,"
				+"a65213fee2884e6099bc5e847f2bc4ec,"
				+"792525d5ccbb4754b4a83123a1d6bd0d,"
				+"80f9d459144d4640bfab037dfc9c3751,"
				+"7e6f37abd64c4dc583c5df42e0ca325b,"
				+"ddfc392e8dda413b99d3875ade613273,"
				+"a73ed38177a14fce91eac35926e391b3,"
				+"da8e098c1dfd4b688292bf8b74e9306b,"
				+"b1b59789d363455894b8d30a3dbe1990,"
				+"17f74daa226743b899e861b04d46594d,"
				+"bc7b9059a43b496eac704584685ac516,"
				+"b51149b9e6b44198acecbea869bdfe13,"
				+"66a57080bc384230a9d942a0cd59a33b,"
				+"a4351f9cb2d64e30b899f05d819bc3ac,"
				+"46ed52fe3a804c45830f04e8d15d21fd,"
				+"88bc6e2d10e24095b05605c84711056a,"
				+"3aa4160ca7934af5bbd4a7ded8afa020,"
				+"307598e22a38489589c5b9bb8669425c,"
				+"ec3b76e06d2d4ff38fc6207639a2d6ad,"
				+"c821563b54394f7facb2ea44182feb60,"
				+"8adbe91c439444a69bdc3de7100db053,"
				+"750318d2fbc746bf98c5986a1e11ed08,"
				+"78a38892738a47fb949bdf0d1b7c2990,"
				+"720cb823beca4b088b7cebc5b3abc2fc,"
				+"b396f941d0574035b1174fe78dfe2653,"
				+"9c80f4dd532f4867a630f0d5216aa840,"
				+"4d8e898488a14d5aa7d73dff2605f8db,"
				+"b1b9d5030bc2404b9840c29863e80882,"
				+"65543446c7e5435080afde98ec5852d9,"
				+"7e5f102da326495ab376083bdc0fcab5,"
				+"ce565e9f878843249f045883b2455fa4,"
				+"eecbaab91b704926ac3bae10c8f5ae48,"
				+"09d5e36d25ee4e278c0f51197004fd21,"
				+"ab0c431de8bc4b11ad7d95df1a69dd88,"
				+"9404fa0cd1f747a2a1294515337e1442,"
				+"6819a66dfa524d3786e21d83387b14e2,"
				+"80b5ecb222f14382a43b30b23fd2fe3a,"
				+"afd901fd0a7c432ea1071da05e9a68ed,"
				+"a465b840c90c4f8fb74894efdbadd991,"
				+"aaf7def40d1841348f6d62461ac03a0e,"
				+"9ef76efbdc184e99b309c783e409cf13,"
				+"08ab89d3545342c1a4f0e0db625de963,"
				+"358430bd5c49444dab5a4ed6268184a1,"
				+"62f9afbd92844ac394a9f13868129dca,"
				+"0dadf524a0bc420e870424aa75f924ec,"
				+"775a24932f5440fb93abb170e5029daf,"
				+"c624f3829f05415aa3c843dd0d5d7ece,"
				+"c55ea5d4e3ad4d7486ee4639b111e9f8,"
				+"0d8c08ada905433cb90f675a576ac121,"
				+"0d9ac2bf69904844b6c87bd7517c5014,"
				+"c3ba8bdc057a40ffb4e59ca48c9e81b4,"
				+"ac3add9c05a448d2a8c74bb0c43381e0,"
				+"5b8ae879c26547808ec4372369a57c4a,"
				+"44947fcc33264cb58b7147c01455f4f4,"
				+"1915e431588e4ffeb9bc236cfe62638f,"
				+"3134f06fe56c4599a9f808160650cf3e,"
				+"f503b7a8c4f34e50b25bb6614db58393,"
				+"1fb654e01b8445ec9a796dc2bebfa70d,"
				+"df9629861d864fe680ab26b8fd3b8ee8,"
				+"904780b25e1544c7a18586393d91671b,"
				+"0653fc97848045058d5dd7e93795b626,"
				+"de486eda82b749b795f6af456968b5fc,"
				+"0afd45c4e4d049688b5ca9df2507b1ef,"
				+"86e0a8a4fe90417a8c540c50bbb7e417,"
				+"014531f5c3744e56b218a2038c39f0a1,"
				+"e7e44a9a27304e32bc7f0d5cc8ddbd6d,"
				+"ef9d6ea54eac46f6b9b145e1217aa957,"
				+"47a2969db93a4ef0b1cb1ffce7bed9fd,"
				+"31051634b5454727a599285384fb3a38,"
				+"2554a020581d4573bb304deea1d7f930,"
				+"86d01255c45749c29bece9ac3b47c103,"
				+"55a128a71efc445294e6d250025f80a2,"
				+"ca342552c457421e8ff83e49abce8846,"
				+"0e53e5b01e9c43db8e6ba00577ffcbb5,"
				+"444711731eb9449eba96b0fa2fca931b,"
				+"bd12122aba8f4fb38fcb6e302995f986,"
				+"a42262ebe1a546788d909f100cdccecf,"
				+"7a1804c69479490baf61251adbe792f6,"
				+"341af16f4fc14e33b74863a1a54f8a19,"
				+"b23c7a4bfc1c4e72b621abf1e20c8b77,"
				+"8573f847312d41e48bbb220492212d12,"
				+"a722d363813a4c6d9daf7accc466d902,"
				+"3065c4a07ec8445cae6a74e634ebc414,"
				+"bc0591bf3a684674b057e710e1598ff1,"
				+"b714d25c883446ffae0bbff70032bc89,"
				+"e4b8b4b4783a4c3c968b2bbf2ed808df,"
				+"aa6390ce833d4a1f92d3043c9de834b0,"
				+"f8de022b0bab48f2aa1a212b43393be3,"
				+"5a6a2501039f40799c5e873bd9e6c470,"
				+"0c66d8b1350248f2ae69fa8678140ea4,"
				+"23db94c82a674d07a694fdd28ab868d9,"
				+"41ecd067fed74f1b9acdb088bf3b1030,"
				+"c8a8ae86adc649dcb8f9458a7f49fd07,"
				+"021379ecc8dd44ea9b8ee7ffcdf89e9a,"
				+"4ec30db149644039a7247818ff445cdf,"
				+"fe9926992d284535af01fab4daaec5f8,"
				+"69f8e19ba30c4249b5f71931e15c64f4,"
				+"75cb7b4bc7984d24a5582ebaaec4d5fc,"
				+"b92f080fd2104cc99ebc2fef01fbe2ef,"
				+"5d43702d66c54ee68ebd3732867e876b,"
				+"5f766f44e0a24ef0b25e3e02233339a4,"
				+"a9409dc99e594c42853561b4d44a61f9,"
				+"de2960fd23e94d82b9d5c4f2313328dd,"
				+"fb887b44de6849b4a395b73ba124b111,"
				+"edd2ea9280b84a37b9107a70a0ed67c7,"
				+"4c0af13def6b4a968ff3000f8302b0b7,"
				+"71915f2ceacf4c399638d6943c1bc942,"
				+"d5000104a4f0484890d4d81615a4e1e0,"
				+"a25d4ac5e61a4582856f7c287732a657,"
				+"39e2da0902144d92b0c56d49417524f7,"
				+"069c35c9efee4210a3233bbccfb14a96,"
				+"aacf6b4578ae43f197d7445150412ee0,"
				+"2a49a2eca4ae455abebeba2ad88c59d3,"
				+"03392080392043f08c963ddddbab174a,"
				+"2fc6e6313fc7498b85efbf810e139521,"
				+"a80ce1c66b2649378e6d210e672b2140,"
				+"97c91eaaa24542a98c08bba5e972bf71,"
				+"9baedf460d694584bddb8481fafb8099,"
				+"347109bd73464380b32a69745427bd8e,"
				+"7c1e46a12ce34a808784d77997e322c3,"
				+"e86391686c3a4c22b01878d11658c8a4,"
				+"b0f3dbb3332f4c329743302bef240de5,"
				+"b019d01cd2294b0baafc0ec393675bb9,"
				+"089e0a9b5a0d45559dd4ec4a78570307,"
				+"e76843f93f434e558a9552b09c943451,"
				+"ae94228a89e54176b4d1f2ffa8cb5390,"
				+"6158c30339274a2e8d0a1c89a7404c38,"
				+"b31c7934ded9414585596363ac7303a3,"
				+"6d172f097cab416a9f045b623b382ae0,"
				+"b28d47b492864cd3b368481a81d31fa2,"
				+"f0e6d5d0d3564f91b09a3a9936b3d9d1,"
				+"5b0a8d90361e4c958b0dbc2694632afb,"
				+"48b4ab7337644e25b7101fec02be4edc,"
				+"afb6b4f2a23540ed94ec4bf9eeae01d7,"
				+"7067e7996e56450a8a2b996b0099ec07,"
				+"c76f8ac527704f75bbc673e9a9d4a00b,"
				+"15969f14b65148c7bb6480bf34ec5972,"
				+"027a8fb318ab461cb9abcc2afc05d43a,"
				+"2acb69504a9a4c4bbed0796a957c49f0,"
				+"1bc0839121724599ab374ae634bac0d3,"
				+"703ec8dc383649c9841f7dfae7333c24,"
				+"e4fc073d43f741eca7c86ddb19f9cb0a,"
				+"d11ea7bacc1847cbb6f333e18af74c4c,"
				+"993c0756b3f94c608ae72b3841eef967,"
				+"ff7ee7ff9f6d46a682208ab4008de7a4,"
				+"696fff2e61d64413a0c0d195e879f8fd,"
				+"e9d8db71f1c94cd3bf8f7b0a27782240,"
				+"36aa530190744d4bbe26fde5d43df473,"
				+"3340b3f6eb984a8589067770b3828c5b,"
				+"d6e520c731784078b91a6f3281ed32e8,"
				+"7f2d126d352646ad985fd5e075b5df7a,"
				+"3c5e10b3bccb4409a29a0867477db18e,"
				+"2494f3a34ee24937a5d59f2ef99450e2,"
				+"be8ab0a2c12642bb8fd7cc11beb65b9f,"
				+"f0b3ad8ce8414262a3caecdb5505248c,"
				+"a20dd372b15e43e886fdda50f8f353df,"
				+"aecf9102fb9b4945b70e0e08423e50ec,"
				+"ec42e5b5f5cf4fa59573309432868264,"
				+"183efa10ae774193aad503d4df26b391,"
				+"639d986abc1b49078a7136c32f07d4e0,"
				+"1ada1b158ca74474b1a902d318bf71fe,"
				+"c7ebf03e069c472484b5fa5b6bc69171,"
				+"641fefa264c8425fb391a6263aa0c9ad,"
				+"8625163c717a46bd8a8a9bbb204cfce0,"
				+"d4150bf28b25409588761741751b2ca1,"
				+"a89b5c85873d4ed9b839539f13992fef,"
				+"31a4a731496e43b3a11a253a0342c64f,"
				+"1f4b9c1dd5624b3d8f501104b42663d0,"
				+"6001820a97644b6082b5ebcae8b7dae0,"
				+"8480f302ef794c90bafaa7fd04a5aad0,"
				+"80c8ba9a40be466a90394c212445218e,"
				+"85f166b8f8904ec2bdbaf59a7eab0559,"
				+"3c7e486de2dd467aaff0f18fbe0bb82b,"
				+"e1b82d5ff3dd4718a5292b81dc0589ae,"
				+"c74efd324b3243e995e498d652a617d6,"
				+"739b5bae696343808d769e2f7c392a95,"
				+"78c42915000d457f8a222e8eb415e6d0,"
				+"df20fb2dd6014561b573dad4c95a4bee,"
				+"bbfada99c07348a6ad5acffddeb01e71,"
				+"8fd72583eaa74b39ab2766aa342f51e4,"
				+"39f93c821885488aaef64406f685f648,"
				+"7e2cf9a44bb146e49ff96032378b42b4,"
				+"7197c22fe13c4e67931de9346ad42430,"
				+"8a454199339542598ffdc29650b173c4,"
				+"46e3101becae45e799ac95226785f6a8,"
				+"51677b8c80084a79b6105be87801557a,"
				+"d1abe36f9a754cecae1d07481ff1c020,"
				+"e288c8dd68dd4e0594ea971600e85f8b,"
				+"e30599f4715841b7b7fb384c9a109707,"
				+"3d920112de64427a99313c15284f8471,"
				+"6a66ed73624741659fcfe5c8d9585b9d,"
				+"2aa617e3d3c345b19d079e599406ec87,"
				+"5212d0898aee4cb299b0a37a305d7c11,"
				+"7fbf1091526343e6986a7ee0ecb1af72,"
				+"f6b7c3e40b6a4c4a8ce327d5dfb677c0,"
				+"42c7bd591a0046cdbfe64213485dd652,"
				+"0ef1088800f445af94afa64e27706d19,"
				+"4942ea843a9f4c54ac6f46c7a001bd96,"
				+"d5cd67e292024a5a99c1a75c8f369252,"
				+"c287798c7d634217912a13ac71f199c4,"
				+"73ac688c01aa4e6f8c7de471b2fd7a2c,"
				+"43b5047eabcf4c6188599f729dae227b,"
				+"e75443f1574543ce9b203066a36ad3a1,"
				+"7b1d51eb21074709a984ff76d7d2951b,"
				+"71ab2bde7c094839926f8f6747525a56,"
				+"ce07f635f5f14fc3b161c6bda0602787,"
				+"9ff2eb0e36c14cc0a8e39b717247a88f,"
				+"21d3c589953841319d0b15c5c9b05b4b,"
				+"d16cbb217d9044a8a2a980bd7c79f489,"
				+"b7d2aab7fcfb43248498eda3814ab376,"
				+"2c9b3ae2809946fb97524a8c96eeca52,"
				+"ad0354db69414e819074bac034f82ecd,"
				+"a3fd4a03c37c40338927fc5e6b0336cb,"
				+"c3d054d46fd9444481bfb5d56ef8d393,"
				+"e88f4880febd46958e46cd306f31dcba,"
				+"45ebbf73aa31421794ae42f6ead51fe7,"
				+"c2c5214d958d49c1a5f56ed68484655a,"
				+"a9c2ceeb240f4aa59549f0e5d8af05d3,"
				+"466952499a28481e85111107fd7a78a5,"
				+"5ebd8185ac994890b8eea14795f9186a,"
				+"4f9857a82494420e85d721d5d70479e6,"
				+"03ed0b9bff104f43b19ba7ae18ef93ea,"
				+"dd1efa7b1a634345bcb77801f9754a42,"
				+"5e8d58d23be142f1bea997569d6b2a21,"
				+"972acc2b47d641fc99435d17e2664c98,"
				+"f172b0ba6f9d4166a88e615120588869,"
				+"4c3622308cd94b9aa362c480ba51528d,"
				+"ad30b6f4021948be9b3521db0761a284,"
				+"5cbf496a81fc4752a8095ef533034129,"
				+"c7d52f4b6d1346ab96b38f3d35095994,"
				+"2de97517f20842c7a99261ed60496fb5,"
				+"c2726535a7e14e33b08f4e1cfb63a369,"
				+"790386bdf23f49dc8cca7ed0acc36d65,"
				+"1e7949f2dc5e492ebeaac89c8bfd4bdc,"
				+"605455c738b6446fb557049fba150e5d,"
				+"1ef242024d964d6a9eff63467b7e325f,"
				+"0aa014b0137245e588dd4afad07f3e2d,"
				+"359184fca7bc4c84b13f47b200be8871,"
				+"5fbfd2adf41c4c0885cb406d62c0fbcf,"
				+"a9c806f95d16465687a822411f9225ee,"
				+"0d7e32d600474dc5a44d39eac382eea5,"
				+"b6f9c0beafba4d20adc7c2fece7aff26,"
				+"d7b2ab228fcc47b1a8b4fb5efbd27dcf,"
				+"861c1df4a009497abb7c99a493daf82f,"
				+"0851235b6e9d4796b34e55e352ae61eb,"
				+"ebdcaf239a504cd1bc174760c69455a0,"
				+"3e5d854e7011462ca65d3fd299d7978e,"
				+"1b6bd6d992ab4d04a5157aad8c7b059b,"
				+"17f4749baff341c89e8f59d42da06153,"
				+"8516b865cae04d82a4637c8eb63f26a7,"
				+"f9f4f8271bb14de38b53e2450cf6094f,"
				+"d58854d70777452fae0f36a3cb5477c3,"
				+"f73c79ddafbb485e864d6d369b116c20,"
				+"3cc017431c0546d2abe494f9a94f7ab9,"
				+"23aba469e3b8435c84c520d710e4bc2b,"
				+"6f5a9511bcc340f3ae43637186aba573,"
				+"1436b7126ead4daaba865a39e2aaddd6,"
				+"1a82eeac14c049c29807872b0e4822a8,"
				+"84355a3da2224d43ab2f55842ce65933,"
				+"15393a14cfd745589efb28e9cfe48c51,"
				+"09e7af5ab2b443a99c06fbca55f3e51d,"
				+"cd18096c4aea4815b6ba350f12b62080,"
				+"ce55c857963048a3b171108b2f234cef,"
				+"f00933b6acf8418bba64d5407b9b2339,"
				+"6fb90912e98649fea6676543bb5850c3,"
				+"9185d5a2a64a424a872e4f743ca501b2,"
				+"fec5703cd713466bad34ac53497cbdb8,"
				+"c8ab90b03ef34bcc9cc491b9e7bae44a,"
				+"ccb0a2d6608249ffa231ca4422c61f44,"
				+"9fabf68510c4413d936ef0e12a2b5a48,"
				+"c2131c52b681431c89d0812140cc916f,"
				+"89a1e5a4886d426d9200b4692d558c66,"
				+"cc6369732cb94ad1949dee74b5dabddd,"
				+"e6686ad1c903476e91e8eb9b35f59582,"
				+"a6b2dd6ae6ca48e794c5c42c23622acc,"
				+"5e06f3f29cf14c07ab7482582bee148b,"
				+"f62bee0f40134a0483d487c16e545d58,"
				+"31e7d7af776f45bfae6baf1dad4a3076,"
				+"3edff4078986499994e0626acb75b1b8,"
				+"fd9e0e4504e54e048a10e25ae010fff6,"
				+"ef8060888dc6488fb240b8a917fe475b,"
				+"34a2f09a617d4af0b6afe5e921af013f,"
				+"3948186c8e764e49a700d688b2017fbc,"
				+"4d6f9bba3bde49a9a6125a817067bbf6,"
				+"52e926e023b048ae9af55e9a9f9b4ff1,"
				+"75decff4efa84113aee87e7caeb979ea,"
				+"b0064179441d4936a23d846c8ca52ca4,"
				+"9904c30b24664946b39a016ae00fbec0,"
				+"c5e64122f7bd43a8a455f8ad6e0a906b,"
				+"80a61baee243424ab98180a5234b6d6d,"
				+"8ba42c91ad134732afb09feda7e954d5,"
				+"72e8d00e4e0c4d4abcfa50491ca3fa14,"
				+"ddcc3b0807514fac8c6e81be4ae7919b,"
				+"f40b71ccfb8c49c68c768398a8199948,"
				+"c29dd19fd8d34fcd91f035cd244a2e58,"
				+"acc33abc97514cbfa9a31dec88817bee,"
				+"dbb1ffae2b3741579d51e08b93a41ee2,"
				+"ced40e7a5d06485180ef423cc375892e,"
				+"e01d2d3bfc0e4eddacfc7eeaa2e8669b,"
				+"a48205d316a74bf5bfd438b2753507df,"
				+"992c595abcae41869e5c526159eac1ac,"
				+"5ec3ed0b6776469183dae42bc7b131c0,"
				+"d19b4826e066485a837228c72ac07c49,"
				+"f8ff5392762040ad99b7068bc1b00596,"
				+"6542da9ef603467ba5221ee8f81f3a86,"
				+"6c7f28af156a470db7ad4bf184880c26,"
				+"a7e41e71193d4db190468068d789a5ef,"
				+"d080063797c74cd1928cba1ff304fd8b,"
				+"88447fb5b7af4cc4a859f0454738685e,"
				+"2bc9a12763774ad6a4ae0fd5f62576e9,"
				+"45dcfee9188c43f6b4cf990a8cbcf497,"
				+"6a0f7caf65464f65a131fdd5e0732028,"
				+"33e307bed43b48988b5b1a830c9c84f0,"
				+"bb5d49bb5afa414bb740d1fe3086c27f,"
				+"2d6f910747e1444e97edf0f083f8348b,"
				+"4882d2eb145c45b6b0b93c9e2f3a9bfa,"
				+"0584274fe7654d658b7fbac9a1a15079,"
				+"c9d2d8d1c8a048d5bc1d245d931afbca,"
				+"81ff27f2685b43329d9d5a844c588eb1,"
				+"d3600706fc7d4273bc30c506e1496f8a,"
				+"0e32701fa6454ebca4b9c87754575a61,"
				+"9dc2b8865a0e40cfa0e49239e227c57f,"
				+"47e5497cedbf44d59afc24c810019739,"
				+"f521291942df4f68a686401ab7b5c70a,"
				+"549ae5b09f6b42398cf151e49c02819b,"
				+"c0d98d34df4745c787ee70878f577347,"
				+"939538f6467246b097df8c21637dc838,"
				+"a487f09982ac4cde8a1da5a0618eb836,"
				+"3634b7510fef4add9f4af2218a13d7fc,"
				+"ede335612e1c491f9ced26fe17e6c67a,"
				+"d88b3be7765c4f8fb99dd26572ab3512,"
				+"ddb9f5f251b74748b0e93dd1ba3d9d11,"
				+"eba9b2fbc7f44bcd9778412399b4d7fd,"
				+"01dde2a2c0954ae0a1bc3041eb516cbb,"
				+"ec1a450a97ea49f1bf3b8f51c4f73c56,"
				+"2bfcc2fc051b4e7cb80d40fe442bf19c,"
				+"4ef3ca12952041099e4438f6996bb89a,"
				+"ef9315c5416442d3b0b64d076f89ecf5,"
				+"a9968cb7c7d141b993462e36778201de,"
				+"38230640b75b48c09b6143e2856d04e0,"
				+"52dabbd514554eaab8f00aa0707d5004,"
				+"16cd71fc78c04373a0c37ce0f09a774b,"
				+"0e1d11b0664440c5a1c721b112c40732,"
				+"229556689bcb4d5f8e74f0f805d4d37c,"
				+"ce5dc52a876f4d2197a9aba461b76f4c,"
				+"b10981efee2c43e593ec532b66f38f6f,"
				+"b2ac11dc68094f8185783120390bfd21,"
				+"f0f1efc97e194e849e8d3b1346159756,"
				+"f6a9fc28c27347809ae371486d2d6bf4,"
				+"5ed4d9c72a9e46e5893cb981fbf97ee3,"
				+"bdcf1ae04329411c953e5da9235c7341,"
				+"9fc60303399348ab8a9f5c351f77d257,"
				+"d665d9a0851e4d3981ce1eea70a054d9,"
				+"ebf3abecb28e41639043b15b8711edee,"
				+"1b4b18fb24f1457e9c0fd393e43d9e61,"
				+"fbb880e00c984918a308c833c4f66ebc,"
				+"3cfe63ae231745549ffa8aa1a0f2e500,"
				+"d4284771154b495fa65be1ddaf798753,"
				+"b2c9cb67a95945e8a1d7343ea52ae18e,"
				+"499d5a25d7184289b63bd3c758d36963,"
				+"78a997839e0f4e9e92330858318ccf1a,"
				+"f09f0139452d45d589d8406504455abf,"
				+"8a4c52e2d527490aae936a2ae40e167f,"
				+"ce8243b8dfd04d79ad66e13cc749580d,"
				+"f4fc125efb614a4ca480af94e4a49e1f,"
				+"2936b4458e394e59a3f074ee297d7e2d,"
				+"dfb0e00ac89749a19110acb8dfc2fd1c,"
				+"5f18e5da017043a98e04bf6e3d2950ef,"
				+"e9600b678f454d3f87091a667a9ae461,"
				+"63af1632fd864d71b316b24ee0364d02,"
				+"ab2d10ff7c7e47c0bf503d9b4c2859a0,"
				+"3a9f2374caae47358379953979843989,"
				+"e259eefb428244269ec470e0b28ded31,"
				+"1d9271f0f6634f4a998a7946e94b2fb1,"
				+"754a8e13b2c047f28f484f4afc22968c,"
				+"96b2dce51f654ba483d82312c1317e6c,"
				+"7babfe251fcc4f19acff28ebfc22e84d,"
				+"7217a76020e64142b2ecf56738f6ddac,"
				+"06275231651e4eee8ac8904000b2de6e,"
				+"82ff89f96b0a4746bed20e52d1e0d501,"
				+"4d4f289da31046e28873532b9b56309b,"
				+"52ebd6d70dec42ce9d51237b1039aba4,"
				+"d32f2420eff34d499f9b528f8f12ced7,"
				+"33988bef683a45dcafa7151ac99f4bfb,"
				+"75d90d0d5ef54b968044fa49fcfc3e74,"
				+"5e7084c88f5b49ab8b83d6c9c18dd6b8,"
				+"5adad1667acb491ba1e58c38b60da6d8,"
				+"cf8536cc0d90493393ec4d2c42ba6d06,"
				+"9eb6e09b59e54b778d92bae1c2c21f7c,"
				+"50d06abda0af443480c587cafd193918,"
				+"17c891ea71084bd2add6169b2cac018d,"
				+"5775ede100164cbb98642b956e07d85a,"
				+"0bfd15b9f51a4903b4974a92f17fd2bc,"
				+"48c3989e955448f395e35b958bd159dc,"
				+"f8c9ae0d484349b5b4d47772040959fd,"
				+"77083074bece43ed99afa04736f30c0a,"
				+"05746754add242348c2ea818d492c966,"
				+"ab6d86490a3044d18e497d0d4c67a845,"
				+"eaab553526874d569674cc3ba5fc60d3,"
				+"a561c14973df4ad58f918ba79773399b,"
				+"4c56420cf0a34203bdc5e738a2a09f00,"
				+"bc633fa39d4447169cc5af049409689d,"
				+"95e8587c35b344f6841dccb1a2b04754,"
				+"99eddb2fe2484b7689e0fabe7399ecec,"
				+"3de01238f7324f7c826319e98bd17e0c,"
				+"a2ac18940dbf47058d4556124a152144,"
				+"8d65a256a3ff4e85945abfccb7a70508,"
				+"4118d87f19554c88a248dc02282a42bf,"
				+"2421bb99ed2748a2b192c67360bfd7c3,"
				+"74b5dc1bb9e54eb7a042c43806f3259a,"
				+"3252c203ffa247ce9133902d39e192bf,"
				+"6a03e6077bbe46f5a0593a5d1d85e879,"
				+"e69573b052d34166b7b0aa3af14ec36d,"
				+"60c001ff64c740dab38e3826bd7576b9,"
				+"f0e7e59a383e436e8f58a05366863274,"
				+"132652a67b0a4cf182d8ce5684f47eed,"
				+"c31d081571b949d19218e0c2cc29ee7b,"
				+"96853cbb6fab43e7b894cf4a5deb425a,"
				+"98d0543af97949159c922e1a400a0d0c,"
				+"c58fdb292170407abb417caebbde8d51,"
				+"efdc3ef8cd634708adf0a6e8246abaf5,"
				+"6406b07b21034f80a65700f0660c045d,"
				+"0dcfd5f689e248b189a089fb7f2480df,"
				+"7d1552548b3141c68d0a2ee865f942ac,"
				+"f0352266dd2e4484b255979da934688a,"
				+"38bb56cb68d940c68691679f23c4a943,"
				+"fbe8c190e22c4981b1c2e7fc6494ec6b,"
				+"de5347cdd6464a6199ddd58b84896c90,"
				+"7e0dc68297454f3fb2dbe49864223515,"
				+"e748ca0b30de4d68bc53053f38cec047,"
				+"e067eaee2ea64478b1e19f131be3cee8,"
				+"a51890eb40a54f6db60268c47ce106e7,"
				+"0694b413b28c4249a5aca7f9f3d05b59,"
				+"172884469a204bdc8b2d7faa46b7af79,"
				+"185305ddc61847bead2ae45f595647f0,"
				+"c003089a9441449f823f8f52ebb735f9,"
				+"f9f7dfaa82094547a363456e78f39b26,"
				+"5267aa8d566d4ed08e9e0bb64157283a,"
				+"77ac97c921af4d2c93fa4154ca744b17,"
				+"25cbe7ceac8343c9af1bf952d35313fe,"
				+"099580898a29406c9f631e3265630282,"
				+"1c3e2cae890643b49663ce0bc8c37de5,"
				+"e2212bce75f64257908dea66015e945f,"
				+"c96cd108cedf45439a1d58a35803ed3f,"
				+"8ecf65def00c4068841287e189c2a1c7,"
				+"0c03c1f2a6244174a6f37d620eabe265,"
				+"4c920341d9bd495e8878489b6b409b75,"
				+"b83ef99fc9b64b69ace86496e1f735ec,"
				+"b59df7cfbbc24ebd8680fefc24963a6e,"
				+"6c96ec634aec4c8ea5358b0b754e9279,"
				+"077ceffc82e7477bbd594da5ca69884c,"
				+"fe76dcb7b1fd4e51a4abbbf02beec10e,"
				+"b43c5c2e3ec141c48e27f0d1f7850fa9,"
				+"214836a9888d4d589f7bb558774a5bea,"
				+"57679983c6724417936302223ceec273,"
				+"9b6b63b407704e16baf17f63428c8de8,"
				+"bc0fc7019f7c40ff8ee10068dd210d1b,"
				+"9d0e6f97867749b1b5a8e5073d23cb23,"
				+"c95f7fab80e840259a0815d12b0a25ba,"
				+"649eff5f252c434781aee67e54ab3f57,"
				+"713bf0d364b04fc0ab01d5664a2598aa,"
				+"198c6e2dd92a4314aaca853763f5eb48,"
				+"20232b28cab24c059febd8ed450c28c3,"
				+"7ae65d1cbaf5406eb7929fecdfab1683,"
				+"ef8b90c8160b4e4f8db2e57b582c2199,"
				+"c88a0781350143bd91deeddaa64bb9d2,"
				+"45c00474f4a74570bcd36d025efee785,"
				+"f4ab25651b2e4b8cbc30f1340df21d95,"
				+"ab586efd01204b78a604fa2ad57e8c7d,"
				+"707d4149eb2a497ab767a356b60ccf37,"
				+"bbed1dcaff0b4a6bb92c4ab12b0117e3,"
				+"2f7e3dac6977418082534e4501276089,"
				+"bd2d654a69894c92a84508ed0be0b5eb,"
				+"0edabb23afcc4a8fb58c2c7eebbab54c,"
				+"593ccfbf3a4f4754b677a89da0315b9d,"
				+"3e8e0cd92ff54fc28e4f1d17e0c1011d,"
				+"e19820774d1944eca58a67fa202af8e6,"
				+"47f0abc0f05248ab88d46e107552f0d2,"
				+"17c21ca19fb1438a81546d3d5a373538,"
				+"70b0637284f840a8bb7aca9f58fa7422,"
				+"dd0e553772274e1c9a38d1a0c064aac5,"
				+"0d0db01073924d14a05cbbc7042d2313,"
				+"0e958071c46142c188572b43609b973c,"
				+"4941fd1483a642828630088665f0b49a,"
				+"a25fa84ad1794cd690977d12704f1f35,"
				+"9252435626ef4c64a1c8be5ac94776b6,"
				+"5459049cff524f9895e2e6dfe630e6b4,"
				+"cc6723cdfb124afdaf3a9ced8ef4bd7e,"
				+"c037d1c4d50a427397c75f8f63065253,"
				+"f150fa5ff6ad4a6b8b7ff08681cfa95f,"
				+"feec98d4206e4fe8b12f3665428a9c84,"
				+"c31d52175a4a413696ab9ac1d3993127,"
				+"4f53314452e348d78ed80d432e93f160,"
				+"4ba90c5aad3540cdbbbd412196c68a31,"
				+"811b3f325f85497fa4cdd601c224d9f2,"
				+"640ad1eaca664c1c8fbca92ba3c44158,"
				+"ebab3b25016041b383512590a770cff9,"
				+"66531ae7bc084b96a852a9ee28a11ac4,"
				+"cdc7a751ae24498f894423b7613a45b0,"
				+"002ba819e7b94d2faf094437c51caeed,"
				+"85216845355e45fd8cb22db7c74d8a1c,"
				+"840b5a16b83e43c39f5d11fb68f2fc2b,"
				+"9dd51a49ec5a43d79daffa28a592f849,"
				+"ff9cc5c16f0d4acda3455087cda0ebc2,"
				+"786d7d39765742da80d49d699f984b5d,"
				+"041f742b2feb4dc8b74318f814e123c2,"
				+"eea85d6bf04e476ea138f7f148f99be0,"
				+"13397eefae7747f293c01decb1011a30,"
				+"bf7b3b90346f432698d80510d05170e7,"
				+"04b98bdf664e44f89981d137ce9e37fd,"
				+"8c40bc96677f4d0bb878fad8495f7575,"
				+"9dd4328e5e2545c594a9d45f20bc235e,"
				+"49d9260fe3ca4501be114e45055256ff,"
				+"30e1c7ae7a2342049ad50142861a43c3,"
				+"582f9100d58c4079872a7e7c27a0d9fd,"
				+"35d1c4ad38e044249679619baae2cdfa,"
				+"dba2bf04ba5741f08ba96bcfcb5ba007,"
				+"bdde40a4f2ec43c3a44d9a4831abbc48,"
				+"ea21a701b5634e2a8195e3da322d2082,"
				+"0f3492ed7cb04cb880e7ba52bf654d13,"
				+"2d3cc5700a054ffd9d987d2c0af572ca,"
				+"770f50a96ae040ed809e9928a248d80b,"
				+"de51e999be4d472f9674f1bacf9d5f12,"
				+"fbb092b1b6bf44408cc549d8fb85e07a,"
				+"63971afac88d4e86a1443724652fdaf4,"
				+"654920472efe46b684a7e45f08373325,"
				+"e1063133333040d4b1032b9077c9f44a,"
				+"eae1737cce40455282539542c385de87,"
				+"82ac094c18cd49c0b3dd1f38fb67294a,"
				+"fbb65c45b2c54cd8beb7556bf09f90ec,"
				+"9cd99bfe3b4844069f0aa4247d051ef5,"
				+"914415e894ab4e96a0b5f556c50d0446,"
				+"0fd8da5b1f744ab7b5f524c9a0365ae8,"
				+"5c90438a179043dfaff5f3c0af6ded85,"
				+"36457dd32de143aaa1843d4cc516056c,"
				+"a31ea38ec06f4b87af7c0747cd56ff3d,"
				+"da76c86d09434c7ab54833310c2a541e,"
				+"c92cd8df6e9c4194bc00881ff1cdd97a,"
				+"f648f806f26544ebbfa4faf7835fc23a,"
				+"62935dd796d5479cbced422c5452795a,"
				+"9bc64ad77b2c45c28f863a42f3b7a105,"
				+"d5efce17917d447d847d01afdeb3648e,"
				+"9c875b82893447459c1de8242ffa05ab,"
				+"73170eb978c1490d91a408a867329020,"
				+"f282eea2147b41c0b9e37f8689df28af,"
				+"0d16c2c6fdbb429881821a5d7e999392,"
				+"ffd137e99ca143feb272a15121679e4d,"
				+"1a988cffd06448819a363bead2578f45,"
				+"88d8d35c1727447c85f49ce198ddd802,"
				+"30d8c757ed7543b3a97d9a53ea086456,"
				+"22d91fbb88b841ed9e593105b1afce92,"
				+"9f8140a6ceff42c2b4e9a6032c88991f,"
				+"dec9240dca8846ed93a1db279c3811d4,"
				+"28e40c1096464a80a97ee340aa62452f,"
				+"898a2925e4a746f8a6a4332e85440833,"
				+"a764d1d4cf184a249b9d4f3f2e694953,"
				+"12aff69a79434376aab1f8832dbd00d9,"
				+"918d5993472f47a5a442a096d8902056,"
				+"71a59202ea2f4450b5ee7f24d48ce392,"
				+"196d7b39ed8444d881856ba2d8278321,"
				+"28cb8fd86e2842bd987f951b591f6ed6,"
				+"c7ef24fd546f4c999f55010f54e5743c,"
				+"74c9b0938b654117a1e5ba7a4165b09a,"
				+"cd1718c4ed0e4ea98a47aa7279922f9d,"
				+"3a8127cfccf24f939d10775ad8e97ba8,"
				+"b2803ba4cab44f6391a7033da2b1ada8,"
				+"b5e39647f3aa4450baf870174eb39cdc,"
				+"6dc565e289f041c5b7fab1b0de00dda0,"
				+"a19c335798754667aa41fff2f0c93a25,"
				+"0e47c84fdf9a41aaa5eeee1f82425a7f,"
				+"dac20bd0480b4be88aa19e395558337e,"
				+"7b8e6d831308432ab084e6f1cbb11565,"
				+"6b5a248f73be411cb49ce67938d48342,"
				+"32c70d3712bd494abcd8ba4bc5fee825,"
				+"8dfb93a8947940758ff30f1306d6df0c,"
				+"fc2feb7ef4184703bb065ea3f66bc5a3,"
				+"2e790dc2c2e442839185edc9a1a24a6e,"
				+"67d4871784f9490ba8963579ffd7ba0f,"
				+"e46a4185a57e44ddad7e0a3889867348,"
				+"b5c7ece252b24061a54be972c3ad56ea,"
				+"77193aa91ec1442f9eacafb9e1ecec12,"
				+"9ab82ecdc5b8476498a0a268d27caef9,"
				+"62ebddbc4b5c43aa8f206523dfb4c3cf,"
				+"298bcf84b54e4655ae108e26ee65312d,"
				+"52364636e9b94ef88cbb9d8a13631674,"
				+"22f7ddaf67b8485fb42043d32557828f,"
				+"51f3b733bb8f4fd5b2f8068069cc6b14,"
				+"ac1c8404a7f24065b485b2befb5f8f9f,"
				+"7801dfe51ce44e1e8aa7819cb56c6dc8,"
				+"f64779a0e5e94adf93810780dc70e6fd,"
				+"cd25325d2424457d9f01f54b932f4dcb,"
				+"08a2863a1613454bbe52280be5b032d2,"
				+"6477259be0b440f2a571bd82513e386b,"
				+"4dd5ac6ef37f4995aaf32641f37f0a92,"
				+"c3741d58605945ba8745b4b7ef8ea78a,"
				+"24437fedf59b42cdbe0364f2c9c8929c,"
				+"1bd496d28928407a89b1ded2d12f2ddb,"
				+"05e5fafbf11d42e1b6b1f89aab2991fb,"
				+"9abe0aa521b54373bdea28e68dba7215,"
				+"36174507cfd64024993e6b6ac7b28d18,"
				+"f477cdcdd4314fa7a1cd2380c499dd19,"
				+"495be93b0f27413c9c09ed93889421e7,"
				+"2fe2f0730dd34e44be1ea483bbb3cb19,"
				+"13fd499bd24a475e8e68da6f2fa608fe,"
				+"c127c44ad9ef420683e06dc5234878e5,"
				+"0e18a8b3b7d64d29bbeb92b87a1f4809,"
				+"1f10937e41ce4c7b938a9c35a0cb2701,"
				+"50411f89a7f9432fa0ca17d8bd303308,"
				+"99d92495ad834f06a8856b43303ba635,"
				+"a2f6759fed99423990dec1051390ac9f,"
				+"a5ef458c1f3e47dd880e4958b447e2da,"
				+"7ebfbfbbe14a4d3385afe8c2d3d4b231,"
				+"edb5fe50c328459db608025b5b7511cf,"
				+"be5da440fa4945c385e33db2da9f92cc,"
				+"6c77aa3f3662421f9fac44805c20a7db,"
				+"293b9ef73e9c4873b61b5858efd5eee6,"
				+"26c40fb25eb44045a395796cdfbef7ca,"
				+"68d3b2085c814f7688f554903db79489,"
				+"4fe0b61d55514cf0b4b84cd29aeb5d43,"
				+"7493e5982d8249e5aa769b3c2cb09c4d,"
				+"1d843d02b86e4d79b1aca76954e482cb,"
				+"fd35c7b5e8134b4f92ea0fdd9db39085,"
				+"9a1185e5c58d4be5a83d10910e2f4d30,"
				+"a44f2e373e1d4290890c5117116a5ba5,"
				+"becaea2982f24f858a344db84684f6ba,"
				+"5cfb86b6c6f9475eb2d5baf59a56b330,"
				+"121051e1406346489f721076ce80fcf4,"
				+"f4748a572a3545a19121abb7eb51de8c,"
				+"0be7551de7df4cd29131f014dd7f6d07,"
				+"249192b838b34d069f642144229995af,"
				+"0d047760bc174602b7de43964a0dd1a3,"
				+"ac22e94b855349f0b44bc57d94429cf3,"
				+"84101dffaf3c43149df46234cfc5d25e,"
				+"2b743ce404af4fa882236d1156c912ac,"
				+"5bb92d1a18ec4557a01dd46c77b89b57,"
				+"fada6f8cded649769318bae3a502fe5b,"
				+"81314a2f05ec40c1949495760614fb02,"
				+"15f7af79f27b4f7bb2ecb953b107708e,"
				+"7f42d8c1e7dd49b79161ef68e57710cb,"
				+"867829a1f03941ddaefcde72af2b3c3a,"
				+"2a8ac82cb01a464a8555e6010537b8f0,"
				+"c04cac6cc61c4236a871b1ab31fffbb6,"
				+"fc4a53937b6541e2b95beb05637171a1,"
				+"9f460d1f2ca44242b60f55c9976d6ca4,"
				+"4088f8987f8c4d7f8c28b940d7598537,"
				+"ad6fe66bdc2248a4990a6cb1f841cd9e,"
				+"9bb492d80424427ea902a276f6d878ae,"
				+"63017f44155c45518c86c08c5e3b9262,"
				+"5d6191bb582b4f7dafce5018d1e58ea6,"
				+"84b8d675d74d453f8cd7cd925ef34242,"
				+"ad7b76f6e5134c49a1ae50586891adde,"
				+"2e4e4ccaf3824108a9e1ced5eef22e4e,"
				+"8b6f3ed4be3349da8c65733be26f1898,"
				+"b2cce820a6ab4bcea7b61bb3bf01c228,"
				+"0e03abac50c844a2a6b432cdd5a0f48e,"
				+"a2c6ab6e2007422f83166f2bed34aba9,"
				+"76c191b59b19419fb102a98d64ab6044,"
				+"fd07ff761b1c41e9ace8d783a2f1b97e,"
				+"bec9866612e04ab0b64d39a83ebe528f,"
				+"8ff32b5b736e45efa2ed04c28cf06346,"
				+"f3314c9e29534b35ad6b4174929dc4dc,"
				+"6b9d0467f536431fb358bd37f50c7e11,"
				+"9f9dac948af4445296808e77ea46fa7d,"
				+"bb6d9d2a5b984439857899aae2612bc7,"
				+"f70a8263b9d440bdb72d7993682e4db3,"
				+"b1935af907ff4d8ab5966760758afc74,"
				+"ab4d20672feb48ac9ad6bdba2ca17123,"
				+"e6122c7627d849fbbc7354e3f556f4cc,"
				+"b8dd60cfddff4309885d4302ea376c14,"
				+"de60d9ef5df8416cbc4f3139440be5be,"
				+"5df203078cf44057acd59bea250a7455,"
				+"13de6e1d9aff4d1285ca0c77d1b21bdd,"
				+"6fa7bf97d7c4493f81ddd41f759fcde1,"
				+"d95b5f4aee044c9c8425d67686cc6388,"
				+"738d4658130d438cbbdf9925cb897531,"
				+"90a23b79e87c46cca2039d5e67959476,"
				+"4c6a286bec0f4deca144a4d9ac1e8e41,"
				+"da8978cc70534a2e85b32e9a30941f5c,"
				+"4fd0f1a25df7408e80997e6c148a7d2a,"
				+"784227250b2e49c4a52d2a50cbde32a1,"
				+"9e63904c9f7048b3979a560746e653c1,"
				+"1cd6f4f0b64d4e29b3d8145c78acd4aa,"
				+"c123b3aab4de47e29107400fb8c4fef5,"
				+"a088f0dd00214d469a9480dc9c6191e7,"
				+"802860a7c5a1442a903999a54834245e,"
				+"83fd4b9f82a649eaa6ae2f795440151d,"
				+"61e0e8a3d1034c3fbec7b9de182d9cb4,"
				+"279e80ff4b5949819b92ed39af074c18,"
				+"70a71b06ba224fba96db6bebfed4e42d,"
				+"5cfa75a2f5d14a52b816b9cf119a9fba,"
				+"aa5a52cfc59f4e7c96f33c9775150abe,"
				+"a078f14b0407463dbaa94709b8f9e926,"
				+"aea292e89fdb47748dba4647e37d3608,"
				+"c077a245417e439f9d942a7ffaa1fecd,"
				+"285bdeed56794d838267f0677fde7df3,"
				+"ef48364a13bc4f8ea728658946bbff68,"
				+"0c8413853d0e4f2f83e0a3338b9973db,"
				+"1117afba11b74b948e938766303e1de3,"
				+"a95273ab8f8d46c98bf41a4a3657b565,"
				+"48c4953bc57e456ca2873bec79e894d0,"
				+"d12227fe6c9148bba7ce0cc436e668f8,"
				+"a7d84d0442614cf3ab333dbbef8cf668,"
				+"2885a1f07f6c40e186c9eb2793e4919b,"
				+"4040d199b34c4e688a3257b93da97b94,"
				+"b4ed4dcb0c134e0f802583f96b025789,"
				+"8658b40733214727b60179327807f21f,"
				+"74856273e89f47dab2418e2c6837ff62,"
				+"a005a1664b7248fe9802dd835af8414e,"
				+"d2eb859f8cc54bcc89857fd79bcbcbca,"
				+"5b9848f79b6c43dca52470db944b76e0,"
				+"62694d7f06f546ecb654d2773d8f250f,"
				+"36606d2e65f6454bb55be96e17c0aba4,"
				+"1baf05cc40ed4a50879eb3e84867a926,"
				+"f4f76c5adfc04f7ab350cc4898c11f51,"
				+"6656d0d78ca043a49defa45927b97dfa,"
				+"ee18405c35b1401985fe7bbbc00c5f65,"
				+"85d58cce3f3444159dac8a379be8e0e9,"
				+"e40cb2b2ff01485eb0f2092e3566a193,"
				+"e42d085788004a2da209d8fe225178ec,"
				+"38e0b7dfd1084472b4abeebba1e16f20,"
				+"1d72b63251904f97bcdd8bc8b5cc861c,"
				+"0ac6fb51749a4f2b8c671452f55ccc36,"
				+"fff1829ed9c94caa8dd1923ea6c9423d,"
				+"f1ef0e404f03437787d9fbeff2bd69a7,"
				+"ea88e7c18b954159a6c9a16228d3502b,"
				+"cfd5fcd1a3e84d3a954686d6a333ed8e,"
				+"1888f62316f74ce0ba2643c305c82296,"
				+"7214c5acbd894f63930ff8f2ecfc2641,"
				+"504b00a274694b0589f7a1704cd77d0d,"
				+"2ff784661df143ccbc2f8a07266faa61,"
				+"8946b897a43b4f6da63b3da9f8e8adac,"
				+"dc65c263e3104795884ccbc4fd3ac42f,"
				+"5c2a180832c849a98c124fa6c095f752,"
				+"536c367ea5214627bc1a1c2f20e86b5c,"
				+"2b4cdf9509a74688af81fd01616367e3,"
				+"a35a9973f13a4fedb5b067be7486c1b9,"
				+"54fa6426c5094f41a34a1dc6226bc21c,"
				+"23e8ab1f8f924c69b457310208c79dd8,"
				+"250f820752bc414bbc97039ba19039f7,"
				+"24cb3c3d35474dd0b181d48ad6883cab,"
				+"c8c340e8bfe94b6fa4b2b72ad7014def,"
				+"1c207b4f952b419880614691fc683c33,"
				+"3ba030f5d5414066a5c6d261cd52dea3,"
				+"036ce24f1d1d46a1a11e551cad2dd376,"
				+"73ac6407b23f4f3f87728686642bbf12,"
				+"1619e3249482441397e383aab0f7af02,"
				+"918cccf0568346948414fe01be865456,"
				+"b2c25fd8b620434e9dce1ef775fe752d,"
				+"35a4b2cb93ed463288695fdb6a769a0a,"
				+"77b6a0438b754b6b875adee2c7dffa90,"
				+"a69ea029bf8b4ad983df1de1257c7f2e,"
				+"af9ec3b3ffeb4cecb8e9ef8170ec4596,"
				+"5def25aba60d4e0a9e87dde6db92459d,"
				+"16da340abacc4d9ba7771515f17d54b1,"
				+"8084db3439b243ed88148554deed539b,"
				+"0bdbe547ce8a45348375a1e31ab6928d,"
				+"f9d85308269a44ab990cb06b9bd6a6a9,"
				+"69ee942869084716a52aa5358b471b4f,"
				+"2a044b0f061c405498f2a5faf65e4071,"
				+"642a2d58ccc844c0b9237612588777ff,"
				+"96fc3f571ff34083a142ee54f8b6f51f,"
				+"72844a67cc3c4fcabcf77ac20a8c2db7,"
				+"cb283a59d6644cb881c7fab59fcd5c57,"
				+"e6911780b2584912bd99d11ca97c5d73,"
				+"d9824f4b35ea474b9a7002e081f2bc45,"
				+"eb547b38d089453292e39caeede3e0fd,"
				+"460a0526adc04d2283d27eefe33e0100,"
				+"aa08e74621a8467981c3a426fbeaabff,"
				+"714ade5d4a034400959def0c49a353f8,"
				+"33a292f8bd904b70950f9403e3faa1dc,"
				+"5f1bf1c0b91c41e0bb475dfbb2da0675,"
				+"12c527231e914491b8cf3014d918ace3,"
				+"fddbfd4c3fba41bfb0e3bba1607a4fea,"
				+"ef6cbd2610db4d6aae988bbae60781b0,"
				+"cf4d1bcc674746f5a0dba7e0fced4d41,"
				+"9905990ae92a4f00bb05f3b50e0b82b6,"
				+"37c74c91677f4de19bb218c5a7132317,"
				+"55179ee440d143e0981b5d4fc5b5a59e,"
				+"51ac423b1f294f1daa363b386b4b3f7d,"
				+"c00978b302404bf6998615da773d9d7e,"
				+"9673b149f9f242ad97c7d6180e008224,"
				+"a6070950e07443ed93702d78392298cd,"
				+"386749edf9324ff0a4cdecb156dd88c8,"
				+"b6704e3663cf48adbd1a9a41bd0b1b4c,"
				+"8927b90c23e3468aaf93bef414fafc62,"
				+"1d5e721fd557490fb7d7b7124f718e09,"
				+"3c5d75513a5843bdb098b56fbe160dd5,"
				+"a7e0d56defed4ff19a17e04b060928d8,"
				+"4fd0c53a20eb49109dad8101e6a06808,"
				+"b537d91e2a9f449cae5184f67c3b35d1,"
				+"975f09f7e30f40399daed895a14c2f9c,"
				+"1c0bab4249704fd7b451584d333855fb,"
				+"950c8fa53e674c1c99c71b293c057835,"
				+"20098ed33ac64bda8eda5ace12b9f405,"
				+"66a708528c1746acba77d2aa55c05d25,"
				+"73d8ef2da2ad4dc292c1e717cb12c143,"
				+"cfc9dfa6f7274b1fb89bff1e711d6f56,"
				+"bf410263f3dd46c5982c46fa98a499a7,"
				+"12c48b3c3b404899bfc80b603d177571,"
				+"d2f9bcd606ca41f9969dce5dc833cbfd,"
				+"6e1fc9413139410f8b91e680149e7a04,"
				+"9484f913f6d14400a42c653318d308b8,"
				+"4a9ce96261a64cc5ad2c1076bfe291df,"
				+"8a44c693db534e2b90597edc45c4d2c7,"
				+"535f2b9f1f2e4b558cfbc1a682221ff4,"
				+"69a1d19268a444b6b16a863161ed7ad7,"
				+"3a971f160bc64563a5d020fc2971c37d,"
				+"847a6e3bef784cf1b0b3708247fecac6,"
				+"7b3a2ada75724dd9a8c822ca98f23506,"
				+"cebfb231d6ce477d9385dce8b9b6f16e,"
				+"0e16b490c40546099beb61016d49b9b2,"
				+"69d8800ef00e45109d33fa203a7a25e1,"
				+"7a4b3e46dbfd4c91bca8793bc78f8e76,"
				+"76d62ac2c3774c5fa65a2935f04df84a,"
				+"2dc0ded3c15a4ccc92f690a6f8b19b70,"
				+"cea48976dbcf414aa0d870e2dbc35cc6,"
				+"329dd0eaf9444b7da3985b1463bda966,"
				+"8109177c61a049a99bba60c64dec2ae4,"
				+"e0842303664f4bf38599fb8a4b568972,"
				+"92d5cccb8a174738b22d8e1bc69d9eb0,"
				+"464d42b4d9294ae49e7bf61aca09f19e,"
				+"3a1e68e62d224482930d85c146fbf459,"
				+"e79ceda3403145a980e0ce5d23b440a7,"
				+"5f1ec5be560b41f78afb1eaf7013fe55,"
				+"ca1845a8363e40eeb9fb942aaa638781,"
				+"158be2e74437438bbfc4e7837e74c4d7,"
				+"d2a360be7044476b82f5cc16d57e41cd,"
				+"b8e4c25974d04b21bde3a91e46cbd851,"
				+"4d4392d84485484ba07291bcc7075a56,"
				+"510ad5f0c92c44aab9125a27df29e8a3,"
				+"35aab39279a0409793c485a862e41dda,"
				+"1400214897a74a11aef600030bf93dd6,"
				+"dab68c58d6eb4491b4bdd3ca884e9a12,"
				+"65ac1faec6934b3c9023fe590d55a849,"
				+"16a9c7f4f3a54f6e9b7038bb3b2455b3,"
				+"a4ffe3a314df46eabc6b12b7692f0f27,"
				+"2087bccdf46a40e2b83b74382e4b3046,"
				+"ba42dd8a58d444b1b2c80db25c27157a,"
				+"1bb6a6d27ffa407e901e2e0cd75485c1,"
				+"a01bf4406b2b4244a8889323b068f3a4,"
				+"e1c7342a68884e31bf4d147fb747049a,"
				+"b908dc36a1924741a233189447e9f51e,"
				+"505c36384ee84f0fafe3c58958d7260c,"
				+"42fee88baeab47f6bea5233ea132902d,"
				+"b1001363b2de4a8c9fc906e3c765c241,"
				+"d6f89f11542840e6b5e20c833c6c9aac,"
				+"37b6ca2dfefe4e3286be56fe1499bf80,"
				+"20b7d7452b9d40688f75c9bc77f0f2d6,"
				+"2db9440996d1499c89697d0a853d9803,"
				+"997fbcc2b80840f7b76e7eaf90c90cca,"
				+"88fb63364682418ebe41b43df617adb7,"
				+"e70af340581148409fcb93a4c5658b53,"
				+"feb4a02f333e47e29b8f76c201ca3b1a,"
				+"20388cccb4b9445ca3125af05783dbf5,"
				+"8d439dd7e5f9452e871347a825dcf87e,"
				+"73a71adb769e4341900e760e1cf5f845,"
				+"6522db7efc094e60947496663c7eef73,"
				+"b8adbcbc3a2742bf91e2c64e22c09d29,"
				+"b127662a380045d786fc1362a49798c4,"
				+"949ec6e47d1b4566b9b96a320ab54f19,"
				+"23db5e2cc9084f3f8159addf4e7a2340,"
				+"2f28e7c2d69442c988dbf9a858a77a94,"
				+"44facb31543c436aa4aeb1f1f2f13786,"
				+"d0e1e2fa1ddd4544b2bbdbf03f04dd2d,"
				+"32b65cf9f8574bf3a19c5c1f55d4fe90,"
				+"3dac7e11f8e846499ed14d073561789a,"
				+"1adde1990aa64681954d2b3cd976bcc0,"
				+"d4152ff350324032b6425eaba207db41,"
				+"54e83fc173844369a3eed02915cbc390,"
				+"315aad9405d64a0cb8fa9dca28c78767,"
				+"3a278a95df54477d833d288ead71d097,"
				+"d72a19b028594fa1858b90df67e46833,"
				+"d919dd48642c4c09b28c1ba22d43dd0c,"
				+"0b846ad243e747ec84f0333e93f5c843,"
				+"bd8b378572944d7ebcf0e5d86ab42e35,"
				+"2ed79673f0204cb6bccd467fa344c09b,"
				+"b2bc0e06142249e8bc05da9a2dacde43,"
				+"7b8bf306839544a08cd80993b1052c93,"
				+"24e03639c6d845dc97db038647356a8e,"
				+"880b3c7a4e5e46f8ba3d4c616e1731f0,"
				+"fb6f481ab04240458dc7ddbf2ab44bce,"
				+"dbd48b9bde8741b6a462a274e4a3c7e5,"
				+"d425ec0006ea4e38ba58567ad6f069ee,"
				+"bb517932b9304267b1834ba988c469f9,"
				+"86ee82ed80f946bf8d7f06f1fdffda1c,"
				+"2054b45212be461b986281fccafd3505,"
				+"e034558225464d66a70faa908d0d6d27,"
				+"a4a7793b5365485fabc2f670165acf01,"
				+"c8cd784757b34e8d9c27b32912070934,"
				+"9e8aecea791843dabf010da77bf0ae98,"
				+"6bec65be68dc4b8694597fd1dffdc2b9,"
				+"6f25916a1c284b1db1243756328b44b7,"
				+"7de27734c64c4b68932e1b99d32cd39f,"
				+"62c294a007ae4c3da2dad7b54d8d046c,"
				+"f86df0d24a1448f2989447932827ec79,"
				+"5d890617ec274a8db61387a1f2673ba0,"
				+"ae11d75879b946b8a1de35ff0d1b83e7,"
				+"ff2604483d0e4f1680d7820fe970b240,"
				+"40089292daf04ffa8a95c4d7fd974efc,"
				+"c12f5fa19256475eb0d4cc2361b6deeb,"
				+"3f300fc3ec06462eb27709a8f2e64f1a,"
				+"67f906d752304c6b86963d955a35436c,"
				+"4d28e5b232b940a59689a661d3c902ed,"
				+"094e55c42dfc46ca96a34e39acfc0133,"
				+"7c3d5cfe66004be7b65815b9b9b8db7a,"
				+"0a8a2655028d4e69a23b15eb04446473,"
				+"d668324b190d48ddaa586b079032715c,"
				+"ab513a27f87a44f7b1bd75c388e3161f,"
				+"52cf2b30caa34c38b69c7cd5f4acef69,"
				+"73c4e43e89c048e9bae5081dd5fca590,"
				+"44a3b7986c2f4a17a594f3426e7a754d,"
				+"872096953757445f89d23469a8fbc0e7,"
				+"adba9c5ea1f3495ab76b5a2a504a3602,"
				+"96029d427ed54bbdb4a8aca1f4bff456,"
				+"65692db4d6884805ae2dd8c8b0288e34,"
				+"7c87ef7f43504fd2b0ba6da36cd4a354,"
				+"7d9cda5aea924527b2d3e9b48e8ed9ab,"
				+"e552d246f8c74326a3ecf76f82969c33,"
				+"007c7f1a3c2e4ba39eb414a46c07b940,"
				+"e65240cb1d1c478ea0a7a2203fe8834d,"
				+"7271fc0f6bff406884f38ad507f2b707,"
				+"d2ce3c49666c4c2e8b1570d2b2766ac7,"
				+"59403162e317421e88fe91cbc5d1c79d,"
				+"7b19326cce234501afbfcbaa438178c1,"
				+"a8657d236087449fafcb53371457b0ab,"
				+"3f963337b93b41e5b12efea67083d43d,"
				+"33c36433011242ce9ab79f2533238dbd,"
				+"4adb50141ebc496fb4e8a6cf94ffa7ae,"
				+"a4a209acf4914d62b6d491a20b03bdd0,"
				+"f916d239db214e38b490ff657d713e59,"
				+"6168a669457a4727ad89bbf32c38fe1f,"
				+"815096622a984c0883f7fde6be7cd9ec,"
				+"98c3017b0aad4654949d942552749562,"
				+"5bca21dada164117bef7584a7da2085c,"
				+"24a31a7f750a4343a5b9718bdea73a27,"
				+"8e02d0075a8f4d8f9fc595b6f9c447c8,"
				+"e68f0774777c4d4193872c2f394ab915,"
				+"c4eecdcdf46a434a82a36b39e8e39337,"
				+"e3a320d9e1da475e9ea300ed4b60e918,"
				+"5ebf2cd3c7ee46cda764bb6cabbda093,"
				+"4915461613b54ef893da43e1d21ef749,"
				+"3a405acc3a5b4a7d8b3563ddaab48056,"
				+"c61eae80b41644f5a033e049261f0164,"
				+"9de6853ff1674d05a72ec8ae13e2d5a7,"
				+"919a68c82966468681a9fb9ed2784570,"
				+"852e7776a29846a698fe20439448d002,"
				+"6a838714f7474257a462fcd431e4baf2,"
				+"8e803c13bf404fd0b4c143f377447d3f,"
				+"d781f102d46643df869dd0ddaa91ece5,"
				+"1c1b376747974e2da5b4523dd2f550c6,"
				+"7c31faa27d18459dae753ac7ad02a6c0,"
				+"bf7c264192a044a7ac448368187f80fc,"
				+"80a6496081e047a0a00553df35eeff38,"
				+"8a3a94ecf0064b6a87895556d76f4838,"
				+"2b9a6dbb77f14cc481e04bbc25d4961b,"
				+"da17b2e565e44d8ca9cf6fed48471450,"
				+"bdac75d5c513454297b2ebc37e37efc6,"
				+"3ba06a19d7e244808c6821c583f2d447,"
				+"edac7f5e38fe49da8be212c486835cf4,"
				+"1789f410f14d479680b9bfbe2b9d8bc7,"
				+"72ba2671e1014ae78a375fc16a512434,"
				+"2af4287086a2426183ef0f3e03ca757f,"
				+"b83f5d2c4877453da84efb61c4fefc39,"
				+"a8ddcf8186b1408bb2e1622a7aaec2d4,"
				+"2a7c1650545742279e9d7ba6570f9577,"
				+"dcd1675030f941498ba8294401b9409c,"
				+"eeba7c9a96894de8af8a2976ddd4b430,"
				+"747177e778b9463d845719ee3b4210dc,"
				+"015c90a7441d4e98bcc2d51ad16fbd29,"
				+"b3a622c849b64e429e720969dc1f8da0,"
				+"64364fcb2bc94299a19a37008d77f357,"
				+"daca7d73a5da4cdcb986406ad2b0a334,"
				+"8deda1e6aed1472e864954d623148c4b,"
				+"1634036c8ebd4461be2c88f89f83b1ab,"
				+"9f72a3969d084dd18fb3a0ba12833f3d,"
				+"45db3e9a965640aa9bf880af5378b4d5,"
				+"9818f333e6654a6b9fc4a2192090eb35,"
				+"0272bb53c66c4a3d94cf276f8157fe4c,"
				+"c77886266652404c9044d2e58fd55c93,"
				+"97c1105a3d9e4d8d901add2f8fc34138,"
				+"f701a52f76384ba1a88544d97889eae5,"
				+"408d2a45938849e492678bd514ac7900,"
				+"121453f8b11a4144835773e3ff212f7d,"
				+"4f58ea06be2e4fd48272e8453bfc314e,"
				+"6ddafdfcb0fc4f119822b3c1e5ebef91,"
				+"21ff64f0c85c4f62a4306ba9432f4696,"
				+"2a4fc96a607143959bfdb8bde73d181e,"
				+"22d7a010af534061aadb9db664d1c7f5,"
				+"0ca877b18eea481b94a5c62008e9e619,"
				+"235828218de64eea913fb9d2926d889d,"
				+"71b3e2392f7d4aefaf79d0e1a872e313,"
				+"c1ae178bd68b4bd589c4625a7096e26d,"
				+"71e707108718427795e506b7d78baf59,"
				+"5d7cfe7a42b74af2b20dc81710520147,"
				+"d451399ced234ec3a10e4045feaeeb45,"
				+"aca5af421bd74a2cb8f34f9f2a54feeb,"
				+"05246c0934be4ffdbffd4361f4c55f69,"
				+"bbe1fd23255849879c80f6646ff0194a,"
				+"78cc95515940450b9037dfeb20277c48,"
				+"405491c09a4e43619dadceee107a5f68,"
				+"a56053a5d3af49ecb53e85b6e3615385,"
				+"6c69788ae553475dbb844e69ee38c6a2,"
				+"fae96469fcb34d5a96d2c46bb6046d6d,"
				+"e1974aec26d84451a2c28c5e48dffb2f,"
				+"4c78a668eea147e3aa8731e0089a31b7,"
				+"131bab405b7f48bc9da9c51f72b62b5e,"
				+"a5127a00b12842789dd0a64c52a8ad3a,"
				+"fef21ba120804a7997a0b2502304d42b,"
				+"b2916335827c439789752f69d7423ce2,"
				+"cc1745c7e9ae45a78719ccb7ec32fcf2,"
				+"400a50469b79429c8f3bd71c1e2569fd,"
				+"391bfa5d8d5447ab8e4a427b62406f85,"
				+"f2bf646a49104b839c37d72b91a436fe,"
				+"14991d2fa980498b9e73c527e36013fc,"
				+"38a40dbf84654d5893a8fbde9d1c2b5c,"
				+"3c815cb5d5a845a4989a8e8688d7da2f,"
				+"ed263131e90f4461b7caf5d32bb58bfb,"
				+"1831ff0be30d49189c256cf2e776cdb8,"
				+"ee86273c4a5c4dbfa8b658a82b56935b,"
				+"93eed340dbfc430f84e0130768f52986,"
				+"7f72276709674c2dbfef5b098174ac3a,"
				+"5b78568d05b1424fa4fbabc7fbf3a576,"
				+"11382d5d35ba416d806e1b5624787c26,"
				+"e4642608974344d997a9b6fe91a902f7,"
				+"e3f1eaf4017b47a380d1773c4c48c185,"
				+"9bbd71402b1e4c948797ba12e3c693da,"
				+"4c09680168064d8aa49caa2a984a4952,"
				+"5d773e15a363498195cd87807343109e,"
				+"95dcacc067e74274ae89f2078b2e012b,"
				+"2ca030b5eb1d492484e5d5b7801f0a31,"
				+"1800f40768c844cc8a39555ba1a38fbd,"
				+"1eaa883f4fa84634a91c46af0ed1e5e9,"
				+"d49d741123554453af425af71e3d075c,"
				+"0f13d766d38d44b19f332cafee3e8c2a,"
				+"e28dde13b2ad45889716356c1315a3eb,"
				+"6a9e01413a614642b18498e1d5bca101,"
				+"ffd18b331aae4d68b48b2ad3f0f3e2d0,"
				+"edc325399d2f414f8c3688eef2db02e6,"
				+"7b35f40f3e9c4817973cfc2d15eec67a,"
				+"cc2f275deba04d468d3b402f492ec0c1,"
				+"bf50edfa83584d3f820fb040ded2c571,"
				+"68008b6504ca4480a7d5c3dd92f930bb,"
				+"960c672b7185407282c7859dbd17a6a2,"
				+"553196b07f304983994306de459ef32a,"
				+"fc7f9d23c03b40faa428f5c50ee720e9,"
				+"1fb64c979b1b4e898f6eeae6e6a1f043,"
				+"dcee8d3b061d4b68aa99e6f78da5a87c,"
				+"f50035968adc4c80b609b6e4208b030b,"
				+"30f2a48eb60d461cb52a097aa8005d8e,"
				+"4e8ff118a0a6468a890a6576f85982c2,"
				+"600436b64d5342a6b9445691821b6019,"
				+"a0ffa66b6c414f8e855b99f81d53f178,"
				+"8cc34ed6cee64adc964bcc8a786fe2b3,"
				+"a99c8cfb8da149a2aea4f5cf4bef8c9d,"
				+"fdb748be294e4d138ddb402b0907fad7,"
				+"163d1d19d7e74d9f8124dac1ff3b64d3,"
				+"fe8898e3a81849b1ad9e99cf2714ccfb,"
				+"d3646a2f9f1a4f2d8401171f404cb825,"
				+"1cb108c02e2b4dbcb382fe22d58063d8,"
				+"b7117c0766b04eb7b9ae29f0d2986a2f,"
				+"241f09030b6945b78893199b019e6235,"
				+"e6ad4cd77f92481793d4cd963f14ea67,"
				+"10ff28ddfca44bda816adbf0c7bb4a8c,"
				+"a02480fbab454cbea58440d14aeff340,"
				+"68e2e2f5312b43bf8108998f04515613,"
				+"17fbe85c59604f02a9e3a9d4e36d443b,"
				+"464078a3bba64c3095f400be3137f6b4,"
				+"9c114216b34e41d88fa188de1b7732e2,"
				+"22caea7a1edd4172aa9966cd9196ad35,"
				+"89cd55caeafe4c1f92c7826013732166,"
				+"924e1f2bdc9e42bd8a5899a97a2faa1e,"
				+"b931559ff0b64ea9a26f06f9ffead4bd,"
				+"aaf4d4f310364b03b17267ea1a89b85c,"
				+"ce0161c403aa45f29080c9f07387b161,"
				+"8bd98cc997b248bdb6278db637ab13f7,"
				+"4c40704c9af44efd9046420448b5492b,"
				+"453fea480d534268aacfd5344ab48963,"
				+"19bbfff224694f68b09bc6105eec3c83,"
				+"8d3bd9299b934912a4226025aece52e2,"
				+"b588aa1a84d24553b45703e34da5c786,"
				+"8c3a9a1da9e742aa92ac273478b4ac70,"
				+"6b86ea1d04234dcc8096c187c5537e9d,"
				+"ebf20163fa584c8a9d46848e737dbd16,"
				+"4f2bb4966a5f4fb6a0a8575d6214f069,"
				+"1c3a3c18e45244c99c4a3a14f2c66c15,"
				+"30bcd8d76bcb4aa4a5a963940f94577e,"
				+"3477a5a340eb422e8cf448cffdfaa478,"
				+"d56b7b75674f4d5fb2c7f82c42b195c5,"
				+"97d2b0f01f164271afd5f5262ba9e548,"
				+"769b20c9c3b44b4393ff4f6ca95d5ad6,"
				+"4e5f3e7f3b8d498a8a4ed3c751e1d750,"
				+"6a45ae5a24ad4b78a6c2c0f232d1ed74,"
				+"142a4d37d9a3424c81cdd2b4dde91a56,"
				+"4d33e61bc30a465abf7c66407c2ff7c0,"
				+"11765a19939640189a292b81a32af0f5,"
				+"6b9064d5d5624494a7f07a943c8c21fa,"
				+"45971b19a6504fd39346e917b6dd9bb0,"
				+"fa4b32cd36304ffeb6be30a57446347a,"
				+"2de22bb4b75544c2a3e619dfac3ec722,"
				+"84a36899382c4b8a9786a223c35631b2,"
				+"aaa373a2190c48139f1ae27c048bd405,"
				+"db3b9101361d4e08a1d82ec0b73d716e,"
				+"a235ee480b6e4d76b40ff194f99657a3,"
				+"b56d39f8e2a64900a9503ffb66871f75,"
				+"7aeb95fc3c7a4386ae85800ffc7d83e2,"
				+"34b9324ea6154b439a139f6c7d29a7fc,"
				+"d493929830394a9bb3fcbc7115e5308b,"
				+"27227645f9494083aeb8688d967cd1f4,"
				+"6e3f7404ff984e5f89e987aeae2dc043,"
				+"3aa1ff7f0d2e480eabde91592027181b,"
				+"e5be8553148c4863aff441194a6fb4cf,"
				+"b3c1a975ea1a4719b841b4fca1f5fd18,"
				+"f16e00056bbd490ca697d0ae82d955f4,"
				+"c115b7bab349493c909ad1262f48df73,"
				+"3f57ac3346ab4fe995cd20126c5ee11a,"
				+"7dd17e12d7cf472db12ed84ceada4920,"
				+"470d56740fd249c6b43d91732509b9e5,"
				+"1565d907e1ed489bb2a60c3ddc8db13d,"
				+"10c6e24991ff4a169e5adc115876a298,"
				+"d89709d78dc040e3ac5233472bb7e9a6,"
				+"717b5bc8feab41f394a38f84b09cdac2,"
				+"0409b2a340f64fe8bbcb1abe968db3a6,"
				+"b9b110184eef4a318be5f6e417d6e279,"
				+"0c724bdc94c34e03bed1b9403f72bc26,"
				+"891f1c1ffeb9420eb34b9fcfd3669857,"
				+"a5e0b981d36d407683fdb6726ee8dd6c,"
				+"b0f14861bd8e4192983ce04e16288096,"
				+"0eb60b460ee0443fafbee17a6f0a9296,"
				+"1adae5f3da7642808e190637e239ee41,"
				+"1cbd2fde6be24735823d9a473e2c90e3,"
				+"da98f1ddd07547989cba4a137b30cab9,"
				+"cef6dddef36047db8466a9d7a657ea06,"
				+"a4e73bf5d57843d7ab2418c950177066,"
				+"9d2d3f4fd2194408af2e19097826c799,"
				+"7eff6bcf020242f292269d885a84f6e8,";
				String [] idArray = ids.split(",");
				
					for(int i=0;i<idArray.length;i++) {
						try {
							String vid = idArray[i];
							String url = getPlaySource(vid);
							if(url==null || "".equals(url)) {
								submitTranscodeJobs(idArray[i]);
							}
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
				
				
	}
}
