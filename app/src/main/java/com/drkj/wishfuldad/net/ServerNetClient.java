package com.drkj.wishfuldad.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ganlong on 2017/12/21.
 */

public class ServerNetClient {

    private static ServerNetClient instance;

    private Retrofit retrofit;

    private ServerNetClient(){
        Gson gson = new GsonBuilder().create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUrl.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static ServerNetClient getInstance(){
        if (instance==null)
            instance = new ServerNetClient();
        return instance;
    }

    public ServerApi getApi(){
        return retrofit.create(ServerApi.class);
    }
}
