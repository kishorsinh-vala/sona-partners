package com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryEdit;

public class SubCategoryEditBody {
    String subcat_id;
    String cat_id;
    String subcategoryName;

    public SubCategoryEditBody(String subcat_id, String cat_id, String subcategoryName) {
        this.subcat_id = subcat_id;
        this.cat_id = cat_id;
        this.subcategoryName = subcategoryName;
    }
}
