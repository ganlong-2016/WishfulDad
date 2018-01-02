package com.drkj.wishfuldad.bean;

/**
 * Created by ganlong on 2017/12/25.
 */

public class SettingInfo {

    //天气 0.正常天气 1.干燥天气 2.湿润天气
    private int weatherType;

    //是否震动 0.是 1.否
    private int isVibrate;

    //0.无 1.铃声1 2.铃声2 3.铃声3
    private int musicType;

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int weatherType) {
        this.weatherType = weatherType;
    }

    public int isVibrate() {
        return isVibrate;
    }

    public void setVibrate(int vibrate) {
        isVibrate = vibrate;
    }

    public int getMusicType() {
        return musicType;
    }

    public void setMusicType(int musicType) {
        this.musicType = musicType;
    }
}
