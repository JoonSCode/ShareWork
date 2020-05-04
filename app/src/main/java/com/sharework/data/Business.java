package com.sharework.data;

import java.util.Date;

public class Business {
    public String id;
    public String user_id;
    public String name;
    public String position;
    public String phone_no;
    public Date created_at;

    public Business(String id, String user_id, String name, String position, String phone_no, Date created_at) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.position = position;
        this.phone_no = phone_no;
        this.created_at = created_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
