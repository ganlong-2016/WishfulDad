package com.drkj.wishfuldad.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ganlong on 2017/12/19.
 */

public class SpUtil {

    public static void setHasShowedGuidePage(Context context, String key, boolean hasShowed){
        SharedPreferences settings= context.getSharedPreferences(context.getPackageName(), 0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean(key, hasShowed);
        editor.apply();
    }

    public static boolean hasShowedGuidePage(Context context, String key){
        SharedPreferences settings=context.getSharedPreferences(context.getPackageName(), 0);
        boolean value=settings.getBoolean(key, false);
        return value;
    }

    public static void putString(Context context,String key,String value){
        SharedPreferences settings= context.getSharedPreferences(context.getPackageName(), 0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getToken(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences(context.getPackageName(), 0);
        String value = sp.getString(key,"");
        return value;
    }
    public static void putInt(Context context,String key,int value){
        SharedPreferences settings= context.getSharedPreferences(context.getPackageName(), 0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getInt(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences(context.getPackageName(), 0);
        int value = sp.getInt(key,0);
        return value;
    }
}
