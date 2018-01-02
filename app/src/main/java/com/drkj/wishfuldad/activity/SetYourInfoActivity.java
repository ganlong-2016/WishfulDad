package com.drkj.wishfuldad.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

public class SetYourInfoActivity extends BaseActivity {

    @BindView(R.id.image_button_papa)
    ImageButton mImagePapa;
    @BindView(R.id.image_button_mama)
    ImageButton mImageMama;
    @BindView(R.id.image_button_grandpa)
    ImageButton mImageGrangpa;
    @BindView(R.id.image_button_grandma)
    ImageButton mImageGrangma;
    @BindView(R.id.image_button_other)
    ImageButton mImageOther;
    @BindView(R.id.et_input_name)
    EditText mInputNmae;
    @BindView(R.id.et_input_age)
    EditText mInputAge;
    private int role = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_your_info);

    }

    @OnTextChanged(R.id.et_input_name)
    void textChnaged(CharSequence s, int start, int before, int count){
        if (!TextUtils.isEmpty(s)){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_clear_text);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mInputNmae.setCompoundDrawables(null,null,drawable,null);
        }else {
            mInputNmae.setCompoundDrawables(null,null,null,null);
        }
    }
    @OnTextChanged(R.id.et_input_age)
    void textChnaged2(CharSequence s, int start, int before, int count){
        if (!TextUtils.isEmpty(s)){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_clear_text);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mInputAge.setCompoundDrawables(null,null,drawable,null);
        }else {
            mInputAge.setCompoundDrawables(null,null,null,null);
        }
    }
    @OnTouch(R.id.et_input_age)
    boolean clearAge(MotionEvent event){
        if (mInputAge.getCompoundDrawables()[2] == null){
            return false;
        }
        //这里一定要对点击事件类型做一次判断，否则你的点击事件会被执行2次
        if (event.getAction() != MotionEvent.ACTION_UP){
            return false;
        } if (event.getX() > mInputAge.getWidth() -mInputAge.getCompoundDrawables()[2].getBounds().width()) {
            //do something you want
            mInputAge.setText("");
            mInputAge.setCompoundDrawables(null,null,null,null);
            return true;
        }
        return false;
    }
    @OnClick(R.id.image_back)
    void back(){
        finish();
    }
    @OnClick(R.id.image_next_step)
    void nextStep(){

        MobclickAgent.onEvent(this, "roleComplete");
        if (TextUtils.isEmpty(mInputNmae.getText().toString())){
            showToast("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(mInputAge.getText().toString())){
            showToast("请输入年龄");
            return;
        }
        if (role==0){
            showToast("请选择角色");
            return;
        }
        BaseApplication.getInstance().getYourInfo().setName(mInputNmae.getText().toString());
        BaseApplication.getInstance().getYourInfo().setAge(Integer.parseInt(mInputAge.getText().toString()));
        BaseApplication.getInstance().getYourInfo().setRole(role);
        startActivity(new Intent(this,SetYourAccountActivity.class));
    }
    @OnTouch(R.id.et_input_name)
    boolean clearName(MotionEvent event){
        if (mInputNmae.getCompoundDrawables()[2] == null){
            return false;
        }
        //这里一定要对点击事件类型做一次判断，否则你的点击事件会被执行2次
        if (event.getAction() != MotionEvent.ACTION_UP){
            return false;
        } if (event.getX() > mInputNmae.getWidth() -mInputNmae.getCompoundDrawables()[2].getBounds().width()) {
            //do something you want
            mInputNmae.setText("");
            mInputNmae.setCompoundDrawables(null,null,null,null);
            return true;
        }
        return false;
    }

    @OnClick({R.id.image_button_papa,R.id.image_button_mama,R.id.image_button_grandpa,R.id.image_button_grandma,R.id.image_button_other})
    void choose(View view){
        MobclickAgent.onEvent(this, "roleChoosed");
        resetChoose();
        switch (view.getId()){
            case R.id.image_button_papa:
                role = 1;
                mImagePapa.setSelected(true);
                break;
            case R.id.image_button_mama:
                role = 2;
                mImageMama.setSelected(true);
                break;
            case R.id.image_button_grandpa:
                role = 3;
                mImageGrangpa.setSelected(true);
                break;
            case R.id.image_button_grandma:
                role = 4;
                mImageGrangma.setSelected(true);
                break;
            case R.id.image_button_other:
                role = 5;
                mImageOther.setSelected(true);
                break;
            default:
                break;
        }
    }

    private void resetChoose(){
        mImagePapa.setSelected(false);
        mImageMama.setSelected(false);
        mImageGrangpa.setSelected(false);
        mImageGrangma.setSelected(false);
        mImageOther.setSelected(false);
    }
}
