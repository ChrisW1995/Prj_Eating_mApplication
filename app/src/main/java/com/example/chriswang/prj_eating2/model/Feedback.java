package com.example.chriswang.prj_eating2.model;

/**
 * Created by ChrisWang on 2017/11/7.
 */

public class Feedback {
    private String r_id;
    private String C_Name;
    private String comment;
    private String commentTime;
    private String title;
    private float rateNum;

    public Feedback(String r_id, String C_Name, String comment,
                    String commentTime, int rateNum, String title) {
        this.r_id = r_id;
        this.C_Name = C_Name;
        this.comment = comment;
        this.commentTime = commentTime;
        this.rateNum = rateNum;
        this.title = title;
    }

    public Feedback() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getC_Name() {
        return C_Name;
    }

    public void setC_Name(String C_Name) {
        this.C_Name = C_Name;
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

    public float getRateNum() {
        return rateNum;
    }

    public void setRateNum(float rateNum) {
        this.rateNum = rateNum;
    }
}
