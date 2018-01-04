package com.drkj.wishfuldad.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.activity.HelpActivity;
import com.drkj.wishfuldad.activity.RepErrorActivity;
import com.drkj.wishfuldad.activity.SetAlertActivity;
import com.drkj.wishfuldad.activity.SetHrActivity;
import com.drkj.wishfuldad.listener.SettingBabyInfoListener;
import com.umeng.analytics.MobclickAgent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BabySettingsFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
//    private ImageView headImageView;
//    private TextView babyName;
//    SettingBabyInfoListener listener;

    public BabySettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.i(TAG, "onCreateView: BabySettingsFragment");
        return inflater.inflate(R.layout.fragment_baby_settings, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        textView1 = activity.findViewById(R.id.village_name_textview1);
        textView2 = activity.findViewById(R.id.village_name_textview2);
        textView3 = activity.findViewById(R.id.village_name_textview3);
        textView4 = activity.findViewById(R.id.village_name_textview4);
//        babyName = activity.findViewById(R.id.text_setting_baby_name);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);

//        headImageView = activity.findViewById(R.id.baby_head_image_view);
//        setBabyName(BaseApplication.getInstance().getBabyInfo().getName());
//        setBabyInfo(BaseApplication.getInstance().getBabyInfo().getHeadImage());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SettingFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SettingFragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.village_name_textview1:
                MobclickAgent.onEvent(getContext(), "weahterEdit");
                startActivity(new Intent(activity, SetHrActivity.class));
                break;
            case R.id.village_name_textview2:
                MobclickAgent.onEvent(getContext(), "warningSet");
                startActivity(new Intent(activity, SetAlertActivity.class));
                break;
            case R.id.village_name_textview3:
                MobclickAgent.onEvent(getContext(), "useInfo");
                startActivity(new Intent(activity, HelpActivity.class));
                break;
            case R.id.village_name_textview4:
                MobclickAgent.onEvent(getContext(), "questionChat");
                startActivity(new Intent(activity, RepErrorActivity.class));
                break;
            default:
                break;
        }
    }

//    public void setBabyInfo(String imageUrlPath) {
//        if (headImageView != null) {
//            try {
//                FileInputStream fis = new FileInputStream(imageUrlPath);
//                Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                headImageView.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    public void setBabyName(String name) {
//        if (babyName != null)
//            babyName.setText(name);
//    }

//    public void addSettingBabyInfoListener(SettingBabyInfoListener listener) {
//        this.listener = listener;
//    }

}
