package com.drkj.wishfuldad.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.view.VerticalRuleView;
import com.drkj.wishfuldad.view.WeightChoiceView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class SetBabyDataActivity extends BaseActivity {
    // 手势事件初始化
    //private GestureDetector 	gestureDetector;

    @BindView(R.id.flag_image_view)
    ImageView mSexImageView;
//    private float mRotation = 0;

    @BindView(R.id.text_kedu)
    TextView mBabyHeightTextView;
    @BindView(R.id.sel_male_textview)
    TextView mSelMaleTextView;
    @BindView(R.id.set_female_textview)
    TextView mSelFemaleTextView;
    @BindView(R.id.save_textview)
    TextView mSaveTextView;

    @BindView(R.id.textView2)
    TextView mSetBloodATextView;
    @BindView(R.id.textView2_b)
    TextView mSetBloodBTextView;
    @BindView(R.id.textView2_ab)
    TextView mSetBloodABTextView;
    @BindView(R.id.textView2_o)
    TextView mSetBloodOTextView;
    @BindView(R.id.kedu)
    VerticalRuleView ruleView;
    @BindView(R.id.weightChoiceView)
    WeightChoiceView weightChoiceView;
    int blood = 0;
    int sex = 1;
    float height = 120;
    float weight = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_baby_data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ruleView.setCallBack(new VerticalRuleView.CallBack() {
            @Override
            public void onScaleChanging(float scale) {
                int a = (int) scale;
                if (a % 10 == 0) {
                    int c = a/10;
                    mBabyHeightTextView.setText(c + "cm");
                    height = c;
                } else {

                    float b = a / 10f;
                    mBabyHeightTextView.setText(b + "cm");
                    height = b;
                }
            }
        });
        weightChoiceView.setSelectScaleListener(new WeightChoiceView.SelectScaleListener() {
            @Override
            public void selectScale(double value) {
                weight = (float) value;
            }
        });
    }

    @OnClick(R.id.back_imageview)
    void back(){
        finish();
    }
    @OnClick(R.id.save_textview)
    void save(){
        MobclickAgent.onEvent(this, "babySettingSave");

        BaseApplication.getInstance().getBabyInfo().setBloodType(blood);
        BaseApplication.getInstance().getBabyInfo().setSex(sex);
        BaseApplication.getInstance().getBabyInfo().setHeight(height);
        BaseApplication.getInstance().getBabyInfo().setWeight(weight);
        finish();
    }
    @OnClick({R.id.sel_a_blood_layout, R.id.sel_b_blood_layout, R.id.sel_ab_blood_layout, R.id.sel_o_blood_layout})
    void setBloodType(View view) {
        MobclickAgent.onEvent(this, "babyBlood");
        mSetBloodATextView.setBackgroundColor(0xffffffff);
        mSetBloodBTextView.setBackgroundColor(0xffffffff);
        mSetBloodABTextView.setBackgroundColor(0xffffffff);
        mSetBloodOTextView.setBackgroundColor(0xffffffff);
        mSetBloodATextView.setTextSize(20);
        mSetBloodBTextView.setTextSize(20);
        mSetBloodABTextView.setTextSize(20);
        mSetBloodOTextView.setTextSize(20);
        switch (view.getId()) {
            case R.id.sel_a_blood_layout:
                mSetBloodATextView.setBackgroundResource(R.drawable.textview_border);
                mSetBloodATextView.setTextSize(30);
                blood = 0;
                break;
            case R.id.sel_b_blood_layout:
                mSetBloodBTextView.setBackgroundResource(R.drawable.textview_border);
                mSetBloodBTextView.setTextSize(30);
                blood = 1;
                break;
            case R.id.sel_ab_blood_layout:
                mSetBloodABTextView.setBackgroundResource(R.drawable.textview_border);
                mSetBloodABTextView.setTextSize(30);
               blood = 2;
                break;
            case R.id.sel_o_blood_layout:
                mSetBloodOTextView.setBackgroundResource(R.drawable.textview_border);
                mSetBloodOTextView.setTextSize(30);
               blood = 3;
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.sel_male_textview, R.id.set_female_textview})
    void setSex(View view) {
        MobclickAgent.onEvent(this, "babySex");
        switch (view.getId()) {
            case R.id.sel_male_textview:
                mSelMaleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
                mSelFemaleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mSexImageView.setImageResource(R.drawable.ic_boy_pic);
               sex = 0;
                break;
            case R.id.set_female_textview:
                mSelMaleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mSelFemaleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
                mSexImageView.setImageResource(R.drawable.ic_girl_pic);
               sex = 1;
                break;
            default:
                break;
        }
    }

}
