package com.drkj.wishfuldad.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ganlong on 2017/12/25.
 */

public class SqlHelper extends SQLiteOpenHelper {
    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstant.CREATE_BABY_INFO_TABLE);
        db.execSQL(DbConstant.CREATE_SETTING_TABLE);
        db.execSQL(DbConstant.CREATE_YOUR_INFO_TABLE);
        db.execSQL(DbConstant.CREATE_DATA_TABLE);
        db.execSQL(DbConstant.CREATE_CHAT_DATA_TABLE);

        db.execSQL(DbConstant.INSERT_DEFAULT_SETTING);
        db.execSQL(DbConstant.INSERT_DEFAULT_BABY_INFO);
        db.execSQL(DbConstant.INSERT_DEFAULT_YOUR_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
