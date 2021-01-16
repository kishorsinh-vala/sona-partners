package com.loginiusinfotech.sonapartner.modal.role;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoleListData {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("role")
    @Expose
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}