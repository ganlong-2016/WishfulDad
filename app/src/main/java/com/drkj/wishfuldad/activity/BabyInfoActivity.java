package com.drkj.wishfuldad.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.db.DbController;
import com.drkj.wishfuldad.fragment.BabyInfoFragment;
import com.drkj.wishfuldad.fragment.BabySettingsFragment;
import com.drkj.wishfuldad.listener.SettingBabyInfoListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class BabyInfoActivity extends BaseActivity implements View.OnClickListener,SettingBabyInfoListener {

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private BabyInfoFragment infoFragment;
    private BabySettingsFragment settingsFragment;
    List<Fragment> fragments = new ArrayList<>();

    private ImageView backImageView;
    private TextView babyInfoTextView;
    private TextView settingTextView;
    private TextView saveTextView;

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

        saveTextView = findViewById(R.id.text_save);
        saveTextView.setOnClickListener(this);

        viewPager = findViewById(R.id.viewPager_baby_info);
        infoFragment = new BabyInfoFragment();
        infoFragment.addSettingBabyInfoListener(this);
        settingsFragment = new BabySettingsFragment();
        settingsFragment.addSettingBabyInfoListener(this);
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
                        break;
                    case 1:
                        babyInfoTextView.setTextColor(getResources().getColor(R.color.toolbar_text_uncheck));
                        babyInfoTextView.getPaint().setFakeBoldText(false);
                        settingTextView.setTextColor(getResources().getColor(R.color.toolbar_text_check));
                        settingTextView.getPaint().setFakeBoldText(true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            showPrompt();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                showPrompt();
                break;
            case R.id.text_baby_info:

                MobclickAgent.onEvent(this, "babyInfo");
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.text_set:
                MobclickAgent.onEvent(this, "set");
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.text_save:
                MobclickAgent.onEvent(this, "editedBabyInfo");
                childUpdate();
                userIcon();
                DbController.getInstance().updateBabyInfoData(BaseApplication.getInstance().getBabyInfo());
                DbController.getInstance().updateSettingData(BaseApplication.getInstance().getSettingInfo());

                finish();
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

    private void showPrompt() {

        final Dialog dialog = new Dialog(this,R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_hint2);
        TextView textOK = dialog.findViewById(R.id.text_ok);
        TextView textMeassge = dialog.findViewById(R.id.text_message);
        textMeassge.setText("您编辑的信息尚未保存,\n确认离开?");
        textOK.setTextColor(getResources().getColor(R.color.blue));
        textOK.setText("确定");
        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                BaseApplication.getInstance().setBabyInfo(DbController.getInstance().queryBabyInfoData());
                BaseApplication.getInstance().setSettingInfo(DbController.getInstance().querySettingData());
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
            }
        });

        dialog.show();
    }

    @Override
    public void settingComplete(String imageUrlPath) {
        settingsFragment.setBabyInfo(imageUrlPath);
    }

    @Override
    public void setName(String name) {
        settingsFragment.setBabyName(name);
    }
}
