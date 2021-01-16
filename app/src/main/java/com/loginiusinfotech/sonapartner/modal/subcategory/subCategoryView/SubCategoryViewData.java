package com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategoryViewData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("size")
    @Expose
    private String size;

    @SerializedName("mob_no")
    @Expose
    private String mob_no;

    @SerializedName("whatsapp_no")
    @Expose
    private String whatsapp_no;

    @SerializedName("offer")
    @Expose
    private String offer;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("images")
    @Expose
    private ArrayList<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getWhatsapp_no() {
        return whatsapp_no;
    }

    public void setWhatsapp_no(String whatsapp_no) {
        this.whatsapp_no = whatsapp_no;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
