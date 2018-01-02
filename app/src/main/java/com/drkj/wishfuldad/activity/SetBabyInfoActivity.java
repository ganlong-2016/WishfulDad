package com.drkj.wishfuldad.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SetBabyInfoActivity extends BaseActivity {

    @BindView(R.id.image_button_boy)
    ImageButton mImageBoy;
    @BindView(R.id.image_button_girl)
    ImageButton mImageGirl;
    @BindView(R.id.et_input_name)
    EditText mName;
    @BindView(R.id.et_input_age)
    EditText mInputAge;
    int sex = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_baby_info);

    }

    @OnClick(R.id.image_back)
    void back() {
        finish();
    }

    @OnTextChanged(R.id.et_input_name)
    void textChnaged(CharSequence s, int start, int before, int count){
        if (!TextUtils.isEmpty(s)){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_clear_text);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mName.setCompoundDrawables(null,null,drawable,null);
        }else {
            mName.setCompoundDrawables(null,null,null,null);
        }
    }

    @OnTouch(R.id.et_input_name)
    boolean clearName(MotionEvent event){
        if (mName.getCompoundDrawables()[2] == null){
            return false;
        }
        //这里一定要对点击事件类型做一次判断，否则你的点击事件会被执行2次
        if (event.getAction() != MotionEvent.ACTION_UP){
            return false;
        } if (event.getX() > mName.getWidth() -mName.getCompoundDrawables()[2].getBounds().width()) {
            //do something you want
            mName.setText("");
            mName.setCompoundDrawables(null,null,null,null);
            return true;
        }
        return false;
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

    @OnClick({R.id.image_button_boy, R.id.image_button_girl})
    void chooseSex(View view) {

        MobclickAgent.onEvent(this, "babySexChoosed");
        switch (view.getId()) {
            case R.id.image_button_boy:
                sex = 0;
                mImageBoy.setSelected(true);
                mImageGirl.setSelected(false);
                break;
            case R.id.image_button_girl:
                sex = 1;
                mImageBoy.setSelected(false);
                mImageGirl.setSelected(true);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.image_next_step)
    void nextStep() {

        MobclickAgent.onEvent(this, "babyInfoCompleted");
        if (TextUtils.isEmpty(mName.getText().toString())){
            showToast("请输入宝宝姓名");
            return;
        }
        if (TextUtils.isEmpty(mInputAge.getText().toString())){
            showToast("请输入宝宝年龄");
            return;
        }
        if (sex==2){
            showToast("请选择宝宝性别");
            return;
        }
        BaseApplication.getInstance().getBabyInfo().setName(mName.getText().toString());
        BaseApplication.getInstance().getBabyInfo().setAge(Integer.parseInt(mInputAge.getText().toString()));
        BaseApplication.getInstance().getBabyInfo().setSex(sex);
        startActivity(new Intent(this, SetBabyHeadActivity.class));
    }
}
