package com.drkj.wishfuldad.bean;

/**
 * Created by ganlong on 2017/12/27.
 */

public class DataBean {
    public static final int TYPE_PEE = 0;
    public static final int TYPE_TIBEI = 1;
    public static final int TYPE_DIAPERS = 2;
    //0. 尿尿数据  1.踢被数据 2.尿片数据
    private int type;
    private String date;
    private String time;
    private int hour;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
