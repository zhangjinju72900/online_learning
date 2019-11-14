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
 * 私有ossClient
 * @ClassName:  OssConfiguration   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: qun
 * @date:   2018年8月15日 下午7:50:31   
 *
 */
@Configuration("ossPriClient")
public class OssPrivateConfiguration implements FactoryBean<OSS>, InitializingBean, DisposableBean{

	@Value("${oss.access_id}")
	private String ACCESS_ID;
	
	@Value("${oss.access_key}")
	private String ACCESS_KEY;
	
	@Value("${oss.oss_endpoint}")
	private String OSS_ENDPOINT;
	
	private OSS ossPriClient;
	
	private String HTTP = "http://";
	
	@Override
	public void destroy() throws Exception {
		ossPriClient.shutdown();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setIdleConnectionTime(1000);
        ossPriClient = new OSSClientBuilder().build(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
//        ossPriClient = new OSSClientBuilder().build(HTTP+OSS_ENDPOINT, OSS_ENDPOINT, ACCESS_KEY, conf);
//		ossClient = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
	}

	@Override
	public OSS getObject() throws Exception {
		return ossPriClient;
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
