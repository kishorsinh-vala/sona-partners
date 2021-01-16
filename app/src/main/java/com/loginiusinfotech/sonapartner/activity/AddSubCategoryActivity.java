package com.loginiusinfotech.sonapartner.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAdd;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAddBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryAdd.SubCategoryAdd;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryAdd.SubCategoryAddBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryEdit.SubCategoryEdit;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryEdit.SubCategoryEditBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryView.SubCategoryView;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryView.SubCategoryViewBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSubCategoryActivity extends AppCompatActivity {

    TextView title;
    Button btn_submit;
    String str_category_id = "";
    ApiInterface mApiInterface;
    ProgressDialog pd;
    Spinner spinner;
    EditText et_sub_category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_category);
        title = findViewById(R.id.title);
        btn_submit = findViewById(R.id.btn_submit);
        et_sub_category_name = findViewById(R.id.et_sub_category_name);
        spinner = findViewById(R.id.spinner);
        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(AddSubCategoryActivity.this);
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        title.setText("Add Subcategory");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_sub_category_name.getText().toString().equals("")){
                    et_sub_category_name.setError("Enter Sub Category Name");
                    return;
                }
                if (str_category_id.equals("")){
                    Toast.makeText(AddSubCategoryActivity.this, "Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (btn_submit.getText().toString().equals("Save")){
                    subCategoryEdit(getIntent().getExtras().getString("sub_cat_id", ""),
                            str_category_id,
                            et_sub_category_name.getText().toString());
                }else {
                    subCategoryAdd(str_category_id,
                            et_sub_category_name.getText().toString());
                }
            }
        });
        try {
            str_category_id=getIntent().getExtras().getString("cat_id", "");
            if (!getIntent().getExtras().getString("sub_cat_id", "").equals("")){
                title.setText("Edit Subcategory");
                btn_submit.setText("Save");
                et_sub_category_name.setText(getIntent().getExtras().getString("sub_cat_name", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawerBack(View view) {
        finish();
    }

    private void subCategoryAdd(String cat_id, String subcategoryName) {
        pd.show();
        Call<SubCategoryAdd> request = mApiInterface.subCategoryAdd(new SubCategoryAddBody(cat_id, subcategoryName));
        request.enqueue(new Callback<SubCategoryAdd>() {
            @Override
            public void onResponse(Call<SubCategoryAdd> call, Response<SubCategoryAdd> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddSubCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddSubCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<SubCategoryAdd> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void subCategoryEdit(String subcat_id, String cat_id, String subcategoryName) {
        pd.show();
        Call<SubCategoryEdit> request = mApiInterface.subCategoryEdit(new SubCategoryEditBody(subcat_id,cat_id,subcategoryName));
        request.enqueue(new Callback<SubCategoryEdit>() {
            @Override
            public void onResponse(Call<SubCategoryEdit> call, Response<SubCategoryEdit> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddSubCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddSubCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<SubCategoryEdit> call, Throwable t) {
                pd.cancel();
            }
        });
    }
}