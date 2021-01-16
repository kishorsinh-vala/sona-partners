package com.loginiusinfotech.sonapartner.remote;

import com.loginiusinfotech.sonapartner.modal.banner.OfferBanner;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAdd;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAddBody;
import com.loginiusinfotech.sonapartner.modal.category.categoryDelete.CategoryDelete;
import com.loginiusinfotech.sonapartner.modal.category.categoryDelete.CategoryDeleteBody;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryList;
import com.loginiusinfotech.sonapartner.modal.category.categoryUpdate.CategoryUpdate;
import com.loginiusinfotech.sonapartner.modal.category.categoryUpdate.CategoryUpdateBody;
import com.loginiusinfotech.sonapartner.modal.product.productAdd.ProductAdd;
import com.loginiusinfotech.sonapartner.modal.product.productAdd.ProductAddBody;
import com.loginiusinfotech.sonapartner.modal.product.productDelete.ProductDelete;
import com.loginiusinfotech.sonapartner.modal.product.productDelete.ProductDeleteBody;
import com.loginiusinfotech.sonapartner.modal.product.productEdit.ProductEdit;
import com.loginiusinfotech.sonapartner.modal.product.productEdit.ProductEditBody;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductListBody;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductView;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductViewBody;
import com.loginiusinfotech.sonapartner.modal.role.RoleList;
import com.loginiusinfotech.sonapartner.modal.setting.maintainstock.MaintaiStock;
import com.loginiusinfotech.sonapartner.modal.setting.maintainstock.MaintaiStockBody;
import com.loginiusinfotech.sonapartner.modal.setting.numberlist.NumberList;
import com.loginiusinfotech.sonapartner.modal.setting.setting.Setting;
import com.loginiusinfotech.sonapartner.modal.setting.setting.SettingBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryAdd.SubCategoryAdd;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryAdd.SubCategoryAddBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryDelete.SubCategoryDelete;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryDelete.SubCategoryDeleteBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryEdit.SubCategoryEdit;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryEdit.SubCategoryEditBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryListBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryView.SubCategoryView;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryView.SubCategoryViewBody;
import com.loginiusinfotech.sonapartner.modal.users.changeStatus.ChangeStatus;
import com.loginiusinfotech.sonapartner.modal.users.changeStatus.ChangeStatusBody;
import com.loginiusinfotech.sonapartner.modal.users.login.Login;
import com.loginiusinfotech.sonapartner.modal.users.login.LoginBody;
import com.loginiusinfotech.sonapartner.modal.users.superAdminList.SuperAdminList;
import com.loginiusinfotech.sonapartner.modal.users.usersAdd.UsersAdd;
import com.loginiusinfotech.sonapartner.modal.users.usersAdd.UsersAddBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("banner/offerbanner.php")
    Call<OfferBanner> bannerList();

    @GET("category/list.php")
    Call<CategoryList> categoryList();

    @POST("category/add.php")
    Call<CategoryAdd> categoryAdd(@Body CategoryAddBody body);

    @POST("category/update.php")
    Call<CategoryUpdate> categoryUpdate(@Body CategoryUpdateBody body);

    @POST("category/delete.php")
    Call<CategoryDelete> categoryDelete(@Body CategoryDeleteBody body);

    @GET("role/list.php")
    Call<RoleList> roleList();

    @POST("subcategory/list.php")
    Call<SubCategoryList> subCategoryList(@Body SubCategoryListBody body);

    @POST("subcategory/add.php")
    Call<SubCategoryAdd> subCategoryAdd(@Body SubCategoryAddBody body);

    @POST("subcategory/view.php")
    Call<SubCategoryView> subCategoryView(@Body SubCategoryViewBody body);

    @POST("subcategory/edit.php")
    Call<SubCategoryEdit> subCategoryEdit(@Body SubCategoryEditBody body);

    @POST("subcategory/delete.php")
    Call<SubCategoryDelete> subCategoryDelete(@Body SubCategoryDeleteBody body);

    @GET("users/superAdminList.php")
    Call<SuperAdminList> superAdminList();

    @POST("users/add.php")
    Call<UsersAdd> usersAdd(@Body UsersAddBody body);

    @POST("users/login.php")
    Call<Login> usersLogin(@Body LoginBody body);

    @POST("users/changeStatus.php")
    Call<ChangeStatus> changeStatus(@Body ChangeStatusBody body);

    @POST("product/add.php")
    Call<ProductAdd> productAdd(@Body ProductAddBody body);

    @POST("product/list.php")
    Call<ProductList> productList(@Body ProductListBody body);

    @POST("product/edit.php")
    Call<ProductEdit> productEdit(@Body ProductEditBody body);

    @POST("product/delete.php")
    Call<ProductDelete> productDelete(@Body ProductDeleteBody body);

    @POST("product/view.php")
    Call<ProductView> productView(@Body ProductViewBody body);

    @POST("setting/maintainstock.php")
    Call<MaintaiStock> maintainStock(@Body MaintaiStockBody body);

    @POST("setting/homescrenn.php")
    Call<ProductList> homeScreen(@Body ProductListBody body);

    @POST("setting/setting.php")
    Call<Setting> settingNumbers(@Body SettingBody body);

    @GET("setting/numberlist.php")
    Call<NumberList> numbersList();

}