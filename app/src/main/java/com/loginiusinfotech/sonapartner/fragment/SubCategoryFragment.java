package com.loginiusinfotech.sonapartner.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.activity.AddCategoryActivity;
import com.loginiusinfotech.sonapartner.activity.AddSubCategoryActivity;
import com.loginiusinfotech.sonapartner.modal.category.categoryDelete.CategoryDelete;
import com.loginiusinfotech.sonapartner.modal.category.categoryDelete.CategoryDeleteBody;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryDelete.SubCategoryDelete;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryDelete.SubCategoryDeleteBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryListBody;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryListData;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryFragment extends Fragment {

    View root;
    Recycle_Adpter recycle_adpter;
    RecyclerView recyclerView;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    SearchView searchView;
    FloatingActionButton fab_add;
    Spinner spinner;
    String str_categoty_id_spinner;

    public SubCategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sub_category, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        searchView = root.findViewById(R.id.searchView);
        fab_add = root.findViewById(R.id.fab_add);
        spinner = root.findViewById(R.id.spinner);
        recyclerView.setNestedScrollingEnabled(false);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddSubCategoryActivity.class);
                intent.putExtra("cat_id",""+str_categoty_id_spinner);
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

    private void subCategoryList(String cat_id) {
        pd.show();
        Call<SubCategoryList> request = mApiInterface.subCategoryList(new SubCategoryListBody(cat_id));
        request.enqueue(new Callback<SubCategoryList>() {
            @Override
            public void onResponse(Call<SubCategoryList> call, Response<SubCategoryList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        recycle_adpter = new Recycle_Adpter(response.body().getData(), getActivity());
                        recyclerView.setAdapter(recycle_adpter);
//
                        searchView.setQueryHint(Html.fromHtml("<font color = #5D5F96> Search Sub Category</font>"));
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
            public void onFailure(Call<SubCategoryList> call, Throwable t) {
                pd.dismiss();
            }
        });
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
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                str_categoty_id_spinner = response.body().getData().get(position).getId();
                                subCategoryList(str_categoty_id_spinner);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
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

    private void categoryDelete(String id) {
        Call<SubCategoryDelete> request = mApiInterface.subCategoryDelete(new SubCategoryDeleteBody(id));
        request.enqueue(new Callback<SubCategoryDelete>() {
            @Override
            public void onResponse(Call<SubCategoryDelete> call, Response<SubCategoryDelete> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<SubCategoryDelete> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    public class Recycle_Adpter extends RecyclerView.Adapter {

        List<SubCategoryListData> spacecrafts;
        List<SubCategoryListData> spacecraftsFilter;
        Context mContext;

        public Recycle_Adpter(List<SubCategoryListData> data, Context mContext) {
            this.spacecrafts = data;
            this.spacecraftsFilter = data;
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int i) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_list_category, parent, false);
            // set the view's size, margins, paddings and layout parameters
            Recycle_Adpter.MyViewHolder vh = new Recycle_Adpter.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@androidx.annotation.NonNull final RecyclerView.ViewHolder holder, final int position) {
            final Recycle_Adpter.MyViewHolder holder1 = (Recycle_Adpter.MyViewHolder) holder;
            final SubCategoryListData subCategoryListData = (SubCategoryListData) spacecraftsFilter.get(position);
            holder1.tv_name.setText(subCategoryListData.getSubcategory_name());
            holder1.cv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddSubCategoryActivity.class);
                    intent.putExtra("sub_cat_id", "" + subCategoryListData.getId());
                    intent.putExtra("sub_cat_name", "" + subCategoryListData.getSubcategory_name());
                    intent.putExtra("cat_id", "" + str_categoty_id_spinner);
                    mContext.startActivity(intent);
                }
            });
            holder1.cv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete Category?")
                            .setMessage("Are you sure you want to delete " + subCategoryListData.getSubcategory_name() + "?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    categoryDelete(subCategoryListData.getId());
                                }
                            }).create().show();
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

        public Filter getFilter() {
            return new Filter() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    try {
                        if (charSequence.toString().isEmpty()) {
                            spacecraftsFilter = spacecrafts;
                        } else {
                            List<SubCategoryListData> filteredList = new ArrayList<>();
                            for (SubCategoryListData row : spacecrafts) {
                                if (row.getSubcategory_name().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
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
                    spacecraftsFilter = (ArrayList<SubCategoryListData>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name;
            FloatingActionButton cv_edit, cv_delete;

            public MyViewHolder(@androidx.annotation.NonNull View itemView) {
                super(itemView);

                tv_name = itemView.findViewById(R.id.tv_name);
                cv_edit = itemView.findViewById(R.id.cv_edit);
                cv_delete = itemView.findViewById(R.id.cv_delete);
            }
        }
    }
}