package com.csu.campusmap.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.csu.campusmap.Base.BaseActivity;
import com.csu.campusmap.R;

public class SplashActivity extends BaseActivity {

    private ProgressBar ui_progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                showProgress();
                Intent i = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(i);
                SplashActivity.this.finish();

            }
        }, 1000);



    }
}
