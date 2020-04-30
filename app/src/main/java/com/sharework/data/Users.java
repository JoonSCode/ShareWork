package com.sharework.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Users implements Serializable {
    private String id;
    private String email;
    private String name;
    private int phone_num;
    private List<String> status;
    private int status_idx;
    private int type;
    private Date created_at;
    private Date last_login_at;
    public Users() {
    }

    public Users(String id, String email, String name, int phone_num, List<String> status, int status_idx, int type, Date created_at, Date last_login_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone_num = phone_num;
        this.status = status;
        this.status_idx = status_idx;
        this.type = type;
        this.created_at = created_at;
        this.last_login_at = last_login_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(int phone_num) {
        this.phone_num = phone_num;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public int getStatus_idx() {
        return status_idx;
    }

    public void setStatus_idx(int status_idx) {
        this.status_idx = status_idx;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getLast_login_at() {
        return last_login_at;
    }

    public void setLast_login_at(Date last_login_at) {
        this.last_login_at = last_login_at;
    }
}
