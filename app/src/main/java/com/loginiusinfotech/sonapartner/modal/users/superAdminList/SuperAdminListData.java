package com.loginiusinfotech.sonapartner.modal.users.superAdminList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuperAdminListData {
    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("role_id")
    @Expose
    private String role_id;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("status")
    @Expose
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}