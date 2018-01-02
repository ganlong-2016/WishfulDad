package com.drkj.wishfuldad.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.activity.SetYourAccountActivity;
import com.drkj.wishfuldad.activity.SetYourInfoActivity;
import com.drkj.wishfuldad.activity.SplashActivity;
import com.drkj.wishfuldad.bean.LoginResultBean;
import com.drkj.wishfuldad.bean.WechatAccessTokenResultBean;
import com.drkj.wishfuldad.bean.WechatRefreshTokenResultBean;
import com.drkj.wishfuldad.bean.WechatUserInfoResultBean;
import com.drkj.wishfuldad.net.ConstantUrl;
import com.drkj.wishfuldad.net.ServerNetClient;
import com.drkj.wishfuldad.net.WeChatNetClient;
import com.drkj.wishfuldad.util.SpUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.analytics.MobclickAgent;

import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //如果没回调onResp，八成是这句没有写
        BaseApplication.getInstance().getmWxApi().handleIntent(getIntent(), this);
    }

    @OnClick(R.id.button_we_chat_login)
    void weChatLogin() {
        MobclickAgent.onEvent(this, "weixinlogin");
        wxLogin();
    }

    //微信登录
    public void wxLogin() {
        if (!BaseApplication.getInstance().getmWxApi().isWXAppInstalled()) {
            Toast.makeText(getApplicationContext(), "请先安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //像微信发送请求
        BaseApplication.getInstance().getmWxApi().sendReq(req);
        finish();
    }

    @OnClick(R.id.button_register)
    void register() {
        MobclickAgent.onEvent(this, "phonelogin");
        if (TextUtils.isEmpty(SpUtil.getToken(this, "token"))) {
            startActivity(new Intent(this, SetYourInfoActivity.class));
        } else {
            startActivity(new Intent(this,SetYourAccountActivity.class));
        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
//        LogUtils.sf(resp.errStr);
//        LogUtils.sf("错误码 : " + resp.errCode + "");
        Log.i("ganlong", "onResp: " + resp.errCode + "," + resp.errStr);
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                if (RETURN_MSG_TYPE_SHARE == resp.getType()) UIUtils.showToast("分享失败");
//                else UIUtils.showToast("登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        WeChatNetClient
                                .getInstance()
                                .getApi()
                                .getAccessToken(ConstantUrl.WEIXIN_APP_ID, ConstantUrl.WEIXIN_SECRET, code, ConstantUrl.GRANT_TYPE_AUTH)
                                .concatMap(new Function<WechatAccessTokenResultBean, ObservableSource<WechatRefreshTokenResultBean>>() {

                                    @Override
                                    public ObservableSource<WechatRefreshTokenResultBean> apply(WechatAccessTokenResultBean wechatAccessTokenResultBean) throws Exception {
                                        return WeChatNetClient.getInstance().getApi().refreshToken(ConstantUrl.WEIXIN_APP_ID, ConstantUrl.GRANT_TYPE_REFRESH, wechatAccessTokenResultBean.getRefresh_token());
                                    }
                                })
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.io())
                                .concatMap(new Function<WechatRefreshTokenResultBean, ObservableSource<WechatUserInfoResultBean>>() {
                                    @Override
                                    public ObservableSource<WechatUserInfoResultBean> apply(WechatRefreshTokenResultBean wechatRefreshTokenResultBean) throws Exception {
                                        return WeChatNetClient.getInstance().getApi().getUserInfo(wechatRefreshTokenResultBean.getAccess_token(), wechatRefreshTokenResultBean.getOpenid());
                                    }
                                })
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<WechatUserInfoResultBean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(WechatUserInfoResultBean wechatUserInfoResultBean) {
                                        wxLogin(wechatUserInfoResultBean);
//                                        BaseApplication.getInstance().setWechatUserInfoResultBean(wechatUserInfoResultBean);


                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });


                        break;

                    case RETURN_MSG_TYPE_SHARE:
//                        UIUtils.showToast("微信分享成功");
                        finish();
                        break;
                }
                break;
        }
    }

    public void wxLogin(final WechatUserInfoResultBean wechatUserInfoResultBean) {
        ServerNetClient.getInstance().getApi().wxLogin(wechatUserInfoResultBean.getUnionid())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean loginResultBean) throws Exception {
                        if (loginResultBean.getStatus() == 1) {
//                            SpUtil.putString(WXEntryActivity.this, "token", loginResultBean.getData().getToken());
                            BaseApplication.getInstance().setWechatUserInfoResultBean(wechatUserInfoResultBean);
                            startActivity(new Intent(WXEntryActivity.this, SetYourInfoActivity.class));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
