package com.loginiusinfotech.sonapartner.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.modal.users.changeStatus.ChangeStatus;
import com.loginiusinfotech.sonapartner.modal.users.changeStatus.ChangeStatusBody;
import com.loginiusinfotech.sonapartner.modal.users.superAdminList.SuperAdminList;
import com.loginiusinfotech.sonapartner.modal.users.superAdminList.SuperAdminListData;
import com.loginiusinfotech.sonapartner.modal.users.usersAdd.UsersAdd;
import com.loginiusinfotech.sonapartner.modal.users.usersAdd.UsersAddBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuperAdminActivity extends AppCompatActivity {
    Recycle_Adpter recycle_adpter;
    RecyclerView recyclerView;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    TextView title;
    SearchView searchView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);
        recyclerView = findViewById(R.id.recyclerView);
        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(SuperAdminActivity.this);
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        title = findViewById(R.id.title);
        title.setText("Super Admin");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            pd.show();
            superAdminList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SuperAdminActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.exit_layout, null);

        Button btn_cancel = (Button) mView.findViewById(R.id.btn_ext_no);
        Button btn_okey = (Button) mView.findViewById(R.id.btn_ext_yes);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void superAdminList() {
        Call<SuperAdminList> request = mApiInterface.superAdminList();
        request.enqueue(new Callback<SuperAdminList>() {
            @Override
            public void onResponse(Call<SuperAdminList> call, Response<SuperAdminList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(SuperAdminActivity.this, RecyclerView.VERTICAL, false));
                        recycle_adpter = new Recycle_Adpter(response.body().getData(), SuperAdminActivity.this);
                        recyclerView.setAdapter(recycle_adpter);
                        searchView = findViewById(R.id.searchView);

                        searchView.setQueryHint(Html.fromHtml("<font color = #5D5F96> Filter using Size</font>"));
                        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                        searchView.setMaxWidth(Integer.MAX_VALUE);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
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
                        Toast.makeText(SuperAdminActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<SuperAdminList> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    public void drawerBack(View view) {
        finish();

    }

    public void add_admin(View view) {
        showAddAdminDialog();
    }

    public class Recycle_Adpter extends RecyclerView.Adapter {

        List<SuperAdminListData> spacecrafts;
        List<SuperAdminListData> spacecraftsFilter;
        Context mContext;

        public Recycle_Adpter(List data, Context mContext) {
            this.spacecraftsFilter = data;
            this.spacecrafts = data;
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int i) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_list_super_admin, parent, false);
            // set the view's size, margins, paddings and layout parameters
            Recycle_Adpter.MyViewHolder vh = new Recycle_Adpter.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@androidx.annotation.NonNull final RecyclerView.ViewHolder holder, final int position) {
            final Recycle_Adpter.MyViewHolder holder1 = (Recycle_Adpter.MyViewHolder) holder;
            final SuperAdminListData superAdminListData = (SuperAdminListData) spacecraftsFilter.get(position);

            holder1.tv_email.setText(superAdminListData.getEmail());
            holder1.tv_role.setText(superAdminListData.getRole());
            if (superAdminListData.getStatus().equals("Active")) {
                holder1.tv_role.setTextColor(mContext.getResources().getColor(R.color.green));
            } else {
                holder1.tv_role.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (superAdminListData.getStatus().equals("Active")) {
                        changeStatus(superAdminListData.getUserId(),"0");
                    } else {
                        changeStatus(superAdminListData.getUserId(),"1");
                    }
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
            TextView tv_email, tv_role;

            public MyViewHolder(@androidx.annotation.NonNull View itemView) {
                super(itemView);
                tv_email = itemView.findViewById(R.id.tv_email);
                tv_role = itemView.findViewById(R.id.tv_role);
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
                            List<SuperAdminListData> filteredList = new ArrayList<>();
                            for (SuperAdminListData row : spacecrafts) {
                                if (row.getEmail().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
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
                    spacecraftsFilter = (ArrayList<SuperAdminListData>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    private void showAddAdminDialog() {
        dialog = new Dialog(SuperAdminActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_amin);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText et_email = dialog.findViewById(R.id.et_email);
        final EditText et_password = dialog.findViewById(R.id.et_password);
        final EditText et_role = dialog.findViewById(R.id.et_role);
        final EditText et_status = dialog.findViewById(R.id.et_status);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ((Button) dialog.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_email.getText().toString().equals("")) {
                    et_email.setError("Enter Email");
                    return;
                }
                if (et_password.getText().toString().equals("")) {
                    et_password.setError("Enter Password");
                    return;
                }
                if (et_role.getText().toString().equals("")) {
                    et_role.setError("Enter Role");
                    return;
                }
                if (et_status.getText().toString().equals("")) {
                    et_status.setError("Enter Status");
                    return;
                }
                usersAdd(et_email.getText().toString(),et_password.getText().toString(),et_status.getText().toString(),et_role.getText().toString());
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void usersAdd(String email, String password, String status, String role_id) {
        pd.show();
        Call<UsersAdd> request = mApiInterface.usersAdd(new UsersAddBody(email, password, status, role_id));
        request.enqueue(new Callback<UsersAdd>() {
            @Override
            public void onResponse(Call<UsersAdd> call, Response<UsersAdd> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        dialog.dismiss();
                        Toast.makeText(SuperAdminActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onResume();
                    }else {
                        Toast.makeText(SuperAdminActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<UsersAdd> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void changeStatus(String userId, String status) {
        pd.show();
        Call<ChangeStatus> request = mApiInterface.changeStatus(new ChangeStatusBody(userId,status));
        request.enqueue(new Callback<ChangeStatus>() {
            @Override
            public void onResponse(Call<ChangeStatus> call, Response<ChangeStatus> response) {
                if (response.body() != null) {
                    Toast.makeText(SuperAdminActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getResponsecode() == 200) {
                        onResume();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<ChangeStatus> call, Throwable t) {
                pd.cancel();
            }
        });
    }
}