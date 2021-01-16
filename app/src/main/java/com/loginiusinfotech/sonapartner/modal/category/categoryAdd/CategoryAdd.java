package com.loginiusinfotech.sonapartner.modal.category.categoryAdd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryAdd {

    @SerializedName("responsecode")
    @Expose
    private Integer responsecode;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
