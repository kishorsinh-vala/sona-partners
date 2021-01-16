package com.loginiusinfotech.sonapartner.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductListBody;
import com.loginiusinfotech.sonapartner.modal.setting.maintainstock.MaintaiStock;
import com.loginiusinfotech.sonapartner.modal.setting.maintainstock.MaintaiStockBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryListBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageStockFragment extends Fragment {

    View root;
    Spinner spinner_cat, spinner_sub_cat, spinner_product, spinner_hl;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    EditText et_quantity, et_hl1, et_hl2, et_hl3, et_hl4, et_hl5, et_hl6, et_hl7;
    Button btn_submit;
    LinearLayout ll_sub, ll_hl;
    String str_cat_id_spinner, str_sub_cat_id_spinner, str_product_spinner, str_hl;

    public ManageStockFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_manage_stock, container, false);
        spinner_cat = root.findViewById(R.id.spinner_cat);
        spinner_sub_cat = root.findViewById(R.id.spinner_sub_cat);
        spinner_product = root.findViewById(R.id.spinner_product);
        spinner_hl = root.findViewById(R.id.spinner_hl);
        et_quantity = root.findViewById(R.id.et_quantity);
        btn_submit = root.findViewById(R.id.btn_submit);
        ll_sub = root.findViewById(R.id.ll_sub);
        ll_hl = root.findViewById(R.id.ll_hl);
        et_hl1 = root.findViewById(R.id.et_hl1);
        et_hl2 = root.findViewById(R.id.et_hl2);
        et_hl3 = root.findViewById(R.id.et_hl3);
        et_hl4 = root.findViewById(R.id.et_hl4);
        et_hl5 = root.findViewById(R.id.et_hl5);
        et_hl6 = root.findViewById(R.id.et_hl6);
        et_hl7 = root.findViewById(R.id.et_hl7);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("please wait ...");
        pd.setCancelable(false);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maintainStock(et_quantity.getText().toString(),str_hl, et_hl1.getText().toString(), et_hl2.getText().toString(), et_hl3.getText().toString(), et_hl4.getText().toString(), et_hl5.getText().toString(), et_hl6.getText().toString(), et_hl7.getText().toString());
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ArrayList<String> categorArrayList = new ArrayList<>();
            categorArrayList.add("No");
            categorArrayList.add("Yes");
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                    R.layout.item_spinner_list_category,
                    R.id.title, categorArrayList);
            spinner_hl.setAdapter(adapter);
            spinner_hl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    str_hl = adapterView.getItemAtPosition(position).toString().toLowerCase();
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
            categoryList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void categoryList() {
        pd.show();
        Call<CategoryList> request = mApiInterface.categoryList();
        request.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            categorArrayList.add(response.body().getData().get(i).getCategory());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_cat.setAdapter(adapter);
                        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                str_cat_id_spinner = response.body().getData().get(position).getId();
                                subCategoryList(response.body().getData().get(position).getId());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinner_cat.setEnabled(true);
                    } else {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        categorArrayList.add(response.body().getMessage());
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_cat.setAdapter(adapter);
                        spinner_cat.setEnabled(false);
                        et_quantity.setText("");
                        ll_sub.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void subCategoryList(String cat_id) {
        pd.show();
        Call<SubCategoryList> request = mApiInterface.subCategoryList(new SubCategoryListBody(cat_id));
        request.enqueue(new Callback<SubCategoryList>() {
            @Override
            public void onResponse(Call<SubCategoryList> call, Response<SubCategoryList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            categorArrayList.add(response.body().getData().get(i).getSubcategory_name());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_sub_cat.setAdapter(adapter);
                        spinner_sub_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                str_sub_cat_id_spinner = response.body().getData().get(position).getId();
                                productList(cat_id, response.body().getData().get(position).getId());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinner_sub_cat.setEnabled(true);
                    } else {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        categorArrayList.add(response.body().getMessage());
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_sub_cat.setAdapter(adapter);
                        spinner_sub_cat.setEnabled(false);
                        et_quantity.setText("");
                        ll_sub.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<SubCategoryList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void productList(String cat_id, String subcat_id) {
        pd.show();
        Call<ProductList> request = mApiInterface.productList(new ProductListBody(cat_id, subcat_id));
        request.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            categorArrayList.add(response.body().getData().get(i).getProductName());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_product.setAdapter(adapter);
                        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                str_product_spinner = response.body().getData().get(position).getId();
                                if (response.body().getResponsecode() == 200) {
                                    et_quantity.setText("" + response.body().getData().get(position).getQuantity());
                                    str_hl = response.body().getData().get(position).getHL();
                                    if (str_hl.equals("yes")) {
                                        ll_hl.setVisibility(View.VISIBLE);
                                        et_hl1.setText(response.body().getData().get(position).getHL1());
                                        et_hl2.setText(response.body().getData().get(position).getHL2());
                                        et_hl3.setText(response.body().getData().get(position).getHL3());
                                        et_hl4.setText(response.body().getData().get(position).getHL4());
                                        et_hl5.setText(response.body().getData().get(position).getHL5());
                                        et_hl6.setText(response.body().getData().get(position).getHL6());
                                        et_hl7.setText(response.body().getData().get(position).getHL7());
                                        spinner_hl.setSelection(1);
                                    } else {
                                        ll_hl.setVisibility(View.GONE);
                                        spinner_hl.setSelection(0);
                                        et_hl1.setText("");
                                        et_hl2.setText("");
                                        et_hl3.setText("");
                                        et_hl4.setText("");
                                        et_hl5.setText("");
                                        et_hl6.setText("");
                                        et_hl7.setText("");
                                    }
                                } else {
                                    ll_hl.setVisibility(View.GONE);
                                    et_quantity.setText("");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinner_product.setEnabled(true);
                        ll_sub.setVisibility(View.VISIBLE);
                    } else {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        categorArrayList.add(response.body().getMessage());
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_product.setAdapter(adapter);
                        spinner_product.setEnabled(false);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        et_quantity.setText("");
                        ll_sub.setVisibility(View.GONE);
                    }
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void maintainStock(String quantity, String hl, String hl1, String hl2, String hl3, String hl4, String hl5, String hl6, String hl7) {
        pd.show();
        Call<MaintaiStock> request = mApiInterface.maintainStock(new MaintaiStockBody(str_product_spinner, str_cat_id_spinner, str_sub_cat_id_spinner, quantity,
                hl,hl1,hl2,hl3,hl4,hl5,hl6,hl7));
        request.enqueue(new Callback<MaintaiStock>() {
            @Override
            public void onResponse(Call<MaintaiStock> call, Response<MaintaiStock> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<MaintaiStock> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
}