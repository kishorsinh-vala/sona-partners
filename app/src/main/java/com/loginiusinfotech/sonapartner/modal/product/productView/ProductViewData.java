package com.loginiusinfotech.sonapartner.modal.product.productView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductViewData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("subcategory")
    @Expose
    private String subcategory;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("images")
    @Expose
    private ArrayList<String> images;

    @SerializedName("offer")
    @Expose
    private String offer;

    @SerializedName("offertype")
    @Expose
    private String offertype;

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("quantity")
    @Expose
    private String quantity;


    @SerializedName("HL")
    @Expose
    private String HL;

    @SerializedName("HL1")
    @Expose
    private String HL1;


    @SerializedName("HL2")
    @Expose
    private String HL2;


    @SerializedName("HL3")
    @Expose
    private String HL3;


    @SerializedName("HL4")
    @Expose
    private String HL4;


    @SerializedName("HL5")
    @Expose
    private String HL5;


    @SerializedName("HL6")
    @Expose
    private String HL6;


    @SerializedName("HL7")
    @Expose
    private String HL7;

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

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getOffertype() {
        return offertype;
    }

    public void setOffertype(String offertype) {
        this.offertype = offertype;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getHL() {
        return HL;
    }

    public void setHL(String HL) {
        this.HL = HL;
    }

    public String getHL1() {
        return HL1;
    }

    public void setHL1(String HL1) {
        this.HL1 = HL1;
    }

    public String getHL2() {
        return HL2;
    }

    public void setHL2(String HL2) {
        this.HL2 = HL2;
    }

    public String getHL3() {
        return HL3;
    }

    public void setHL3(String HL3) {
        this.HL3 = HL3;
    }

    public String getHL4() {
        return HL4;
    }

    public void setHL4(String HL4) {
        this.HL4 = HL4;
    }

    public String getHL5() {
        return HL5;
    }

    public void setHL5(String HL5) {
        this.HL5 = HL5;
    }

    public String getHL6() {
        return HL6;
    }

    public void setHL6(String HL6) {
        this.HL6 = HL6;
    }

    public String getHL7() {
        return HL7;
    }

    public void setHL7(String HL7) {
        this.HL7 = HL7;
    }
}
