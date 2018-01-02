package com.drkj.wishfuldad.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SetHrActivity extends BaseActivity {

    @BindView(R.id.set_hr_normal_imageview)
    CheckBox checkBox1;
    @BindView(R.id.set_dry_imageview)
    CheckBox checkBox2;
    @BindView(R.id.set_humidity_imageview)
    CheckBox checkBox3;
    CheckBox[] checkBoxes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hr);

    }

    @Override
    protected void onResume() {
        super.onResume();
        int a = BaseApplication.getInstance().getSettingInfo().getWeatherType();
        checkBoxes = new CheckBox[]{checkBox1,checkBox2,checkBox3};
        checkBoxes[a].setChecked(true);
    }

    @OnClick(R.id.back_imageview)
    void back() {
        finish();
    }

    @OnCheckedChanged({R.id.set_hr_normal_imageview, R.id.set_dry_imageview, R.id.set_humidity_imageview})
    void checkChanged(CompoundButton buttonView, boolean isChecked) {
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        switch (buttonView.getId()) {
            case R.id.set_hr_normal_imageview:
                checkBox1.setChecked(isChecked);
                if (isChecked){
                    BaseApplication.getInstance().getSettingInfo().setWeatherType(0);
                }
                break;
            case R.id.set_dry_imageview:
                checkBox2.setChecked(isChecked);
                if (isChecked){
                    BaseApplication.getInstance().getSettingInfo().setWeatherType(1);
                }
                break;
            case R.id.set_humidity_imageview:
                checkBox3.setChecked(isChecked);
                if (isChecked){
                    BaseApplication.getInstance().getSettingInfo().setWeatherType(2);
                }
                break;
            default:
                break;
        }
    }
}
