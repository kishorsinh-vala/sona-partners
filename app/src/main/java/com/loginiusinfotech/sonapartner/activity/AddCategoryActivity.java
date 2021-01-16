package com.loginiusinfotech.sonapartner.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAdd;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAddBody;
import com.loginiusinfotech.sonapartner.modal.category.categoryUpdate.CategoryUpdate;
import com.loginiusinfotech.sonapartner.modal.category.categoryUpdate.CategoryUpdateBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {

    EditText et_category_name;
    TextView title;
    Button btn_submit;
    ApiInterface mApiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        title = findViewById(R.id.title);
        et_category_name = findViewById(R.id.et_category_name);
        btn_submit = findViewById(R.id.btn_submit);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(AddCategoryActivity.this);
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        title.setText("Add Category");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_category_name.getText().toString().equals("")) {
                    et_category_name.setError("Enter Name");
                    return;
                }
                if (btn_submit.getText().toString().equals("Save")) {
                    categoryUpdate(getIntent().getExtras().getString("cat_id", ""),
                            et_category_name.getText().toString());
                } else {
                    categoryAdd(et_category_name.getText().toString());
                }
            }
        });

        try {
            if (!getIntent().getExtras().getString("cat_name", "").equals("")) {
                btn_submit.setText("Save");
                title.setText("Edit Category");
                et_category_name.setText("" + getIntent().getExtras().getString("cat_name", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawerBack(View view) {
        finish();
    }

    private void categoryAdd(String category) {
        pd.show();
        Call<CategoryAdd> request = mApiInterface.categoryAdd(new CategoryAddBody(category));
        request.enqueue(new Callback<CategoryAdd>() {
            @Override
            public void onResponse(Call<CategoryAdd> call, Response<CategoryAdd> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<CategoryAdd> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void categoryUpdate(String id, String category) {
        pd.show();
        Call<CategoryUpdate> request = mApiInterface.categoryUpdate(new CategoryUpdateBody(id, category));
        request.enqueue(new Callback<CategoryUpdate>() {
            @Override
            public void onResponse(Call<CategoryUpdate> call, Response<CategoryUpdate> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCategoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<CategoryUpdate> call, Throwable t) {
                pd.cancel();
            }
        });
    }
}