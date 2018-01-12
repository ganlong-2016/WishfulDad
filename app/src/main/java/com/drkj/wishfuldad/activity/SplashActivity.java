package com.drkj.wishfuldad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.LoginResultBean;
import com.drkj.wishfuldad.net.ServerNetClient;
import com.drkj.wishfuldad.util.SpUtil;
import com.drkj.wishfuldad.wxapi.WXEntryActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    private CountDownTimer timer;
    private boolean loginSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (loginSuccess) {
                    startActivity(new Intent(SplashActivity.this, CardActivity.class));
                } else if (SpUtil.hasShowedGuidePage(SplashActivity.this, "splash")) {
                    startActivity(new Intent(SplashActivity.this, WXEntryActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    SpUtil.setHasShowedGuidePage(SplashActivity.this, "splash", true);
                }
                finish();
            }
        };
        timer.start();
        if (!TextUtils.isEmpty(SpUtil.getString(this, "token"))) {
            ServerNetClient.getInstance().getApi()
                    .autoLogin(SpUtil.getString(this, "token"))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LoginResultBean>() {
                        @Override
                        public void accept(LoginResultBean loginResultBean) throws Exception {
                            if (loginResultBean.getStatus() == 90001) {
                                loginSuccess = true;
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }
}
