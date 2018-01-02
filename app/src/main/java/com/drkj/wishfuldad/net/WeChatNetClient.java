package com.drkj.wishfuldad.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ganlong on 2017/12/11.
 */

public class WeChatNetClient {
    private static WeChatNetClient instance;

    private Retrofit retrofit;

    private WeChatNetClient(){
        Gson gson = new GsonBuilder().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUrl.WECHAT_BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static WeChatNetClient getInstance(){
        if (instance==null)
            instance = new WeChatNetClient();
        return instance;
    }

    public WeChatApi getApi(){
        return retrofit.create(WeChatApi.class);
    }
}
