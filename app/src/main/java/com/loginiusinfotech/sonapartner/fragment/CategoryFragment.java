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
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.activity.AddCategoryActivity;
import com.loginiusinfotech.sonapartner.modal.category.categoryDelete.CategoryDelete;
import com.loginiusinfotech.sonapartner.modal.category.categoryDelete.CategoryDeleteBody;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryList;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryListData;
import com.loginiusinfotech.sonapartner.modal.setting.numberlist.NumberList;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    View root;
    Recycle_Adpter recycle_adpter;
    RecyclerView recyclerView;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    SearchView searchView;
    FloatingActionButton fab_add;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        searchView = root.findViewById(R.id.searchView);
        fab_add = root.findViewById(R.id.fab_add);
        recyclerView.setNestedScrollingEnabled(false);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
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
            if (Pref.getPrefDate(getActivity(),AppConstants.SAVE_WHATSAPP_NUMBER,"").equals("")){
                numbersList();
            }else if (Pref.getPrefDate(getActivity(),AppConstants.SAVE_MOBILE_NUMBER,"").equals("")){
                numbersList();
            }
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
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        recycle_adpter = new Recycle_Adpter(response.body().getData(), getActivity());
                        recyclerView.setAdapter(recycle_adpter);
//
                        searchView.setQueryHint(Html.fromHtml("<font color = #5D5F96> Search Category</font>"));
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
            public void onFailure(Call<CategoryList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }


    public class Recycle_Adpter extends RecyclerView.Adapter {

        List<CategoryListData> spacecrafts;
        List<CategoryListData> spacecraftsFilter;
        Context mContext;

        public Recycle_Adpter(List<CategoryListData> data, Context mContext) {
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
            final CategoryListData categoryListData = (CategoryListData) spacecraftsFilter.get(position);
            holder1.tv_name.setText(categoryListData.getCategory());
            holder1.cv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddCategoryActivity.class);
                    intent.putExtra("cat_id",""+categoryListData.getId());
                    intent.putExtra("cat_name",""+categoryListData.getCategory());
                    mContext.startActivity(intent);
                }
            });
            holder1.cv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete Category?")
                            .setMessage("Are you sure you want to delete "+categoryListData.getCategory()+"?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    categoryDelete(categoryListData.getId());
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

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name;
            FloatingActionButton cv_edit,cv_delete;

            public MyViewHolder(@androidx.annotation.NonNull View itemView) {
                super(itemView);

                tv_name = itemView.findViewById(R.id.tv_name);
                cv_edit = itemView.findViewById(R.id.cv_edit);
                cv_delete = itemView.findViewById(R.id.cv_delete);
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
                            List<CategoryListData> filteredList = new ArrayList<>();
                            for (CategoryListData row : spacecrafts) {
                                if (row.getCategory().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
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
                    spacecraftsFilter = (ArrayList<CategoryListData>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    private void categoryDelete(String id) {
        Call<CategoryDelete> request = mApiInterface.categoryDelete(new CategoryDeleteBody(id));
        request.enqueue(new Callback<CategoryDelete>() {
            @Override
            public void onResponse(Call<CategoryDelete> call, Response<CategoryDelete> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<CategoryDelete> call, Throwable t) {
                pd.cancel();
            }
        });
    }
    private void numbersList() {
        pd.show();
        Call<NumberList> request = mApiInterface.numbersList();
        request.enqueue(new Callback<NumberList>() {
            @Override
            public void onResponse(Call<NumberList> call, Response<NumberList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Pref.setPrefDate(getActivity(), AppConstants.SAVE_MOBILE_NUMBER,""+response.body().getData().get(0).getValue());
                        Pref.setPrefDate(getActivity(),AppConstants.SAVE_WHATSAPP_NUMBER,""+response.body().getData().get(1).getValue());
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<NumberList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
}