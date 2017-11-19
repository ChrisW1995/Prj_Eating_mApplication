package com.example.chriswang.prj_eating2.model;

/**
 * Created by ChrisWang on 2017/11/7.
 */

public class Feedback {
    private String r_id;
    private String c_id;
    private String comment;
    private String commentTime;
    private int rateNum;

    public Feedback(String r_id, String c_id, String comment, String commentTime, int rateNum) {
        this.r_id = r_id;
        this.c_id = c_id;
        this.comment = comment;
        this.commentTime = commentTime;
        this.rateNum = rateNum;
    }

    public Feedback() {
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getRateNum() {
        return rateNum;
    }

    public void setRateNum(int rateNum) {
        this.rateNum = rateNum;
    }
}
