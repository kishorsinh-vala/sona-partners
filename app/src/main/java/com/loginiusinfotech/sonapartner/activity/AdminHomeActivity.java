package com.loginiusinfotech.sonapartner.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;

public class AdminHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_category, R.id.nav_sub_category, R.id.nav_product,
                R.id.nav_modify_stock,R.id.nav_modify_number, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        ActivityCompat.requestPermissions(AdminHomeActivity.this, new String[]
                { Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onBackPressed() {
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(AdminHomeActivity.this);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void drawerOpen(View view) {
        DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
        if (!navDrawer.isDrawerOpen(Gravity.START)) {
            navDrawer.openDrawer(Gravity.START);
        } else {
            navDrawer.closeDrawer(Gravity.END);
        }
    }

    @Override
    protected void onDestroy() {
        Pref.setPrefDate(AdminHomeActivity.this, AppConstants.SAVE_USER_ID, "");
        Pref.setPrefDate(AdminHomeActivity.this, AppConstants.SAVE_EMAIL, "");
        Pref.setPrefDate(AdminHomeActivity.this, AppConstants.SAVE_ROLE_ID, "");
        Pref.setPrefDate(AdminHomeActivity.this, AppConstants.SAVE_ROLE, "");
        super.onDestroy();
    }
}