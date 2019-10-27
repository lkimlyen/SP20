package com.demo.sp19.screen.splash;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.sp19.R;
import com.demo.sp19.manager.UserManager;
import com.demo.sp19.screen.dashboard.DashboardActivity;
import com.demo.sp19.screen.login.LoginActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (UserManager.getInstance().getUser() == null){
                    LoginActivity.start(SplashActivity.this);
                    finish();
                }else {
                    DashboardActivity.start(SplashActivity.this);
                    finish();
                }


            }
        };
        countDownTimer.start();
    }
}
