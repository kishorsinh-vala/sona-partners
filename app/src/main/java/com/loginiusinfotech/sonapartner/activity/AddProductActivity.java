package com.loginiusinfotech.sonapartner.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.modal.product.productAdd.ProductAdd;
import com.loginiusinfotech.sonapartner.modal.product.productAdd.ProductAddBody;
import com.loginiusinfotech.sonapartner.modal.product.productDelete.ProductDelete;
import com.loginiusinfotech.sonapartner.modal.product.productDelete.ProductDeleteBody;
import com.loginiusinfotech.sonapartner.modal.product.productEdit.ProductEdit;
import com.loginiusinfotech.sonapartner.modal.product.productEdit.ProductEditBody;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductView;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductViewBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    TextView title;
    Button btn_mul_category, btn_submit, btn_delete;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    ArrayList<String> imagesEncodedList;
    Spinner spinner_offer, spinner_hl;
    LinearLayout ll_offer, ll_hl;
    EditText et_product_name, et_description, et_offer_type, et_quantity,
            et_hl1, et_hl2, et_hl3, et_hl4, et_hl5, et_hl6, et_hl7;
    String str_offer, str_hl, cat_id = "", sub_cat_id = "", product_id = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 200 && resultCode == RESULT_OK && null != data) {
                imagesEncodedList = new ArrayList<String>();
                LinearLayout layout = (LinearLayout) findViewById(R.id.ll_dynamic_img);
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddProductActivity.this.getContentResolver(), uri);
                        ImageView image = new ImageView(this);
                        image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        image.setImageBitmap(bitmap);
                        image.setPadding(10, 10, 10, 10);
                        layout.addView(image);
                        imagesEncodedList.add(imageToString(bitmap));
                    }
                } else if (data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddProductActivity.this.getContentResolver(), data.getData());
                    ImageView image = new ImageView(this);
                    image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    image.setImageBitmap(bitmap);
                    image.setPadding(10, 10, 10, 10);
                    layout.addView(image);
                    imagesEncodedList.add(imageToString(bitmap));
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        et_hl1 = findViewById(R.id.et_hl1);
        et_hl2 = findViewById(R.id.et_hl2);
        et_hl3 = findViewById(R.id.et_hl3);
        et_hl4 = findViewById(R.id.et_hl4);
        et_hl5 = findViewById(R.id.et_hl5);
        et_hl6 = findViewById(R.id.et_hl6);
        et_hl7 = findViewById(R.id.et_hl7);
        title = findViewById(R.id.title);
        btn_submit = findViewById(R.id.btn_submit);
        btn_delete = findViewById(R.id.btn_delete);
        spinner_offer = findViewById(R.id.spinner_offer);
        spinner_hl = findViewById(R.id.spinner_hl);
        et_product_name = findViewById(R.id.et_product_name);
        et_offer_type = findViewById(R.id.et_offer_type);
        et_description = findViewById(R.id.et_description);
        et_quantity = findViewById(R.id.et_quantity);
        ll_offer = findViewById(R.id.ll_offer);
        ll_hl = findViewById(R.id.ll_hl);
        btn_mul_category = findViewById(R.id.btn_mul_category);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(AddProductActivity.this);
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        title.setText("Add Product");

        btn_mul_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AddProductActivity.this)
                        .setTitle("Delete Category?")
                        .setMessage("Are you sure you want to delete " + et_product_name.getText().toString() + "?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                productDelete(product_id);
                            }
                        }).create().show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_product_name.getText().toString().equals("")) {
                    et_product_name.setError("Enter product name");
                    return;
                }
                if (btn_submit.getText().toString().equals("Save")) {
                    productEdit(product_id,
                            cat_id, sub_cat_id,
                            et_product_name.getText().toString(),
                            et_description.getText().toString(),
                            str_offer,
                            et_quantity.getText().toString(),
                            et_offer_type.getText().toString(),
                            str_hl, et_hl1.getText().toString(), et_hl2.getText().toString(), et_hl3.getText().toString(), et_hl4.getText().toString(), et_hl5.getText().toString(), et_hl6.getText().toString(), et_hl7.getText().toString(),
                            imagesEncodedList
                    );
                } else {
                    productAdd(cat_id, sub_cat_id, et_product_name.getText().toString(), et_description.getText().toString(),
                            str_offer, et_quantity.getText().toString(), et_offer_type.getText().toString(),
                            str_hl, et_hl1.getText().toString(), et_hl2.getText().toString(), et_hl3.getText().toString(), et_hl4.getText().toString(), et_hl5.getText().toString(), et_hl6.getText().toString(), et_hl7.getText().toString(),
                            imagesEncodedList);
                }
            }
        });
        try {
            cat_id = getIntent().getExtras().getString("cat_id", "");
            sub_cat_id = getIntent().getExtras().getString("sub_cat_id", "");
            if (!getIntent().getExtras().getString("product_id", "").equals("")) {
                product_id = getIntent().getExtras().getString("product_id", "");
                productView(product_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinner_offer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_offer = adapterView.getItemAtPosition(i).toString().toLowerCase();
                if (str_offer.equals("yes")) {
                    ll_offer.setVisibility(View.VISIBLE);
                } else {
                    ll_offer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_hl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_hl = adapterView.getItemAtPosition(i).toString().toLowerCase();
                if (str_hl.equals("yes")) {
                    ll_hl.setVisibility(View.VISIBLE);
                } else {
                    ll_hl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void drawerBack(View view) {
        finish();
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void productAdd(String cat_id, String subcat_id, String productName, String description, String offer, String quantity, String offertype, String hl, String hl1, String hl2, String hl3, String hl4, String hl5, String hl6, String hl7, ArrayList<String> images) {
        pd.show();
        Call<ProductAdd> request = mApiInterface.productAdd(new ProductAddBody(cat_id, subcat_id, productName, description, offer, quantity, offertype, hl, hl1, hl2, hl3, hl4, hl5, hl6, hl7, images));
        request.enqueue(new Callback<ProductAdd>() {
            @Override
            public void onResponse(Call<ProductAdd> call, Response<ProductAdd> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<ProductAdd> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void productEdit(String product_id, String cat_id, String subcat_id, String productName, String description, String offer, String quantity, String offertype, String hl, String hl1, String hl2, String hl3, String hl4, String hl5, String hl6, String hl7, ArrayList<String> images) {
        pd.show();
        Call<ProductEdit> request = mApiInterface.productEdit(new ProductEditBody(product_id, cat_id, subcat_id, productName, description, offer, quantity, offertype, hl, hl1, hl2, hl3, hl4, hl5, hl6, hl7, images));
        request.enqueue(new Callback<ProductEdit>() {
            @Override
            public void onResponse(Call<ProductEdit> call, Response<ProductEdit> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<ProductEdit> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void productDelete(String product_id) {
        pd.show();
        Call<ProductDelete> request = mApiInterface.productDelete(new ProductDeleteBody(product_id));
        request.enqueue(new Callback<ProductDelete>() {
            @Override
            public void onResponse(Call<ProductDelete> call, Response<ProductDelete> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<ProductDelete> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void productView(String product_id) {
        pd.show();
        Call<ProductView> request = mApiInterface.productView(new ProductViewBody(product_id));
        request.enqueue(new Callback<ProductView>() {
            @Override
            public void onResponse(Call<ProductView> call, Response<ProductView> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        et_product_name.setText(response.body().getData().get(0).getProductName());
                        et_description.setText(response.body().getData().get(0).getDescription());
                        et_quantity.setText(response.body().getData().get(0).getQuantity());
                        et_offer_type.setText(response.body().getData().get(0).getOffertype());
                        et_hl1.setText(response.body().getData().get(0).getHL1());
                        et_hl2.setText(response.body().getData().get(0).getHL2());
                        et_hl3.setText(response.body().getData().get(0).getHL3());
                        et_hl4.setText(response.body().getData().get(0).getHL4());
                        et_hl5.setText(response.body().getData().get(0).getHL5());
                        et_hl6.setText(response.body().getData().get(0).getHL6());
                        et_hl7.setText(response.body().getData().get(0).getHL7());
                        if (response.body().getData().get(0).getOffer().equals("no")) {
                            spinner_offer.setSelection(0);
                        } else {
                            spinner_offer.setSelection(1);
                        }
                        if (response.body().getData().get(0).getHL().equals("no")) {
                            spinner_hl.setSelection(0);
                        } else {
                            spinner_hl.setSelection(1);
                        }
                        LinearLayout layout = (LinearLayout) findViewById(R.id.ll_dynamic_img);
                        for (int i = 0; i < response.body().getData().get(0).getImages().size(); i++) {
                            ImageView image = new ImageView(AddProductActivity.this);
                            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
                            image.setScaleType(ImageView.ScaleType.FIT_XY);
                            image.setPadding(10, 10, 10, 10);
                            Picasso.get().load("" + response.body().getData().get(0).getImages().get(i)).error(R.mipmap.ic_launcher_foreground).into(image);
                            layout.addView(image);
                        }
                        title.setText("Edit Product");
                        btn_submit.setText("Save");
                        btn_delete.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(AddProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<ProductView> call, Throwable t) {
                pd.cancel();
            }
        });
    }
}


