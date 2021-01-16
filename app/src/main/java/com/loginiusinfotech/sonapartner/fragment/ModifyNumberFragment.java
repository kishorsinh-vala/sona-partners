package com.loginiusinfotech.sonapartner.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.activity.AddCategoryActivity;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAdd;
import com.loginiusinfotech.sonapartner.modal.category.categoryAdd.CategoryAddBody;
import com.loginiusinfotech.sonapartner.modal.setting.setting.Setting;
import com.loginiusinfotech.sonapartner.modal.setting.setting.SettingBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyNumberFragment extends Fragment {

    View root;
    EditText et_mobile,et_whatsapp;
    Button btn_submit;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    public ModifyNumberFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_modify_number, container, false);
        et_mobile=root.findViewById(R.id.et_mobile);
        et_whatsapp=root.findViewById(R.id.et_whatsapp);
        btn_submit=root.findViewById(R.id.btn_submit);

        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("please wait ...");
        pd.setCancelable(false);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_mobile.getText().toString().equals("")){
                    et_mobile.setError("Enter Mobile Number");
                    return;
                }
                if (et_whatsapp.getText().toString().equals("")){
                    et_whatsapp.setError("Enter Whatsapp Number");
                    return;
                }
                categoryAdd("mobileno",et_mobile.getText().toString());
                categoryAdd("whatsappno",et_whatsapp.getText().toString());
            }
        });
        et_mobile.setText(Pref.getPrefDate(getActivity(), AppConstants.SAVE_MOBILE_NUMBER,""));
        et_whatsapp.setText(Pref.getPrefDate(getActivity(), AppConstants.SAVE_WHATSAPP_NUMBER,""));
        return root;
    }

    private void categoryAdd(String key_of, String value) {
        pd.show();
        Call<Setting> request = mApiInterface.settingNumbers(new SettingBody(key_of,value));
        request.enqueue(new Callback<Setting>() {
            @Override
            public void onResponse(Call<Setting> call, Response<Setting> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        if (key_of.equals("mobileno")){
                            Pref.setPrefDate(getActivity(), AppConstants.SAVE_MOBILE_NUMBER,""+value);
                        }else {
                            Pref.setPrefDate(getActivity(), AppConstants.SAVE_WHATSAPP_NUMBER,""+value);
                        }
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<Setting> call, Throwable t) {
                pd.cancel();
            }
        });
    }
}