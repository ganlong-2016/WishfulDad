package com.drkj.wishfuldad;

import android.app.Application;

import com.drkj.wishfuldad.bean.BabyInfo;
import com.drkj.wishfuldad.bean.SettingInfo;
import com.drkj.wishfuldad.bean.WechatUserInfoResultBean;
import com.drkj.wishfuldad.bean.YourInfo;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.net.ConstantUrl;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by ganlong on 2017/12/13.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private IWXAPI mWxApi;
    private BabyInfo babyInfo;
    private YourInfo yourInfo;
    private SettingInfo settingInfo;
    private String token;
    private WechatUserInfoResultBean wechatUserInfoResultBean;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registToWX();
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        babyInfo = DbController.getInstance().queryBabyInfoData();
        yourInfo = DbController.getInstance().queryYourInfoData();
        settingInfo = DbController.getInstance().querySettingData();
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, ConstantUrl.WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(ConstantUrl.WEIXIN_APP_ID);
    }

    public IWXAPI getmWxApi() {
        return mWxApi;
    }

    public BabyInfo getBabyInfo() {
        return babyInfo;
    }

    public void setBabyInfo(BabyInfo babyInfo) {
        this.babyInfo = babyInfo;
    }

    public void setYourInfo(YourInfo yourInfo) {
        this.yourInfo = yourInfo;
    }

    public void setSettingInfo(SettingInfo settingInfo) {
        this.settingInfo = settingInfo;
    }

    public YourInfo getYourInfo() {
        return yourInfo;
    }


    public SettingInfo getSettingInfo() {
        return settingInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public WechatUserInfoResultBean getWechatUserInfoResultBean() {
        return wechatUserInfoResultBean;
    }

    public void setWechatUserInfoResultBean(WechatUserInfoResultBean wechatUserInfoResultBean) {
        this.wechatUserInfoResultBean = wechatUserInfoResultBean;
    }
}
