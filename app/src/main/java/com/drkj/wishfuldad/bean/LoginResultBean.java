package com.drkj.wishfuldad.bean;

/**
 * Created by ganlong on 2017/12/21.
 */

public class LoginResultBean {
    //status: 登录状态，90001表示token验证成功，90001表示token错误验证失败，90003表示token过期。
    //status: 登录或注册的状态，1表示成功，0表示失败。
    /**
     * status : 1
     * message : ok
     * data : {"id":12,"username":"wx-1514376872980","pwd":"","groupid":1,"phone":"","token":"8f9270daf067989d9b6a9c2398c6581a7492e7cb","time_out":1514981671,"uuid":"234234234234fsdfsdfsdfsdf44","name":"","age":0,"role":0,"cage":0,"cname":"","csex":0,"cphoto":"","signature":"","create_time":"2017-12-27 20:14:32","update_time":"2017-12-27 20:14:32","status":1,"sign":"","avatar":"/upload/timg2.jpg","is_online":""}
     */

    private int status;
    private String message;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 12
         * username : wx-1514376872980
         * pwd :
         * groupid : 1
         * phone :
         * token : 8f9270daf067989d9b6a9c2398c6581a7492e7cb
         * time_out : 1514981671
         * uuid : 234234234234fsdfsdfsdfsdf44
         * name :
         * age : 0
         * role : 0
         * cage : 0
         * cname :
         * csex : 0
         * cphoto :
         * signature :
         * create_time : 2017-12-27 20:14:32
         * update_time : 2017-12-27 20:14:32
         * status : 1
         * sign :
         * avatar : /upload/timg2.jpg
         * is_online :
         */

        private int id;
        private String username;
        private String pwd;
        private int groupid;
        private String phone;
        private String token;
        private int time_out;
        private String uuid;
        private String name;
        private int age;
        private int role;
        private int cage;
        private String cname;
        private int csex;
        private String cphoto;
        private String signature;
        private String create_time;
        private String update_time;
        private int status;
        private String sign;
        private String avatar;
        private String is_online;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public int getGroupid() {
            return groupid;
        }

        public void setGroupid(int groupid) {
            this.groupid = groupid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getTime_out() {
            return time_out;
        }

        public void setTime_out(int time_out) {
            this.time_out = time_out;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getCage() {
            return cage;
        }

        public void setCage(int cage) {
            this.cage = cage;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public int getCsex() {
            return csex;
        }

        public void setCsex(int csex) {
            this.csex = csex;
        }

        public String getCphoto() {
            return cphoto;
        }

        public void setCphoto(String cphoto) {
            this.cphoto = cphoto;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }
    }



}
