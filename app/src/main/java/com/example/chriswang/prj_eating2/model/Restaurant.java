package com.example.chriswang.prj_eating2.model;

/**
 * Created by ChrisWang on 2017/10/30.
 */

public class Restaurant {
    private String r_Name;
    private String r_Address;
    private String r_Phone;
    private String r_id;

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public Restaurant(String r_Name, String r_Address, String r_Phone, String r_id) {
        this.r_Name = r_Name;
        this.r_Address = r_Address;
        this.r_Phone = r_Phone;
        this.r_id = r_id;

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
