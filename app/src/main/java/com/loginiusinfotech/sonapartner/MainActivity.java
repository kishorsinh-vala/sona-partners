package com.loginiusinfotech.sonapartner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.loginiusinfotech.sonapartner.activity.AdminHomeActivity;
import com.loginiusinfotech.sonapartner.activity.SuperAdminActivity;
import com.loginiusinfotech.sonapartner.modal.users.login.Login;
import com.loginiusinfotech.sonapartner.modal.users.login.LoginBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    ProgressDialog pd;
    EditText et_password, et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
    }

    public void login(View view) {
        pd.show();
        usersLogin(et_email.getText().toString(), et_password.getText().toString());
//        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    private void usersLogin(String email, String password) {
        Call<Login> request = mApiInterface.usersLogin(new LoginBody(email, password));
        request.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        if (response.body().getData().getStatus().equals("Active")) {
                            Pref.setPrefDate(MainActivity.this, AppConstants.SAVE_USER_ID, response.body().getData().getEmail());
                            Pref.setPrefDate(MainActivity.this, AppConstants.SAVE_EMAIL, response.body().getData().getEmail());
                            Pref.setPrefDate(MainActivity.this, AppConstants.SAVE_ROLE_ID, response.body().getData().getRole_id());
                            Pref.setPrefDate(MainActivity.this, AppConstants.SAVE_ROLE, response.body().getData().getRole());
                            if (response.body().getData().getRole_id().equals("1")) {
                                startActivity(new Intent(MainActivity.this, SuperAdminActivity.class));
                                finish();
                            } else if (response.body().getData().getRole_id().equals("2")) {
                                startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Not Active User!", Toast.LENGTH_SHORT).show();
                        }
                        et_password.setText("");
                        Pref.setPrefDate(MainActivity.this, AppConstants.SAVE_STATUS, response.body().getData().getStatus());
                    } else {
                        et_password.setText("");
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                pd.cancel();
            }
        });
    }
}