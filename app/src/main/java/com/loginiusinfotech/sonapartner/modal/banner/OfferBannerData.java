package com.loginiusinfotech.sonapartner.modal.banner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferBannerData {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("bannerImages")
    @Expose
    private ArrayList<String> bannerImages;

    @SerializedName("offer")
    @Expose
    private String discouoffernt;

    @SerializedName("offertype")
    @Expose
    private String offertype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ArrayList<String> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(ArrayList<String> bannerImages) {
        this.bannerImages = bannerImages;
    }

    public String getDiscouoffernt() {
        return discouoffernt;
    }

    public void setDiscouoffernt(String discouoffernt) {
        this.discouoffernt = discouoffernt;
    }

    public String getOffertype() {
        return offertype;
    }

    public void setOffertype(String offertype) {
        this.offertype = offertype;
    }
}
