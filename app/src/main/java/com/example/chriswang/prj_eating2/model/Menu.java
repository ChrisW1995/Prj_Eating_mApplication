package com.example.chriswang.prj_eating2.model;

/**
 * Created by ChrisWang on 2017/12/17.
 */

public class Menu {
    private String r_id;
    private String name;
    private String path;

    public Menu(String r_id, String name, String path) {
        this.r_id = r_id;
        this.name = name;
        this.path = path;
    }

    public Menu() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
