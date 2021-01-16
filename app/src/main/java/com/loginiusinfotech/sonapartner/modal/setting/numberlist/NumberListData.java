package com.loginiusinfotech.sonapartner.modal.setting.numberlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberListData {
    @SerializedName("key_of")
    @Expose
    String key_of;

    @SerializedName("value")
    @Expose
    String value;

    public String getKey_of() {
        return key_of;
    }

    public void setKey_of(String key_of) {
        this.key_of = key_of;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
