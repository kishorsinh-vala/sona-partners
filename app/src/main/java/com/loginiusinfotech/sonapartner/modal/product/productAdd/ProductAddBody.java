package com.loginiusinfotech.sonapartner.modal.product.productAdd;

import java.util.ArrayList;

public class ProductAddBody {

    String cat_id;
    String subcat_id;
    String productName;
    String description;
    String offer;
    String quantity;
    String offertype;
    String hl;
    String hl1;
    String hl2;
    String hl3;
    String hl4;
    String hl5;
    String hl6;
    String hl7;
    ArrayList<String> images;

    public ProductAddBody(String cat_id, String subcat_id, String productName, String description, String offer, String quantity, String offertype, String hl, String hl1, String hl2, String hl3, String hl4, String hl5, String hl6, String hl7, ArrayList<String> images) {
        this.cat_id = cat_id;
        this.subcat_id = subcat_id;
        this.productName = productName;
        this.description = description;
        this.offer = offer;
        this.quantity = quantity;
        this.offertype = offertype;
        this.hl = hl;
        this.hl1 = hl1;
        this.hl2 = hl2;
        this.hl3 = hl3;
        this.hl4 = hl4;
        this.hl5 = hl5;
        this.hl6 = hl6;
        this.hl7 = hl7;
        this.images = images;
    }
}
