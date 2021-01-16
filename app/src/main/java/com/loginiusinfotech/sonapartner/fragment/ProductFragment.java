package com.loginiusinfotech.sonapartner.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.activity.AddProductActivity;
import com.loginiusinfotech.sonapartner.activity.ViewProductActivity;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductListBody;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductListData;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryListBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    View root;
    Recycle_Adpter recycle_adpter;
    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    SearchView searchView;
    Spinner spinner_cat,spinner_sub_cat;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    String str_cat_id_spinner,str_sub_cat_id_spinner;

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        searchView = root.findViewById(R.id.searchView);
        recyclerView.setNestedScrollingEnabled(false);
        fab_add = root.findViewById(R.id.fab_add);
        spinner_cat = root.findViewById(R.id.spinner_cat);
        spinner_sub_cat = root.findViewById(R.id.spinner_sub_cat);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                intent.putExtra("cat_id",""+str_cat_id_spinner);
                intent.putExtra("sub_cat_id",""+str_sub_cat_id_spinner);
                startActivity(intent);
            }
        });

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
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
                                subCategoryList(str_cat_id_spinner);
                                Pref.setPrefDate(getActivity(), AppConstants.SAVE_CAT_POSITION, ""+adapterView.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        try {
                            if (!Pref.getPrefDate(getActivity(), AppConstants.SAVE_CAT_POSITION, "").equals("")) {
                                ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) spinner_cat.getAdapter();
                                spinner_cat.setSelection(array_spinner.getPosition(Pref.getPrefDate(getActivity(), AppConstants.SAVE_CAT_POSITION, "")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
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
                                productList();
                                Pref.setPrefDate(getActivity(), AppConstants.SAVE_SUB_CAT_POSITION, ""+adapterView.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        try {
                            if (!Pref.getPrefDate(getActivity(), AppConstants.SAVE_SUB_CAT_POSITION, "").equals("")) {
                                ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) spinner_sub_cat.getAdapter();
                                spinner_sub_cat.setSelection(array_spinner.getPosition(Pref.getPrefDate(getActivity(), AppConstants.SAVE_SUB_CAT_POSITION, "")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                     } else {
                        recyclerView.setVisibility(View.GONE);
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

    private void productList() {
        pd.show();
        Call<ProductList> request = mApiInterface.productList(new ProductListBody(str_cat_id_spinner,str_sub_cat_id_spinner));
        request.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        recycle_adpter = new Recycle_Adpter(response.body().getData(), getActivity());
                        recyclerView.setAdapter(recycle_adpter);
//
                        searchView.setQueryHint(Html.fromHtml("<font color = #5D5F96> Search Product</font>"));
                        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                        searchView.setMaxWidth(Integer.MAX_VALUE);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                // filter recycler view when query submitted
                                recycle_adpter.getFilter().filter(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {
                                recycle_adpter.getFilter().filter(query);
                                return false;
                            }
                        });
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    public class Recycle_Adpter extends RecyclerView.Adapter {

        List<ProductListData> spacecrafts;
        List<ProductListData> spacecraftsFilter;
        Context mContext;

        public Recycle_Adpter(List<ProductListData> data, Context mContext) {
            this.spacecrafts = data;
            this.spacecraftsFilter = data;
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int i) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_list_nav_home_fragment, parent, false);
            // set the view's size, margins, paddings and layout parameters
            Recycle_Adpter.MyViewHolder vh = new Recycle_Adpter.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@androidx.annotation.NonNull final RecyclerView.ViewHolder holder, final int position) {
            final Recycle_Adpter.MyViewHolder holder1 = (Recycle_Adpter.MyViewHolder) holder;
            final ProductListData productListData = (ProductListData) spacecraftsFilter.get(position);
            Picasso.get().load("" + productListData.getImages().get(0)).error(R.mipmap.ic_launcher_foreground).into(holder1.image);
            holder1.title.setText(productListData.getProductName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder1.btn_design.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddProductActivity.class);
                    intent.putExtra("cat_id",""+str_cat_id_spinner);
                    intent.putExtra("sub_cat_id",""+str_sub_cat_id_spinner);
                    intent.putExtra("product_id",""+productListData.getId());
                    mContext.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return spacecraftsFilter.size();
        }

        @Override
        public long getItemId(int position) {
            return getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            Button btn_design;
            TextView title;

            public MyViewHolder(@androidx.annotation.NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.images);
                title = itemView.findViewById(R.id.title);
                btn_design = itemView.findViewById(R.id.btn_design);
            }
        }

        public Filter getFilter() {
            return new Filter() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    try {
                        if (charSequence.toString().isEmpty()) {
                            spacecraftsFilter = spacecrafts;
                        } else {
                            List<ProductListData> filteredList = new ArrayList<>();
                            for (ProductListData row : spacecrafts) {
                                if (row.getProductName().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
                                    filteredList.add(row);
                                }
                            }
                            spacecraftsFilter = filteredList;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = spacecraftsFilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    spacecraftsFilter = (ArrayList<ProductListData>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }
}