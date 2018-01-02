package com.drkj.wishfuldad.bean;

/**
 * Created by ganlong on 2017/12/25.
 */

public class BabyInfo {
    private String name;
    private int age;
    private int sex=2;
    private String headImage;
    private int birthDayYear;
    private int birthDayMonth;
    private int birthDayDay;
    //血型 0.A 1.B 2.AB 3.O
    private int bloodType=4;
    private float height;
    private float weight;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getBirthDayYear() {
        return birthDayYear;
    }

    public void setBirthDayYear(int birthDayYear) {
        this.birthDayYear = birthDayYear;
    }

    public int getBirthDayMonth() {
        return birthDayMonth;
    }

    public void setBirthDayMonth(int birthDayMonth) {
        this.birthDayMonth = birthDayMonth;
    }

    public int getBirthDayDay() {
        return birthDayDay;
    }

    public void setBirthDayDay(int birthDayDay) {
        this.birthDayDay = birthDayDay;
    }

    public int getBloodType() {
        return bloodType;
    }

    public void setBloodType(int bloodType) {
        this.bloodType = bloodType;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
