package com.drkj.wishfuldad.bean;

/**
 * Created by ganlong on 2017/12/11.
 */

public class WechatRefreshTokenResultBean {

    /**
     * openid : owybf1WfdbmcjlsVAgRrAkczZVNs
     * access_token : 4_OYim8Ue7-ru6AFvyJbs32fjwr29erUFIcYxj7IBXyIhJiPmvj6oyEZtScpEZf9HX1TaVDsh5FK8NpkgkouKCW0IaeuN0Qw_Zd9FiBQj1OKY
     * expires_in : 7200
     * refresh_token : 4_s597jn3EgZ_HshkosDn2-ApbZ-ndH0wuLSgj9iiC2hhcPfCMcjri2c9jdmyTgaa5unOGsTX-F9J8sqEG54_Ud6Ko6wbdX-jdyqVIwlO5NE0
     * scope : snsapi_base,snsapi_userinfo,
     */

    private String openid;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String scope;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
