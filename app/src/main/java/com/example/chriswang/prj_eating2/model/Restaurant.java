package com.example.chriswang.prj_eating2.model;

import android.support.annotation.NonNull;

/**
 * Created by ChrisWang on 2017/10/30.
 */

public class Restaurant {
    private String r_Name;
    private String r_Address;
    private String r_Phone;
    private String r_id;
    private String r_CloseTime;
    private String r_OpenTime;
    private String r_imgPath;
    private boolean wait_switch;
    private double r_lat;
    private double r_lng;
    private double distance;
    private float score;
    private boolean exist_coupon;
    private int waitNum;


    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public Restaurant(String r_Name, String r_Address, String r_Phone, String r_id,
                      String r_OpenTime, String r_CloseTime, String r_imgPath,float score, boolean wait_switch) {
        this.r_Name = r_Name;
        this.r_Address = r_Address;
        this.r_Phone = r_Phone;
        this.r_id = r_id;
        this.r_OpenTime = r_OpenTime;
        this.r_CloseTime = r_CloseTime;
        this.r_imgPath = r_imgPath;
        this.wait_switch = wait_switch;
        this.score = score;

    }

    public boolean isExist_coupon() {
        return exist_coupon;
    }

    public int getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(int waitNum) {
        this.waitNum = waitNum;
    }

    public void setExist_coupon(boolean exist_coupon) {
        this.exist_coupon = exist_coupon;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isWait_status() {
        return wait_switch;
    }

    public void setWait_status(boolean wait_status) {
        this.wait_switch= wait_status;
    }

    public double getR_lat() {
        return r_lat;
    }

    public void setR_lat(double r_lat) {
        this.r_lat = r_lat;
    }

    public double getR_lng() {
        return r_lng;
    }

    public void setR_lng(double r_lng) {
        this.r_lng = r_lng;
    }

    public String getR_imgPath() {
        return r_imgPath;
    }

    public void setR_imgPath(String r_imgPath) {
        this.r_imgPath = r_imgPath;
    }

    public String getR_CloseTime() {
        return r_CloseTime;
    }

    public void setR_CloseTime(String r_CloseTime) {
        this.r_CloseTime = r_CloseTime;
    }

    public String getR_OpenTime() {
        return r_OpenTime;
    }

    public void setR_OpenTime(String r_OpenTime) {
        this.r_OpenTime = r_OpenTime;
    }

    public Restaurant() {

    }

    public String getR_Name() {
        return r_Name;
    }

    public void setR_Name(String r_Name) {
        this.r_Name = r_Name;
    }

    public String getR_Address() {
        return r_Address;
    }

    public void setR_Address(String r_Address) {
        this.r_Address = r_Address;
    }

    public String getR_Phone() {
        return r_Phone;
    }

    public void setR_Phone(String r_Phone) {
        this.r_Phone = r_Phone;
    }


}
