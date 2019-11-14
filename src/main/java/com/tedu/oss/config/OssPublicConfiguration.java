package com.tedu.oss.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
/**
 * 公有ossClient
 * @ClassName:  OssConfiguration2   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月15日 下午7:50:56   
 *
 */
@Configuration("ossPubClient")
public class OssPublicConfiguration implements FactoryBean<OSS>, InitializingBean, DisposableBean{

	@Value("${oss.access_id}")
	private String ACCESS_ID;
	
	@Value("${oss.access_key}")
	private String ACCESS_KEY;
	
	@Value("${oss.oss_endpoint2}")
	private String OSS_ENDPOINT;
	
	private OSS ossPubClient;
	
	private String HTTP = "http://";
	
	@Override
	public void destroy() throws Exception {
		ossPubClient.shutdown();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setIdleConnectionTime(1000);
        ossPubClient = new OSSClientBuilder().build(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
       // ossPubClient = new OSSClientBuilder().build(HTTP+OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY, conf);
//		ossClient = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
	}

	@Override
	public OSS getObject() throws Exception {
		return ossPubClient;
	}

	@Override
	public Class<?> getObjectType() {
		return OSS.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
}
