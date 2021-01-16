package com.loginiusinfotech.sonapartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.loginiusinfotech.sonapartner.R;

public class SplashActivity extends AppCompatActivity {
    private  static  int SPLASH_SCREEN=5000;
    Animation top, bottom;
    TextView txt1, txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        txt1 = findViewById(R.id.txtSona);
        txt2 = findViewById(R.id.txtPartner);
        txt1.startAnimation(bottom);
        txt2.startAnimation(top);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                finish();
            }
        },SPLASH_SCREEN);

    }
}