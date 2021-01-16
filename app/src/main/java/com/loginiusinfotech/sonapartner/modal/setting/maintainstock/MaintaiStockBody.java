package com.loginiusinfotech.sonapartner.modal.setting.maintainstock;

public class MaintaiStockBody {
    String product_id;
    String cat_id;
    String subcat_id;
    String quantity;
    String hl;
    String hl1;
    String hl2;
    String hl3;
    String hl4;
    String hl5;
    String hl6;
    String hl7;

    public MaintaiStockBody(String product_id, String cat_id, String subcat_id, String quantity, String hl, String hl1, String hl2, String hl3, String hl4, String hl5, String hl6, String hl7) {
        this.product_id = product_id;
        this.cat_id = cat_id;
        this.subcat_id = subcat_id;
        this.quantity = quantity;
        this.hl = hl;
        this.hl1 = hl1;
        this.hl2 = hl2;
        this.hl3 = hl3;
        this.hl4 = hl4;
        this.hl5 = hl5;
        this.hl6 = hl6;
        this.hl7 = hl7;
    }
}
