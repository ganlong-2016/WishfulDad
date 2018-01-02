package com.drkj.wishfuldad.net;

import com.drkj.wishfuldad.bean.WechatAccessTokenResultBean;
import com.drkj.wishfuldad.bean.WechatRefreshTokenResultBean;
import com.drkj.wishfuldad.bean.WechatUserInfoResultBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ganlong on 2017/12/11.
 */

public interface WeChatApi {

    @GET(ConstantUrl.WECHAT_ACCESS_TOKEN)
    Observable<WechatAccessTokenResultBean> getAccessToken(@Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);

    @GET(ConstantUrl.WECHAT_REFRESH_TOKEN)
    Observable<WechatRefreshTokenResultBean> refreshToken(@Query("appid") String appid, @Query("grant_type") String grant_type, @Query("refresh_token") String refresh_token);

    @GET(ConstantUrl.WECHAT_USERINFO)
    Observable<WechatUserInfoResultBean> getUserInfo(@Query("access_token") String access_token, @Query("openid") String openid);


}
