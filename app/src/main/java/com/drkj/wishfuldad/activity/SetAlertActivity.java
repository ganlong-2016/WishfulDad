package com.drkj.wishfuldad.activity;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.drkj.wishfuldad.BaseActivity;
import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.R;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SetAlertActivity extends BaseActivity {

    @BindView(R.id.village_set_imageview)
    ImageView mOpenLayout;
    @BindView(R.id.layout_choose_music)
    LinearLayout mLayout;
    boolean open = true;
    @BindView(R.id.village_sel_imageview)
    CheckBox vibrate;
    @BindView(R.id.checkbox_raw1)
    CheckBox checkBox1;
    @BindView(R.id.checkbox_raw2)
    CheckBox checkBox2;
    @BindView(R.id.checkbox_raw3)
    CheckBox checkBox3;
    private SoundPool Cry0;
    private SoundPool Cry1;
    private SoundPool Cry2;
    private int StreamId0;
    private int StreamId1;
    private int StreamId2;
    boolean flag = false;
    boolean flag2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alert);

        Cry0 = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        Cry0.load(this, R.raw.cry0, 1);
        Cry1 = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        Cry1.load(this, R.raw.cry1, 1);
        Cry2 = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        Cry2.load(this, R.raw.cry2, 1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        int a = BaseApplication.getInstance().getSettingInfo().isVibrate();
        if (a == 0) {
            flag = true;
            vibrate.setChecked(true);
        }
        int b = BaseApplication.getInstance().getSettingInfo().getMusicType();
        switch (b) {
            case 1:
                flag2=true;
                checkBox1.setChecked(true);
                break;
            case 2:
                flag2=true;
                checkBox2.setChecked(true);
                break;
            case 3:
                flag2=true;
                checkBox3.setChecked(true);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.back_imageview)
    void back() {
        finish();
    }

    @OnClick(R.id.village_set_imageview)
    void openLayout() {
        if (open) {
            open = false;
            mOpenLayout.setImageDrawable(getResources().getDrawable(R.drawable.ic_arror));
            mLayout.setVisibility(View.GONE);
        } else {
            open = true;
            mOpenLayout.setImageDrawable(getResources().getDrawable(R.drawable.ic_arror_down));
            mLayout.setVisibility(View.VISIBLE);

        }
    }

    @OnCheckedChanged(R.id.village_sel_imageview)
    void changeZhengDong(CompoundButton buttonView, boolean isChecked) {
        if (flag){
            flag = false;
            return;
        }
        if (isChecked) {
            Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(3000);
            BaseApplication.getInstance().getSettingInfo().setVibrate(0);
        } else {
            Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vib.cancel();
            BaseApplication.getInstance().getSettingInfo().setVibrate(1);
        }
    }

    @OnCheckedChanged({R.id.checkbox_raw1, R.id.checkbox_raw2, R.id.checkbox_raw3})
    void chooseMusic(CompoundButton buttonView, boolean isChecked) {
        if (flag2){
            flag2 = false;
            return;
        }
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        switch (buttonView.getId()) {
            case R.id.checkbox_raw1:
                checkBox1.setChecked(isChecked);
                if (isChecked) {
                    StreamId0 = Cry0.play(1, 1, 1, 0, 0, 1);
                    BaseApplication.getInstance().getSettingInfo().setMusicType(1);
                } else {
                    Cry0.stop(StreamId0);
                    BaseApplication.getInstance().getSettingInfo().setMusicType(0);
                }
                Cry1.stop(StreamId1);
                Cry2.stop(StreamId2);
                break;
            case R.id.checkbox_raw2:
                checkBox2.setChecked(isChecked);
                if (isChecked) {
                    StreamId1 = Cry1.play(1, 1, 1, 0, 0, 1);
                    BaseApplication.getInstance().getSettingInfo().setMusicType(2);
                } else {
                    Cry1.stop(StreamId1);
                    BaseApplication.getInstance().getSettingInfo().setMusicType(0);
                }
                Cry0.stop(StreamId0);
                Cry2.stop(StreamId2);
                break;
            case R.id.checkbox_raw3:
                checkBox3.setChecked(isChecked);
                if (isChecked) {
                    StreamId2 = Cry2.play(1, 1, 1, 0, 0, 1);
                    BaseApplication.getInstance().getSettingInfo().setMusicType(3);
                } else {
                    Cry2.stop(StreamId2);
                    BaseApplication.getInstance().getSettingInfo().setMusicType(0);
                }
                Cry0.stop(StreamId0);
                Cry1.stop(StreamId1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cry0.stop(StreamId0);
        Cry1.stop(StreamId1);
        Cry2.stop(StreamId2);
    }
}
