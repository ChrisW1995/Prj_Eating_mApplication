package com.example.chriswang.prj_eating2.model;

/**
 * Created by ChrisWang on 2017/12/22.
 */

public class WaitList {
    private String r_id;
    private String r_name;
    private String r_phone;
    private String r_addr;
    private int myNum;
    private int restNum;
    private int id;

    public WaitList() {
    }

    public String getR_id() {
        return r_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_phone() {
        return r_phone;
    }

    public void setR_phone(String r_phone) {
        this.r_phone = r_phone;
    }

    public String getR_addr() {
        return r_addr;
    }

    public void setR_addr(String r_addr) {
        this.r_addr = r_addr;
    }

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }

    public int getRestNum() {
        return restNum;
    }

    public void setRestNum(int restNum) {
        this.restNum = restNum;
    }
}
