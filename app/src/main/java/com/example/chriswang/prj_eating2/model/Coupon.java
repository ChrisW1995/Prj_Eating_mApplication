package com.example.chriswang.prj_eating2.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

/**
 * Created by ChrisWang on 2017/12/20.
 */

public class Coupon {
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String r_id;
    private boolean isReceived;
    private String couponId;

    public Coupon() {
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public String getCouponId() {
        return couponId;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime.toString();
    }

    public void setStartTime(String startTime)  {

        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime.toString();
    }

    public void setEndTime(String endTime) {

        this.endTime = endTime;

    }
}
