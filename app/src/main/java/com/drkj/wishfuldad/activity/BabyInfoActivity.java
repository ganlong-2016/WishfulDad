package com.drkj.wishfuldad.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.BabyInfo;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.fragment.BabyInfoFragment;
import com.drkj.wishfuldad.fragment.BabySettingsFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BabyInfoActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private BabyInfoFragment infoFragment;
    private BabySettingsFragment settingsFragment;
    List<Fragment> fragments = new ArrayList<>();

    private ImageView backImageView;
    private TextView babyInfoTextView;
    private TextView settingTextView;

    @BindView(R.id.text_save)
    TextView saveText;
    @BindView(R.id.image_baby_info_select)
    ImageView babyinfoSelect;
    @BindView(R.id.image_baby_setting_select)
    ImageView babySettingSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_baby_info);
        initView();

    }

    private void initView() {
        backImageView = findViewById(R.id.imageView_back);
        backImageView.setOnClickListener(this);

        babyInfoTextView = findViewById(R.id.text_baby_info);
        babyInfoTextView.setOnClickListener(this);

        settingTextView = findViewById(R.id.text_set);
        settingTextView.setOnClickListener(this);


        viewPager = findViewById(R.id.viewPager_baby_info);
        infoFragment = new BabyInfoFragment();
//        infoFragment.addSettingBabyInfoListener(this);
        settingsFragment = new BabySettingsFragment();
//        settingsFragment.addSettingBabyInfoListener(this);
        fragments.add(infoFragment);
        fragments.add(settingsFragment);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        babyInfoTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        babyInfoTextView.getPaint().setFakeBoldText(true);
                        settingTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        settingTextView.getPaint().setFakeBoldText(false);
                        saveText.setVisibility(View.VISIBLE);
                        babyinfoSelect.setVisibility(View.VISIBLE);
                        babySettingSelect.setVisibility(View.GONE);
                        break;
                    case 1:
                        babyInfoTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        babyInfoTextView.getPaint().setFakeBoldText(false);
                        settingTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        settingTextView.getPaint().setFakeBoldText(true);
                        saveText.setVisibility(View.GONE);
                        babySettingSelect.setVisibility(View.VISIBLE);
                        babyinfoSelect.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.text_save)
    void saveBabyInfo(){
        childUpdate();
        userIcon();
        DbController.getInstance().updateBabyInfoData(BaseApplication.getInstance().getBabyInfo());
        DbController.getInstance().updateSettingData(BaseApplication.getInstance().getSettingInfo());
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            saveData();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                saveData();
                finish();
                break;
            case R.id.text_baby_info:

                MobclickAgent.onEvent(this, "babyInfo");
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.text_set:
                MobclickAgent.onEvent(this, "set");
                viewPager.setCurrentItem(1, true);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BaseApplication.getInstace().setBabyInfoBean(DbController.getInstance().queryBabyInfoData());
//        BaseApplication.getInstace().setSettingsBean(DbController.getInstance().querySettingData());
    }
    private void saveData(){

        DbController.getInstance().updateSettingData(BaseApplication.getInstance().getSettingInfo());
        BabyInfo info1 = BaseApplication.getInstance().getBabyInfo();
        BabyInfo info2 = DbController.getInstance().queryBabyInfoData();
        if (!info1.equals(info2)){
            showPrompt();
        }
    }
    private void showPrompt() {
        final Dialog dialog = new Dialog(this,R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint2);
        TextView textOK = dialog.findViewById(R.id.text_ok);
        TextView textMeassge = dialog.findViewById(R.id.text_message);
        textMeassge.setText("您的资料已修改尚未保存");
        textOK.setTextColor(getResources().getColor(R.color.blue));
        textOK.setText("保存");
        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                childUpdate();
                userIcon();
                DbController.getInstance().updateBabyInfoData(BaseApplication.getInstance().getBabyInfo());
                finish();
            }
        });
        TextView textCancel = dialog.findViewById(R.id.text_cancel);
        textCancel.setText("取消");
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                BaseApplication.getInstance().setBabyInfo(DbController.getInstance().queryBabyInfoData());
                finish();
            }
        });

        dialog.show();
    }

//    @Override
//    public void settingComplete(String imageUrlPath) {
//        settingsFragment.setBabyInfo(imageUrlPath);
//    }
//
//    @Override
//    public void setName(String name) {
//        settingsFragment.setBabyName(name);
//    }
}
