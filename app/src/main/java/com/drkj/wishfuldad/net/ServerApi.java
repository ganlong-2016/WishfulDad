package com.drkj.wishfuldad.net;

import com.drkj.wishfuldad.bean.ChatResultBean;
import com.drkj.wishfuldad.bean.IdentifyResultBean;
import com.drkj.wishfuldad.bean.LoginResultBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ganlong on 2017/12/11.
 */

public interface ServerApi {

    @FormUrlEncoded
    @POST(ConstantUrl.API_IDENTIFY)
    Observable<IdentifyResultBean> identify(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(ConstantUrl.API_LOGIN)
    Observable<LoginResultBean> login(@Field("phone") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST(ConstantUrl.API_AUTOLOGIN)
    Observable<LoginResultBean> autoLogin(@Field("token") String token);

    @FormUrlEncoded
    @POST(ConstantUrl.API_WX_LOGIN)
    Observable<LoginResultBean> wxLogin(@Field("uuid") String uuid);

    @FormUrlEncoded
    @POST(ConstantUrl.API_USER_UPDATE)
    Observable<LoginResultBean> userUpdate(@Field("token") String token,@Field("name") String name,@Field("age") int age,@Field("role") int role);

    @FormUrlEncoded
    @POST(ConstantUrl.API_CHILD_UPDATE)
    Observable<LoginResultBean> childUpdate(@Field("token") String token,@Field("cname") String cname,@Field("cage") int cage,@Field("csex") int csex,@Field("cweight") float cweight,@Field("wheight") int wheight,@Field("cbloodtype") int cbloodtype);

    @Multipart
    @POST(ConstantUrl.API_USER_ICON)
    Observable<LoginResultBean> userIcon(@Part List<MultipartBody.Part> partList);

    @FormUrlEncoded
    @POST(ConstantUrl.API_MSG_LIST)
    Observable<ChatResultBean> getMsgList(@Field("token") String token, @Field("isnew") int isnew, @Field("mid") int mid);

    @FormUrlEncoded
    @POST(ConstantUrl.API_SEND_MSG)
    Observable<ChatResultBean> sendMsg(@Field("token") String token, @Field("content") String content);

}
