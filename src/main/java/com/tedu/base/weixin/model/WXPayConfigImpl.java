package com.tedu.base.weixin.model;

import com.github.wxpay.sdk.WXPayConfig;
import com.tedu.base.common.utils.PropertiesUtil;

import java.io.InputStream;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2018/1/23
 */
public class WXPayConfigImpl implements WXPayConfig {


    private String appID;

    private String mchID;

    private String key;

    private String appSecretKey;

    public WXPayConfigImpl() {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        propertiesUtil.loadProperties("engine/conf","base");
        appID=propertiesUtil.getProperties("wx.appID");
        mchID=propertiesUtil.getProperties("wx.mchID");
        key=propertiesUtil.getProperties("wx.key");
        appSecretKey=propertiesUtil.getProperties("wx.appSecretKey");
    }


    //wxfa7e717a33609f89童程 wx8743a5ffa3adc626海南
    @Override
    public String getAppID() {
        return appID;
    }

    //1498157212童程  1498222832海南
    @Override
    public String getMchID() {
        return mchID;
    }

    //957c0a23a1dc9758581a3ff3cf1cde85童程 海南c40ca724a181h128f0e4ef0352aea434
    @Override
    public String getKey() {
        return key;
    }

    //童程ccc273f289ab4705d4bd7c6c32b73c03  //海南0606ea43c400e4124f0352aecaa18172
    public String getAppSecretKey(){
        return appSecretKey;
    }
    
    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 100000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 100000;
    }


}

