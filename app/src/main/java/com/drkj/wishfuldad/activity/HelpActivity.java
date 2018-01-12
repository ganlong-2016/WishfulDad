package com.drkj.wishfuldad.activity;

import android.os.Bundle;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.R;

import butterknife.OnClick;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @OnClick(R.id.back_imageview)
    void back(){
        finish();
    }
}
