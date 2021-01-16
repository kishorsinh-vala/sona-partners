package com.loginiusinfotech.sonapartner.modal.category.categoryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryListData {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("category")
    @Expose
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}