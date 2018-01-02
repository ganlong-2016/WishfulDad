package com.drkj.wishfuldad.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.IdentifyResultBean;
import com.drkj.wishfuldad.bean.LoginResultBean;
import com.drkj.wishfuldad.net.ServerNetClient;
import com.drkj.wishfuldad.util.SpUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SetYourAccountActivity extends BaseActivity {

    @BindView(R.id.et_input_phone)
    EditText mPhone;
    @BindView(R.id.et_input_verification_code)
    EditText mVerificationCode;
    @BindView(R.id.btn_get_verification_code)
    Button getVerificationCode;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_your_count);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();
    }

    @OnClick(R.id.image_back)
    void back() {
        finish();
    }

    @OnTextChanged(R.id.et_input_phone)
    void textChnaged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_clear_text);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mPhone.setCompoundDrawables(null, null, drawable, null);
        } else {
            mPhone.setCompoundDrawables(null, null, null, null);
        }
    }

    @OnTextChanged(R.id.et_input_verification_code)
    void textChnaged2(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_clear_text);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mVerificationCode.setCompoundDrawables(null, null, drawable, null);
        } else {
            mVerificationCode.setCompoundDrawables(null, null, null, null);
        }
    }

    @OnTouch(R.id.et_input_verification_code)
    boolean clearCode(MotionEvent event) {
        if (mVerificationCode.getCompoundDrawables()[2] == null) {
            return false;
        }
        //这里一定要对点击事件类型做一次判断，否则你的点击事件会被执行2次
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (event.getX() > mVerificationCode.getWidth() - mVerificationCode.getCompoundDrawables()[2].getBounds().width()) {
            //do something you want
            mVerificationCode.setText("");
            mVerificationCode.setCompoundDrawables(null, null, null, null);
            return true;
        }
        return false;
    }

    @OnClick(R.id.btn_get_verification_code)
    void getVerificationCode() {
        MobclickAgent.onEvent(this, "vedifyTapped");
        if (!isChinaPhoneLegal(mPhone.getText().toString())) {
            showDialog();
        } else {
            timer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    getVerificationCode.setText(millisUntilFinished / 1000 + "s后重新获取");
                    getVerificationCode.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    getVerificationCode.setText("重新获取验证码");
                    getVerificationCode.setEnabled(true);
                }
            };
            timer.start();
            ServerNetClient.getInstance().getApi().identify(mPhone.getText().toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<IdentifyResultBean>() {
                        @Override
                        public void accept(IdentifyResultBean loginResultBean) throws Exception {
                            int b = loginResultBean.getStatus();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });

        }
    }

    @OnTouch(R.id.et_input_phone)
    boolean clearPhone(MotionEvent event) {
        if (mPhone.getCompoundDrawables()[2] == null) {
            return false;
        }
        //这里一定要对点击事件类型做一次判断，否则你的点击事件会被执行2次
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (event.getX() > mPhone.getWidth() - mPhone.getCompoundDrawables()[2].getBounds().width()) {
            //do something you want
            mPhone.setText("");
            mPhone.setCompoundDrawables(null, null, null, null);
            return true;
        }
        return false;
    }

    @OnClick(R.id.btn_complete)
    void complete() {

        MobclickAgent.onEvent(this, "signOrLoginComplete");
        ServerNetClient.getInstance().getApi().login(mPhone.getText().toString(), mVerificationCode.getText().toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean loginResultBean) throws Exception {
                        int a = loginResultBean.getStatus();
                        if (a == 1) {
                            BaseApplication.getInstance().getYourInfo().setPhone(mPhone.getText().toString());
                            BaseApplication.getInstance().setToken(loginResultBean.getData().getToken());
                            if (TextUtils.isEmpty(SpUtil.getToken(SetYourAccountActivity.this, "token"))) {

                                startActivity(new Intent(SetYourAccountActivity.this, SetBabyInfoActivity.class));
                            } else {
                                SpUtil.putString(SetYourAccountActivity.this,"token",BaseApplication.getInstance().getToken());
                                startActivity(new Intent(SetYourAccountActivity.this, CardActivity.class));
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }

    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint);
        dialog.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
