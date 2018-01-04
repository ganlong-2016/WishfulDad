package com.drkj.wishfuldad.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.drkj.wishfuldad.BaseApplication;
import com.drkj.wishfuldad.bean.BabyInfo;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.bean.MessageInfo;
import com.drkj.wishfuldad.bean.SettingInfo;
import com.drkj.wishfuldad.bean.YourInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ganlong on 2017/12/25.
 */

public class DbController {
    private static DbController instance;
    private SqlHelper sqlHelper;

    private DbController() {
        sqlHelper = new SqlHelper(BaseApplication.getInstance(), DbConstant.DB_NAME, null, DbConstant.DB_VERSION);
    }

    public static DbController getInstance() {
        if (instance == null) {
            instance = new DbController();
        }
        return instance;
    }
    public void updateBabyInfoData(BabyInfo bean) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("age", bean.getAge());
        values.put("sex", bean.getSex());
        values.put("height", bean.getHeight());
        values.put("weight", bean.getWeight());
        values.put("birthday_year", bean.getBirthDayYear());
        values.put("birthday_month", bean.getBirthDayMonth());
        values.put("birthday_day", bean.getBirthDayDay());
        values.put("bloodType", bean.getBloodType());
        values.put("imagePath", bean.getHeadImage());
        db.update(DbConstant.BABY_INFO_TABLE_NAME, values,null,null);
        sqlHelper.close();
    }

    public BabyInfo queryBabyInfoData() {
        BabyInfo bean = new BabyInfo();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.BABY_INFO_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            bean.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
            bean.setHeight(cursor.getFloat(cursor.getColumnIndex("height")));
            bean.setWeight(cursor.getFloat(cursor.getColumnIndex("weight")));
            bean.setBirthDayYear(cursor.getInt(cursor.getColumnIndex("birthday_year")));
            bean.setBirthDayMonth(cursor.getInt(cursor.getColumnIndex("birthday_month")));
            bean.setBirthDayDay(cursor.getInt(cursor.getColumnIndex("birthday_day")));
            bean.setBloodType(cursor.getInt(cursor.getColumnIndex("bloodType")));
            bean.setHeadImage(cursor.getString(cursor.getColumnIndex("imagePath")));
        }
        cursor.close();
        sqlHelper.close();
        return bean;

    }

    public void updateSettingData(SettingInfo bean) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isVibrate", bean.isVibrate());
        values.put("alarmSound", bean.getMusicType());
//        values.put("alarmTime", bean.getAlarmTime());
        values.put("weather", bean.getWeatherType());
        db.update(DbConstant.SETTING_TABLE_NAME,values,null,null);
        sqlHelper.close();
    }

    public SettingInfo querySettingData() {
        SettingInfo bean = new SettingInfo();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.SETTING_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            bean.setVibrate(cursor.getInt(cursor.getColumnIndex("isVibrate")));
            bean.setMusicType(cursor.getInt(cursor.getColumnIndex("alarmSound")));
//            bean.setAlarmTime(cursor.getInt(cursor.getColumnIndex("alarmTime")));
            bean.setWeatherType(cursor.getInt(cursor.getColumnIndex("weather")));
        }
        cursor.close();
        sqlHelper.close();
        return bean;
    }

    public void updateYourInfoData(YourInfo info){
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",info.getName());
        values.put("age",info.getAge());
        values.put("role",info.getRole());
        values.put("phone",info.getPhone());
        db.update(DbConstant.YOUR_INFO_TABLE_NAME,values,null,null);
        sqlHelper.close();
    }
    public YourInfo queryYourInfoData(){
        YourInfo bean = new YourInfo();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.YOUR_INFO_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            bean.setRole(cursor.getInt(cursor.getColumnIndex("role")));
            bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        }
        cursor.close();
        sqlHelper.close();
        return bean;
    }

    public List<DataBean> queryData(){
        List<DataBean> beans = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.DATA_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            while (cursor.moveToNext()){
                DataBean dataBean = new DataBean();
                dataBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time_stamp"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH");
                Date date = new Date(timeStamp);
                dataBean.setDate(simpleDateFormat.format(date));
                dataBean.setTime(simpleDateFormat2.format(date));
                dataBean.setHour(Integer.parseInt(simpleDateFormat3.format(date)));
                beans.add(dataBean);
            }
        }
        return beans;
    }

    public void insertData(int type,long timeStamp){
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type",type);
        values.put("time_stamp",timeStamp);
        db.insert(DbConstant.DATA_TABLE_NAME,null,values);
    }

    public List<MessageInfo> queryChatData(){
        List<MessageInfo> beans = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Cursor cursor = db.query(DbConstant.CHAT_DATA_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            while (cursor.moveToNext()){
                MessageInfo dataBean = new MessageInfo();
                dataBean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                dataBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                beans.add(dataBean);
            }
        }
        return beans;
    }
    public void insertChatData(MessageInfo info){
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type",info.getType());
        values.put("content",info.getContent());
        db.insert(DbConstant.CHAT_DATA_TABLE_NAME,null,values);
    }
}
