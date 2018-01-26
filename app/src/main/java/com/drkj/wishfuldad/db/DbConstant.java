package com.drkj.wishfuldad.db;

/**
 * Created by ganlong on 2017/12/25.
 */

public class DbConstant {
    public static final String DB_NAME = "baby_monitor";

    public static final int DB_VERSION = 1;

    public static final String BABY_INFO_TABLE_NAME = "baby_info";

    public static final String CREATE_BABY_INFO_TABLE =
            "create table "+BABY_INFO_TABLE_NAME+
                    "(id int auto_increment primary key," +
                    "name varchar(256),"+
                    "age integer,"+
                    "sex integer," +
                    "height float(5,1)," +
                    "weight float(5,1)," +
                    "birthday_year integer," +
                    "birthday_month integer," +
                    "birthday_day integer," +
                    "bloodType integer," +
                    "imagePath varchar(256)" +
                    ")";

    public static final String SETTING_TABLE_NAME = "setting";

    public static final String CREATE_SETTING_TABLE =
            "create table "+SETTING_TABLE_NAME+
                    "(id int auto_increment primary key," +
                    "isVibrate integer," +
                    "alarmSound integer," +
//                    "alarmTime integer," +
                    "weather integer)";

    public static final String INSERT_DEFAULT_SETTING =
            "insert into "+SETTING_TABLE_NAME+
                    "(isVibrate,alarmSound,weather) " +
                    "values (0,1,0)";
    public static final String YOUR_INFO_TABLE_NAME = "your_info";

    public static final String CREATE_YOUR_INFO_TABLE =
            "create table "+YOUR_INFO_TABLE_NAME+
                    "(id int auto_increment primary key," +
                    "name varchar(256),"+
                    "age integer,"+
                    "role integer,"+
                    "phone varchar(256)" +
                    ")";

    public static final String INSERT_DEFAULT_BABY_INFO =
            "insert into "+BABY_INFO_TABLE_NAME+
                    "(name,age,sex,height,weight,birthday_year,birthday_month,birthday_day,bloodType,imagePath)" +
                    "values (null,null,null,null,null,null,null,null,null,null)";

    public static final String INSERT_DEFAULT_YOUR_INFO =
            "insert into "+YOUR_INFO_TABLE_NAME+
                    "(name,age,role,phone)" +
                    "values (null,null,null,null)";

    public static final String DATA_TABLE_NAME = "data";

    public static final String CREATE_DATA_TABLE =
            "create table "+DATA_TABLE_NAME+
                    "(id int auto_increment primary key," +
                    "type integer,"+
                    "time_stamp long"+
                    ")";

    public static final String CHAT_DATA_TABLE_NAME = "chat_data";

    public static final String CREATE_CHAT_DATA_TABLE =
            "create table "+CHAT_DATA_TABLE_NAME+
                    "(id int auto_increment primary key," +
                    "type integer,"+
                    "content text,"+
                    "time_stamp long"+
                    ")";
}
