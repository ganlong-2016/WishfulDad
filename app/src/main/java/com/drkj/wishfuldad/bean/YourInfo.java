package com.drkj.wishfuldad.bean;

/**
 * Created by ganlong on 2017/12/25.
 */

public class YourInfo {
    private String name;
    private int age;
    //角色 1.baba 2.mama 3.yeye 4.nainai 5.qita
    private int role;
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
